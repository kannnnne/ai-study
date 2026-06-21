<template>
  <PageHeader
    title="操作日志"
    eyebrow="OPERATION LOG"
    description="按 requestId、operator、operationType、targetBizNo 和 result 追踪成功 / 失败样例。"
  />

  <ApiErrorAlert :error="error" />

  <el-card shadow="never">
    <el-form :model="filters" label-position="top">
      <el-row :gutter="12">
        <el-col :xs="24" :sm="12" :lg="5">
          <el-form-item label="requestId">
            <el-input v-model="filters.requestId" clearable placeholder="REQ-DEMO-0002" />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="4">
          <el-form-item label="operator">
            <el-input v-model="filters.operator" clearable placeholder="demo-admin" />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="5">
          <el-form-item label="operationType">
            <el-select v-model="filters.operationType" clearable placeholder="全部">
              <el-option label="QUERY_CUSTOMER" value="QUERY_CUSTOMER" />
              <el-option label="UPDATE_CUSTOMER" value="UPDATE_CUSTOMER" />
              <el-option label="RECALCULATE_RISK" value="RECALCULATE_RISK" />
              <el-option label="QUERY_LOG" value="QUERY_LOG" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="4">
          <el-form-item label="targetBizNo">
            <el-input v-model="filters.targetBizNo" clearable placeholder="CUST100002" />
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="3">
          <el-form-item label="result">
            <el-select v-model="filters.result" clearable placeholder="全部">
              <el-option label="SUCCESS" value="SUCCESS" />
              <el-option label="FAIL" value="FAIL" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :xs="24" :sm="12" :lg="3" class="filter-actions">
          <el-button type="primary" :loading="loading" @click="search">查询</el-button>
          <el-button @click="reset">重置</el-button>
        </el-col>
      </el-row>
      <el-row :gutter="12">
        <el-col :xs="24" :lg="12">
          <el-form-item label="createdAt 范围">
            <el-date-picker
              v-model="timeRange"
              type="datetimerange"
              start-placeholder="startTime"
              end-placeholder="endTime"
              value-format="YYYY-MM-DDTHH:mm:ss+08:00"
              clearable
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <el-table v-loading="loading" :data="page.records" stripe empty-text="暂无样例数据">
      <el-table-column prop="requestId" label="requestId" min-width="170" />
      <el-table-column prop="operator" label="operator" width="130" />
      <el-table-column prop="operationType" label="operationType" min-width="170" />
      <el-table-column prop="targetType" label="targetType" width="120" />
      <el-table-column prop="targetBizNo" label="targetBizNo" min-width="140" />
      <el-table-column label="result" width="110">
        <template #default="{ row }">
          <ResultTag :result="row.result" />
        </template>
      </el-table-column>
      <el-table-column prop="errorMessageSample" label="errorMessageSample" min-width="220" show-overflow-tooltip />
      <el-table-column prop="operationDescSample" label="operationDescSample" min-width="260" show-overflow-tooltip />
      <el-table-column prop="clientIpMasked" label="clientIpMasked" min-width="150" />
      <el-table-column prop="durationMs" label="durationMs" width="110" align="right" />
      <el-table-column prop="createdAt" label="createdAt" min-width="190" />
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
import { onMounted, reactive, ref, watch } from 'vue';
import { listOperationLogs } from '@/api/services';
import { toApiError } from '@/api/client';
import type { ApiErrorInfo, OperationLogItem, OperationLogParams, PageResponse } from '@/api/types';
import ApiErrorAlert from '@/components/ApiErrorAlert.vue';
import PageHeader from '@/components/PageHeader.vue';
import ResultTag from '@/components/ResultTag.vue';

const loading = ref(false);
const error = ref<ApiErrorInfo | null>(null);
const timeRange = ref<[string, string] | null>(null);
const filters = reactive<OperationLogParams>({
  pageNo: 1,
  pageSize: 10,
  requestId: '',
  operator: '',
  operationType: '',
  targetType: '',
  targetBizNo: '',
  result: '',
  startTime: '',
  endTime: ''
});
const page = reactive<PageResponse<OperationLogItem>>({
  records: [],
  pageNo: 1,
  pageSize: 10,
  total: 0,
  pages: 0,
  hasNext: false
});

watch(timeRange, (value) => {
  filters.startTime = value?.[0] ?? '';
  filters.endTime = value?.[1] ?? '';
});

async function load() {
  loading.value = true;
  error.value = null;
  try {
    const response = await listOperationLogs(filters);
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
    requestId: '',
    operator: '',
    operationType: '',
    targetType: '',
    targetBizNo: '',
    result: '',
    startTime: '',
    endTime: ''
  });
  timeRange.value = null;
  void load();
}

onMounted(load);
</script>

<style scoped>
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
