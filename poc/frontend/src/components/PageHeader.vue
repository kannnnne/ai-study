<template>
  <div class="page-header">
    <div>
      <div class="eyebrow">{{ eyebrow }}</div>
      <h1>{{ title }}</h1>
      <p v-if="description">{{ description }}</p>
    </div>
    <slot />
  </div>
</template>

<script setup lang="ts">
defineProps<{
  title: string;
  eyebrow: string;
  description?: string;
}>();
</script>

<style scoped>
.page-header {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 16px;
}

.eyebrow {
  color: var(--console-blue);
  font-size: 12px;
  font-weight: 800;
  letter-spacing: 0.04em;
}

h1 {
  margin: 4px 0 0;
  color: #203247;
  font-size: 22px;
  line-height: 1.25;
}

p {
  margin: 6px 0 0;
  color: var(--console-muted);
}

@media (max-width: 760px) {
  .page-header {
    align-items: flex-start;
    flex-direction: column;
  }
}
</style>
