# 后端代码实现主控验收记录

## 验收结论

后端代码实现通过主控验收，可以作为第一阶段 POC 后端基线，并可用于后续前端联调、测试验收和第 5 课录屏。

## 验收范围

代码位置：

```text
poc/backend
```

主控检查内容：

- Spring Boot 3.x + JDK 21 + Maven 项目结构。
- 包名 `com.example.aistudy.riskpoc`。
- Flyway SQL 是否复用 `docs/db-migration-draft` 草案。
- 是否实现 customer、account、risk、operationlog 四个模块。
- 是否实现统一响应、分页响应、错误码、业务异常、全局异常处理。
- 是否实现 `X-Request-Id` / `X-Operator` 请求上下文。
- 风险重算事务是否覆盖 `risk_record` 插入、`customer` 快照回写、`operation_log` 写入。
- 是否守住第一阶段边界，没有引入真实登录、权限、MQ、缓存、分布式事务或微服务内容。
- 测试是否通过。

## 验证结果

已在主控窗口复跑：

```bash
mvn test
```

结果：

```text
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

测试覆盖：

- `ControllerIntegrationTest`：3 个测试。
- `MapperIntegrationTest`：2 个测试。
- `RiskCalculatorTest`：2 个测试。
- `RiskServiceIntegrationTest`：2 个测试。

Flyway SQL hash 已与 `docs/db-migration-draft` 中的 V1 / V2 草案一致。

## 主控观察

- 项目结构符合 `docs/backend-implementation-plan.md`。
- `RiskService` 中风险重算链路符合主控要求：客户校验、关闭状态冲突、账户读取、风险计算、历史记录插入、客户快照回写、成功日志写入。
- 风险重算失败日志使用独立记录方法，便于演示失败日志，同时避免记录请求体全文、堆栈或敏感信息。
- `RequestContextFilter` 能处理 `X-Request-Id` 和 `X-Operator`，并在响应头回写 requestId。
- 边界扫描未发现 Spring Security、Redis、Kafka、RabbitMQ、Nacos、Spring Cloud、缓存等第一阶段之外的能力。
- `application.yml` 中使用本地 demo MySQL 配置，未发现真实生产地址或真实密钥。

## 未验证项

- 当前机器未启动 MySQL 8，因此未用默认 profile 连接真实 MySQL 常驻启动。
- 已通过 test profile 使用 H2 跑通 Flyway 和集成测试。
- 后续若要录制“真实 MySQL 启动”片段，需要先准备 `docker-compose.yml` 或本机 MySQL 8。

## 后续建议

下一步建议优先派发前端页面设计子窗口。理由：

- 后端第一阶段已经有可用契约和代码基线。
- 前端页面可以直接基于 `docs/api-design.md` 和 `poc/backend` 进行设计。
- 前端完成后，课程能形成第一个完整可演示闭环：后端接口 + 前端页面 + 风险重算 + 操作日志。

也可以先派发测试验收子窗口，补齐 HTTP 文件和手工验收清单。但从课程展示价值看，先做前端更容易形成可视化效果。

