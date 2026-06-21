# 后端第一阶段最小实现设计

## 主控验收结论

本设计通过主控窗口验收，可以作为后续创建 Spring Boot 单体后端代码项目的执行蓝图。

通过理由：

- 严格遵守第一阶段边界：单体 Spring Boot 后端 + MySQL + Flyway + MyBatis Plus。
- 未引入真实登录、权限系统、MQ、缓存、分布式事务、真实风控模型或真实银行系统对接。
- 能覆盖客户列表、详情、新增、修改、账户列表、当前风险、历史风险、风险重算和操作日志查询。
- 明确了 Maven 项目结构、依赖、分层、核心类、Flyway 接入、风险重算事务、测试计划和录制顺序。
- 体现了课程方法论：先需求和契约，再设计计划，再测试验收，最后进入开发。

主控补充裁决：

- 第一阶段使用 Maven，不使用 Gradle。
- 基础包名暂定为 `com.example.aistudy.riskpoc`，后续可在代码实现前按公司内部命名规范调整。
- 查询类日志不强制全量记录。第一阶段必须记录客户修改、风险重算、风险重算失败样例和日志查询；客户详情查询可记录，客户列表不记录以降低日志噪声。
- 风险重算失败日志建议独立事务记录，避免业务事务回滚导致失败样例无法在日志页演示。
- Lombok 作为可选项。若课程面向入门同学，建议少用或谨慎讲清，避免学员看不到 Java Bean 细节。
- Testcontainers 作为扩展测试，不作为第一阶段必须项。

## 1. 目标边界

第一阶段目标：用 Spring Boot 3.x 单体服务跑通客户列表、详情、新增、修改、账户列表、当前风险、历史风险、风险重算、操作日志查询，并接入 MySQL 8、Flyway、MyBatis Plus、统一响应、统一异常、OpenAPI、基础测试。

不做：

- 真实登录。
- 权限系统。
- MQ。
- 缓存。
- 分布式事务。
- 真实风控模型。
- 真实银行系统对接。
- 第二阶段微服务实现细节。

操作人只来自 `X-Operator`，缺省为 `demo-operator`。

## 2. 项目结构建议

默认 Maven，理由是 Spring Boot 教程、企业 Java 项目和银行研发团队里 Maven 更常见，录屏讲解成本更低。

```text
backend/
  pom.xml
  src/main/java/com/example/aistudy/riskpoc/
    RiskPocApplication.java
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
  src/test/java/...
```

包结构按业务模块垂直切分，每个模块内再分 `controller`、`service`、`mapper`、`entity`、`dto`、`assembler`。`common` 放统一响应、错误码、异常、分页、上下文、过滤器等横切能力。

## 3. 依赖清单

必须依赖：

- `spring-boot-starter-web`
- `spring-boot-starter-validation`
- `mybatis-plus-spring-boot3-starter`
- `mysql-connector-j`
- `flyway-core`
- `flyway-mysql`
- `springdoc-openapi-starter-webmvc-ui`
- `spring-boot-starter-actuator`
- `spring-boot-starter-test`
- `mockito-core` 或随 Spring Boot Test 使用 Mockito

可选依赖：

- `lombok`：可选。若担心隐藏细节，可不使用。
- `testcontainers-mysql`：数据库测试扩展，不列为第一阶段必须。

## 4. 分层设计

- Controller：只负责路径、参数校验、调用 Service、返回 `ApiResponse<T>`，严格遵守 `docs/api-design.md` 的路径和字段。
- Service：承载业务流程、事务、客户存在性校验、风险重算、操作日志写入。
- Mapper：MyBatis Plus 基础 CRUD 加少量条件分页查询，不把业务规则写进 Mapper。
- Entity：贴合四张表：`customer`、`account`、`risk_record`、`operation_log`。
- DTO：按 API 契约拆请求和响应对象，API 字段使用 camelCase，继续保留 `masked` / `sample` 后缀。
- Assembler：负责 Entity 和 DTO 转换、UTC 时间到 `Asia/Shanghai` ISO-8601 输出。
- Common：统一响应、分页响应、错误码、业务异常、全局异常处理、请求上下文、`requestId` 过滤器、操作人解析。

## 5. 核心类清单

- `RiskPocApplication`：启动类。
- `ApiResponse`：统一响应体。
- `PageResponse`：分页响应结构。
- `ErrorCode`：`000000`、`400001`、`404001`、`409001`、`500000`。
- `BusinessException`、`NotFoundException`、`ConflictException`：业务异常。
- `GlobalExceptionHandler`：统一错误响应。
- `RequestContext`：保存 `requestId`、`operator`、开始时间。
- `RequestContextFilter`：处理 `X-Request-Id`、`X-Operator`、响应头。
- `CustomerController`、`AccountController`、`RiskController`、`OperationLogController`：API 入口。
- `CustomerService`、`AccountService`、`RiskService`、`OperationLogService`：业务服务。
- `RiskCalculator`：教学样例风险规则计算。
- `CustomerMapper`、`AccountMapper`、`RiskRecordMapper`、`OperationLogMapper`：数据访问。
- `CustomerEntity`、`AccountEntity`、`RiskRecordEntity`、`OperationLogEntity`：表实体。
- `CustomerAssembler`、`AccountAssembler`、`RiskAssembler`、`OperationLogAssembler`：DTO 转换。
- `OpenApiConfig`、`MybatisPlusConfig`、`JacksonTimeConfig`：配置类。

## 6. 统一响应和错误处理

所有业务接口返回 `ApiResponse<T>`。

- 成功：HTTP 200 + `code=000000`。
- 参数校验失败：HTTP 400 + `400001`。
- 客户不存在：HTTP 404 + `404001`。
- 客户号重复、版本冲突、关闭客户不允许重算：HTTP 409 + `409001`。
- 未知异常：HTTP 500 + `500000`，不返回堆栈、SQL、连接串。

`RequestContextFilter` 优先透传 `X-Request-Id`，缺省生成 `REQ-DEMO-AUTO-{短随机}`；响应头和响应体都带同一个 `requestId`。`X-Operator` 缺省为 `demo-operator`，用于 `createdBy`、`updatedBy`、`calculatedBy`、`operation_log.operator`。

## 7. Flyway 接入

后续创建项目时，把现有草案直接复制到：

```text
backend/src/main/resources/db/migration/
  V1__init_schema.sql
  V2__seed_demo_data.sql
```

`application.yml` 开启 Flyway，连接 MySQL 8，本地库首次启动自动建表和插入脱敏样例数据。SQL 来源以 `docs/db-migration-draft/` 为准，不在后端实现窗口重新发明表结构。

若后续改 SQL，必须同步 `docs/database-design.md`，避免课程材料和代码漂移。

## 8. 功能实现步骤

1. 基础骨架：启动类、配置、统一响应、异常、`requestId`、`X-Operator`。
2. Flyway + MyBatis Plus：四张表实体、Mapper、分页配置。
3. 客户列表：实现 `GET /api/customers`，支持客户号、样例名称、类型、状态、风险等级筛选。
4. 客户详情：实现 `GET /api/customers/{customerNo}`，不存在返回 `404001`。
5. 客户新增 / 修改：实现 `POST /api/customers`、`PUT /api/customers/{customerNo}`，处理重复客户号和版本冲突。
6. 账户列表：实现 `GET /api/customers/{customerNo}/accounts`，先校验客户存在。
7. 当前风险：从 `customer` 快照 + 最新 `risk_record` 原因组装。
8. 历史风险：按 `calculated_at desc, id desc` 分页查询。
9. 风险重算：实现事务闭环。
10. 操作日志：实现 `GET /api/operation-logs`，支持契约中的筛选条件。

## 9. 风险重算事务设计

`POST /api/customers/{customerNo}/risk/recalculate` 主事务内完成：

1. 校验客户存在、未逻辑删除。
2. 若客户 `status=CLOSED`，返回 `409001`，不执行重算。
3. 查询客户和账户列表。
4. `RiskCalculator` 使用教学规则计算分数和等级：基础分 20，冻结 / 销户、异常账户、冻结 / 关闭账户、可用余额低等加分，最高 100；等级按 LOW / MEDIUM / HIGH。
5. 插入 `risk_record`，`source_type=MANUAL`，`calculated_by` 来自 `X-Operator`。
6. 回写 `customer.current_risk_level`、`current_risk_score`、`last_risk_calculated_at`、`updated_by`、`updated_at`。
7. 插入成功 `operation_log`，`operation_type=RECALCULATE_RISK`，`target_type=RISK`，`result=SUCCESS`。
8. 返回 API 契约中的 `transactionEffect`。

失败日志建议单独用轻量方法记录，必要时独立事务，确保客户不存在、状态冲突等失败样例能在日志页查到；但不记录请求体全文、堆栈或敏感信息。

## 10. 测试计划

第一阶段必须做：

- Service 单测：客户查询、客户不存在、风险计算 LOW / MEDIUM / HIGH、关闭客户不允许重算。
- 风险重算事务测试：断言新增 `risk_record`、回写 `customer` 快照、写入 `operation_log`。
- Controller 测试：统一响应、参数校验、`404001`、`409001`、`X-Request-Id` 透传。
- Mapper / 数据库测试：客户分页筛选、账户列表、风险历史倒序、操作日志筛选。
- 错误场景测试：非法枚举、重复客户号、版本冲突、客户不存在。

扩展测试：

- Testcontainers MySQL 真实迁移验证。
- OpenAPI 契约快照。
- 查询类操作是否写日志的覆盖测试。
- 风险分数精确断言。第一阶段优先断言等级和链路，不强断言所有种子客户精确分数。

## 11. 本地启动和验证命令建议

后续项目创建后建议：

```bash
docker compose up -d mysql
mvn clean test
mvn spring-boot:run
curl http://localhost:8080/actuator/health
curl -H "X-Request-Id: REQ-DEMO-CUSTOMER-LIST" -H "X-Operator: demo-query" "http://localhost:8080/api/customers?pageNo=1&pageSize=10"
curl -X POST -H "Content-Type: application/json" -H "X-Operator: demo-admin" http://localhost:8080/api/customers/CUST100002/risk/recalculate -d '{"reasonSample":"课程录屏手动触发风险重算样例","ruleCode":"DEMO_RULE_V1"}'
```

## 12. 课程录制讲解顺序

1. 先展示需求和边界：第一阶段只做单体最小闭环。
2. 展示 API 契约，说明后端必须按契约实现。
3. 展示数据库四张表和 Flyway 草案。
4. 让 AI 先生成实现计划和测试计划，而不是直接写代码。
5. 搭统一响应、异常、`requestId`、`X-Operator`。
6. 实现客户查询主链路并测试。
7. 接入账户、风险查询、历史记录。
8. 重点演示风险重算事务和测试。
9. 展示操作日志和失败样例。
10. 运行测试、Swagger、curl / HTTP 文件，最后做安全检查。

## 13. 风险点和待确认

风险点：

- 实现时擅自改 API 字段。
- 操作日志记录敏感请求体。
- 风险重算只写历史不回写快照。
- 时间 UTC 和 `Asia/Shanghai` 转换混乱。
- 过早引入登录、权限、缓存、MQ。

待确认：

- 查询类日志是否全部记录。主控裁决：第一阶段只强制记录客户修改、风险重算、风险重算失败样例和日志查询；客户详情查询可记录，客户列表不记录。

## 14. 后续可派发子任务

- 后端代码实现子窗口：创建 `backend` 项目并实现第一阶段单体 POC。
- 后端测试验收子窗口：基于 API 契约和数据库样例补测试矩阵。
- 后端录屏讲稿子窗口：把本设计转换为第 5 课录制脚本。

