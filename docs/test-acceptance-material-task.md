# 测试验收材料子任务

## 子任务目标

产出“客户风险评级与账户查询 POC”的测试验收材料，让课程能够讲清楚：AI 生成代码之后，如何用自动化测试、接口测试、浏览器人工验收、AI 审查和安全脱敏检查来判断它是否真的完成。

本子任务只整理测试与验收材料，不改后端代码、不改前端代码、不调整数据库脚本。

允许新增文档和 HTTP 样例文件，例如：

- `docs/test-acceptance-material.md`
- `docs/http/customer-risk-poc.http`

## 输入基线

必须阅读：

- `README.md`
- `docs/course-blueprint.md`
- `docs/lesson-plan.md`
- `docs/ai-dev-workflow.md`
- `docs/poc-architecture.md`
- `docs/database-design.md`
- `docs/api-design.md`
- `docs/backend-code-implementation-acceptance.md`
- `docs/frontend-code-implementation-acceptance.md`
- `docs/fullstack-integration-acceptance.md`
- `docs/lesson-06-recording-material.md`
- `poc/backend`
- `poc/frontend`

## 子窗口提示词

```text
你现在是 AI 教程项目的“测试验收材料子窗口”。请只负责整理“客户风险评级与账户查询 POC”的测试验收材料，不改后端代码、不改前端代码、不调整数据库脚本。

课程背景：
- 这是面向赞同科技银行解决方案研发同学的 AI 编程入门教程。
- 教程主线是用 Claude Code + DeepSeek 从 0 搭建一个最小可运行 Java 微服务 POC。
- 当前第一阶段已经完成 Spring Boot 3.x 后端、Vue 3 前端和前后端联调闭环。
- 这节材料要服务后续“AI 验收、人工验收和安全运维验收”的课程，也要给主控窗口提供可复验的质量基线。

请先阅读：
- README.md
- docs/course-blueprint.md
- docs/lesson-plan.md
- docs/ai-dev-workflow.md
- docs/poc-architecture.md
- docs/database-design.md
- docs/api-design.md
- docs/backend-code-implementation-acceptance.md
- docs/frontend-code-implementation-acceptance.md
- docs/fullstack-integration-acceptance.md
- docs/lesson-06-recording-material.md
- poc/backend
- poc/frontend

输出要求：

1. 新增 `docs/test-acceptance-material.md`，内容至少包括：
   - 测试验收目标和边界。
   - POC 测试分层：单元测试、集成测试、API 测试、前端构建、浏览器联调、AI 审查、人工验收、安全脱敏检查。
   - 本地启动与验证命令清单，区分后端 test profile、后端默认 MySQL profile、前端 Vite。
   - 测试数据说明，明确只使用 `CUST10000*`、`SAMPLE-*`、`masked`、`sample` 语义数据。
   - 后端自动化测试矩阵，覆盖已有测试类和核心验证点。
   - API 验收矩阵，覆盖客户列表、客户详情、账户、当前风险、风险历史、风险重算、操作日志、404、409、参数校验。
   - 前端人工验收矩阵，覆盖仪表盘、客户列表、客户详情、风险重算弹窗、操作日志、错误展示。
   - 风险重算事务验收，必须说明 risk_record 新增、customer 风险快照回写、operation_log 写入三项效果。
   - 安全与脱敏检查清单，覆盖页面、日志、配置、样例数据、错误提示、密钥和生产地址。
   - AI 验收提示词模板：用于让一个新会话审查代码、接口契约、测试覆盖、安全风险。
   - 人工验收清单：讲师录屏前逐项勾选。
   - 常见失败和兜底方案：MySQL 未启动、端口占用、前端代理失败、H2 profile 与 MySQL profile 差异、npm audit 提示、Vite chunk warning。
   - 录制讲解顺序建议。
   - 主控窗口验收清单。

2. 新增 `docs/http/customer-risk-poc.http`，内容至少包括：
   - baseUrl 变量：`http://localhost:8080`
   - `X-Operator` 和 `X-Request-Id` 示例头。
   - GET /api/customers 正常分页。
   - GET /api/customers 风险等级筛选。
   - GET /api/customers 状态筛选。
   - GET /api/customers/{customerNo} 成功。
   - GET /api/customers/{customerNo}/accounts 成功。
   - GET /api/customers/{customerNo}/risk 成功。
   - GET /api/customers/{customerNo}/risk-records 成功。
   - POST /api/customers/{customerNo}/risk/recalculate 成功，body 使用 `reasonSample`。
   - GET /api/operation-logs 按 operationType 查询。
   - 404 客户不存在样例：`CUST999999`。
   - 409 关闭客户不允许重算样例：`CUST100006`。
   - 参数校验失败样例。

3. 不要引入真实账号、手机号、身份证号、客户姓名、生产地址、密钥或内部系统地址。

4. 不要要求必须连接真实银行环境。默认演示优先使用后端 test profile + H2 内存库；如果提到 MySQL，只作为真实本地环境验证的可选路径。

5. 风格要求：
   - 像真实项目的验收手册，清晰、可执行、可录屏。
   - 不要夸大 AI，重点强调“AI 产出必须被测试、审查和人工验收接住”。
   - 面向银行研发场景，突出脱敏、错误信息、日志和配置安全。

最后给主控窗口一段简短汇报，说明：
- 产出了哪些文件。
- 覆盖了哪些测试和验收层次。
- 是否足够支撑后续第 9 课或验收复盘课录制。
- 是否有未实际执行的命令或依赖环境限制。
```

## 主控验收重点

- 是否只新增测试验收材料和 HTTP 样例，未改业务代码。
- 是否覆盖后端、前端、接口、浏览器联调、AI 审查、人工验收和安全脱敏。
- 是否能支撑讲师录屏前逐项复验。
- 是否使用样例数据，没有真实敏感信息。
- 是否明确 test profile/H2 与默认 MySQL profile 的差异。
- 是否给出可直接复制执行或导入 IDE REST Client 的 HTTP 请求样例。
- 是否能和第 6 课联调材料、第 9 课验收复盘形成衔接。
