import { request } from './client';
import type {
  AccountItem,
  CurrentRisk,
  CustomerDetail,
  CustomerListItem,
  CustomerListParams,
  OperationLogItem,
  OperationLogParams,
  PageResponse,
  RiskRecalculateRequest,
  RiskRecalculateResponse,
  RiskRecord
} from './types';

export function listCustomers(params: CustomerListParams) {
  return request<PageResponse<CustomerListItem>>('/api/customers', { params: { ...params }, operator: 'demo-query' });
}

export function getCustomerDetail(customerNo: string) {
  return request<CustomerDetail>(`/api/customers/${encodeURIComponent(customerNo)}`, { operator: 'demo-query' });
}

export function listAccounts(customerNo: string) {
  return request<AccountItem[]>(`/api/customers/${encodeURIComponent(customerNo)}/accounts`, { operator: 'demo-query' });
}

export function getCurrentRisk(customerNo: string) {
  return request<CurrentRisk>(`/api/customers/${encodeURIComponent(customerNo)}/risk`, { operator: 'demo-query' });
}

export function listRiskRecords(customerNo: string, params: { pageNo: number; pageSize: number; riskLevel?: string }) {
  return request<PageResponse<RiskRecord>>(`/api/customers/${encodeURIComponent(customerNo)}/risk-records`, {
    params,
    operator: 'demo-query'
  });
}

export function recalculateRisk(customerNo: string, body: RiskRecalculateRequest) {
  return request<RiskRecalculateResponse>(`/api/customers/${encodeURIComponent(customerNo)}/risk/recalculate`, {
    method: 'POST',
    body,
    operator: 'demo-admin',
    notifyError: false
  });
}

export function listOperationLogs(params: OperationLogParams) {
  return request<PageResponse<OperationLogItem>>('/api/operation-logs', { params: { ...params }, operator: 'demo-admin' });
}
