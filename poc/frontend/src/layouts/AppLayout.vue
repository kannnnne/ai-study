<template>
  <el-container class="app-shell">
    <el-aside width="232px" class="side">
      <div class="brand">
        <div class="brand-mark">风控</div>
        <div>
          <strong>风险评级操作台</strong>
          <span>AI Study POC</span>
        </div>
      </div>
      <el-menu :default-active="activePath" router class="side-menu">
        <el-menu-item index="/dashboard">
          <el-icon><DataLine /></el-icon>
          <span>仪表盘</span>
        </el-menu-item>
        <el-menu-item index="/customers">
          <el-icon><OfficeBuilding /></el-icon>
          <span>客户列表</span>
        </el-menu-item>
        <el-menu-item index="/operation-logs">
          <el-icon><Tickets /></el-icon>
          <span>操作日志</span>
        </el-menu-item>
      </el-menu>
      <div class="safety-note">
        <strong>脱敏边界</strong>
        <span>仅展示 CUST / SAMPLE / masked / sample 字段语义。</span>
      </div>
    </el-aside>
    <el-container>
      <el-header height="56px" class="top">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item>第一阶段单体 POC</el-breadcrumb-item>
          <el-breadcrumb-item>{{ route.meta.title ?? '操作台' }}</el-breadcrumb-item>
        </el-breadcrumb>
        <div class="operator">
          <span>X-Operator</span>
          <strong>demo-query / demo-admin</strong>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import { DataLine, OfficeBuilding, Tickets } from '@element-plus/icons-vue';

const route = useRoute();
const activePath = computed(() => {
  if (route.path.startsWith('/customers')) return '/customers';
  return route.path;
});
</script>

<style scoped>
.app-shell {
  min-height: 100vh;
}

.side {
  display: flex;
  flex-direction: column;
  background: #172335;
  border-right: 1px solid #111b2a;
}

.brand {
  display: flex;
  align-items: center;
  gap: 12px;
  height: 68px;
  padding: 0 18px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.08);
  color: #eef4fb;
}

.brand-mark {
  display: grid;
  place-items: center;
  width: 42px;
  height: 42px;
  border: 1px solid rgba(179, 199, 219, 0.28);
  border-radius: 8px;
  background: #22324a;
  font-size: 13px;
  font-weight: 800;
}

.brand strong,
.brand span {
  display: block;
}

.brand span {
  margin-top: 3px;
  color: #9fb1c5;
  font-size: 12px;
}

.side-menu {
  flex: 1;
  border-right: 0;
  background: transparent;
  --el-menu-bg-color: transparent;
  --el-menu-text-color: #b8c6d6;
  --el-menu-hover-bg-color: #21324a;
  --el-menu-active-color: #ffffff;
}

.side-menu :deep(.is-active) {
  background: #2b4464;
  border-right: 3px solid #89a9ca;
}

.safety-note {
  margin: 14px;
  padding: 12px;
  border: 1px solid rgba(176, 196, 218, 0.2);
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.04);
  color: #cbd8e6;
  font-size: 12px;
  line-height: 1.55;
}

.safety-note strong,
.safety-note span {
  display: block;
}

.top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  background: #fff;
  border-bottom: 1px solid var(--console-border-soft);
}

.operator {
  display: flex;
  align-items: center;
  gap: 8px;
  color: var(--console-muted);
  font-size: 12px;
}

.operator strong {
  color: #314862;
}

.main {
  padding: 18px 20px 28px;
}

@media (max-width: 900px) {
  .side {
    width: 190px !important;
  }

  .operator {
    display: none;
  }
}
</style>
