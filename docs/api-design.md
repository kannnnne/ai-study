# POC REST API 设计

## 主控验收结论

本 API 设计用于“客户风险评级与账户查询”第一阶段单体 POC，覆盖后端最小实现、前端联调、接口测试和课程录屏所需的 REST 契约。设计严格沿用 `docs/poc-architecture.md` 中的统一响应、错误码和第一阶段边界，并以 `docs/database-design.md` 的四张核心表与脱敏样例数据为字段和示例基线。

本 API 设计通过主控窗口验收，可以作为后续后端最小实现、前端页面设计、HTTP 测试样例和课程录屏的接口契约基线。

主控补充裁决：

- 后端实现必须以本文档为 API 契约，不应在第一阶段擅自新增认证、导出、删除、批量导入等接口。
- 新增客户接口文档中复用 `CUST100004` 只是为了展示脱敏样例字段。实际联调和 HTTP 测试必须使用未被种子数据占用的样例号，例如 `CUST199901`。
- 第一阶段所有业务接口统一返回 `ApiResponse<T>` 结构，错误响应也保持同一结构。
- 风险重算接口必须在事务内完成风险记录插入、客户风险快照回写和操作日志写入；测试验收优先检查事务效果，不强断言所有样例客户精确分数。
- 查询类操作是否全部写入操作日志由后端最小实现阶段裁决，但风险重算、客户修改和失败样例必须写日志。

## 1. API 设计目标和边界

设计目标：

- 支撑客户列表、客户详情、新增客户、修改客户、账户列表、当前风险、历史风险、风险重算和操作日志查询。
- 给后端子窗口提供可以直接实现的请求参数、响应结构、错误码和事务效果说明。
- 给前端子窗口提供列表筛选、详情页、重算弹窗、日志页所需字段。
- 给测试验收子窗口提供 HTTP 文件样例规划和主要成功 / 失败场景。
- 给课程录屏提供一条可讲清楚的接口设计顺序。

边界：

- 只设计第一阶段单体 POC 的 REST API 契约，不创建代码项目，不实现 Controller，不写前端，不改数据库表。
- 第一阶段不做真实登录认证，操作人来自固定值或 `X-Operator` 请求头。
- 风险规则是教学样例，不代表真实风控、反洗钱、征信或核心账务规则。
- 示例数据只使用脱敏样例，不出现真实客户、真实账号、真实手机号、真实证件号、生产地址、密钥或内部系统名称。

## 2. 全局约定

### 2.1 Base Path

第一阶段单体服务统一使用：

```text
/api
```

示例：

- `GET /api/customers`
- `GET /api/customers/{customerNo}/risk`
- `POST /api/customers/{customerNo}/risk/recalculate`

健康检查和 OpenAPI 文档入口沿用 Spring 生态默认路径，不放在 `/api` 下：

- `GET /actuator/health`
- `GET /v3/api-docs`
- `GET /swagger-ui/index.html`

### 2.2 统一响应

所有业务接口，无论成功还是失败，都返回统一响应体。错误场景使用对应 HTTP 状态码，同时响应体仍保持统一结构。

```json
{
  "success": true,
  "code": "000000",
  "message": "success",
  "data": {},
  "requestId": "REQ-DEMO-0001",
  "timestamp": "2026-06-21T10:00:00+08:00"
}
```

字段说明：

| 字段 | 类型 | 说明 |
| --- | --- | --- |
| `success` | boolean | 是否成功 |
| `code` | string | 业务错误码，成功固定为 `000000` |
| `message` | string | 面向前端展示和测试断言的简短信息 |
| `data` | object / array / null | 业务数据，错误时可为 `null` |
| `requestId` | string | 请求追踪号 |
| `timestamp` | string | 响应时间，ISO-8601，`Asia/Shanghai` 展示 |

### 2.3 分页格式

列表接口统一使用 `pageNo`、`pageSize`，页码从 `1` 开始。

请求参数：

| 参数 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `pageNo` | integer | `1` | 页码，从 1 开始 |
| `pageSize` | integer | `10` | 每页条数，建议最大 100 |

分页响应数据：

```json
{
  "records": [],
  "pageNo": 1,
  "pageSize": 10,
  "total": 6,
  "pages": 1,
  "hasNext": false
}
```

### 2.4 RequestId

请求追踪号来源优先级：

1. 如果请求头传入 `X-Request-Id`，后端透传。
2. 如果没有传入，后端生成一个样例追踪号，例如 `REQ-DEMO-AUTO-0001`。
3. 响应体 `requestId`、响应头 `X-Request-Id` 和 `operation_log.request_id` 应保持一致。

### 2.5 时间格式

- 数据库存储：UTC，字段使用 `datetime(3)`。
- API 返回：ISO-8601 字符串，统一展示为 `Asia/Shanghai`，例如 `2026-06-21T10:05:00+08:00`。
- 业务日期：如 `openedAt` 使用 `yyyy-MM-dd`，例如 `2024-01-10`。

### 2.6 操作人来源

请求头：

```text
X-Operator: demo-admin
```

约定：

- 查询类示例可使用 `demo-query`。
- 维护和风险重算示例可使用 `demo-admin`。
- 如果请求头缺失，后端使用固定值 `demo-operator`。
- 操作人会写入 `customer.created_by`、`customer.updated_by`、`risk_record.calculated_by` 和 `operation_log.operator` 等审计字段。

### 2.7 字段命名和数据库映射

数据库字段使用下划线命名，API 使用前端更友好的 camelCase。敏感含义字段仍保留 `masked` 或 `sample` 后缀，提醒学员这是脱敏样例。

| 数据库字段 | API 字段 | 说明 |
| --- | --- | --- |
| `customer_no` | `customerNo` | 客户号 |
| `customer_name_sample` | `customerNameSample` | 样例客户名称 |
| `customer_type` | `customerType` | `PERSONAL` / `COMPANY` |
| `mobile_masked` | `mobileMasked` | 脱敏手机号样例 |
| `id_no_masked` | `idNoMasked` | 脱敏证件号样例 |
| `current_risk_level` | `currentRiskLevel` | 当前风险等级 |
| `current_risk_score` | `currentRiskScore` | 当前风险分数 |
| `last_risk_calculated_at` | `lastRiskCalculatedAt` | 最近风险计算时间 |
| `remark_sample` | `remarkSample` | 样例备注 |
| `account_no_masked` | `accountNoMasked` | 脱敏账号样例 |
| `available_balance` | `availableBalance` | 可用余额 |
| `account_status` | `accountStatus` | 账户状态 |
| `has_abnormal_flag` | `hasAbnormalFlag` | 是否有样例异常标记 |
| `risk_reason_sample` | `riskReasonSample` | 样例风险原因 |
| `source_type` | `sourceType` | 风险记录来源 |
| `target_biz_no` | `targetBizNo` | 目标业务号 |
| `error_message_sample` | `errorMessageSample` | 样例错误信息 |
| `operation_desc_sample` | `operationDescSample` | 样例操作描述 |
| `client_ip_masked` | `clientIpMasked` | 脱敏客户端地址 |

## 3. 错误码和 HTTP 状态码映射

| HTTP 状态码 | 业务码 | 名称 | 使用场景 |
| --- | --- | --- | --- |
| 200 | `000000` | 成功 | 请求成功 |
| 400 | `400001` | 参数校验失败 | 页码非法、状态码非法、必填字段缺失、枚举值不合法 |
| 404 | `404001` | 客户不存在 | 查询、修改、账户、风险或重算目标客户不存在 |
| 409 | `409001` | 业务状态冲突 | 客户号重复、乐观锁版本冲突、关闭客户不允许维护或重算 |
| 500 | `500000` | 系统异常 | 未预期异常，不向前端暴露堆栈 |

错误响应示例：

```json
{
  "success": false,
  "code": "404001",
  "message": "客户不存在",
  "data": null,
  "requestId": "REQ-DEMO-404001",
  "timestamp": "2026-06-21T10:40:00+08:00"
}
```

## 4. 客户 API

### 4.1 分页查询客户

```text
GET /api/customers
```

查询参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageNo` | integer | 否 | 默认 `1` |
| `pageSize` | integer | 否 | 默认 `10`，最大 `100` |
| `customerNo` | string | 否 | 客户号精确匹配 |
| `customerNameSample` | string | 否 | 样例客户名称模糊匹配 |
| `customerType` | string | 否 | `PERSONAL` / `COMPANY` |
| `status` | string | 否 | `ACTIVE` / `FROZEN` / `CLOSED` |
| `riskLevel` | string | 否 | `LOW` / `MEDIUM` / `HIGH` |

请求示例：

```http
GET /api/customers?pageNo=1&pageSize=10&riskLevel=MEDIUM&status=ACTIVE HTTP/1.1
Host: localhost:8080
X-Request-Id: REQ-DEMO-CUSTOMER-LIST
X-Operator: demo-query
```

成功响应示例：

```json
{
  "success": true,
  "code": "000000",
  "message": "success",
  "data": {
    "records": [
      {
        "customerNo": "CUST100002",
        "customerNameSample": "样例客户B",
        "customerType": "PERSONAL",
        "mobileMasked": "SAMPLE-MOBILE-****-0002",
        "status": "ACTIVE",
        "currentRiskLevel": "MEDIUM",
        "currentRiskScore": 55.00,
        "lastRiskCalculatedAt": "2026-06-21T10:05:00+08:00",
        "updatedAt": "2026-06-21T10:05:00+08:00"
      },
      {
        "customerNo": "CUST100005",
        "customerNameSample": "样例客户E",
        "customerType": "PERSONAL",
        "mobileMasked": "SAMPLE-MOBILE-****-0005",
        "status": "ACTIVE",
        "currentRiskLevel": "MEDIUM",
        "currentRiskScore": 64.00,
        "lastRiskCalculatedAt": "2026-06-21T10:20:00+08:00",
        "updatedAt": "2026-06-21T10:20:00+08:00"
      }
    ],
    "pageNo": 1,
    "pageSize": 10,
    "total": 2,
    "pages": 1,
    "hasNext": false
  },
  "requestId": "REQ-DEMO-CUSTOMER-LIST",
  "timestamp": "2026-06-21T10:35:00+08:00"
}
```

错误响应示例，风险等级参数非法：

```json
{
  "success": false,
  "code": "400001",
  "message": "riskLevel 只允许 LOW、MEDIUM、HIGH",
  "data": null,
  "requestId": "REQ-DEMO-CUSTOMER-LIST-INVALID",
  "timestamp": "2026-06-21T10:35:10+08:00"
}
```

### 4.2 查询客户详情

```text
GET /api/customers/{customerNo}
```

路径参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `customerNo` | string | 是 | 客户号，例如 `CUST100001` |

请求示例：

```http
GET /api/customers/CUST100001 HTTP/1.1
Host: localhost:8080
X-Request-Id: REQ-DEMO-CUSTOMER-DETAIL
X-Operator: demo-query
```

成功响应示例：

```json
{
  "success": true,
  "code": "000000",
  "message": "success",
  "data": {
    "customerNo": "CUST100001",
    "customerNameSample": "样例客户A",
    "customerType": "PERSONAL",
    "mobileMasked": "SAMPLE-MOBILE-****-0001",
    "idNoMasked": "SAMPLE-ID-********-0001",
    "status": "ACTIVE",
    "currentRiskLevel": "LOW",
    "currentRiskScore": 18.50,
    "lastRiskCalculatedAt": "2026-06-21T10:00:00+08:00",
    "remarkSample": "低风险样例客户",
    "createdBy": "system",
    "createdAt": "2026-06-20T10:00:00+08:00",
    "updatedBy": "system",
    "updatedAt": "2026-06-21T10:00:00+08:00",
    "version": 0
  },
  "requestId": "REQ-DEMO-CUSTOMER-DETAIL",
  "timestamp": "2026-06-21T10:36:00+08:00"
}
```

错误响应示例：

```json
{
  "success": false,
  "code": "404001",
  "message": "客户不存在",
  "data": null,
  "requestId": "REQ-DEMO-CUSTOMER-DETAIL-404",
  "timestamp": "2026-06-21T10:36:10+08:00"
}
```

### 4.3 新增客户

```text
POST /api/customers
```

请求体：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `customerNo` | string | 是 | 客户号，唯一 |
| `customerNameSample` | string | 是 | 样例客户名称 |
| `customerType` | string | 是 | `PERSONAL` / `COMPANY` |
| `mobileMasked` | string | 是 | 脱敏手机号样例 |
| `idNoMasked` | string | 是 | 脱敏证件号样例 |
| `status` | string | 是 | `ACTIVE` / `FROZEN` / `CLOSED` |
| `remarkSample` | string | 否 | 样例备注 |

请求示例：

```http
POST /api/customers HTTP/1.1
Host: localhost:8080
Content-Type: application/json
X-Request-Id: REQ-DEMO-CUSTOMER-CREATE
X-Operator: demo-admin

{
  "customerNo": "CUST100004",
  "customerNameSample": "样例企业D",
  "customerType": "COMPANY",
  "mobileMasked": "SAMPLE-MOBILE-****-0004",
  "idNoMasked": "SAMPLE-ID-********-0004",
  "status": "ACTIVE",
  "remarkSample": "企业客户低风险样例"
}
```

说明：为满足课程材料只使用既有脱敏样例数据的要求，本文档复用 `docs/database-design.md` 中的 `CUST100004` 展示请求和响应结构。实际 HTTP 联调时，如果种子数据已加载，应把客户号替换成未占用的样例号，且仍保持 `SAMPLE-*` 脱敏格式。

成功响应示例：

```json
{
  "success": true,
  "code": "000000",
  "message": "success",
  "data": {
    "customerNo": "CUST100004",
    "customerNameSample": "样例企业D",
    "customerType": "COMPANY",
    "mobileMasked": "SAMPLE-MOBILE-****-0004",
    "idNoMasked": "SAMPLE-ID-********-0004",
    "status": "ACTIVE",
    "currentRiskLevel": "LOW",
    "currentRiskScore": 0.00,
    "lastRiskCalculatedAt": null,
    "remarkSample": "企业客户低风险样例",
    "createdBy": "demo-admin",
    "createdAt": "2026-06-21T10:37:00+08:00",
    "updatedBy": "demo-admin",
    "updatedAt": "2026-06-21T10:37:00+08:00",
    "version": 0
  },
  "requestId": "REQ-DEMO-CUSTOMER-CREATE",
  "timestamp": "2026-06-21T10:37:00+08:00"
}
```

错误响应示例，客户号重复：

```json
{
  "success": false,
  "code": "409001",
  "message": "客户号已存在",
  "data": null,
  "requestId": "REQ-DEMO-CUSTOMER-CREATE-DUP",
  "timestamp": "2026-06-21T10:37:10+08:00"
}
```

### 4.4 修改客户

```text
PUT /api/customers/{customerNo}
```

请求体：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `customerNameSample` | string | 是 | 样例客户名称 |
| `customerType` | string | 是 | `PERSONAL` / `COMPANY` |
| `mobileMasked` | string | 是 | 脱敏手机号样例 |
| `idNoMasked` | string | 是 | 脱敏证件号样例 |
| `status` | string | 是 | `ACTIVE` / `FROZEN` / `CLOSED` |
| `remarkSample` | string | 否 | 样例备注 |
| `version` | integer | 是 | 乐观锁版本 |

请求示例：

```http
PUT /api/customers/CUST100003 HTTP/1.1
Host: localhost:8080
Content-Type: application/json
X-Request-Id: REQ-DEMO-CUSTOMER-UPDATE
X-Operator: demo-admin

{
  "customerNameSample": "样例客户C",
  "customerType": "PERSONAL",
  "mobileMasked": "SAMPLE-MOBILE-****-0003",
  "idNoMasked": "SAMPLE-ID-********-0003",
  "status": "FROZEN",
  "remarkSample": "高风险样例客户，存在冻结和异常标记",
  "version": 0
}
```

成功响应示例：

```json
{
  "success": true,
  "code": "000000",
  "message": "success",
  "data": {
    "customerNo": "CUST100003",
    "customerNameSample": "样例客户C",
    "customerType": "PERSONAL",
    "mobileMasked": "SAMPLE-MOBILE-****-0003",
    "idNoMasked": "SAMPLE-ID-********-0003",
    "status": "FROZEN",
    "currentRiskLevel": "HIGH",
    "currentRiskScore": 86.00,
    "lastRiskCalculatedAt": "2026-06-21T10:10:00+08:00",
    "remarkSample": "高风险样例客户，存在冻结和异常标记",
    "updatedBy": "demo-admin",
    "updatedAt": "2026-06-21T10:38:00+08:00",
    "version": 1
  },
  "requestId": "REQ-DEMO-CUSTOMER-UPDATE",
  "timestamp": "2026-06-21T10:38:00+08:00"
}
```

错误响应示例，版本冲突：

```json
{
  "success": false,
  "code": "409001",
  "message": "客户信息已被更新，请刷新后重试",
  "data": null,
  "requestId": "REQ-DEMO-CUSTOMER-UPDATE-CONFLICT",
  "timestamp": "2026-06-21T10:38:10+08:00"
}
```

## 5. 账户 API

### 5.1 按客户查询账户列表

```text
GET /api/customers/{customerNo}/accounts
```

请求示例：

```http
GET /api/customers/CUST100001/accounts HTTP/1.1
Host: localhost:8080
X-Request-Id: REQ-DEMO-ACCOUNT-LIST
X-Operator: demo-query
```

成功响应示例：

```json
{
  "success": true,
  "code": "000000",
  "message": "success",
  "data": [
    {
      "accountNoMasked": "SAMPLE-ACC-****-0001",
      "customerNo": "CUST100001",
      "accountType": "DEBIT",
      "currency": "CNY",
      "balance": 12500.50,
      "availableBalance": 12500.50,
      "accountStatus": "NORMAL",
      "hasAbnormalFlag": false,
      "openedAt": "2024-01-10",
      "updatedAt": "2026-06-20T11:00:00+08:00"
    },
    {
      "accountNoMasked": "SAMPLE-ACC-****-0002",
      "customerNo": "CUST100001",
      "accountType": "CREDIT",
      "currency": "CNY",
      "balance": 3200.00,
      "availableBalance": 3200.00,
      "accountStatus": "NORMAL",
      "hasAbnormalFlag": false,
      "openedAt": "2024-05-18",
      "updatedAt": "2026-06-20T11:01:00+08:00"
    }
  ],
  "requestId": "REQ-DEMO-ACCOUNT-LIST",
  "timestamp": "2026-06-21T10:39:00+08:00"
}
```

错误响应示例：

```json
{
  "success": false,
  "code": "404001",
  "message": "客户不存在",
  "data": null,
  "requestId": "REQ-DEMO-ACCOUNT-LIST-404",
  "timestamp": "2026-06-21T10:39:10+08:00"
}
```

## 6. 风险 API

### 6.1 查询当前风险

```text
GET /api/customers/{customerNo}/risk
```

请求示例：

```http
GET /api/customers/CUST100003/risk HTTP/1.1
Host: localhost:8080
X-Request-Id: REQ-DEMO-RISK-CURRENT
X-Operator: demo-query
```

成功响应示例：

```json
{
  "success": true,
  "code": "000000",
  "message": "success",
  "data": {
    "customerNo": "CUST100003",
    "customerNameSample": "样例客户C",
    "currentRiskLevel": "HIGH",
    "currentRiskScore": 86.00,
    "lastRiskCalculatedAt": "2026-06-21T10:10:00+08:00",
    "latestRiskReasonSample": "样例规则：客户状态冻结，账户存在异常标记，可用余额为零",
    "ruleCode": "DEMO_RULE_V1"
  },
  "requestId": "REQ-DEMO-RISK-CURRENT",
  "timestamp": "2026-06-21T10:40:00+08:00"
}
```

错误响应示例：

```json
{
  "success": false,
  "code": "404001",
  "message": "客户不存在",
  "data": null,
  "requestId": "REQ-DEMO-RISK-CURRENT-404",
  "timestamp": "2026-06-21T10:40:10+08:00"
}
```

### 6.2 查询历史风险记录

```text
GET /api/customers/{customerNo}/risk-records
```

查询参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageNo` | integer | 否 | 默认 `1` |
| `pageSize` | integer | 否 | 默认 `10` |
| `riskLevel` | string | 否 | `LOW` / `MEDIUM` / `HIGH` |

请求示例：

```http
GET /api/customers/CUST100002/risk-records?pageNo=1&pageSize=10 HTTP/1.1
Host: localhost:8080
X-Request-Id: REQ-DEMO-RISK-HISTORY
X-Operator: demo-query
```

成功响应示例：

```json
{
  "success": true,
  "code": "000000",
  "message": "success",
  "data": {
    "records": [
      {
        "customerNo": "CUST100002",
        "riskLevel": "MEDIUM",
        "riskScore": 55.00,
        "riskReasonSample": "样例规则：存在异常标记账户，风险分数上调",
        "ruleCode": "DEMO_RULE_V1",
        "sourceType": "MANUAL",
        "calculatedBy": "demo-operator",
        "calculatedAt": "2026-06-21T10:05:00+08:00",
        "createdAt": "2026-06-21T10:05:00+08:00"
      },
      {
        "customerNo": "CUST100002",
        "riskLevel": "MEDIUM",
        "riskScore": 45.00,
        "riskReasonSample": "样例规则：余额较低，需关注",
        "ruleCode": "DEMO_RULE_V1",
        "sourceType": "INIT",
        "calculatedBy": "system",
        "calculatedAt": "2026-06-20T10:05:00+08:00",
        "createdAt": "2026-06-20T10:05:00+08:00"
      }
    ],
    "pageNo": 1,
    "pageSize": 10,
    "total": 2,
    "pages": 1,
    "hasNext": false
  },
  "requestId": "REQ-DEMO-RISK-HISTORY",
  "timestamp": "2026-06-21T10:41:00+08:00"
}
```

错误响应示例，分页参数非法：

```json
{
  "success": false,
  "code": "400001",
  "message": "pageSize 最大值为 100",
  "data": null,
  "requestId": "REQ-DEMO-RISK-HISTORY-INVALID",
  "timestamp": "2026-06-21T10:41:10+08:00"
}
```

### 6.3 触发风险重算

```text
POST /api/customers/{customerNo}/risk/recalculate
```

请求体：

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `reasonSample` | string | 否 | 触发重算的样例原因 |
| `ruleCode` | string | 否 | 默认 `DEMO_RULE_V1` |

事务效果：

同一事务内完成以下动作：

1. 校验客户存在且未被逻辑删除。
2. 读取客户状态与账户列表。
3. 按教学样例规则计算风险等级和分数。
4. 新增一条 `risk_record`，`source_type = MANUAL`，`calculated_by` 来自 `X-Operator`。
5. 回写 `customer.current_risk_level`、`customer.current_risk_score`、`customer.last_risk_calculated_at`、`customer.updated_by`、`customer.updated_at`。
6. 写入 `operation_log`，`operation_type = RECALCULATE_RISK`，成功时 `result = SUCCESS`。
7. 业务失败时也应写入失败日志，`result = FAIL`，但不记录敏感参数或堆栈。

请求示例：

```http
POST /api/customers/CUST100002/risk/recalculate HTTP/1.1
Host: localhost:8080
Content-Type: application/json
X-Request-Id: REQ-DEMO-RISK-RECALCULATE
X-Operator: demo-admin

{
  "reasonSample": "课程录屏手动触发风险重算样例",
  "ruleCode": "DEMO_RULE_V1"
}
```

成功响应示例：

```json
{
  "success": true,
  "code": "000000",
  "message": "success",
  "data": {
    "customerNo": "CUST100002",
    "riskLevel": "MEDIUM",
    "riskScore": 55.00,
    "riskReasonSample": "样例规则：存在异常标记账户，风险分数上调",
    "ruleCode": "DEMO_RULE_V1",
    "sourceType": "MANUAL",
    "calculatedBy": "demo-admin",
    "calculatedAt": "2026-06-21T10:42:00+08:00",
    "transactionEffect": {
      "riskRecordInserted": true,
      "customerRiskSnapshotUpdated": true,
      "operationLogInserted": true
    }
  },
  "requestId": "REQ-DEMO-RISK-RECALCULATE",
  "timestamp": "2026-06-21T10:42:00+08:00"
}
```

错误响应示例，客户不存在：

```json
{
  "success": false,
  "code": "404001",
  "message": "客户不存在",
  "data": null,
  "requestId": "REQ-DEMO-RISK-RECALCULATE-404",
  "timestamp": "2026-06-21T10:42:10+08:00"
}
```

错误响应示例，状态冲突：

```json
{
  "success": false,
  "code": "409001",
  "message": "关闭状态客户不允许触发风险重算",
  "data": null,
  "requestId": "REQ-DEMO-RISK-RECALCULATE-CONFLICT",
  "timestamp": "2026-06-21T10:42:20+08:00"
}
```

## 7. 操作日志 API

### 7.1 分页查询操作日志

```text
GET /api/operation-logs
```

查询参数：

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageNo` | integer | 否 | 默认 `1` |
| `pageSize` | integer | 否 | 默认 `10` |
| `requestId` | string | 否 | 请求追踪号 |
| `operator` | string | 否 | 操作人 |
| `operationType` | string | 否 | `QUERY_CUSTOMER` / `UPDATE_CUSTOMER` / `RECALCULATE_RISK` / `QUERY_LOG` |
| `targetType` | string | 否 | `CUSTOMER` / `ACCOUNT` / `RISK` / `LOG` |
| `targetBizNo` | string | 否 | 客户号或脱敏账号 |
| `result` | string | 否 | `SUCCESS` / `FAIL` |
| `startTime` | string | 否 | ISO-8601，按创建时间筛选 |
| `endTime` | string | 否 | ISO-8601，按创建时间筛选 |

请求示例：

```http
GET /api/operation-logs?pageNo=1&pageSize=10&operationType=RECALCULATE_RISK HTTP/1.1
Host: localhost:8080
X-Request-Id: REQ-DEMO-LOG-LIST
X-Operator: demo-admin
```

成功响应示例：

```json
{
  "success": true,
  "code": "000000",
  "message": "success",
  "data": {
    "records": [
      {
        "requestId": "REQ-DEMO-0002",
        "operator": "demo-admin",
        "operationType": "RECALCULATE_RISK",
        "targetType": "RISK",
        "targetBizNo": "CUST100002",
        "result": "SUCCESS",
        "errorMessageSample": null,
        "operationDescSample": "触发风险重算样例，结果为MEDIUM",
        "clientIpMasked": "SAMPLE-IP-10.*.*.2",
        "durationMs": 82,
        "createdAt": "2026-06-21T10:31:00+08:00"
      },
      {
        "requestId": "REQ-DEMO-0005",
        "operator": "demo-admin",
        "operationType": "RECALCULATE_RISK",
        "targetType": "RISK",
        "targetBizNo": "CUST999999",
        "result": "FAIL",
        "errorMessageSample": "样例错误：客户不存在",
        "operationDescSample": "触发不存在客户风险重算样例",
        "clientIpMasked": "SAMPLE-IP-10.*.*.5",
        "durationMs": 41,
        "createdAt": "2026-06-21T10:34:00+08:00"
      }
    ],
    "pageNo": 1,
    "pageSize": 10,
    "total": 2,
    "pages": 1,
    "hasNext": false
  },
  "requestId": "REQ-DEMO-LOG-LIST",
  "timestamp": "2026-06-21T10:43:00+08:00"
}
```

错误响应示例，时间范围非法：

```json
{
  "success": false,
  "code": "400001",
  "message": "startTime 不能晚于 endTime",
  "data": null,
  "requestId": "REQ-DEMO-LOG-LIST-INVALID",
  "timestamp": "2026-06-21T10:43:10+08:00"
}
```

## 8. 健康检查和 OpenAPI 文档入口

### 8.1 健康检查

```text
GET /actuator/health
```

成功响应示例：

```json
{
  "status": "UP"
}
```

### 8.2 OpenAPI 入口

```text
GET /v3/api-docs
GET /swagger-ui/index.html
```

OpenAPI 分组建议：

| 分组 | 路径 |
| --- | --- |
| `customer-api` | `/api/customers/**`，不含账户和风险子路径也可接受 |
| `account-api` | `/api/customers/{customerNo}/accounts` |
| `risk-api` | `/api/customers/{customerNo}/risk/**`、`/api/customers/{customerNo}/risk-records` |
| `operation-log-api` | `/api/operation-logs` |
| `support-api` | `/actuator/health` |

课程录屏时建议先展示 Swagger UI 的分组，再进入 HTTP 文件跑关键链路。

## 9. 第一阶段到第二阶段的路由演进建议

第一阶段单体：

```text
Vue -> /api/customers -> Spring Boot 单体
Vue -> /api/customers/{customerNo}/risk -> Spring Boot 单体
```

第二阶段 Gateway：

```text
Vue -> /api/customers/** -> gateway-service -> customer-service
Vue -> /api/customers/{customerNo}/accounts -> gateway-service -> customer-service
Vue -> /api/customers/{customerNo}/risk/** -> gateway-service -> risk-service
Vue -> /api/operation-logs -> gateway-service -> 各服务本地日志或扩展 audit 能力
```

兼容原则：

- 前端仍调用 `/api/...`，不直接感知 `customer-service` 或 `risk-service`。
- Gateway 负责把同一路由转发到拆分后的服务，保持前端接口路径稳定。
- 统一响应结构、错误码、`X-Request-Id`、`X-Operator` 继续保持一致。
- 第二阶段如果服务内部调用需要新路径，可使用内部路由，例如 `/internal/risk/...`，不要暴露给前端课程主线。

## 10. 接口安全和脱敏注意事项

- API 不返回数据库自增主键 `id`，对外统一使用 `customerNo`、`accountNoMasked` 等业务号或脱敏号。
- 不返回完整手机号、证件号、账号，只返回 `mobileMasked`、`idNoMasked`、`accountNoMasked`。
- 错误响应不返回 SQL、堆栈、连接串、表结构异常细节。
- 操作日志的 `errorMessageSample` 只记录样例错误，不记录请求体完整内容。
- `X-Operator` 只用于教学模拟，不代表真实身份认证。
- 录屏、截图、HTTP 文件都只保留 `SAMPLE-*` 和 `CUST10000*` 样例数据。
- 如果后续新增导出能力，第一阶段不建议实现；需要实现时必须单独做脱敏和权限设计。

## 11. HTTP 测试样例规划

建议后续由测试验收子窗口创建以下文件：

```text
docs/http/
  customer.http
  account.http
  risk.http
  operation-log.http
  support.http
```

`docs/http/customer.http` 建议包含：

- 分页查询客户成功：`riskLevel=MEDIUM&status=ACTIVE`。
- 分页查询参数错误：`riskLevel=UNKNOWN`。
- 查询 `CUST100001` 详情成功。
- 查询 `CUST999999` 详情返回 `404001`。
- 新增样例客户成功。
- 新增重复客户返回 `409001`。
- 修改客户成功。
- 修改版本冲突返回 `409001`。

`docs/http/account.http` 建议包含：

- 查询 `CUST100001` 账户列表成功。
- 查询 `CUST999999` 账户列表返回 `404001`。

`docs/http/risk.http` 建议包含：

- 查询 `CUST100003` 当前风险成功。
- 查询 `CUST100002` 历史风险成功。
- 风险历史分页参数错误返回 `400001`。
- 重算 `CUST100002` 成功，并检查返回 `transactionEffect`。
- 重算 `CUST999999` 返回 `404001`。
- 重算关闭状态客户返回 `409001`。

`docs/http/operation-log.http` 建议包含：

- 按 `operationType=RECALCULATE_RISK` 查询成功。
- 按 `result=FAIL` 查询失败日志样例。
- 时间范围非法返回 `400001`。

`docs/http/support.http` 建议包含：

- 健康检查。
- OpenAPI JSON。
- Swagger UI 地址说明。

## 12. 课程录制讲解顺序

1. 回到 POC 架构基线，说明第一阶段为什么只暴露 `/api/...`。
2. 讲统一响应：成功和失败都能让前端、测试、日志按同一种结构处理。
3. 讲错误码：参数错误、客户不存在、业务状态冲突、系统异常四类足够支撑最小闭环。
4. 讲字段映射：数据库下划线、API camelCase，同时保留 `masked` / `sample` 安全提示。
5. 讲客户列表：筛选条件如何支撑前端列表页。
6. 讲客户详情和账户列表：客户号作为业务主线。
7. 讲当前风险和历史风险：`customer` 当前快照 + `risk_record` 历史记录。
8. 重点讲风险重算事务：新增记录、回写快照、写操作日志。
9. 讲操作日志：按请求号、操作人、类型和目标业务号排查。
10. 讲 OpenAPI 和 HTTP 文件：让后端实现、前端联调、接口测试都对着同一份契约。
11. 讲第二阶段路由兼容：前端保持 `/api/...`，Gateway 在后面做转发。
12. 以安全检查收尾：只用脱敏样例，不暴露真实数据和内部信息。

## 13. 风险点和待确认问题

风险点：

- API 字段如果直接暴露数据库 `id`，后续前端和日志容易依赖内部主键。
- 新增 / 修改客户如果不带 `version` 或不处理并发，课程中很难讲清维护场景的状态冲突。
- 风险重算如果只插入 `risk_record` 不回写 `customer` 快照，客户列表风险筛选会和详情历史不一致。
- 操作日志如果记录完整请求体，可能误导学员在真实项目里写入敏感信息。
- 第二阶段如果直接把前端路径改成服务名，会破坏课程对“Gateway 保持兼容”的演示价值。

待确认问题：

- 第一阶段是否允许关闭状态 `CLOSED` 客户修改基础信息。本文建议不允许重算，修改规则可由后端子窗口按课程演示需要裁决。
- 新增客户后是否立即生成初始化风险记录。本文建议新增时只给默认低风险快照，录屏中通过重算接口生成风险记录，更利于讲清事务链路。
- 查询类操作是否全部写操作日志。本文建议第一阶段至少记录详情查询、维护、重算和日志查询；列表查询是否记录可由后端实现按复杂度取舍。
- 风险重算分数是否精确断言。本文沿用数据库设计裁决，优先验收等级、记录写入、快照回写和日志写入，不强断言所有样例精确分数。

## 14. 主控窗口验收清单

- [ ] API 覆盖客户列表、详情、新增、修改。
- [ ] API 覆盖按客户查询账户列表。
- [ ] API 覆盖当前风险、历史风险、风险重算。
- [ ] API 覆盖操作日志分页查询。
- [ ] 统一响应结构与 `docs/poc-architecture.md` 保持一致。
- [ ] 错误码覆盖 `400001`、`404001`、`409001`、`500000`。
- [ ] HTTP 状态码和业务错误码映射清楚。
- [ ] 分页格式可直接支撑前端列表页。
- [ ] 客户列表筛选包含客户号、样例客户名、风险等级、客户状态。
- [ ] 请求和响应字段与数据库设计可追溯映射。
- [ ] API 对前端使用 camelCase，脱敏字段保留 `masked` / `sample` 后缀。
- [ ] 每个核心接口都有请求示例、成功响应示例和至少一个错误响应示例。
- [ ] 风险重算明确同一事务内新增 `risk_record`、回写 `customer` 当前风险快照、写入 `operation_log`。
- [ ] 示例数据为脱敏样例，不含真实客户、账号、手机号、证件号、生产地址、密钥或内部系统名称。
- [ ] 第二阶段路由演进说明能保持前端 `/api/...` 兼容。
- [ ] HTTP 测试样例规划可交给测试验收子窗口继续落地。
- [ ] 课程录制讲解顺序清楚，适合讲师直接展开。

## 给主控窗口的简短汇报

API 设计已足够支撑后续后端最小实现和前端联调。当前契约覆盖客户列表、详情、维护、账户、当前风险、历史风险、风险重算和操作日志，统一响应与错误码保持架构基线一致，字段映射贴合数据库设计，并明确了风险重算的事务效果。后端子窗口可以直接按本文件实现 Controller / Service，前端子窗口可以直接按本文件设计列表页、详情页、重算弹窗和日志页联调。
