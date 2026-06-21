CREATE TABLE customer (
  id BIGINT NOT NULL AUTO_INCREMENT,
  customer_no VARCHAR(32) NOT NULL,
  customer_name_sample VARCHAR(80) NOT NULL,
  customer_type VARCHAR(16) NOT NULL DEFAULT 'PERSONAL',
  mobile_masked VARCHAR(32) NOT NULL,
  id_no_masked VARCHAR(64) NOT NULL,
  status VARCHAR(16) NOT NULL DEFAULT 'ACTIVE',
  current_risk_level VARCHAR(8) NOT NULL DEFAULT 'LOW',
  current_risk_score DECIMAL(5,2) NOT NULL DEFAULT 0.00,
  last_risk_calculated_at DATETIME(3) NULL,
  remark_sample VARCHAR(200) NULL,
  created_by VARCHAR(64) NOT NULL DEFAULT 'system',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by VARCHAR(64) NOT NULL DEFAULT 'system',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  version INT NOT NULL DEFAULT 0,
  deleted TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY uk_customer_no (customer_no)
);

CREATE TABLE account (
  id BIGINT NOT NULL AUTO_INCREMENT,
  customer_no VARCHAR(32) NOT NULL,
  account_no_masked VARCHAR(64) NOT NULL,
  account_type VARCHAR(16) NOT NULL DEFAULT 'DEBIT',
  currency CHAR(3) NOT NULL DEFAULT 'CNY',
  balance DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  available_balance DECIMAL(18,2) NOT NULL DEFAULT 0.00,
  account_status VARCHAR(16) NOT NULL DEFAULT 'NORMAL',
  has_abnormal_flag TINYINT NOT NULL DEFAULT 0,
  opened_at DATE NOT NULL,
  created_by VARCHAR(64) NOT NULL DEFAULT 'system',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  updated_by VARCHAR(64) NOT NULL DEFAULT 'system',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  version INT NOT NULL DEFAULT 0,
  deleted TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY uk_account_no_masked (account_no_masked),
  KEY idx_account_customer_no (customer_no),
  CONSTRAINT fk_account_customer_no FOREIGN KEY (customer_no) REFERENCES customer (customer_no)
);

CREATE TABLE risk_record (
  id BIGINT NOT NULL AUTO_INCREMENT,
  customer_no VARCHAR(32) NOT NULL,
  risk_level VARCHAR(8) NOT NULL,
  risk_score DECIMAL(5,2) NOT NULL,
  risk_reason_sample VARCHAR(500) NOT NULL,
  rule_code VARCHAR(32) NOT NULL DEFAULT 'DEMO_RULE_V1',
  source_type VARCHAR(16) NOT NULL DEFAULT 'MANUAL',
  calculated_by VARCHAR(64) NOT NULL DEFAULT 'system',
  calculated_at DATETIME(3) NOT NULL,
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (id),
  KEY idx_risk_customer_time (customer_no, calculated_at DESC, id DESC),
  CONSTRAINT fk_risk_customer_no FOREIGN KEY (customer_no) REFERENCES customer (customer_no)
);

CREATE TABLE operation_log (
  id BIGINT NOT NULL AUTO_INCREMENT,
  request_id VARCHAR(64) NOT NULL,
  operator VARCHAR(64) NOT NULL,
  operation_type VARCHAR(32) NOT NULL,
  target_type VARCHAR(32) NOT NULL,
  target_biz_no VARCHAR(64) NULL,
  result VARCHAR(16) NOT NULL DEFAULT 'SUCCESS',
  error_message_sample VARCHAR(500) NULL,
  operation_desc_sample VARCHAR(500) NULL,
  client_ip_masked VARCHAR(64) NULL,
  duration_ms BIGINT NOT NULL DEFAULT 0,
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  PRIMARY KEY (id),
  KEY idx_op_request_id (request_id),
  KEY idx_op_type_time (operation_type, created_at DESC)
);
