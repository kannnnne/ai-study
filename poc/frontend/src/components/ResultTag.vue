<template>
  <el-tag :type="result === 'SUCCESS' ? 'success' : 'danger'" effect="plain" round>
    {{ result === 'SUCCESS' ? 'SUCCESS 成功' : 'FAIL 失败' }}
  </el-tag>
</template>

<script setup lang="ts">
import type { OperationResult } from '@/api/types';

defineProps<{ result: OperationResult }>();
</script>
