export type RiskLevel = 'LOW' | 'MEDIUM' | 'HIGH';
export type CustomerType = 'PERSONAL' | 'COMPANY';
export type CustomerStatus = 'ACTIVE' | 'FROZEN' | 'CLOSED';
export type OperationResult = 'SUCCESS' | 'FAIL';

export interface ApiResponse<T> {
  success: boolean;
  code: string;
  message: string;
  data: T;
  requestId: string;
  timestamp: string;
}

export interface PageResponse<T> {
  records: T[];
  pageNo: number;
  pageSize: number;
  total: number;
  pages: number;
  hasNext: boolean;
}

export interface ApiErrorInfo {
  code: string;
  message: string;
  requestId: string;
  timestamp?: string;
  httpStatus?: number;
}

export interface CustomerListParams {
  pageNo: number;
  pageSize: number;
  customerNo?: string;
  customerNameSample?: string;
  customerType?: CustomerType | '';
  status?: CustomerStatus | '';
  riskLevel?: RiskLevel | '';
}

export interface CustomerListItem {
  customerNo: string;
  customerNameSample: string;
  customerType: CustomerType;
  mobileMasked: string;
  status: CustomerStatus;
  currentRiskLevel: RiskLevel;
  currentRiskScore: number;
  lastRiskCalculatedAt: string | null;
  updatedAt: string;
}

export interface CustomerDetail extends CustomerListItem {
  idNoMasked: string;
  remarkSample: string | null;
  createdBy: string;
  createdAt: string;
  updatedBy: string;
  version: number;
}

export interface AccountItem {
  accountNoMasked: string;
  customerNo: string;
  accountType: 'DEBIT' | 'CREDIT' | string;
  currency: string;
  balance: number;
  availableBalance: number;
  accountStatus: 'NORMAL' | 'FROZEN' | 'CLOSED' | string;
  hasAbnormalFlag: boolean;
  openedAt: string;
  updatedAt: string;
}

export interface CurrentRisk {
  customerNo: string;
  customerNameSample: string;
  currentRiskLevel: RiskLevel;
  currentRiskScore: number;
  lastRiskCalculatedAt: string | null;
  latestRiskReasonSample: string | null;
  ruleCode: string;
}

export interface RiskRecord {
  customerNo: string;
  riskLevel: RiskLevel;
  riskScore: number;
  riskReasonSample: string;
  ruleCode: string;
  sourceType: 'INIT' | 'MANUAL' | string;
  calculatedBy: string;
  calculatedAt: string;
  createdAt: string;
}

export interface TransactionEffect {
  riskRecordInserted: boolean;
  customerRiskSnapshotUpdated: boolean;
  operationLogInserted: boolean;
}

export interface RiskRecalculateRequest {
  reasonSample?: string;
  ruleCode?: string;
}

export interface RiskRecalculateResponse {
  customerNo: string;
  riskLevel: RiskLevel;
  riskScore: number;
  riskReasonSample: string;
  ruleCode: string;
  sourceType: 'MANUAL' | string;
  calculatedBy: string;
  calculatedAt: string;
  transactionEffect: TransactionEffect;
}

export interface OperationLogParams {
  pageNo: number;
  pageSize: number;
  requestId?: string;
  operator?: string;
  operationType?: string;
  targetType?: string;
  targetBizNo?: string;
  result?: OperationResult | '';
  startTime?: string;
  endTime?: string;
}

export interface OperationLogItem {
  requestId: string;
  operator: string;
  operationType: string;
  targetType: string;
  targetBizNo: string;
  result: OperationResult;
  errorMessageSample: string | null;
  operationDescSample: string | null;
  clientIpMasked: string;
  durationMs: number;
  createdAt: string;
}
