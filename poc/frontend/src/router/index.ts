import { createRouter, createWebHistory } from 'vue-router';
import DashboardView from '@/views/DashboardView.vue';
import CustomersView from '@/views/CustomersView.vue';
import CustomerDetailView from '@/views/CustomerDetailView.vue';
import OperationLogsView from '@/views/OperationLogsView.vue';

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', redirect: '/dashboard' },
    { path: '/dashboard', name: 'dashboard', component: DashboardView, meta: { title: '仪表盘' } },
    { path: '/customers', name: 'customers', component: CustomersView, meta: { title: '客户列表' } },
    {
      path: '/customers/:customerNo',
      name: 'customer-detail',
      component: CustomerDetailView,
      meta: { title: '客户详情' }
    },
    { path: '/operation-logs', name: 'operation-logs', component: OperationLogsView, meta: { title: '操作日志' } },
    { path: '/:pathMatch(.*)*', redirect: '/dashboard' }
  ]
});

export default router;
