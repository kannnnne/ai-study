<template>
  <el-tag :class="['risk-tag', `risk-${level.toLowerCase()}`]" effect="plain" round>
    {{ label }}
  </el-tag>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import type { RiskLevel } from '@/api/types';

const props = defineProps<{ level: RiskLevel }>();

const label = computed(() => {
  const labels: Record<RiskLevel, string> = {
    LOW: 'LOW 低风险',
    MEDIUM: 'MEDIUM 中风险',
    HIGH: 'HIGH 高风险'
  };
  return labels[props.level];
});
</script>

<style scoped>
.risk-tag {
  border-width: 1px;
  font-weight: 700;
}

.risk-low {
  --el-tag-text-color: var(--risk-low);
  --el-tag-border-color: rgba(47, 143, 104, 0.35);
  --el-tag-bg-color: rgba(47, 143, 104, 0.08);
}

.risk-medium {
  --el-tag-text-color: var(--risk-medium);
  --el-tag-border-color: rgba(183, 121, 31, 0.35);
  --el-tag-bg-color: rgba(183, 121, 31, 0.09);
}

.risk-high {
  --el-tag-text-color: var(--risk-high);
  --el-tag-border-color: rgba(191, 58, 55, 0.35);
  --el-tag-bg-color: rgba(191, 58, 55, 0.08);
}
</style>
