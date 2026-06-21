<template>
  <PageHeader
    title="客户列表"
    eyebrow="CUSTOMER QUERY"
    description="按客户号、样例名称、客户状态和风险等级查询脱敏样例客户。"
  />

  <ApiErrorAlert :error="error" />

  <el-card shadow="never">
    <el-form :model="filters" class="filter-form" label-position="top">
      <el-row :gutter="12">
        <el-col :xs="24" :sm="12" :lg="5">
          <el-form-item label="customerNo">
            <el-input v-model="filters.customerNo" clearable placeholder="CUST100001" />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="5">
          <el-form-item label="customerNameSample">
            <el-input v-model="filters.customerNameSample" clearable placeholder="样例客户" />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8" :lg="4">
          <el-form-item label="customerType">
            <el-select v-model="filters.customerType" clearable placeholder="全部">
              <el-option label="PERSONAL" value="PERSONAL" />
              <el-option label="COMPANY" value="COMPANY" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8" :lg="4">
          <el-form-item label="status">
            <el-select v-model="filters.status" clearable placeholder="全部">
              <el-option label="ACTIVE" value="ACTIVE" />
              <el-option label="FROZEN" value="FROZEN" />
              <el-option label="CLOSED" value="CLOSED" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="8" :lg="4">
          <el-form-item label="riskLevel">
            <el-select v-model="filters.riskLevel" clearable placeholder="全部">
              <el-option label="LOW" value="LOW" />
              <el-option label="MEDIUM" value="MEDIUM" />
              <el-option label="HIGH" value="HIGH" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :lg="2" class="filter-actions">
          <el-button type="primary" :loading="loading" @click="search">查询</el-button>
          <el-button @click="reset">重置</el-button>
        </el-col>
      </el-row>
    </el-form>

    <el-table v-loading="loading" :data="page.records" stripe empty-text="暂无样例数据">
      <el-table-column prop="customerNo" label="customerNo" min-width="130" />
      <el-table-column prop="customerNameSample" label="customerNameSample" min-width="140" />
      <el-table-column prop="customerType" label="customerType" width="130" />
      <el-table-column prop="mobileMasked" label="mobileMasked" min-width="190" />
      <el-table-column label="status" width="130">
        <template #default="{ row }">
          <CustomerStatusTag :status="row.status" />
        </template>
      </el-table-column>
      <el-table-column label="currentRiskLevel" width="150">
        <template #default="{ row }">
          <RiskLevelTag :level="row.currentRiskLevel" />
        </template>
      </el-table-column>
      <el-table-column label="currentRiskScore" width="150" align="right">
        <template #default="{ row }">{{ row.currentRiskScore.toFixed(2) }}</template>
      </el-table-column>
      <el-table-column prop="lastRiskCalculatedAt" label="lastRiskCalculatedAt" min-width="190" />
      <el-table-column label="操作" width="100" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="goDetail(row.customerNo)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pager">
      <el-pagination
        v-model:current-page="filters.pageNo"
        v-model:page-size="filters.pageSize"
        :total="page.total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next"
        @size-change="load"
        @current-change="load"
      />
    </div>
  </el-card>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { listCustomers } from '@/api/services';
import { toApiError } from '@/api/client';
import type { ApiErrorInfo, CustomerListItem, CustomerListParams, PageResponse } from '@/api/types';
import ApiErrorAlert from '@/components/ApiErrorAlert.vue';
import CustomerStatusTag from '@/components/CustomerStatusTag.vue';
import PageHeader from '@/components/PageHeader.vue';
import RiskLevelTag from '@/components/RiskLevelTag.vue';

const router = useRouter();
const loading = ref(false);
const error = ref<ApiErrorInfo | null>(null);
const filters = reactive<CustomerListParams>({
  pageNo: 1,
  pageSize: 10,
  customerNo: '',
  customerNameSample: '',
  customerType: '',
  status: '',
  riskLevel: ''
});
const page = reactive<PageResponse<CustomerListItem>>({
  records: [],
  pageNo: 1,
  pageSize: 10,
  total: 0,
  pages: 0,
  hasNext: false
});

async function load() {
  loading.value = true;
  error.value = null;
  try {
    const response = await listCustomers(filters);
    Object.assign(page, response.data);
  } catch (err) {
    error.value = toApiError(err);
    page.records = [];
    page.total = 0;
  } finally {
    loading.value = false;
  }
}

function search() {
  filters.pageNo = 1;
  void load();
}

function reset() {
  Object.assign(filters, {
    pageNo: 1,
    pageSize: 10,
    customerNo: '',
    customerNameSample: '',
    customerType: '',
    status: '',
    riskLevel: ''
  });
  void load();
}

function goDetail(customerNo: string) {
  void router.push(`/customers/${customerNo}`);
}

onMounted(load);
</script>

<style scoped>
.filter-form {
  margin-bottom: 4px;
}

.filter-actions {
  display: flex;
  align-items: flex-end;
  gap: 8px;
  padding-bottom: 18px;
}

.filter-actions .el-button + .el-button {
  margin-left: 0;
}

.pager {
  display: flex;
  justify-content: flex-end;
  margin-top: 14px;
}
</style>
