<template>
  <el-tag :class="['status-tag', `status-${status.toLowerCase()}`]" effect="plain" round>
    {{ label }}
  </el-tag>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import type { CustomerStatus } from '@/api/types';

const props = defineProps<{ status: CustomerStatus }>();

const label = computed(() => {
  const labels: Record<CustomerStatus, string> = {
    ACTIVE: 'ACTIVE 正常',
    FROZEN: 'FROZEN 冻结',
    CLOSED: 'CLOSED 关闭'
  };
  return labels[props.status];
});
</script>

<style scoped>
.status-tag {
  border-width: 1px;
  font-weight: 700;
}

.status-active {
  --el-tag-text-color: var(--status-active);
  --el-tag-border-color: rgba(47, 143, 104, 0.34);
  --el-tag-bg-color: rgba(47, 143, 104, 0.07);
}

.status-frozen {
  --el-tag-text-color: var(--status-frozen);
  --el-tag-border-color: rgba(123, 111, 91, 0.38);
  --el-tag-bg-color: rgba(123, 111, 91, 0.08);
}

.status-closed {
  --el-tag-text-color: var(--status-closed);
  --el-tag-border-color: rgba(105, 115, 134, 0.34);
  --el-tag-bg-color: rgba(105, 115, 134, 0.08);
}
</style>
