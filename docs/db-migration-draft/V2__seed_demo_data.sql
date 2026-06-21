SET NAMES utf8mb4;
SET time_zone = '+00:00';

INSERT INTO customer (
  customer_no, customer_name_sample, customer_type, mobile_masked, id_no_masked,
  status, current_risk_level, current_risk_score, last_risk_calculated_at,
  remark_sample, created_by, created_at, updated_by, updated_at
) VALUES
('CUST100001', '样例客户A', 'PERSONAL', 'SAMPLE-MOBILE-****-0001', 'SAMPLE-ID-********-0001', 'ACTIVE', 'LOW', 18.50, '2026-06-21 02:00:00.000', '低风险样例客户', 'system', '2026-06-20 02:00:00.000', 'system', '2026-06-21 02:00:00.000'),
('CUST100002', '样例客户B', 'PERSONAL', 'SAMPLE-MOBILE-****-0002', 'SAMPLE-ID-********-0002', 'ACTIVE', 'MEDIUM', 55.00, '2026-06-21 02:05:00.000', '中风险样例客户，有一条关注原因', 'system', '2026-06-20 02:05:00.000', 'system', '2026-06-21 02:05:00.000'),
('CUST100003', '样例客户C', 'PERSONAL', 'SAMPLE-MOBILE-****-0003', 'SAMPLE-ID-********-0003', 'FROZEN', 'HIGH', 86.00, '2026-06-21 02:10:00.000', '高风险样例客户，存在冻结和异常标记', 'system', '2026-06-20 02:10:00.000', 'system', '2026-06-21 02:10:00.000'),
('CUST100004', '样例企业D', 'COMPANY', 'SAMPLE-MOBILE-****-0004', 'SAMPLE-ID-********-0004', 'ACTIVE', 'LOW', 28.00, '2026-06-21 02:15:00.000', '企业客户低风险样例', 'system', '2026-06-20 02:15:00.000', 'system', '2026-06-21 02:15:00.000'),
('CUST100005', '样例客户E', 'PERSONAL', 'SAMPLE-MOBILE-****-0005', 'SAMPLE-ID-********-0005', 'ACTIVE', 'MEDIUM', 64.00, '2026-06-21 02:20:00.000', '中风险边界样例客户', 'system', '2026-06-20 02:20:00.000', 'system', '2026-06-21 02:20:00.000'),
('CUST100006', '样例客户F', 'PERSONAL', 'SAMPLE-MOBILE-****-0006', 'SAMPLE-ID-********-0006', 'CLOSED', 'HIGH', 78.50, '2026-06-21 02:25:00.000', '销户状态高风险样例', 'system', '2026-06-20 02:25:00.000', 'system', '2026-06-21 02:25:00.000');

INSERT INTO account (
  customer_no, account_no_masked, account_type, currency, balance, available_balance,
  account_status, has_abnormal_flag, opened_at, created_by, created_at, updated_by, updated_at
) VALUES
('CUST100001', 'SAMPLE-ACC-****-0001', 'DEBIT', 'CNY', 12500.50, 12500.50, 'NORMAL', 0, '2024-01-10', 'system', '2026-06-20 03:00:00.000', 'system', '2026-06-20 03:00:00.000'),
('CUST100001', 'SAMPLE-ACC-****-0002', 'CREDIT', 'CNY', 3200.00, 3200.00, 'NORMAL', 0, '2024-05-18', 'system', '2026-06-20 03:01:00.000', 'system', '2026-06-20 03:01:00.000'),
('CUST100002', 'SAMPLE-ACC-****-0003', 'DEBIT', 'CNY', 860.30, 860.30, 'NORMAL', 0, '2023-11-03', 'system', '2026-06-20 03:02:00.000', 'system', '2026-06-20 03:02:00.000'),
('CUST100002', 'SAMPLE-ACC-****-0004', 'LOAN', 'CNY', 45000.00, 0.00, 'NORMAL', 1, '2025-02-14', 'system', '2026-06-20 03:03:00.000', 'system', '2026-06-20 03:03:00.000'),
('CUST100003', 'SAMPLE-ACC-****-0005', 'DEBIT', 'CNY', 120.00, 0.00, 'FROZEN', 1, '2022-08-08', 'system', '2026-06-20 03:04:00.000', 'system', '2026-06-20 03:04:00.000'),
('CUST100004', 'SAMPLE-ACC-****-0006', 'DEBIT', 'CNY', 250000.00, 250000.00, 'NORMAL', 0, '2021-06-30', 'system', '2026-06-20 03:05:00.000', 'system', '2026-06-20 03:05:00.000'),
('CUST100005', 'SAMPLE-ACC-****-0007', 'DEBIT', 'CNY', 980.00, 980.00, 'NORMAL', 0, '2025-09-12', 'system', '2026-06-20 03:06:00.000', 'system', '2026-06-20 03:06:00.000'),
('CUST100006', 'SAMPLE-ACC-****-0008', 'DEBIT', 'CNY', 0.00, 0.00, 'CLOSED', 1, '2020-04-01', 'system', '2026-06-20 03:07:00.000', 'system', '2026-06-20 03:07:00.000');

INSERT INTO risk_record (
  customer_no, risk_level, risk_score, risk_reason_sample, rule_code, source_type,
  calculated_by, calculated_at, created_at
) VALUES
('CUST100001', 'LOW', 18.50, '样例规则：客户状态正常，账户无异常标记，余额充足', 'DEMO_RULE_V1', 'INIT', 'system', '2026-06-21 02:00:00.000', '2026-06-21 02:00:00.000'),
('CUST100002', 'MEDIUM', 45.00, '样例规则：余额较低，需关注', 'DEMO_RULE_V1', 'INIT', 'system', '2026-06-20 02:05:00.000', '2026-06-20 02:05:00.000'),
('CUST100002', 'MEDIUM', 55.00, '样例规则：存在异常标记账户，风险分数上调', 'DEMO_RULE_V1', 'MANUAL', 'demo-operator', '2026-06-21 02:05:00.000', '2026-06-21 02:05:00.000'),
('CUST100003', 'HIGH', 86.00, '样例规则：客户状态冻结，账户存在异常标记，可用余额为零', 'DEMO_RULE_V1', 'INIT', 'system', '2026-06-21 02:10:00.000', '2026-06-21 02:10:00.000'),
('CUST100004', 'LOW', 28.00, '样例规则：企业客户状态正常，账户无异常标记', 'DEMO_RULE_V1', 'INIT', 'system', '2026-06-21 02:15:00.000', '2026-06-21 02:15:00.000'),
('CUST100005', 'MEDIUM', 64.00, '样例规则：余额低于样例阈值，接近高风险边界', 'DEMO_RULE_V1', 'INIT', 'system', '2026-06-21 02:20:00.000', '2026-06-21 02:20:00.000'),
('CUST100006', 'HIGH', 78.50, '样例规则：客户已销户且存在异常标记账户', 'DEMO_RULE_V1', 'INIT', 'system', '2026-06-21 02:25:00.000', '2026-06-21 02:25:00.000');

INSERT INTO operation_log (
  request_id, operator, operation_type, target_type, target_biz_no, result,
  error_message_sample, operation_desc_sample, client_ip_masked, duration_ms, created_at
) VALUES
('REQ-DEMO-0001', 'demo-query', 'QUERY_CUSTOMER', 'CUSTOMER', 'CUST100001', 'SUCCESS', NULL, '查询客户详情样例', 'SAMPLE-IP-10.*.*.1', 35, '2026-06-21 02:30:00.000'),
('REQ-DEMO-0002', 'demo-admin', 'RECALCULATE_RISK', 'RISK', 'CUST100002', 'SUCCESS', NULL, '触发风险重算样例，结果为MEDIUM', 'SAMPLE-IP-10.*.*.2', 82, '2026-06-21 02:31:00.000'),
('REQ-DEMO-0003', 'demo-admin', 'UPDATE_CUSTOMER', 'CUSTOMER', 'CUST100003', 'SUCCESS', NULL, '更新客户状态样例', 'SAMPLE-IP-10.*.*.3', 64, '2026-06-21 02:32:00.000'),
('REQ-DEMO-0004', 'demo-query', 'QUERY_LOG', 'LOG', NULL, 'SUCCESS', NULL, '查询操作日志样例', 'SAMPLE-IP-10.*.*.4', 28, '2026-06-21 02:33:00.000'),
('REQ-DEMO-0005', 'demo-admin', 'RECALCULATE_RISK', 'RISK', 'CUST999999', 'FAIL', '样例错误：客户不存在', '触发不存在客户风险重算样例', 'SAMPLE-IP-10.*.*.5', 41, '2026-06-21 02:34:00.000');
