# 后端代码实现子任务

## 子任务目标

创建第一阶段 Spring Boot 单体后端，完成“客户风险评级与账户查询 POC”的最小可运行后端实现。

本子任务进入代码实现阶段，但必须严格基于已有设计文档，不重新发明架构、数据库和 API。

## 输入基线

实现前必须阅读：

- `docs/poc-architecture.md`
- `docs/database-design.md`
- `docs/api-design.md`
- `docs/backend-implementation-plan.md`
- `docs/db-migration-draft/V1__init_schema.sql`
- `docs/db-migration-draft/V2__seed_demo_data.sql`

## 输出位置

建议创建：

```text
poc/
  backend/
```

后端项目结构：

```text
poc/backend/
  pom.xml
  src/main/java/com/example/aistudy/riskpoc/
  src/main/resources/application.yml
  src/main/resources/db/migration/
    V1__init_schema.sql
    V2__seed_demo_data.sql
  src/test/java/com/example/aistudy/riskpoc/
```

## 子窗口提示词

```text
你现在是 AI 教程项目的后端代码实现子窗口。请在当前工作区创建第一阶段 Spring Boot 单体后端代码，但只实现第一阶段，不做第二阶段微服务。

项目背景：
- 这是面向银行解决方案研发同学的 AI 编程入门教程。
- 课程主线是用 Claude Code + DeepSeek 从 0 搭建一个最小可运行 Java 微服务 POC。
- 当前任务只做第一阶段单体后端。
- 技术栈：JDK 21、Spring Boot 3.x、Maven、MyBatis Plus、MySQL 8、Flyway、springdoc-openapi、Actuator、JUnit 5。
- 第一阶段不做真实登录，操作人来自 `X-Operator`，缺省为 `demo-operator`。
- 所有数据必须是脱敏样例数据，不允许出现真实客户、真实账号、真实手机号、真实证件号、生产地址、密钥或内部系统名称。

请先阅读：
- docs/poc-architecture.md
- docs/database-design.md
- docs/api-design.md
- docs/backend-implementation-plan.md
- docs/db-migration-draft/V1__init_schema.sql
- docs/db-migration-draft/V2__seed_demo_data.sql

实现要求：
1. 在 `poc/backend` 下创建 Maven Spring Boot 3.x 项目。
2. 使用包名 `com.example.aistudy.riskpoc`。
3. 复制 Flyway SQL 到 `src/main/resources/db/migration/`。
4. 实现统一响应 `ApiResponse<T>`、分页响应、错误码、业务异常、全局异常处理。
5. 实现 `RequestContextFilter`，处理 `X-Request-Id` 和 `X-Operator`。
6. 实现四个业务模块：customer、account、risk、operationlog。
7. 严格按 `docs/api-design.md` 实现接口路径、字段、响应和错误码。
8. 风险重算必须在事务内完成：新增 `risk_record`、回写 `customer` 当前风险快照、写入 `operation_log`。
9. 风险重算失败样例应写失败日志，但不能记录请求体全文、堆栈或敏感信息。
10. 实现 OpenAPI 和 Actuator health。
11. 补第一阶段必须测试：Service、Controller、Mapper 或数据库关键路径。
12. 不引入真实登录、权限、MQ、缓存、分布式事务。

开发方式：
- 先输出实现计划和文件清单。
- 再分步实现，每一步完成后说明变更和验证方式。
- 修改后运行可用测试或说明无法运行的原因。
- 最后输出主控验收摘要。

验收标准：
- `mvn test` 通过，或清楚说明未通过原因和剩余问题。
- 后端能启动。
- Flyway 能初始化四张核心表和样例数据。
- `/actuator/health` 可访问。
- 客户列表、详情、账户列表、当前风险、历史风险、风险重算、操作日志接口符合契约。
- 统一响应、错误码、requestId、X-Operator 行为符合契约。
- 不包含真实敏感数据。
```

## 主控验收重点

- 是否严格遵守 `docs/api-design.md`。
- 是否复用 `docs/db-migration-draft` SQL。
- 是否保持第一阶段边界，没有抢跑微服务、权限、缓存、MQ。
- 风险重算事务是否完整。
- 失败日志是否能演示，但不泄露敏感信息。
- 测试是否覆盖关键路径。
- 是否适合后续录制第 5 课“后端最小实现”。

