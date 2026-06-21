# POC 数据库设计

## 主控验收结论

本数据库设计通过主控窗口验收，可以作为后续后端最小实现、API 设计、测试验收和课程录制的数据库基线。

主控补充裁决：

- `V1__init_schema.sql` 可作为 Flyway 初始建表脚本草案。
- `V2__seed_demo_data.sql` 可作为 Flyway 脱敏样例数据脚本草案。
- 第一阶段风险评级以低 / 中 / 高等级和演示场景覆盖为主，样例分数用于页面展示和测试数据分层。
- 文档中的风险加分规则是教学用简化规则，不要求所有初始化样例分数严格由该公式反推。
- 后端子窗口如果要实现精确分数算法，必须先统一公式和种子数据，再编写断言精确分数的测试。
- 后端第一阶段测试建议优先断言风险等级、记录写入、当前风险快照回写和操作日志，不优先断言所有样例客户的精确分数。

## 1. 设计目标和边界

本设计服务于“客户风险评级与账户查询”第一阶段单体 POC，目标是让后端子窗口可以直接基于 MySQL 8、Flyway 和 MyBatis Plus 完成最小实现，同时让课程录制时能讲清银行研发里常见的数据设计关注点。

设计目标：

- 支撑客户列表、客户详情、账户列表、当前风险展示、风险重算、操作日志查询。
- 使用四张核心表完成最小闭环：`customer`、`account`、`risk_record`、`operation_log`。
- 样例数据全部脱敏，不出现真实客户、真实账号、真实手机号、真实证件号、生产地址、密钥或内部系统名称。
- 字段不过度复杂，但体现银行研发常见要求：金额精度、状态码、审计字段、脱敏字段、时间规范、幂等追踪。
- DDL 可直接复制为 Flyway 初始脚本，样例数据可直接复制为 Flyway 数据初始化脚本。

边界说明：

- 第一阶段只做单体应用单库，不做分库分表、不做多租户、不做复杂权限。
- 第二阶段微服务演进仍先共享库分表，不拆独立库；本设计只预留 `customer_no` 作为天然分片键和跨服务业务关联键。
- 风险评级规则只是教学样例，不代表真实风控、反洗钱、征信或核心账务规则。
- 不设计真实登录用户表，第一阶段使用固定操作人或 `X-Operator` 请求头写入审计字段。

## 2. 表清单和关系

| 表名 | 作用 | 主要关系 |
| --- | --- | --- |
| `customer` | 客户基础信息和当前风险快照 | 以 `customer_no` 关联账户和风险记录 |
| `account` | 客户账户样例信息、余额和异常标记 | 多个账户归属一个客户 |
| `risk_record` | 风险评级历史记录 | 一个客户有多条评级记录，最新一条回写到 `customer` 当前风险字段 |
| `operation_log` | 查询、维护、风险重算等操作审计日志 | 通过 `target_biz_no` 记录客户号、账户号或其他业务号 |

关系说明：

- `customer.customer_no` 是客户业务号，作为后端 API 和页面展示的主要业务键。
- `account.customer_no` 关联 `customer.customer_no`，用于详情页展示账户列表。
- `risk_record.customer_no` 关联 `customer.customer_no`，用于展示历史评级记录。
- `customer.current_risk_level`、`customer.current_risk_score`、`customer.last_risk_calculated_at` 是当前风险快照，方便客户列表分页筛选；风险重算时应和最新 `risk_record` 在同一事务内更新。
- `operation_log` 不强制外键，保持审计日志追加写入，避免目标数据删除或变更影响日志可查。

## 3. Flyway 脚本规划

| 文件 | 内容 | 说明 |
| --- | --- | --- |
| `V1__init_schema.sql` | 建表、约束、索引 | 后端第一阶段启动时自动初始化结构 |
| `V2__seed_demo_data.sql` | 脱敏样例客户、账户、风险记录、操作日志 | 支撑录屏和接口测试 |
| `V3__add_microservice_sharding_hint.sql` | 可选，第二阶段再增加分表或服务拆分辅助字段 | 第一阶段不创建 |

建议 Flyway 目录：

```text
backend/src/main/resources/db/migration/
  V1__init_schema.sql
  V2__seed_demo_data.sql
```

本仓库同时提供纯 SQL 草案素材：

```text
docs/db-migration-draft/
  V1__init_schema.sql
  V2__seed_demo_data.sql
```

## 4. MySQL 8 DDL 草案

建议统一：

- 字符集：`utf8mb4`。
- 排序规则：MySQL 8 默认可用 `utf8mb4_0900_ai_ci`。
- 时间存储：数据库存 UTC，字段使用 `datetime(3)` 保留毫秒；应用层统一转换为 `Asia/Shanghai` 展示。
- 金额：必须使用 `decimal`，不使用 `float` 或 `double`。
- 状态码：使用短字符串，便于课程讲解和接口展示；生产可进一步改为字典表或枚举治理。

```sql
SET NAMES utf8mb4;
SET time_zone = '+00:00';

CREATE TABLE customer (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  customer_no VARCHAR(32) NOT NULL COMMENT '客户号，样例业务键',
  customer_name_sample VARCHAR(80) NOT NULL COMMENT '样例客户名称，不是真实姓名',
  customer_type VARCHAR(16) NOT NULL DEFAULT 'PERSONAL' COMMENT '客户类型：PERSONAL/COMPANY',
  mobile_masked VARCHAR(32) NOT NULL COMMENT '脱敏手机号样例',
  id_no_masked VARCHAR(64) NOT NULL COMMENT '脱敏证件号样例',
  status VARCHAR(16) NOT NULL DEFAULT 'ACTIVE' COMMENT '客户状态：ACTIVE/FROZEN/CLOSED',
  current_risk_level VARCHAR(8) NOT NULL DEFAULT 'LOW' COMMENT '当前风险等级：LOW/MEDIUM/HIGH',
  current_risk_score DECIMAL(5,2) NOT NULL DEFAULT 0.00 COMMENT '当前风险分数，0-100',
  last_risk_calculated_at DATETIME(3) NULL COMMENT '最近风险计算时间，UTC',
  remark_sample VARCHAR(200) NULL COMMENT '样例备注，不记录真实敏感信息',
  created_by VARCHAR(64) NOT NULL DEFAULT 'system' COMMENT '创建人',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间，UTC',
  updated_by VARCHAR(64) NOT NULL DEFAULT 'system' COMMENT '更新人',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间，UTC',
  version INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本',
  deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0未删除，1已删除',
  PRIMARY KEY (id),
  UNIQUE KEY uk_customer_no (customer_no),
  KEY idx_customer_name_sample (customer_name_sample),
  KEY idx_customer_risk_status (current_risk_level, status),
  KEY idx_customer_updated_at (updated_at),
  CONSTRAINT chk_customer_type CHECK (customer_type IN ('PERSONAL', 'COMPANY')),
  CONSTRAINT chk_customer_status CHECK (status IN ('ACTIVE', 'FROZEN', 'CLOSED')),
  CONSTRAINT chk_customer_risk_level CHECK (current_risk_level IN ('LOW', 'MEDIUM', 'HIGH')),
  CONSTRAINT chk_customer_risk_score CHECK (current_risk_score >= 0 AND current_risk_score <= 100),
  CONSTRAINT chk_customer_deleted CHECK (deleted IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='客户基础信息样例表';

CREATE TABLE account (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  customer_no VARCHAR(32) NOT NULL COMMENT '客户号',
  account_no_masked VARCHAR(64) NOT NULL COMMENT '脱敏账号样例',
  account_type VARCHAR(16) NOT NULL DEFAULT 'DEBIT' COMMENT '账户类型：DEBIT/CREDIT/LOAN',
  currency CHAR(3) NOT NULL DEFAULT 'CNY' COMMENT '币种，ISO 4217',
  balance DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '账户余额',
  available_balance DECIMAL(18,2) NOT NULL DEFAULT 0.00 COMMENT '可用余额',
  account_status VARCHAR(16) NOT NULL DEFAULT 'NORMAL' COMMENT '账户状态：NORMAL/FROZEN/CLOSED',
  has_abnormal_flag TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否存在样例异常标记',
  opened_at DATE NOT NULL COMMENT '开户日期，样例数据',
  created_by VARCHAR(64) NOT NULL DEFAULT 'system' COMMENT '创建人',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间，UTC',
  updated_by VARCHAR(64) NOT NULL DEFAULT 'system' COMMENT '更新人',
  updated_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3) COMMENT '更新时间，UTC',
  version INT NOT NULL DEFAULT 0 COMMENT '乐观锁版本',
  deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除标记：0未删除，1已删除',
  PRIMARY KEY (id),
  UNIQUE KEY uk_account_no_masked (account_no_masked),
  KEY idx_account_customer_no (customer_no),
  KEY idx_account_status (account_status),
  KEY idx_account_abnormal (has_abnormal_flag),
  CONSTRAINT fk_account_customer_no FOREIGN KEY (customer_no) REFERENCES customer (customer_no),
  CONSTRAINT chk_account_type CHECK (account_type IN ('DEBIT', 'CREDIT', 'LOAN')),
  CONSTRAINT chk_account_status CHECK (account_status IN ('NORMAL', 'FROZEN', 'CLOSED')),
  CONSTRAINT chk_account_abnormal CHECK (has_abnormal_flag IN (0, 1)),
  CONSTRAINT chk_account_deleted CHECK (deleted IN (0, 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='账户样例表';

CREATE TABLE risk_record (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  customer_no VARCHAR(32) NOT NULL COMMENT '客户号',
  risk_level VARCHAR(8) NOT NULL COMMENT '风险等级：LOW/MEDIUM/HIGH',
  risk_score DECIMAL(5,2) NOT NULL COMMENT '风险分数，0-100',
  risk_reason_sample VARCHAR(500) NOT NULL COMMENT '样例风险原因，不包含真实风控信息',
  rule_code VARCHAR(32) NOT NULL DEFAULT 'DEMO_RULE_V1' COMMENT '样例规则编号',
  source_type VARCHAR(16) NOT NULL DEFAULT 'MANUAL' COMMENT '来源：INIT/MANUAL/SCHEDULED',
  calculated_by VARCHAR(64) NOT NULL DEFAULT 'system' COMMENT '计算人或操作人',
  calculated_at DATETIME(3) NOT NULL COMMENT '计算时间，UTC',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间，UTC',
  PRIMARY KEY (id),
  KEY idx_risk_customer_time (customer_no, calculated_at DESC, id DESC),
  KEY idx_risk_level_time (risk_level, calculated_at DESC),
  CONSTRAINT fk_risk_customer_no FOREIGN KEY (customer_no) REFERENCES customer (customer_no),
  CONSTRAINT chk_risk_level CHECK (risk_level IN ('LOW', 'MEDIUM', 'HIGH')),
  CONSTRAINT chk_risk_score CHECK (risk_score >= 0 AND risk_score <= 100),
  CONSTRAINT chk_risk_source CHECK (source_type IN ('INIT', 'MANUAL', 'SCHEDULED'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='风险评级历史样例表';

CREATE TABLE operation_log (
  id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  request_id VARCHAR(64) NOT NULL COMMENT '请求追踪号',
  operator VARCHAR(64) NOT NULL COMMENT '操作人，来自固定值或X-Operator',
  operation_type VARCHAR(32) NOT NULL COMMENT '操作类型：QUERY_CUSTOMER/UPDATE_CUSTOMER/RECALCULATE_RISK/QUERY_LOG',
  target_type VARCHAR(32) NOT NULL COMMENT '目标类型：CUSTOMER/ACCOUNT/RISK/LOG',
  target_biz_no VARCHAR(64) NULL COMMENT '目标业务号，例如客户号或脱敏账号',
  result VARCHAR(16) NOT NULL DEFAULT 'SUCCESS' COMMENT '结果：SUCCESS/FAIL',
  error_message_sample VARCHAR(500) NULL COMMENT '样例错误信息，不记录堆栈和敏感字段',
  operation_desc_sample VARCHAR(500) NULL COMMENT '样例操作描述，不记录敏感明文',
  client_ip_masked VARCHAR(64) NULL COMMENT '脱敏客户端地址样例',
  duration_ms INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '耗时，毫秒',
  created_at DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) COMMENT '创建时间，UTC',
  PRIMARY KEY (id),
  KEY idx_op_request_id (request_id),
  KEY idx_op_operator_time (operator, created_at DESC),
  KEY idx_op_target (target_type, target_biz_no),
  KEY idx_op_type_time (operation_type, created_at DESC),
  CONSTRAINT chk_operation_result CHECK (result IN ('SUCCESS', 'FAIL'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志样例表';
```

## 5. 字段、约束和索引说明

### `customer`

| 字段 | 类型 | 约束/默认值 | 设计说明 |
| --- | --- | --- | --- |
| `id` | `bigint unsigned` | 主键自增 | MyBatis Plus 可使用自增主键 |
| `customer_no` | `varchar(32)` | 唯一非空 | 对外业务键，API 路径和表关联使用 |
| `customer_name_sample` | `varchar(80)` | 非空 | 用 `sample` 明确是样例名称 |
| `mobile_masked` | `varchar(32)` | 非空 | 只存脱敏手机号样例 |
| `id_no_masked` | `varchar(64)` | 非空 | 只存脱敏证件号样例 |
| `current_risk_level` | `varchar(8)` | 默认 `LOW` | 支撑客户列表风险筛选 |
| `current_risk_score` | `decimal(5,2)` | 默认 `0.00` | 分数保留两位小数，避免浮点误差 |
| `created_at/updated_at` | `datetime(3)` | 默认当前时间 | UTC 存储，展示时转 `Asia/Shanghai` |
| `version/deleted` | `int/tinyint` | 默认 `0` | 支撑 MyBatis Plus 乐观锁和逻辑删除演示 |

索引：

- `uk_customer_no`：客户号唯一。
- `idx_customer_name_sample`：客户名称模糊查询的教学演示索引。
- `idx_customer_risk_status`：客户列表按风险和状态筛选。
- `idx_customer_updated_at`：按更新时间排序或增量排查。

### `account`

| 字段 | 类型 | 约束/默认值 | 设计说明 |
| --- | --- | --- | --- |
| `account_no_masked` | `varchar(64)` | 唯一非空 | 使用样例脱敏账号，不保留真实账号 |
| `balance` | `decimal(18,2)` | 默认 `0.00` | 金额字段必须用 `decimal` |
| `available_balance` | `decimal(18,2)` | 默认 `0.00` | 展示余额和可用余额区别 |
| `has_abnormal_flag` | `tinyint(1)` | 默认 `0` | 给风险重算提供简单输入 |
| `opened_at` | `date` | 非空 | 开户日只需日期，不带时间 |

索引：

- `idx_account_customer_no`：详情页查询客户账户列表。
- `idx_account_status`：账户状态筛选。
- `idx_account_abnormal`：风险重算时快速发现异常账户。

### `risk_record`

| 字段 | 类型 | 约束/默认值 | 设计说明 |
| --- | --- | --- | --- |
| `risk_level` | `varchar(8)` | 非空 | `LOW/MEDIUM/HIGH` 三档 |
| `risk_score` | `decimal(5,2)` | 非空 | 0 到 100 分，保留两位小数 |
| `risk_reason_sample` | `varchar(500)` | 非空 | 只写教学原因，不写真实模型特征 |
| `rule_code` | `varchar(32)` | 默认 `DEMO_RULE_V1` | 后续演示规则版本 |
| `source_type` | `varchar(16)` | 默认 `MANUAL` | 区分初始化、人工重算、定时重算 |

索引：

- `idx_risk_customer_time`：查询某客户最新和历史风险记录。
- `idx_risk_level_time`：按风险等级看分布或列表。

### `operation_log`

| 字段 | 类型 | 约束/默认值 | 设计说明 |
| --- | --- | --- | --- |
| `request_id` | `varchar(64)` | 非空 | 与接口统一响应中的 `requestId` 对齐 |
| `operator` | `varchar(64)` | 非空 | 第一阶段来自固定值或 `X-Operator` |
| `operation_type` | `varchar(32)` | 非空 | 记录查询、维护、重算等动作 |
| `target_biz_no` | `varchar(64)` | 可空 | 记录客户号或脱敏账号 |
| `error_message_sample` | `varchar(500)` | 可空 | 不记录堆栈和敏感信息 |
| `client_ip_masked` | `varchar(64)` | 可空 | 地址也按样例脱敏处理 |

索引：

- `idx_op_request_id`：按请求号排查问题。
- `idx_op_operator_time`：按操作人和时间查询。
- `idx_op_target`：按目标客户或对象追溯。
- `idx_op_type_time`：按操作类型查看日志。

## 6. 脱敏样例数据设计

样例数据覆盖：

- 低、中、高风险客户。
- 一个客户多个账户。
- 正常账户、冻结账户、异常标记账户。
- 客户列表筛选、详情展示、账户展示、风险历史、风险重算、操作日志查询。

```sql
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
```

## 7. 风险评级测试数据矩阵

样例风险规则建议：

> 主控说明：以下规则用于课程讲解和后端重算接口的第一版实现参考。初始化样例数据中的 `risk_score` 是为了页面展示和测试分层准备的演示分数，不要求每条历史样例都严格由该公式反推。若后端要把分数作为强断言，需要先统一公式与种子数据。

- 基础分：`20`。
- 客户状态 `FROZEN` 加 `35`，`CLOSED` 加 `30`。
- 任一账户 `has_abnormal_flag = 1` 加 `25`。
- 任一账户 `account_status = FROZEN` 加 `20`，`CLOSED` 加 `10`。
- 所有账户可用余额合计小于 `1000.00` 加 `10`。
- 分数上限为 `100`。
- `0 <= score < 40` 为 `LOW`，`40 <= score < 70` 为 `MEDIUM`，`70 <= score <= 100` 为 `HIGH`。

| 用例 | 客户号 | 样例输入 | 预期等级 | 预期分数方向 | 支撑演示 |
| --- | --- | --- | --- | --- | --- |
| 低风险正常客户 | `CUST100001` | 状态正常、账户正常、余额充足 | `LOW` | 约 `20` | 客户列表、详情默认路径 |
| 中风险异常账户 | `CUST100002` | 状态正常、存在异常标记账户 | `MEDIUM` | `20 + 25 + 低余额加分` | 风险重算后仍为中风险 |
| 高风险冻结客户 | `CUST100003` | 客户冻结、账户冻结、异常标记、可用余额为零 | `HIGH` | 高于 `70` | 高风险标签和详情提示 |
| 企业低风险客户 | `CUST100004` | 企业客户、账户正常、余额充足 | `LOW` | 低分 | 客户类型筛选和企业样例 |
| 中高边界客户 | `CUST100005` | 状态正常、余额低、接近高风险阈值 | `MEDIUM` | 接近 `70` | 边界测试 |
| 销户高风险客户 | `CUST100006` | 客户销户、账户关闭、异常标记 | `HIGH` | 高于 `70` | 状态码和异常日志演示 |
| 不存在客户 | `CUST999999` | 无客户记录 | 业务错误 | 不计算 | `404001` 客户不存在 |
| 参数非法 | 空客户号 | 路径或查询参数为空 | 参数错误 | 不计算 | `400001` 参数校验失败 |

后端测试建议：

- Service 层按矩阵验证低、中、高等级。
- Controller 层验证查询客户、查询风险、重算风险、客户不存在。
- Repository 层验证 `customer_no`、风险等级、客户账户列表、风险历史倒序查询。
- 重算风险时验证：新增 `risk_record`、更新 `customer.current_risk_*`、写入 `operation_log`。

## 8. 银行研发场景数据设计注意事项

金额：

- 所有金额字段使用 `decimal(18,2)`，不使用 `float/double`。
- 金额计算建议在 Java 侧使用 `BigDecimal`，明确舍入规则。
- 金额字段要区分账面余额和可用余额，避免演示时概念混淆。

时间：

- 数据库存 UTC，字段使用 `datetime(3)`。
- 接口返回建议使用 ISO-8601，例如 `2026-06-21T10:00:00+08:00`。
- 应用配置明确 JDBC 时区，页面展示统一转 `Asia/Shanghai`。
- 开户日使用 `date`，因为它是业务日期，不是事件时间。

状态码：

- POC 使用短字符串状态码，便于阅读和录屏讲解。
- 后端接口不要直接返回数据库错误或堆栈。
- 生产环境可引入状态字典、枚举治理、状态流转校验。

脱敏：

- 字段名必须体现 `masked` 或 `sample`，例如 `mobile_masked`、`id_no_masked`、`customer_name_sample`。
- 样例账号使用 `SAMPLE-ACC-****-0001`，不使用可能被误解为真实银行卡号的数字串。
- 日志、错误信息、备注都使用 `sample` 字段，提醒学员不要写入真实敏感信息。

审计：

- 主数据表保留 `created_by`、`created_at`、`updated_by`、`updated_at`。
- 关键操作写入 `operation_log`，包括请求号、操作人、目标业务号、结果和耗时。
- 操作日志建议追加写，不建议物理删除。

一致性：

- 风险重算需要在同一事务里完成：读取客户和账户、插入 `risk_record`、更新 `customer` 当前风险快照、插入 `operation_log`。
- `operation_log` 即使业务失败也应记录失败日志，但注意不要记录敏感参数。
- 第一阶段可以使用外键帮助学员理解关系；如果后续团队规范不使用数据库外键，可在后端通过服务层校验替代。

第二阶段共享库分表预留：

- `customer_no` 是客户、账户、风险记录的共同业务键，适合作为未来分片键。
- 第二阶段先共享库分表即可，不拆独立库，避免在入门课程里引入分布式事务。
- 操作日志可按时间归档或按目标业务号查询，第一阶段不实现归档。

## 9. MyBatis Plus 实体和 Mapper 设计建议

不在本任务中编写 Java 代码，后端子窗口可按以下建议实现：

- 实体类建议：`CustomerEntity`、`AccountEntity`、`RiskRecordEntity`、`OperationLogEntity`。
- 主键：`id` 使用自增策略。
- 逻辑删除：`customer`、`account` 可启用 `deleted`；`risk_record` 和 `operation_log` 保持追加记录，不建议逻辑删除。
- 乐观锁：`customer`、`account` 使用 `version` 字段，演示维护场景的并发保护。
- 自动填充：`created_at`、`updated_at`、`created_by`、`updated_by` 可通过 MyBatis Plus MetaObjectHandler 或服务层统一处理。
- 枚举映射：`customer_type`、`status`、`risk_level`、`account_status` 在 Java 侧可定义枚举，但数据库保持短字符串。
- Mapper 方法建议保持简单：分页查客户、按客户号查客户、按客户号查账户、按客户号倒序查风险记录、分页查操作日志。
- 风险重算不建议写在 Mapper 里，应放在 Service 层，保证事务和日志写入清晰。

## 10. 课程录制讲解顺序

建议把数据库部分录成 15 到 25 分钟：

1. 先回到 POC 架构图，说明数据库只服务第一阶段单体最小闭环。
2. 讲四张表的角色：客户是主线，账户是详情，风险记录是历史，操作日志是审计。
3. 解释为什么 `customer` 里保留当前风险快照：列表筛选快，详情再查历史。
4. 展示脱敏字段命名：`masked` 和 `sample` 是给团队的安全提醒。
5. 展示金额字段：为什么必须用 `decimal(18,2)`。
6. 展示时间字段：为什么统一 UTC 存储、前端按 `Asia/Shanghai` 展示。
7. 展示 Flyway 两个脚本：先结构，后样例数据。
8. 展示低、中、高风险样例客户，说明它们如何支撑页面和接口测试。
9. 讲风险重算事务：读客户账户、写风险记录、回写当前风险、写操作日志。
10. 收束到后端子任务：这些表足够支撑客户列表、详情、风险重算和日志演示。

录屏提醒：

- 不展示真实连接串、真实库地址、真实客户数据。
- 不把样例账号写成真实银行卡格式。
- 讲清楚“这是教学样例规则，不是真实风控模型”。
- 如果现场 AI 生成 DDL，要人工检查金额、时间、脱敏和索引。

## 11. 风险点和待确认问题

风险点：

- 过度设计：一开始引入客户画像、授信、交易流水、复杂风控模型，会把入门课拖重。
- 脱敏误解：字段里如果继续使用 `customer_name`、`mobile`、`id_no`，学员可能照搬到真实项目。
- 时间混乱：本地 MySQL、JDBC、前端展示时区不一致会导致录屏结果看起来“差 8 小时”。
- 外键策略：课程里用外键直观，但部分银行生产规范可能不建议强外键，需要讲清可替换方案。
- 当前风险冗余：`customer` 当前风险和 `risk_record` 最新记录需要事务保持一致。

待确认问题：

- 后端子窗口是否使用数据库外键，还是只保留索引和服务层校验。
- 风险重算接口是否需要返回完整 `risk_record`，还是只返回当前风险摘要。
- 操作日志第一阶段是否记录查询类操作，还是只记录维护和重算类操作。
- 第二阶段共享库分表是否按 `customer_no` 哈希分表，还是只作为架构讲解不落库。

## 12. 主控窗口验收清单

- [ ] `customer`、`account`、`risk_record`、`operation_log` 四张表齐全。
- [ ] DDL 可作为 `V1__init_schema.sql` 直接使用。
- [ ] 样例数据可作为 `V2__seed_demo_data.sql` 直接使用。
- [ ] 客户列表可按客户号、客户名称样例、风险等级、状态筛选。
- [ ] 客户详情可展示基础信息、账户列表、当前风险和历史风险记录。
- [ ] 风险重算可新增历史记录并回写当前风险快照。
- [ ] 操作日志可展示查询、修改、重算、失败样例。
- [ ] 低、中、高风险客户样例齐全。
- [ ] 金额字段全部使用 `decimal`。
- [ ] 时间字段明确 UTC 存储和 `Asia/Shanghai` 展示。
- [ ] 脱敏字段命名包含 `masked` 或 `sample`。
- [ ] 不包含真实客户、真实账号、真实手机号、真实证件号、生产地址、密钥或内部系统名称。
- [ ] 设计能在课程中讲清楚“为什么这么设计”。

## 给主控窗口的简短汇报

数据库设计已足够支撑后续后端最小实现子任务。四张核心表可以覆盖客户列表、详情、账户展示、风险历史、风险重算和操作日志；DDL 和样例数据可直接拆成 Flyway `V1__init_schema.sql`、`V2__seed_demo_data.sql`。当前设计保持最小可运行边界，同时体现银行研发关注的金额精度、时间时区、状态码、审计字段和脱敏要求。
