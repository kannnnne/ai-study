<template>
  <el-alert
    v-if="error"
    class="api-error"
    type="error"
    :closable="false"
    show-icon
  >
    <template #title>
      {{ error.message }}
    </template>
    <template #default>
      <div class="error-grid">
        <span>code：<strong>{{ error.code }}</strong></span>
        <span>requestId：<strong>{{ error.requestId }}</strong></span>
      </div>
    </template>
  </el-alert>
</template>

<script setup lang="ts">
import type { ApiErrorInfo } from '@/api/types';

defineProps<{ error: ApiErrorInfo | null }>();
</script>

<style scoped>
.api-error {
  margin-bottom: 12px;
}

.error-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 12px 20px;
  font-family: "Cascadia Mono", Consolas, monospace;
  font-size: 12px;
}
</style>
