<template>
  <PageHeader
    title="客户详情"
    eyebrow="CUSTOMER DETAIL"
    description="围绕 customerNo 聚合客户、账户、当前风险和历史风险记录。"
  >
    <el-button @click="router.push('/customers')">返回列表</el-button>
    <el-button type="primary" :disabled="!detail" @click="dialogVisible = true">风险重算</el-button>
  </PageHeader>

  <ApiErrorAlert :error="error" />

  <el-skeleton v-if="loading" :rows="8" animated />

  <template v-else-if="detail">
    <div class="detail-grid">
      <el-card shadow="never">
        <template #header>
          <span class="section-title">基础信息</span>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="customerNo">{{ detail.customerNo }}</el-descriptions-item>
          <el-descriptions-item label="customerNameSample">{{ detail.customerNameSample }}</el-descriptions-item>
          <el-descriptions-item label="customerType">{{ detail.customerType }}</el-descriptions-item>
          <el-descriptions-item label="status">
            <CustomerStatusTag :status="detail.status" />
          </el-descriptions-item>
          <el-descriptions-item label="mobileMasked">{{ detail.mobileMasked }}</el-descriptions-item>
          <el-descriptions-item label="idNoMasked">{{ detail.idNoMasked }}</el-descriptions-item>
          <el-descriptions-item label="remarkSample" :span="2">{{ detail.remarkSample ?? '暂无样例备注' }}</el-descriptions-item>
          <el-descriptions-item label="version">{{ detail.version }}</el-descriptions-item>
          <el-descriptions-item label="updatedBy">{{ detail.updatedBy }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <el-card shadow="never" class="risk-card">
        <template #header>
          <span class="section-title">当前风险</span>
        </template>
        <div v-if="currentRisk" class="risk-overview">
          <RiskLevelTag :level="currentRisk.currentRiskLevel" />
          <div class="score">{{ currentRisk.currentRiskScore.toFixed(2) }}</div>
          <div class="muted">ruleCode：{{ currentRisk.ruleCode }}</div>
          <p>{{ currentRisk.latestRiskReasonSample ?? '暂无 riskReasonSample' }}</p>
          <div class="risk-time">lastRiskCalculatedAt：{{ currentRisk.lastRiskCalculatedAt ?? '-' }}</div>
        </div>
        <el-empty v-else description="暂无样例风险数据" />
      </el-card>
    </div>

    <el-card shadow="never" class="block-card">
      <template #header>
        <div class="table-toolbar">
          <span class="section-title">账户列表</span>
          <span class="muted">accountNoMasked / availableBalance / hasAbnormalFlag</span>
        </div>
      </template>
      <el-table :data="accounts" stripe empty-text="暂无样例数据">
        <el-table-column prop="accountNoMasked" label="accountNoMasked" min-width="190" />
        <el-table-column prop="accountType" label="accountType" width="120" />
        <el-table-column prop="currency" label="currency" width="100" />
        <el-table-column label="balance" width="130" align="right">
          <template #default="{ row }">{{ money(row.balance) }}</template>
        </el-table-column>
        <el-table-column label="availableBalance" width="160" align="right">
          <template #default="{ row }">{{ money(row.availableBalance) }}</template>
        </el-table-column>
        <el-table-column prop="accountStatus" label="accountStatus" width="140" />
        <el-table-column label="hasAbnormalFlag" width="150">
          <template #default="{ row }">
            <el-tag :type="row.hasAbnormalFlag ? 'danger' : 'success'" effect="plain">
              {{ row.hasAbnormalFlag ? 'true' : 'false' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="openedAt" label="openedAt" width="120" />
      </el-table>
    </el-card>

    <el-card shadow="never" class="block-card">
      <template #header>
        <div class="table-toolbar">
          <span class="section-title">历史风险记录</span>
          <el-select v-model="riskParams.riskLevel" clearable placeholder="riskLevel" style="width: 150px" @change="reloadRiskRecords">
            <el-option label="LOW" value="LOW" />
            <el-option label="MEDIUM" value="MEDIUM" />
            <el-option label="HIGH" value="HIGH" />
          </el-select>
        </div>
      </template>
      <el-table :data="riskPage.records" stripe empty-text="暂无样例数据">
        <el-table-column label="riskLevel" width="150">
          <template #default="{ row }">
            <RiskLevelTag :level="row.riskLevel" />
          </template>
        </el-table-column>
        <el-table-column label="riskScore" width="120" align="right">
          <template #default="{ row }">{{ row.riskScore.toFixed(2) }}</template>
        </el-table-column>
        <el-table-column prop="riskReasonSample" label="riskReasonSample" min-width="280" show-overflow-tooltip />
        <el-table-column prop="ruleCode" label="ruleCode" width="150" />
        <el-table-column prop="sourceType" label="sourceType" width="120" />
        <el-table-column prop="calculatedBy" label="calculatedBy" width="140" />
        <el-table-column prop="calculatedAt" label="calculatedAt" min-width="190" />
      </el-table>
      <div class="pager">
        <el-pagination
          v-model:current-page="riskParams.pageNo"
          v-model:page-size="riskParams.pageSize"
          :total="riskPage.total"
          :page-sizes="[5, 10, 20]"
          layout="total, sizes, prev, pager, next"
          @size-change="reloadRiskRecords"
          @current-change="reloadRiskRecords"
        />
      </div>
    </el-card>

    <RiskRecalculateDialog
      v-model="dialogVisible"
      :customer-no="detail.customerNo"
      :current-risk-level="currentRisk?.currentRiskLevel"
      @success="refreshAfterRecalculate"
    />
  </template>

  <el-empty v-else description="暂无样例数据" />
</template>

<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getCurrentRisk, getCustomerDetail, listAccounts, listRiskRecords } from '@/api/services';
import { toApiError } from '@/api/client';
import type {
  AccountItem,
  ApiErrorInfo,
  CurrentRisk,
  CustomerDetail,
  PageResponse,
  RiskRecord
} from '@/api/types';
import ApiErrorAlert from '@/components/ApiErrorAlert.vue';
import CustomerStatusTag from '@/components/CustomerStatusTag.vue';
import PageHeader from '@/components/PageHeader.vue';
import RiskLevelTag from '@/components/RiskLevelTag.vue';
import RiskRecalculateDialog from '@/components/RiskRecalculateDialog.vue';

const route = useRoute();
const router = useRouter();
const customerNo = computed(() => String(route.params.customerNo));
const loading = ref(false);
const error = ref<ApiErrorInfo | null>(null);
const detail = ref<CustomerDetail | null>(null);
const accounts = ref<AccountItem[]>([]);
const currentRisk = ref<CurrentRisk | null>(null);
const dialogVisible = ref(false);
const riskParams = reactive({ pageNo: 1, pageSize: 10, riskLevel: '' });
const riskPage = reactive<PageResponse<RiskRecord>>({
  records: [],
  pageNo: 1,
  pageSize: 10,
  total: 0,
  pages: 0,
  hasNext: false
});

function money(value: number) {
  return value.toLocaleString('zh-CN', { style: 'currency', currency: 'CNY' });
}

async function load(showLoading = true) {
  if (showLoading) {
    loading.value = true;
  }
  error.value = null;
  try {
    const [detailResponse, accountResponse, riskResponse, riskRecordResponse] = await Promise.all([
      getCustomerDetail(customerNo.value),
      listAccounts(customerNo.value),
      getCurrentRisk(customerNo.value),
      listRiskRecords(customerNo.value, riskParams)
    ]);
    detail.value = detailResponse.data;
    accounts.value = accountResponse.data;
    currentRisk.value = riskResponse.data;
    Object.assign(riskPage, riskRecordResponse.data);
  } catch (err) {
    error.value = toApiError(err);
  } finally {
    if (showLoading) {
      loading.value = false;
    }
  }
}

async function reloadRiskRecords() {
  try {
    const response = await listRiskRecords(customerNo.value, riskParams);
    Object.assign(riskPage, response.data);
  } catch (err) {
    error.value = toApiError(err);
  }
}

async function refreshAfterRecalculate() {
  await load(false);
}

onMounted(load);
</script>

<style scoped>
.detail-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.45fr) minmax(320px, 0.55fr);
  gap: 14px;
}

.risk-overview {
  min-height: 214px;
}

.score {
  margin-top: 14px;
  color: #203247;
  font-size: 42px;
  font-weight: 800;
  line-height: 1;
}

.risk-overview p {
  margin: 14px 0;
  color: #314862;
  line-height: 1.7;
}

.risk-time {
  padding-top: 12px;
  border-top: 1px solid var(--console-border-soft);
  color: var(--console-muted);
  font-family: "Cascadia Mono", Consolas, monospace;
  font-size: 12px;
}

.block-card {
  margin-top: 14px;
}

.pager {
  display: flex;
  justify-content: flex-end;
  margin-top: 14px;
}

@media (max-width: 1050px) {
  .detail-grid {
    grid-template-columns: 1fr;
  }
}
</style>
