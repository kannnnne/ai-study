<template>
  <el-dialog
    :model-value="modelValue"
    title="风险重算"
    width="620px"
    destroy-on-close
    @update:model-value="emit('update:modelValue', $event)"
    @open="resetState"
  >
    <div class="dialog-intro">
      <div>
        <span class="label">customerNo</span>
        <strong>{{ customerNo }}</strong>
      </div>
      <div>
        <span class="label">当前风险</span>
        <RiskLevelTag v-if="currentRiskLevel" :level="currentRiskLevel" />
        <span v-else class="muted">暂无样例风险</span>
      </div>
    </div>

    <el-form label-position="top">
      <el-form-item label="reasonSample">
        <el-input
          v-model="reasonSample"
          type="textarea"
          :rows="3"
          maxlength="120"
          show-word-limit
          placeholder="课程录屏手动触发风险重算样例"
        />
      </el-form-item>
      <el-form-item label="ruleCode">
        <el-input v-model="ruleCode" disabled />
      </el-form-item>
    </el-form>

    <ApiErrorAlert :error="error" />

    <el-result
      v-if="result"
      class="recalc-result"
      icon="success"
      title="重算完成"
      sub-title="后端已返回 transactionEffect，可用于讲解同一事务内的写入效果。"
    >
      <template #extra>
        <div class="result-panel">
          <div>
            <span>riskLevel</span>
            <RiskLevelTag :level="result.riskLevel" />
          </div>
          <div>
            <span>riskScore</span>
            <strong>{{ result.riskScore.toFixed(2) }}</strong>
          </div>
          <div>
            <span>calculatedBy</span>
            <strong>{{ result.calculatedBy }}</strong>
          </div>
        </div>
        <el-descriptions :column="1" border size="small">
          <el-descriptions-item label="riskRecordInserted">
            {{ result.transactionEffect.riskRecordInserted ? 'true' : 'false' }}
          </el-descriptions-item>
          <el-descriptions-item label="customerRiskSnapshotUpdated">
            {{ result.transactionEffect.customerRiskSnapshotUpdated ? 'true' : 'false' }}
          </el-descriptions-item>
          <el-descriptions-item label="operationLogInserted">
            {{ result.transactionEffect.operationLogInserted ? 'true' : 'false' }}
          </el-descriptions-item>
          <el-descriptions-item label="riskReasonSample">
            {{ result.riskReasonSample }}
          </el-descriptions-item>
        </el-descriptions>
      </template>
    </el-result>

    <template #footer>
      <el-button @click="emit('update:modelValue', false)">关闭</el-button>
      <el-button type="primary" :loading="submitting" :disabled="Boolean(result)" @click="submit">
        触发重算
      </el-button>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { recalculateRisk } from '@/api/services';
import { toApiError } from '@/api/client';
import type { ApiErrorInfo, RiskLevel, RiskRecalculateResponse } from '@/api/types';
import ApiErrorAlert from '@/components/ApiErrorAlert.vue';
import RiskLevelTag from '@/components/RiskLevelTag.vue';

const props = defineProps<{
  modelValue: boolean;
  customerNo: string;
  currentRiskLevel?: RiskLevel;
}>();

const emit = defineEmits<{
  'update:modelValue': [value: boolean];
  success: [];
}>();

const reasonSample = ref('课程录屏手动触发风险重算样例');
const ruleCode = ref('DEMO_RULE_V1');
const submitting = ref(false);
const result = ref<RiskRecalculateResponse | null>(null);
const error = ref<ApiErrorInfo | null>(null);

function resetState() {
  reasonSample.value = '课程录屏手动触发风险重算样例';
  ruleCode.value = 'DEMO_RULE_V1';
  submitting.value = false;
  result.value = null;
  error.value = null;
}

async function submit() {
  submitting.value = true;
  error.value = null;
  try {
    const response = await recalculateRisk(props.customerNo, {
      reasonSample: reasonSample.value,
      ruleCode: ruleCode.value
    });
    result.value = response.data;
    ElMessage.success(`风险重算成功：${response.requestId}`);
    emit('success');
  } catch (err) {
    error.value = toApiError(err);
  } finally {
    submitting.value = false;
  }
}
</script>

<style scoped>
.dialog-intro {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
  margin-bottom: 14px;
  padding: 12px;
  border: 1px solid var(--console-border-soft);
  border-radius: 8px;
  background: #f8fafc;
}

.dialog-intro > div {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
}

.label,
.result-panel span {
  color: var(--console-muted);
  font-size: 12px;
}

.recalc-result {
  padding: 4px 0 0;
}

.result-panel {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 10px;
  margin-bottom: 12px;
  text-align: left;
}

.result-panel > div {
  padding: 10px;
  border: 1px solid var(--console-border-soft);
  border-radius: 8px;
  background: #fff;
}

.result-panel span,
.result-panel strong {
  display: block;
}

.result-panel strong {
  margin-top: 6px;
  color: #203247;
  font-size: 16px;
}
</style>
