<template>
  <PageHeader
    title="仪表盘"
    eyebrow="RISK CONSOLE"
    description="基于样例分页数据做 POC 级轻量聚合，生产统计应由专用聚合接口承载。"
  >
    <el-button :loading="loading" @click="load">刷新</el-button>
  </PageHeader>

  <ApiErrorAlert :error="error" />

  <div class="metric-grid">
    <div class="metric console-card">
      <span>样例客户总数</span>
      <strong>{{ totalCustomers }}</strong>
      <em>GET /api/customers</em>
    </div>
    <div class="metric console-card">
      <span>高风险客户</span>
      <strong class="danger">{{ riskCounts.HIGH }}</strong>
      <em>currentRiskLevel = HIGH</em>
    </div>
    <div class="metric console-card">
      <span>冻结 / 关闭</span>
      <strong>{{ statusCounts.FROZEN + statusCounts.CLOSED }}</strong>
      <em>status = FROZEN / CLOSED</em>
    </div>
    <div class="metric console-card">
      <span>最近日志</span>
      <strong>{{ logs.length }}</strong>
      <em>pageSize = 5</em>
    </div>
  </div>

  <div class="chart-grid">
    <el-card shadow="never">
      <template #header>
        <span class="section-title">风险等级分布</span>
      </template>
      <div v-loading="loading" ref="riskChartEl" class="chart"></div>
    </el-card>
    <el-card shadow="never">
      <template #header>
        <span class="section-title">客户状态统计</span>
      </template>
      <div v-loading="loading" ref="statusChartEl" class="chart"></div>
    </el-card>
  </div>

  <el-card shadow="never" class="log-card">
    <template #header>
      <div class="table-toolbar">
        <span class="section-title">最近操作日志</span>
        <el-button link type="primary" @click="router.push('/operation-logs')">查看全部</el-button>
      </div>
    </template>
    <el-table v-loading="loading" :data="logs" stripe empty-text="暂无样例数据">
      <el-table-column prop="requestId" label="requestId" min-width="170" />
      <el-table-column prop="operator" label="operator" width="130" />
      <el-table-column prop="operationType" label="operationType" min-width="170" />
      <el-table-column prop="targetBizNo" label="targetBizNo" width="140" />
      <el-table-column label="result" width="110">
        <template #default="{ row }">
          <ResultTag :result="row.result" />
        </template>
      </el-table-column>
      <el-table-column prop="errorMessageSample" label="errorMessageSample" min-width="220" show-overflow-tooltip />
      <el-table-column prop="createdAt" label="createdAt" min-width="190" />
    </el-table>
  </el-card>
</template>

<script setup lang="ts">
import { computed, nextTick, onBeforeUnmount, onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import * as echarts from 'echarts';
import { listCustomers, listOperationLogs } from '@/api/services';
import { toApiError } from '@/api/client';
import type { ApiErrorInfo, CustomerListItem, CustomerStatus, OperationLogItem, RiskLevel } from '@/api/types';
import ApiErrorAlert from '@/components/ApiErrorAlert.vue';
import PageHeader from '@/components/PageHeader.vue';
import ResultTag from '@/components/ResultTag.vue';

const router = useRouter();
const loading = ref(false);
const error = ref<ApiErrorInfo | null>(null);
const customers = ref<CustomerListItem[]>([]);
const logs = ref<OperationLogItem[]>([]);
const riskChartEl = ref<HTMLDivElement | null>(null);
const statusChartEl = ref<HTMLDivElement | null>(null);
let riskChart: echarts.ECharts | null = null;
let statusChart: echarts.ECharts | null = null;

const riskCounts = computed<Record<RiskLevel, number>>(() => ({
  LOW: customers.value.filter((item) => item.currentRiskLevel === 'LOW').length,
  MEDIUM: customers.value.filter((item) => item.currentRiskLevel === 'MEDIUM').length,
  HIGH: customers.value.filter((item) => item.currentRiskLevel === 'HIGH').length
}));

const statusCounts = computed<Record<CustomerStatus, number>>(() => ({
  ACTIVE: customers.value.filter((item) => item.status === 'ACTIVE').length,
  FROZEN: customers.value.filter((item) => item.status === 'FROZEN').length,
  CLOSED: customers.value.filter((item) => item.status === 'CLOSED').length
}));

const totalCustomers = computed(() => customers.value.length);

function renderCharts() {
  if (riskChartEl.value) {
    riskChart ??= echarts.init(riskChartEl.value);
    riskChart.setOption({
      color: ['#2f8f68', '#b7791f', '#bf3a37'],
      tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
      legend: { bottom: 0, itemWidth: 10, itemHeight: 10 },
      series: [
        {
          name: 'riskLevel',
          type: 'pie',
          radius: ['48%', '70%'],
          center: ['50%', '45%'],
          label: { formatter: '{b}\n{c}' },
          data: [
            { name: 'LOW', value: riskCounts.value.LOW },
            { name: 'MEDIUM', value: riskCounts.value.MEDIUM },
            { name: 'HIGH', value: riskCounts.value.HIGH }
          ]
        }
      ]
    });
  }

  if (statusChartEl.value) {
    statusChart ??= echarts.init(statusChartEl.value);
    statusChart.setOption({
      color: ['#355f8c'],
      tooltip: { trigger: 'axis' },
      grid: { left: 42, right: 18, top: 20, bottom: 34 },
      xAxis: { type: 'category', data: ['ACTIVE', 'FROZEN', 'CLOSED'], axisTick: { show: false } },
      yAxis: { type: 'value', minInterval: 1 },
      series: [
        {
          name: 'status',
          type: 'bar',
          barWidth: 34,
          data: [statusCounts.value.ACTIVE, statusCounts.value.FROZEN, statusCounts.value.CLOSED],
          itemStyle: { borderRadius: [4, 4, 0, 0] }
        }
      ]
    });
  }
}

function resizeCharts() {
  riskChart?.resize();
  statusChart?.resize();
}

async function load() {
  loading.value = true;
  error.value = null;
  try {
    const [customerResponse, logResponse] = await Promise.all([
      listCustomers({ pageNo: 1, pageSize: 100 }),
      listOperationLogs({ pageNo: 1, pageSize: 5 })
    ]);
    customers.value = customerResponse.data.records;
    logs.value = logResponse.data.records;
    await nextTick();
    renderCharts();
  } catch (err) {
    error.value = toApiError(err);
    customers.value = [];
    logs.value = [];
    await nextTick();
    renderCharts();
  } finally {
    loading.value = false;
  }
}

onMounted(() => {
  void load();
  window.addEventListener('resize', resizeCharts);
});

onBeforeUnmount(() => {
  window.removeEventListener('resize', resizeCharts);
  riskChart?.dispose();
  statusChart?.dispose();
});
</script>

<style scoped>
.metric {
  padding: 14px 16px;
}

.metric span,
.metric em {
  display: block;
  color: var(--console-muted);
  font-size: 12px;
  font-style: normal;
}

.metric strong {
  display: block;
  margin: 8px 0 6px;
  color: #203247;
  font-size: 30px;
  line-height: 1;
}

.metric strong.danger {
  color: var(--risk-high);
}

.chart-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
  margin-top: 14px;
}

.chart {
  height: 300px;
}

.log-card {
  margin-top: 14px;
}

@media (max-width: 960px) {
  .chart-grid {
    grid-template-columns: 1fr;
  }
}
</style>
