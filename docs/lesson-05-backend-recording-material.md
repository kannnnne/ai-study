# 第 5 课录屏材料：后端最小服务开发

## 1. 标题与目标

标题：第 5 课：后端最小服务开发

一句话目标：演示如何在已完成需求分析和设计评审的前提下，让 AI 辅助实现一个可运行、可测试、可验收的 Spring Boot 3.x 单体后端，同时由人控制 API 契约、事务边界、安全脱敏和质量验收。

## 2. 本课要解决的问题

第 4 课已经把客户风险评级与账户查询 POC 收敛为第一阶段单体设计：Spring Boot 后端、Flyway 样例数据、MyBatis Plus、REST API、统一响应、错误码、`requestId`、`X-Operator`、风险重算事务和自动化测试。

第 5 课要解决的问题是：设计评审之后，如何把后端开发拆成 AI 能稳定完成的小步，并确保 AI 没有擅自改契约、扩大范围或绕过测试。

本课重点不是现场从空目录写完整后端，而是讲清楚后端开发的受控流程：

```text
设计基线 -> API 契约 -> 数据库脚本 -> 后端骨架 -> 分层实现 -> 事务闭环 -> 自动化测试 -> 人工验收
```

AI 在本课里的价值：

- 帮助生成项目骨架、DTO、Controller、Service、Mapper、Assembler 和测试草案。
- 帮助按设计基线补齐异常处理、统一响应、`requestId`、操作日志和测试用例。
- 帮助做代码审查，检查契约漂移、事务遗漏、日志敏感信息和边界扩散。

人的职责：

- 决定第一阶段只做单体后端，不引入真实登录、权限、MQ、缓存、微服务和分布式事务。
- 锁定 `docs/api-design.md` 中的 API 契约，不让 AI 自行新增或改字段。
- 判断风险重算事务是否真的写入 `risk_record`、回写 `customer` 快照并写入 `operation_log`。
- 运行测试、审查差异、检查样例数据和配置不含真实敏感信息。

本课核心口径：

```text
AI 可以辅助写代码，但不能替代 API 契约、事务设计、测试和人工验收。
```

## 3. 推荐时长与章节安排

建议时长：35 到 45 分钟。

| 章节 | 内容 | 建议时长 |
| --- | --- | --- |
| 1 | 开场：从设计评审进入后端开发 | 3 分钟 |
| 2 | 录制前准备：后端位置、JDK、Maven、profile | 4 分钟 |
| 3 | 展示基线：API、数据库、后端实现计划 | 5 分钟 |
| 4 | 展示 `poc/backend` 项目骨架和依赖 | 5 分钟 |
| 5 | 讲解统一响应、异常、`requestId`、`X-Operator` | 6 分钟 |
| 6 | 讲解客户、账户、风险、日志模块分层 | 7 分钟 |
| 7 | 重点讲风险重算事务 | 6 分钟 |
| 8 | 展示 Flyway 样例数据和 test profile + H2 | 4 分钟 |
| 9 | 展示或运行 `mvn test`，讲测试覆盖点 | 5 分钟 |
| 10 | AI 提示词、常见错误、讲义和练习收束 | 5 分钟 |

## 4. 录制前准备

### 4.1 后端项目位置

后端代码位置：

```text
poc/backend
```

关键目录：

```text
poc/backend/
  pom.xml
  src/main/java/com/example/aistudy/riskpoc/
    common/
    config/
    customer/
    account/
    risk/
    operationlog/
  src/main/resources/
    application.yml
    db/migration/
      V1__init_schema.sql
      V2__seed_demo_data.sql
  src/test/java/com/example/aistudy/riskpoc/
  src/test/resources/application-test.yml
```

录制时建议先说明：本课只讲后端第一阶段单体服务，不修改前端、不调整数据库脚本、不接真实银行环境。

### 4.2 工具和版本

后端基线：

- JDK 21。
- Spring Boot 3.3.5。
- Maven。
- MyBatis Plus 3.5.9。
- Flyway。
- Springdoc OpenAPI。
- Spring Boot Actuator。
- 测试默认使用 Spring Boot Test + H2。

默认演示路径：

```powershell
cd D:\project\ai-study\poc\backend
mvn test
```

如果要启动后端录制接口：

```powershell
cd D:\project\ai-study\poc\backend
mvn spring-boot:test-run -Dspring-boot.run.profiles=test
```

健康检查：

```powershell
Invoke-WebRequest http://localhost:8080/actuator/health
```

### 4.3 profile 说明

录制默认使用 test profile + H2：

- 不依赖本机 MySQL。
- H2 内存库会加载测试 Flyway 脚本。
- 适合课程演示、自动化测试和错误路径复验。

MySQL 是可选本地验证：

- 默认配置只指向 `localhost:3306/ai_study_risk_poc`。
- 账号是教学样例 `risk_poc` / `risk_poc_demo`。
- 不连接真实银行数据库。
- 不展示生产地址、内部系统地址、真实账号、真实密码或密钥。

## 5. 设计基线快速回顾

录制时建议打开三份基线文档：

| 文件 | 用途 |
| --- | --- |
| `docs/api-design.md` | 锁定 REST 路径、统一响应、错误码、字段和风险重算事务效果 |
| `docs/database-design.md` | 锁定四张表、脱敏字段、金额、时间和样例数据 |
| `docs/backend-implementation-plan.md` | 锁定后端分层、模块、核心类、测试计划和边界 |

第一阶段后端必须覆盖：

- 客户列表、详情、新增、修改。
- 账户列表。
- 当前风险、历史风险、风险重算。
- 操作日志查询。
- 统一响应 `ApiResponse<T>`。
- 分页响应 `PageResponse<T>`。
- 错误码 `000000`、`400001`、`404001`、`409001`、`500000`。
- `X-Request-Id` 和 `X-Operator`。
- Flyway 初始化脱敏样例数据。
- 自动化测试和接口验收。

第一阶段明确不做：

- 真实登录。
- 权限系统。
- MQ。
- 缓存。
- 分布式事务。
- 真实风控模型。
- 真实核心、风控、征信、反洗钱、认证中心等系统接入。
- 真实客户数据、内部系统地址、密钥或生产配置。

## 6. 后端项目骨架讲解

### 6.1 Maven 依赖

打开 `poc/backend/pom.xml`，重点讲：

- `spring-boot-starter-web`：REST API。
- `spring-boot-starter-validation`：参数校验。
- `mybatis-plus-spring-boot3-starter`：Mapper 和分页。
- `mysql-connector-j`：本地 MySQL 可选验证。
- `flyway-core`、`flyway-mysql`：数据库迁移。
- `springdoc-openapi-starter-webmvc-ui`：Swagger UI。
- `spring-boot-starter-actuator`：健康检查。
- `h2` 只在 test scope，用于测试 profile。
- `spring-boot-starter-test`：单元和集成测试。

讲解口径：

```text
这里的依赖都是为了第一阶段最小闭环服务。我们没有引入 Spring Security、Redis、Kafka、RabbitMQ、Nacos 或 Spring Cloud，因为这些属于后续阶段，不能让 AI 把课程范围提前拖重。
```

### 6.2 包结构

后端基础包名：

```text
com.example.aistudy.riskpoc
```

模块划分：

| 模块 | 职责 |
| --- | --- |
| `common` | 统一响应、分页、错误码、异常、请求上下文、工具方法 |
| `config` | MyBatis Plus、OpenAPI 等配置 |
| `customer` | 客户列表、详情、新增、修改 |
| `account` | 客户账户列表 |
| `risk` | 当前风险、历史风险、风险重算 |
| `operationlog` | 操作日志查询和日志写入 |

每个业务模块基本按以下结构拆分：

```text
controller -> service -> mapper -> entity
                  |
               assembler -> dto
```

讲解重点：

- Controller 不写复杂业务，只接收参数、调用 Service、返回统一响应。
- Service 负责业务流程、事务、校验和日志。
- Mapper 只做数据访问，不承载风控规则。
- Entity 对齐数据库表。
- DTO 对齐 API 契约。
- Assembler 负责 Entity 到 DTO 的转换，避免 Controller 里堆字段拼装。

## 7. 关键实现讲解

### 7.1 统一响应 `ApiResponse<T>`

打开：

```text
poc/backend/src/main/java/com/example/aistudy/riskpoc/common/api/ApiResponse.java
```

讲解点：

- 成功响应固定 `success=true`、`code=000000`、`message=success`。
- 错误响应固定 `success=false`，错误码来自 `ErrorCode`。
- `requestId` 来自 `RequestContextHolder`。
- `timestamp` 使用 `Asia/Shanghai` 展示。
- 所有业务接口返回统一结构，便于前端、测试和录屏讲解。

口播示例：

```text
统一响应不是为了形式统一，而是为了让前端、测试、日志和排查链路对齐。成功、失败、参数错误、客户不存在，都能用同一个结构读取 code、message 和 requestId。
```

### 7.2 分页响应 `PageResponse<T>`

打开：

```text
poc/backend/src/main/java/com/example/aistudy/riskpoc/common/api/PageResponse.java
```

讲解点：

- 列表接口统一返回 `records`、`pageNo`、`pageSize`、`total`、`pages`、`hasNext`。
- 对齐 `docs/api-design.md` 的分页契约。
- 前端客户列表、风险历史、操作日志都可以复用同一种分页展示。

### 7.3 错误码和全局异常

打开：

```text
poc/backend/src/main/java/com/example/aistudy/riskpoc/common/error/ErrorCode.java
poc/backend/src/main/java/com/example/aistudy/riskpoc/common/exception/GlobalExceptionHandler.java
```

讲解点：

- 参数校验失败：HTTP 400 + `400001`。
- 客户不存在：HTTP 404 + `404001`。
- 业务状态冲突：HTTP 409 + `409001`。
- 未预期异常：HTTP 500 + `500000`。
- 500 只返回“系统异常”，不把堆栈、SQL、连接串返回给前端。

口播示例：

```text
银行研发里错误码不是可有可无。页面提示、接口测试、日志排查、问题流转都需要稳定的错误码。AI 写代码时很容易只处理成功路径，所以我们要把错误码作为契约提前锁死。
```

### 7.4 `X-Request-Id` 和 `X-Operator`

打开：

```text
poc/backend/src/main/java/com/example/aistudy/riskpoc/common/context/RequestContextFilter.java
poc/backend/src/main/java/com/example/aistudy/riskpoc/common/context/RequestContextHolder.java
```

讲解点：

- 如果请求头传入 `X-Request-Id`，后端透传。
- 如果没有传入，后端生成 `REQ-DEMO-AUTO-*` 样例追踪号。
- 响应头回写同一个 `X-Request-Id`。
- `X-Operator` 缺省为 `demo-operator`，用于操作日志、风险记录和审计字段。
- 客户端地址会被处理成 `SAMPLE-IP-*`，避免演示日志里出现真实地址语义。

口播示例：

```text
requestId 的价值是把一次页面操作、一条接口响应、一行后端日志和一条操作日志串起来。第一阶段即使没有真实登录，也要通过 X-Operator 模拟操作人，让学员理解审计链路。
```

## 8. 业务模块分层讲解

### 8.1 客户模块

建议打开：

```text
poc/backend/src/main/java/com/example/aistudy/riskpoc/customer/controller/CustomerController.java
poc/backend/src/main/java/com/example/aistudy/riskpoc/customer/service/CustomerService.java
poc/backend/src/main/java/com/example/aistudy/riskpoc/customer/assembler/CustomerAssembler.java
```

覆盖接口：

- `GET /api/customers`
- `GET /api/customers/{customerNo}`
- `POST /api/customers`
- `PUT /api/customers/{customerNo}`

讲解点：

- Controller 路径和参数严格对齐 API 文档。
- 列表支持客户号、样例客户名、客户类型、状态、风险等级筛选。
- 详情不存在返回 `404001`。
- 新增重复客户号返回 `409001`。
- 修改使用 `version` 支撑乐观锁冲突演示。
- DTO 字段保留 `customerNameSample`、`mobileMasked`、`idNoMasked`、`remarkSample`，提醒学员只使用样例和脱敏数据。

### 8.2 账户模块

建议打开：

```text
poc/backend/src/main/java/com/example/aistudy/riskpoc/account/controller/AccountController.java
poc/backend/src/main/java/com/example/aistudy/riskpoc/account/service/AccountService.java
```

覆盖接口：

```text
GET /api/customers/{customerNo}/accounts
```

讲解点：

- 账户查询先校验客户存在。
- 响应只返回 `accountNoMasked`，不返回真实账号。
- 金额字段在实体和数据库中使用 `BigDecimal` / `decimal`，避免浮点误差。
- `hasAbnormalFlag` 会参与教学用风险计算。

### 8.3 风险模块

建议打开：

```text
poc/backend/src/main/java/com/example/aistudy/riskpoc/risk/controller/RiskController.java
poc/backend/src/main/java/com/example/aistudy/riskpoc/risk/service/RiskService.java
poc/backend/src/main/java/com/example/aistudy/riskpoc/risk/service/RiskCalculator.java
```

覆盖接口：

- `GET /api/customers/{customerNo}/risk`
- `GET /api/customers/{customerNo}/risk-records`
- `POST /api/customers/{customerNo}/risk/recalculate`

讲解点：

- 当前风险来自 `customer` 当前风险快照，并补充最新 `risk_record` 原因。
- 历史风险按 `calculatedAt desc, id desc` 倒序分页。
- 风险计算是教学样例规则，不代表真实风控模型。
- 风险重算是本课最重要的事务案例。

### 8.4 操作日志模块

建议打开：

```text
poc/backend/src/main/java/com/example/aistudy/riskpoc/operationlog/controller/OperationLogController.java
poc/backend/src/main/java/com/example/aistudy/riskpoc/operationlog/service/OperationLogService.java
```

覆盖接口：

```text
GET /api/operation-logs
```

讲解点：

- 支持按 `requestId`、`operator`、`operationType`、`targetType`、`targetBizNo`、`result`、时间范围筛选。
- 查询日志本身也会记录 `QUERY_LOG` 操作。
- 成功日志记录结果、目标业务号、耗时和脱敏客户端地址。
- 失败日志使用 `errorMessageSample`，不记录堆栈、SQL 或请求体全文。

## 9. 风险重算事务重点

风险重算接口：

```text
POST /api/customers/{customerNo}/risk/recalculate
```

建议打开：

```text
poc/backend/src/main/java/com/example/aistudy/riskpoc/risk/service/RiskService.java
poc/backend/src/main/java/com/example/aistudy/riskpoc/risk/dto/RiskRecalculateResponse.java
poc/backend/src/main/java/com/example/aistudy/riskpoc/risk/dto/TransactionEffectResponse.java
```

事务链路：

1. 校验客户存在。
2. 客户不存在时写入安全失败日志，返回 `404001`。
3. `status=CLOSED` 时写入失败日志，返回 `409001`。
4. 查询客户和账户列表。
5. 使用 `RiskCalculator` 计算样例风险分数、等级和原因。
6. 插入一条 `risk_record`，`sourceType=MANUAL`，`calculatedBy` 来自 `X-Operator`。
7. 回写 `customer.current_risk_level`、`current_risk_score`、`last_risk_calculated_at`。
8. 写入成功 `operation_log`。
9. 返回 `transactionEffect`，三项为 `true`。

`RiskCalculator` 教学规则：

- 基础分 20。
- 客户冻结加分，客户关闭加分。
- 存在异常标记账户加分。
- 存在冻结或关闭账户加分。
- 可用余额合计低于样例阈值加分。
- 分数最高 100。
- `LOW`、`MEDIUM`、`HIGH` 三档。

讲解口径：

```text
风险重算不能只看接口返回成功。我们要验收三个副作用：risk_record 是否新增，customer 当前风险快照是否回写，operation_log 是否可追踪。这个地方最能体现 AI 写业务代码时必须被事务设计和测试接住。
```

## 10. Flyway 和样例数据讲解

建议打开：

```text
poc/backend/src/main/resources/db/migration/V1__init_schema.sql
poc/backend/src/main/resources/db/migration/V2__seed_demo_data.sql
poc/backend/src/test/resources/db/testmigration/V1__init_schema.sql
poc/backend/src/test/resources/db/testmigration/V2__seed_demo_data.sql
```

讲解点：

- 主资源目录下是默认 MySQL profile 的迁移脚本。
- 测试资源目录下是 H2 兼容迁移脚本。
- 四张核心表：`customer`、`account`、`risk_record`、`operation_log`。
- 样例数据只使用 `CUST10000*`、`SAMPLE-*`、`masked`、`sample` 语义。
- 录屏中强调这些不是生产数据，也不是内部系统数据。

可讲的样例客户：

| 客户号 | 用途 |
| --- | --- |
| `CUST100001` | 低风险正常客户，适合客户详情和账户列表 |
| `CUST100002` | 中风险客户，适合演示风险重算成功 |
| `CUST100003` | 高风险冻结客户，适合风险标签和错误路径讲解 |
| `CUST100006` | 关闭状态客户，适合演示 `409001` |
| `CUST999999` | 不存在客户，适合演示 `404001` |

## 11. 后端开发提示词模板

录屏时可以把这些提示词作为“如何让 AI 受控写后端”的材料讲解。建议讲师不必全部现场执行，可以选择一两个片段演示。

### 11.1 创建项目骨架提示词

```text
你现在是银行研发场景下的 Java 后端开发助手。

背景：
- 项目是教学用客户风险评级与账户查询 POC。
- 第一阶段只做 Spring Boot 3.x 单体后端。
- JDK 21，Maven，MyBatis Plus，Flyway，Springdoc OpenAPI。
- 后端必须遵守 docs/api-design.md、docs/database-design.md、docs/backend-implementation-plan.md。

任务：
请为 poc/backend 创建或整理 Spring Boot 后端项目骨架。

必须包含：
1. Maven pom.xml，依赖只包含第一阶段必要项。
2. 基础包名 com.example.aistudy.riskpoc。
3. common、config、customer、account、risk、operationlog 模块。
4. ApiResponse、PageResponse、ErrorCode、BusinessException、GlobalExceptionHandler。
5. RequestContextFilter，处理 X-Request-Id 和 X-Operator。
6. Flyway 目录 db/migration。
7. 测试 profile 使用 H2。

强约束：
- 不引入 Spring Security、Redis、Kafka、RabbitMQ、Nacos、Spring Cloud。
- 不连接真实银行系统。
- 不写真实客户数据、生产地址、密钥或 token。
- 先给出影响范围和文件清单，再修改文件。
- 修改后说明验证命令。
```

### 11.2 实现客户查询提示词

```text
请基于 docs/api-design.md 的客户 API，实现客户列表和客户详情。

范围：
- 只处理 customer 模块和必要 common 代码。
- 路径必须是 GET /api/customers 和 GET /api/customers/{customerNo}。
- 响应必须使用 ApiResponse<T> 和 PageResponse<T>。
- DTO 字段必须使用 customerNameSample、mobileMasked、idNoMasked、currentRiskLevel 等契约字段。
- 查询参数支持 pageNo、pageSize、customerNo、customerNameSample、customerType、status、riskLevel。
- 客户不存在返回 HTTP 404 + 404001。

要求：
1. 不新增 API 契约之外的字段或接口。
2. 不返回数据库自增 id。
3. 不记录真实敏感信息。
4. 补 Controller 或 Service 测试，覆盖成功和客户不存在。
5. 完成后说明 mvn test 的预期验证。
```

### 11.3 实现风险重算提示词

```text
请实现 POST /api/customers/{customerNo}/risk/recalculate。

必须遵守 docs/api-design.md 和 docs/backend-implementation-plan.md。

事务效果：
1. 校验客户存在，客户不存在返回 404001。
2. CLOSED 客户不允许重算，返回 409001。
3. 读取客户和账户列表。
4. 使用教学样例规则计算 LOW / MEDIUM / HIGH。
5. 插入 risk_record。
6. 回写 customer 当前风险快照。
7. 写入 operation_log。
8. 响应 data.transactionEffect 中 riskRecordInserted、customerRiskSnapshotUpdated、operationLogInserted 均为 true。

失败日志：
- 客户不存在和状态冲突要写入安全失败日志。
- 不记录请求体全文、堆栈、SQL、连接串、真实敏感信息。

测试要求：
- Service 集成测试断言风险记录新增、客户快照更新、成功日志写入。
- 失败测试断言客户不存在返回异常并写入 errorMessageSample。

边界：
- 不引入真实风控模型。
- 不引入 MQ、缓存、分布式事务。
```

### 11.4 补测试提示词

```text
请站在后端测试工程师角度，为当前 poc/backend 补齐第一阶段关键测试。

请先阅读：
- docs/api-design.md
- docs/backend-implementation-plan.md
- docs/test-acceptance-material.md
- poc/backend/src/main/java

测试目标：
1. RiskCalculatorTest 覆盖 LOW / MEDIUM / HIGH 样例规则。
2. MapperIntegrationTest 覆盖账户列表和风险历史倒序。
3. RiskServiceIntegrationTest 覆盖风险重算事务三项效果和客户不存在失败日志。
4. ControllerIntegrationTest 覆盖统一响应、X-Request-Id 透传、参数校验 400001、关闭客户重算 409001。

要求：
- 使用 test profile + H2。
- 不依赖本机 MySQL。
- 不引入真实客户数据。
- 测试断言优先关注契约、事务效果和错误码，不强行扩大功能范围。
```

### 11.5 AI 审查提示词

```text
你现在是“客户风险评级与账户查询 POC”的后端代码审查员。

请审查 poc/backend 是否符合第一阶段基线：
- docs/api-design.md
- docs/database-design.md
- docs/backend-implementation-plan.md
- docs/test-acceptance-material.md

重点检查：
1. API 路径、字段、统一响应、分页和错误码是否契约一致。
2. 是否实现 X-Request-Id 和 X-Operator，并在响应和日志中可追踪。
3. 风险重算是否完成 risk_record 新增、customer 快照回写、operation_log 写入。
4. 失败路径是否覆盖 400001、404001、409001。
5. 日志和错误响应是否泄露 SQL、堆栈、连接串、真实账号、手机号、证件号、客户姓名、生产地址或密钥。
6. 是否引入了第一阶段不该出现的真实登录、权限、MQ、缓存、微服务、分布式事务。
7. 测试是否覆盖关键路径和事务效果。

输出格式：
- P0：必须立即修复，否则不能录屏或验收的问题。
- P1：建议录屏前修复的问题。
- P2：后续扩展建议。
- 已通过项：列有证据的通过点。
- 建议补充测试。

要求：
- 只列有证据的问题。
- 不要建议把第一阶段做成生产级复杂系统。
```

## 12. 录屏操作步骤

### 12.1 展示设计输入

依次打开：

```text
docs/api-design.md
docs/database-design.md
docs/backend-implementation-plan.md
```

讲解口径：

```text
后端开发不是从一句“帮我写个服务”开始，而是从契约开始。路径、字段、错误码、表结构、事务效果都已经在前面课程确定。AI 在这里执行，不重新定义边界。
```

### 12.2 展示 `poc/backend` 结构

建议在 IDE 或终端展示：

```powershell
cd D:\project\ai-study\poc\backend
```

重点展开：

- `pom.xml`
- `src/main/java/com/example/aistudy/riskpoc/common`
- `src/main/java/com/example/aistudy/riskpoc/customer`
- `src/main/java/com/example/aistudy/riskpoc/risk`
- `src/main/resources/db/migration`
- `src/test/java/com/example/aistudy/riskpoc`

### 12.3 展示一个 Controller / Service / Mapper

建议选择客户查询链路：

- `CustomerController`
- `CustomerService`
- `CustomerMapper`
- `CustomerAssembler`
- `CustomerListItemResponse`

讲解方式：

1. Controller 对齐 `/api/customers`。
2. Service 做分页、枚举校验和客户存在性判断。
3. Mapper 用 MyBatis Plus 查询。
4. Assembler 把数据库字段转成 API 字段。
5. 响应统一包在 `ApiResponse.success(...)`。

### 12.4 展示 Flyway 样例数据

打开：

```text
poc/backend/src/main/resources/db/migration/V1__init_schema.sql
poc/backend/src/main/resources/db/migration/V2__seed_demo_data.sql
```

讲解点：

- 表结构来自数据库设计基线。
- 样例数据来自脱敏数据设计。
- 真实项目不能把真实客户、账号、手机号、证件号放进课程仓库。
- `sample` 和 `masked` 不只是命名习惯，也是安全提醒。

### 12.5 展示风险重算核心代码

打开：

```text
poc/backend/src/main/java/com/example/aistudy/riskpoc/risk/service/RiskService.java
poc/backend/src/main/java/com/example/aistudy/riskpoc/risk/service/RiskCalculator.java
```

讲解顺序：

1. `recalculate` 方法先校验客户。
2. 404 和 409 都写失败日志。
3. `RiskCalculator` 只做教学样例规则。
4. 插入风险历史。
5. 回写客户快照。
6. 写成功操作日志。
7. 返回 `transactionEffect`。

### 12.6 运行或展示 `mvn test`

命令：

```powershell
cd D:\project\ai-study\poc\backend
mvn test
```

通过参考：

```text
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

测试类讲解：

| 测试类 | 覆盖点 |
| --- | --- |
| `RiskCalculatorTest` | LOW / MEDIUM / HIGH 样例风险规则 |
| `MapperIntegrationTest` | 账户列表、风险历史倒序 |
| `RiskServiceIntegrationTest` | 风险重算事务三项效果、客户不存在失败日志 |
| `ControllerIntegrationTest` | 统一响应、请求号透传、参数校验、状态冲突 |

如果现场不运行测试，也可以展示 `docs/backend-code-implementation-acceptance.md` 中主控复跑结果，并说明录屏前仍应在本机复验。

### 12.7 说明 H2 test profile 和 MySQL profile 区别

讲解口径：

```text
课程默认用 test profile + H2，是为了让学员不依赖本机数据库也能复现后端测试。MySQL profile 用于本地真实数据库行为验证，但仍然只能连接 localhost 样例库，不能连接真实银行环境。
```

对比表：

| 路径 | 用途 | 是否课程默认 |
| --- | --- | --- |
| test profile + H2 | 自动化测试、录屏演示、快速复验 | 是 |
| 默认 profile + MySQL | 本地 MySQL 8 验证 Flyway 和 MyBatis 行为 | 否，可选 |
| 真实银行环境 | 不属于本课程第一阶段 | 禁止 |

## 13. 讲解重点

- 先按 API 契约开发，不让 AI 自行改路径、字段、错误码和响应结构。
- 后端分层不是为了复杂，而是为了让 Controller、Service、Mapper、DTO 各司其职。
- 统一响应、错误码、`requestId` 和 `X-Operator` 是银行研发排查问题的基础能力。
- Flyway 样例数据必须脱敏，字段名要保留 `sample` / `masked` 语义。
- 风险重算必须验收事务效果：新增历史、回写快照、写操作日志。
- 写业务代码时同步补测试，不把测试留到最后。
- AI 生成代码后必须看 diff、跑测试、做人工验收。
- 日志和错误响应不能泄露请求体全文、SQL、堆栈、连接串、真实客户信息或密钥。
- test profile + H2 是课程默认演示路径，MySQL 只是本地可选验证。
- 第一阶段不做真实登录、权限、MQ、缓存、微服务和分布式事务。

## 14. 常见错误

### 14.1 AI 擅自新增接口或改字段

错误表现：

- 把 `/api/customers/{customerNo}/risk/recalculate` 改成 `/risk/calculate`。
- 返回 `name`、`mobile`、`idNo`，丢掉 `sample` / `masked` 后缀。
- 返回数据库自增 `id` 给前端。
- 新增导出、删除、批量导入等不在第一阶段范围内的接口。

处理方式：

- 以 `docs/api-design.md` 为准。
- 要求 AI 先列影响范围，不允许自行改契约。
- 契约确需变更时，必须先回到主控设计评审，不在开发中顺手改。

### 14.2 忘记统一响应和错误码

错误表现：

- Controller 直接返回 DTO。
- 404 返回默认 HTML 或 Spring 默认 JSON。
- 参数错误只返回 “Bad Request”，没有 `400001`。
- 500 把异常类名或堆栈返回给前端。

处理方式：

- 所有业务接口返回 `ApiResponse<T>`。
- 通过 `GlobalExceptionHandler` 统一转换异常。
- Controller 测试必须断言 `code` 和 `requestId`。

### 14.3 风险重算只返回结果，不写事务副作用

错误表现：

- 只计算风险等级并返回。
- 没有插入 `risk_record`。
- 没有回写 `customer` 当前风险快照。
- 没有写操作日志。
- 响应里没有 `transactionEffect`。

处理方式：

- Service 层用事务包住主链路。
- 测试断言历史记录条数增加、快照更新、日志写入。
- 页面或 HTTP 文件按 `requestId` 查日志。

### 14.4 只跑成功路径，不测 404 / 409 / 参数校验

错误表现：

- 只测试 `CUST100002` 重算成功。
- 不测 `CUST999999` 客户不存在。
- 不测 `CUST100006` 关闭客户冲突。
- 不测非法 `riskLevel=UNKNOWN`。

处理方式：

- 至少覆盖成功、400、404、409。
- 错误响应也要断言统一结构。
- 失败日志也要检查不泄露敏感信息。

### 14.5 把真实配置或真实数据放进课程

错误表现：

- 配置中出现真实数据库地址、真实账号、真实密码。
- 样例数据使用真实姓名、手机号、证件号、账号。
- 日志记录完整请求体和客户端地址。
- HTTP 文件或截图里出现内部系统地址。

处理方式：

- 配置只使用 `localhost` 和教学账号。
- 样例数据只使用 `CUST10000*`、`SAMPLE-*`、`masked`、`sample`。
- 录屏前做关键词和截图检查。

## 15. 半口语口播稿

大家好，这节是第 5 课，后端最小服务开发。

第 4 课我们已经做了一件很重要的事：把需求转换成设计，并且通过设计评审收敛了第一阶段边界。第一阶段不做真实登录，不做复杂权限，不接真实银行系统，不引入 MQ、缓存、分布式事务，也不一上来拆微服务。我们要先把一个单体 Spring Boot 后端跑通。

这节课的重点不是表演从空目录写完整项目。真实开发里也不应该把一句“帮我写个后端”丢给 AI，然后直接相信它生成的结果。我们要做的是把设计基线拆成 AI 能执行的小任务：先看 API 契约，再看数据库脚本，再搭项目骨架，再做统一响应和异常处理，再实现客户、账户、风险、日志模块，最后用测试和人工验收接住。

先看后端目录，位置在 `poc/backend`。这是一个 Spring Boot 3.x、JDK 21、Maven 项目。依赖里有 Web、Validation、MyBatis Plus、Flyway、MySQL driver、Springdoc OpenAPI、Actuator 和测试依赖。大家也可以注意一下，这里没有 Spring Security、Redis、Kafka、RabbitMQ、Nacos、Spring Cloud。不是这些技术不好，而是它们不属于第一阶段最小闭环。

第一阶段我们要讲清的是：客户能查，详情能看，账户能展示，风险能重算，日志能追踪，错误能统一返回，测试能跑通。这个闭环成立以后，后面再讲微服务演进才有基础。

接着看包结构。基础包名是 `com.example.aistudy.riskpoc`，下面按 `common`、`config`、`customer`、`account`、`risk`、`operationlog` 分模块。`common` 里放统一响应、分页、错误码、异常、请求上下文这些横切能力；业务模块内部再按 Controller、Service、Mapper、Entity、DTO、Assembler 拆分。

这个分层很适合给 AI 下任务。比如我们可以明确告诉 AI：Controller 只对齐路径和参数，不写业务；Service 负责事务和校验；Mapper 只做数据访问；DTO 对齐 API 文档；Assembler 负责字段转换。任务边界越清楚，AI 越不容易把代码写散。

先看统一响应 `ApiResponse`。成功时固定 `success=true`、`code=000000`、`message=success`，失败时使用错误码和错误消息。这里还有两个字段很关键：`requestId` 和 `timestamp`。`requestId` 从请求上下文里拿，时间按 `Asia/Shanghai` 展示。

为什么要统一响应？不是为了写起来好看，而是为了前端、测试和排查问题都能按同一个结构处理。客户不存在、参数错误、状态冲突，都能读到 `code`、`message`、`requestId`。

再看全局异常处理。业务异常按自己的 HTTP 状态码返回，参数校验失败返回 `400001`，未预期异常返回 `500000`，但前端只看到“系统异常”，不会看到堆栈、SQL 或连接串。这一点在银行研发场景里非常重要，错误响应不能把内部实现细节暴露出去。

然后看 `RequestContextFilter`。它会读取 `X-Request-Id`，有就透传，没有就生成 `REQ-DEMO-AUTO-*`。它还会读取 `X-Operator`，缺省为 `demo-operator`。响应头会回写同一个请求号。这样一次页面操作就能从前端错误提示追到后端响应，再追到操作日志。

第一阶段没有真实登录，所以 `X-Operator` 只是教学模拟。但这不代表操作人不重要。相反，我们正好借它讲清楚审计字段和操作追踪的习惯。

接下来打开客户模块。`CustomerController` 对齐 `/api/customers`，实现列表、详情、新增和修改。列表支持客户号、样例客户名、客户类型、状态和风险等级筛选。客户不存在返回 `404001`，新增重复客户号或版本冲突返回 `409001`。

这里注意 DTO 字段，比如 `customerNameSample`、`mobileMasked`、`idNoMasked`。这些字段名看起来有点啰嗦，但它们是刻意保留的安全提醒：课程里只展示样例名称和脱敏字段，不展示真实客户信息。

账户模块比较简单，核心是 `GET /api/customers/{customerNo}/accounts`。它先校验客户存在，再返回账户列表。账号字段叫 `accountNoMasked`，金额字段用 `BigDecimal`，数据库里是 `decimal`，这也是银行系统里必须强调的基础习惯。

风险模块是本节重点。它有当前风险、历史风险和风险重算三个接口。当前风险来自客户表里的风险快照，再结合最新风险记录的原因；历史风险按计算时间倒序分页；风险重算则是一个写操作事务。

我们打开 `RiskService.recalculate`。第一步先找客户。如果客户不存在，它会写一条失败操作日志，然后抛出 `NotFoundException`，前端拿到 `404001`。如果客户状态是 `CLOSED`，它也会写失败日志，然后返回 `409001`。

客户存在且状态允许时，服务会读取账户列表，交给 `RiskCalculator` 做教学样例规则计算。这个规则非常简单：基础分，加上客户状态、异常账户、冻结账户、低余额等因素，最后映射成 LOW、MEDIUM、HIGH。这里要强调，它不是生产风控模型，只是课程样例。

算出结果后，事务里会做三件事：插入一条 `risk_record`，回写 `customer` 当前风险快照，写入 `operation_log`。接口响应里的 `transactionEffect` 会告诉我们这三项都完成了。

这就是本课最值得反复强调的地方：风险重算不能只看接口返回一个等级。我们必须验证历史记录有没有新增，客户快照有没有更新，操作日志能不能按 requestId 追到。否则代码看起来能跑，业务上其实没有闭环。

再看操作日志模块。日志查询支持 requestId、operator、operationType、targetType、targetBizNo、result 和时间范围筛选。成功日志记录操作类型、目标业务号、结果、耗时和脱敏客户端地址。失败日志用 `errorMessageSample`，不记录堆栈、SQL 或请求体全文。

接下来讲 Flyway。主资源目录里有 `V1__init_schema.sql` 和 `V2__seed_demo_data.sql`，测试资源目录里也有 H2 兼容版本。四张核心表是 customer、account、risk_record、operation_log。样例数据只使用 `CUST10000*`、`SAMPLE-*`、`masked`、`sample`。课程录屏不能出现真实客户、真实账号、手机号、证件号、生产地址或密钥。

最后看测试。后端现在有四类测试：`RiskCalculatorTest` 测风险规则，`MapperIntegrationTest` 测账户和风险历史查询，`RiskServiceIntegrationTest` 测风险重算事务和失败日志，`ControllerIntegrationTest` 测统一响应、请求号、参数校验和状态冲突。

录制时可以运行 `mvn test`。通过参考是 9 个测试全部通过，BUILD SUCCESS。这里要跟学员讲清楚：AI 写完代码不算完成，测试通过也不是全部完成，但它是进入人工验收前的基本门槛。

本节收束一下。后端开发里，AI 很适合帮我们生成重复结构和测试草案，但人要把住四个边界：API 契约不能漂移，事务效果必须可验收，日志和错误信息不能泄露敏感内容，第一阶段不能擅自扩成生产级复杂系统。

只要这四个边界守住，再配合自动化测试和人工验收，AI 就能真正成为可靠的工程搭档，而不是一个看起来很快、后面返工很多的代码生成器。

## 16. 讲义精简版

### 16.1 本课核心

第 5 课承接第 4 课设计评审结果，演示如何用 AI 辅助实现 Spring Boot 3.x 单体后端。

核心原则：

```text
先按契约开发，再让 AI 写代码。
AI 可以生成实现，但 API、事务、测试和验收由人控制。
```

### 16.2 后端第一阶段范围

必须做：

- Spring Boot 3.x + JDK 21 + Maven。
- Flyway 初始化四张表和脱敏样例数据。
- MyBatis Plus 数据访问。
- 客户、账户、风险、操作日志四个业务模块。
- 统一响应、分页响应、错误码、全局异常。
- `X-Request-Id`、`X-Operator`。
- 风险重算事务。
- 自动化测试。

不做：

- 真实登录和权限。
- MQ、缓存、分布式事务。
- 微服务拆分。
- 真实风控模型。
- 真实银行系统接入。
- 真实客户数据、内部系统地址、密钥或生产配置。

### 16.3 后端分层

| 层 | 职责 |
| --- | --- |
| Controller | 路径、参数、统一响应 |
| Service | 业务流程、事务、校验、日志 |
| Mapper | 数据访问 |
| Entity | 数据库表映射 |
| DTO | API 请求和响应 |
| Assembler | Entity 和 DTO 转换 |
| Common | 统一响应、异常、错误码、上下文 |

### 16.4 风险重算验收

重算成功必须确认：

- `risk_record` 新增。
- `customer` 当前风险快照回写。
- `operation_log` 写入。
- 响应 `transactionEffect` 三项为 `true`。

失败路径必须确认：

- 不存在客户返回 `404001`。
- 关闭客户返回 `409001`。
- 失败日志不暴露敏感内容。

### 16.5 测试覆盖

| 测试类 | 重点 |
| --- | --- |
| `RiskCalculatorTest` | 风险规则 |
| `MapperIntegrationTest` | 账户和风险历史查询 |
| `RiskServiceIntegrationTest` | 风险重算事务和失败日志 |
| `ControllerIntegrationTest` | 统一响应、错误码、请求号 |

默认验证命令：

```powershell
cd D:\project\ai-study\poc\backend
mvn test
```

### 16.6 本课最重要的一句话

```text
AI 生成后端代码只是开始，契约一致、事务可验收、测试通过、安全不泄露，才是完成。
```

## 17. 课后练习

练习目标：让学员不用新增功能，只围绕已有后端做一次受控 AI 协作。

任选一个练习：

1. 让 AI 给 `AccountService` 或 `OperationLogService` 补充一个测试建议清单，不要求立即改代码。
2. 让 AI 审查 `RiskService.recalculate`，只检查事务、错误码、日志和敏感信息，不做功能扩展。
3. 让 AI 基于 `docs/api-design.md` 检查某个 Controller 是否契约一致。

练习提示词：

```text
请审查 poc/backend 中的【指定 Service 或 Controller】。

背景：
- 这是教学用客户风险评级与账户查询 POC。
- 第一阶段只做 Spring Boot 单体后端。
- 必须遵守 docs/api-design.md 和 docs/backend-implementation-plan.md。

请只检查：
1. API 契约是否一致。
2. 成功和失败响应是否统一。
3. 是否保留 requestId 和 operator。
4. 是否有必要的测试覆盖。
5. 日志和错误信息是否可能泄露敏感内容。

输出：
- P0 / P1 / P2 问题。
- 建议补充的测试。
- 不要新增接口，不要修改数据库脚本，不要引入真实数据或生产配置。
```

练习验收：

- AI 输出的问题必须有文件或代码依据。
- 学员能判断哪些建议属于第一阶段，哪些应该放到后续阶段。
- 不因为 AI 建议就直接扩范围。

## 18. 失败兜底方案

| 问题 | 表现 | 兜底 |
| --- | --- | --- |
| Maven 测试失败 | `mvn test` 不通过 | 先看失败测试名，回到对应 Service / Controller；录屏可展示主控验收记录作为备用，但录制前应复验 |
| JDK 版本不对 | 编译提示 release 21 或 class version 问题 | 切换到 JDK 21，再执行 `mvn -v` 确认 |
| 8080 端口占用 | 后端启动失败 | 关闭占用进程；课程录屏不建议临时改端口 |
| MySQL 启动失败 | 默认 profile 连接失败 | 切回 test profile + H2；MySQL 是可选验证 |
| Swagger UI 打不开 | `/swagger-ui/index.html` 访问失败 | 先确认后端健康检查，再确认使用的是后端服务端口 |
| HTTP 请求数据被多次重算 | 风险历史记录变多 | 写操作预期如此；重启 test profile 恢复内存库初始数据 |
| AI 建议引入权限或微服务 | 输出超出第一阶段 | 要求按“第一阶段单体 / 第二阶段演进 / 明确不做”重写 |

## 19. 主控验收清单

- [x] 本文明确第 5 课标题和一句话目标。
- [x] 本文讲清本课要解决的问题：AI 辅助后端实现，但由人控制边界、测试和验收。
- [x] 本文给出 35 到 45 分钟章节安排。
- [x] 本文覆盖录制前准备：后端项目位置、JDK 21、Spring Boot 3.x、Maven、test profile + H2、MySQL 可选。
- [x] 本文覆盖后端项目骨架和 Maven 依赖。
- [x] 本文覆盖 Flyway 脚本和脱敏样例数据。
- [x] 本文覆盖 entity / mapper / service / controller / dto / assembler 分层。
- [x] 本文覆盖统一响应 `ApiResponse<T>` 和分页响应。
- [x] 本文覆盖 `X-Request-Id` / `X-Operator`。
- [x] 本文覆盖全局异常处理和错误码。
- [x] 本文覆盖客户、账户、风险、操作日志模块。
- [x] 本文突出风险重算三项事务效果：新增 `risk_record`、回写 `customer` 快照、写 `operation_log`。
- [x] 本文提供创建骨架、客户查询、风险重算、补测试、AI 审查提示词模板。
- [x] 本文覆盖录屏操作步骤：展示设计基线、后端结构、Controller / Service / Mapper、Flyway、风险重算、`mvn test`、profile 区别。
- [x] 本文覆盖讲解重点和常见错误。
- [x] 本文提供半口语口播稿。
- [x] 本文提供讲义精简版。
- [x] 本文提供课后练习。
- [x] 本文提供主控验收清单。
- [x] 本文没有引入真实客户数据、内部系统地址、密钥或生产配置。
- [x] 本文没有要求修改后端代码、前端代码或数据库脚本。

## 给主控窗口的简短汇报

本次新增 `docs/lesson-05-backend-recording-material.md`。

材料覆盖第 5 课“后端最小服务开发”的录屏结构、录制前准备、后端项目骨架、关键实现讲解、AI 提示词模板、测试与验收、常见错误、半口语口播稿、讲义精简版、课后练习和主控验收清单。

内容已覆盖后端结构、关键实现、提示词、测试和验收，重点包含 Spring Boot 3.x + JDK 21 + Maven、Flyway、分层实现、统一响应、分页、`X-Request-Id` / `X-Operator`、错误码、客户 / 账户 / 风险 / 操作日志模块、风险重算事务三项效果，以及 test profile + H2 和 MySQL 可选验证差异。

当前材料足够支撑第 5 课 35 到 45 分钟录制。待主控确认的问题：录制时是否需要实际演示 `mvn test`，还是只展示主控验收记录和测试覆盖点；是否要补一个更短的课堂用“一页提示词速查表”。
