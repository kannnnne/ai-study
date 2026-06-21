# 客户风险评级与账户查询 POC 测试验收材料

## 1. 测试验收目标和边界

本文用于验收“客户风险评级与账户查询 POC”第一阶段成果，目标是给课程第 9 课“AI 验收、人工验收和安全运维验收”提供可复验的质量基线。

验收目标：

- 确认 Spring Boot 后端、Vue 前端、API 契约、样例数据和浏览器联调闭环可重复验证。
- 确认 AI 生成或辅助生成的代码被自动化测试、接口测试、人工操作、安全脱敏检查接住。
- 确认风险重算写操作具备可解释的事务效果：新增风险记录、回写客户风险快照、写入操作日志。
- 确认录屏材料只使用脱敏样例数据，不引导连接真实银行环境。

验收边界：

- 本材料只整理测试验收方案、命令、矩阵、HTTP 样例和讲解顺序。
- 不修改后端代码，不修改前端代码，不调整数据库脚本。
- 默认演示优先使用后端 `test` profile + H2 内存库；MySQL 只作为本地真实环境验证的可选路径。
- 风险评级规则为教学样例，不代表真实风控、反洗钱、征信或核心账务规则。

## 2. POC 测试分层

| 层次 | 目标 | 当前材料中的验收方式 | 通过标准 |
| --- | --- | --- | --- |
| 单元测试 | 验证风险计算规则边界 | `RiskCalculatorTest` | 低 / 中 / 高风险规则结果稳定，分数和原因可解释 |
| 集成测试 | 验证 Mapper、Service、事务、日志 | `MapperIntegrationTest`、`RiskServiceIntegrationTest` | H2 + Flyway 样例数据下通过，事务三项效果可断言 |
| API 测试 | 验证统一响应、状态码、错误码、分页和参数校验 | `ControllerIntegrationTest`、`docs/http/customer-risk-poc.http` | 成功、400、404、409 场景可重复跑通 |
| 前端构建 | 验证 TypeScript、Vite 打包和依赖链 | `npm run build` | 构建成功；chunk warning 可解释 |
| 浏览器联调 | 验证页面到接口再到日志的闭环 | 手工操作仪表盘、列表、详情、重算、日志页 | 主流程和错误路径均可展示 |
| AI 审查 | 用新会话审查代码、契约、测试、安全 | 本文 AI 验收提示词模板 | 输出 P0/P1/P2 问题或明确无阻断问题 |
| 人工验收 | 讲师录屏前逐项复验 | 本文人工验收清单 | 每项勾选，有失败项则记录兜底方案 |
| 安全脱敏检查 | 检查页面、日志、配置、样例数据和错误提示 | 本文安全清单 + 关键词扫描 | 不出现真实账号、手机号、证件号、客户姓名、生产地址、密钥或内部系统地址 |

核心原则：AI 产出不是完成定义，测试、审查和人工验收通过才算完成。

## 3. 本地启动与验证命令清单

### 3.1 后端自动化测试，使用 test profile + H2

```powershell
cd D:\project\ai-study\poc\backend
mvn test
```

用途：

- 执行后端 9 个自动化测试。
- 使用 `src/test/resources/application-test.yml`。
- 使用 H2 内存库和 `classpath:db/testmigration` 下的 Flyway 测试脚本。

通过参考：

```text
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

### 3.2 后端演示启动，使用 test profile + H2

```powershell
cd D:\project\ai-study\poc\backend
mvn spring-boot:test-run -Dspring-boot.run.profiles=test
```

用途：

- 课程录屏和浏览器联调优先使用。
- 不依赖本机 MySQL。
- 重启后 H2 内存库会重新加载样例数据，便于复验。

健康检查：

```powershell
Invoke-WebRequest http://localhost:8080/actuator/health
```

### 3.3 后端默认 MySQL profile，可选

```powershell
cd D:\project\ai-study\poc\backend
mvn spring-boot:run
```

用途：

- 只用于本地 MySQL 8 已准备好的真实本地环境验证。
- 默认连接本机 `localhost:3306/ai_study_risk_poc`，不是银行真实环境。
- 如果 MySQL 未启动或库、账号、权限未准备，启动失败属于环境问题，不阻塞课程录屏。

验收口径：

- 课程默认不要求连接真实银行环境。
- MySQL 路径只验证 Flyway、MyBatis 和本地数据库行为，不能引入生产地址、真实密钥或真实客户数据。

### 3.4 前端 Vite

```powershell
cd D:\project\ai-study\poc\frontend
npm run dev -- --host 127.0.0.1
```

访问：

```text
http://127.0.0.1:5173/dashboard
```

说明：

- `vite.config.ts` 中 `/api` 代理到 `http://localhost:8080`。
- 前端页面默认通过后端 test profile 的接口完成演示。

### 3.5 前端构建

```powershell
cd D:\project\ai-study\poc\frontend
npm run build
```

通过标准：

- `vue-tsc --noEmit` 通过。
- `vite build` 产物生成成功。
- Vite chunk size warning、Rollup PURE 注释提示属于当前 POC 可接受提示，需要在录屏中解释为构建提示，不等同功能失败。

## 4. 测试数据说明

本 POC 只使用脱敏语义数据：

- 客户号：`CUST10000*`，例如 `CUST100001` 到 `CUST100006`。
- 不存在客户样例：`CUST999999`。
- 样例账号、手机号、证件号、客户端地址：统一使用 `SAMPLE-*`、`****`、`masked`、`sample` 语义。
- 页面和接口字段保留 `mobileMasked`、`idNoMasked`、`accountNoMasked`、`riskReasonSample`、`errorMessageSample`、`clientIpMasked` 等命名。
- 操作人只使用 `demo-query`、`demo-admin`、`demo-operator` 等教学值。

禁止内容：

- 真实账号、真实手机号、真实身份证号、真实客户姓名。
- 真实生产地址、内部系统地址、真实密钥、真实令牌。
- 可被误解为真实银行卡号、证件号或客户隐私的连续数字串。

录屏提醒：

- 所有截图、终端输出、浏览器 Network、HTTP 文件都应只出现样例数据。
- 如果讲 MySQL 默认 profile，也只能讲“本地 MySQL 可选验证”，不能要求连接真实银行环境。

## 5. 后端自动化测试矩阵

| 测试类 | 类型 | 覆盖点 | 核心断言 |
| --- | --- | --- | --- |
| `RiskCalculatorTest` | 单元测试 | 风险规则计算 | 异常标记 + 低余额客户为 `MEDIUM`，冻结客户 + 冻结账户为 `HIGH`，风险原因包含样例文本 |
| `MapperIntegrationTest` | Mapper 集成测试 | 账户查询、风险历史排序 | `CUST100001` 有 2 个 `SAMPLE-ACC-****-*` 账户；`CUST100002` 风险记录按计算时间倒序 |
| `RiskServiceIntegrationTest` | Service 集成测试 | 风险重算事务、失败日志 | 重算 `CUST100002` 后新增 `risk_record`、回写 `customer.current_risk_score`、写入成功 `operation_log`；`CUST999999` 写入脱敏失败日志 |
| `ControllerIntegrationTest` | API 集成测试 | 统一响应、请求号、参数校验、409 | 客户列表返回 `000000` 并透传 `X-Request-Id`；非法 `riskLevel` 返回 `400001`；关闭客户重算返回 `409001` |

当前主控已记录后端测试结果：

```text
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

建议新增测试方向，留作后续课程扩展：

- `GET /api/customers/{customerNo}` 的 404 Controller 测试。
- `GET /api/customers/{customerNo}/accounts` 的成功和 404 测试。
- `GET /api/operation-logs` 的 `operationType` 筛选测试。
- 风险历史分页参数非法的 400 测试。

## 6. API 验收矩阵

HTTP 样例文件：`docs/http/customer-risk-poc.http`。

| 场景 | 请求 | 样例数据 | 预期 |
| --- | --- | --- | --- |
| 客户列表正常分页 | `GET /api/customers?pageNo=1&pageSize=10` | 全部样例客户 | HTTP 200，`success=true`，`code=000000`，分页字段完整 |
| 客户列表按风险等级筛选 | `GET /api/customers?riskLevel=HIGH` | `CUST100003`、`CUST100006` | HTTP 200，结果只包含 `HIGH` 风险客户 |
| 客户列表按状态筛选 | `GET /api/customers?status=ACTIVE` | `CUST100001/2/4/5` | HTTP 200，结果只包含 `ACTIVE` 客户 |
| 客户详情成功 | `GET /api/customers/CUST100002` | 中风险客户 | HTTP 200，包含基础信息、脱敏字段、当前风险快照 |
| 账户列表成功 | `GET /api/customers/CUST100001/accounts` | 两个样例账户 | HTTP 200，账号字段为 `accountNoMasked` |
| 当前风险成功 | `GET /api/customers/CUST100003/risk` | 高风险客户 | HTTP 200，当前风险等级为 `HIGH` |
| 风险历史成功 | `GET /api/customers/CUST100002/risk-records` | 两条以上历史记录 | HTTP 200，按 `calculatedAt` 倒序 |
| 风险重算成功 | `POST /api/customers/CUST100002/risk/recalculate` | `reasonSample` | HTTP 200，返回 `transactionEffect` 三项为 `true` |
| 操作日志查询 | `GET /api/operation-logs?operationType=RECALCULATE_RISK` | 成功和失败样例日志 | HTTP 200，可追踪 `requestId`、`operator`、`targetBizNo`、`result` |
| 404 客户不存在 | `GET /api/customers/CUST999999` | 不存在客户 | HTTP 404，`code=404001`，不暴露 SQL 或堆栈 |
| 409 关闭客户不允许重算 | `POST /api/customers/CUST100006/risk/recalculate` | 关闭客户 | HTTP 409，`code=409001`，message 为业务提示 |
| 参数校验失败 | `GET /api/customers?riskLevel=UNKNOWN` | 非法枚举 | HTTP 400，`code=400001`，响应含 `requestId` |

验收注意：

- 每个请求都带 `X-Request-Id` 和 `X-Operator`，方便日志追踪。
- 写操作会改变 H2 内存库中的风险历史和日志条数；需要恢复初始数据时重启后端 test profile。
- 失败响应也必须保持统一响应结构。

## 7. 前端人工验收矩阵

| 页面 / 功能 | 操作 | 预期结果 | 重点检查 |
| --- | --- | --- | --- |
| 仪表盘 `/dashboard` | 打开页面 | 展示总客户 6、高风险 2、冻结 / 关闭 2、最近日志 5 | 数据来自样例接口，图表不展示真实信息 |
| 客户列表 `/customers` | 按 `HIGH` 筛选 | `Total 2`，只显示高风险客户 | 请求参数为 `riskLevel=HIGH`，风险标签正确 |
| 客户列表 `/customers` | 按 `ACTIVE` 筛选 | `Total 4`，只显示正常客户 | 请求参数为 `status=ACTIVE` |
| 客户详情 `/customers/CUST100002` | 打开详情页 | 展示基础信息、账户、当前风险、历史风险 | 字段保留 `masked` / `sample` 语义 |
| 风险重算弹窗 | 输入或保留 `reasonSample`，点击触发 | 展示重算结果和 `transactionEffect` | 三项事务效果均为 `true`，弹窗不被刷新卸载 |
| 操作日志 `/operation-logs` | 筛选 `RECALCULATE_RISK` | 能看到成功和失败日志 | `requestId`、`operator`、`targetBizNo`、`result` 可追踪 |
| 错误展示 | 访问 `/customers/CUST999999` | 展示客户不存在、`404001`、`requestId` | 不展示 SQL、堆栈、连接串 |
| 错误展示 | 对 `CUST100006` 触发重算 | 展示关闭客户不允许重算、`409001` | 前端按统一错误响应展示 |

人工验收不只看“页面能点”，还要确认：用户操作、请求参数、后端响应、页面状态和操作日志能串成一条可解释链路。

## 8. 风险重算事务验收

风险重算是本 POC 最重要的写操作，验收时不能只看成功提示。

接口：

```text
POST /api/customers/{customerNo}/risk/recalculate
```

成功验收对象建议使用：

```text
CUST100002
```

请求体：

```json
{
  "reasonSample": "课程验收手动触发风险重算样例",
  "ruleCode": "DEMO_RULE_V1"
}
```

必须确认三项效果：

| 效果 | 说明 | 验收方法 |
| --- | --- | --- |
| `risk_record` 新增 | 生成一条新的风险评级历史记录，`sourceType=MANUAL`，`calculatedBy` 来自 `X-Operator` | 调用重算前后查看风险历史条数，或检查响应 `transactionEffect.riskRecordInserted=true` |
| `customer` 风险快照回写 | 回写 `currentRiskLevel`、`currentRiskScore`、`lastRiskCalculatedAt` | 重算后刷新客户详情或当前风险接口，确认快照与本次结果一致 |
| `operation_log` 写入 | 记录 `RECALCULATE_RISK` 操作，包含 `requestId`、`operator`、目标客户、结果 | 进入操作日志页按 `operationType=RECALCULATE_RISK` 或 `requestId` 查询 |

失败路径也要验收：

- `CUST999999`：客户不存在，返回 `404001`，写入安全失败日志。
- `CUST100006`：关闭状态客户不允许重算，返回 `409001`，前端展示业务提示。

验收结论模板：

```text
风险重算验收通过：接口返回成功，transactionEffect 三项均为 true；
风险历史新增记录，客户当前风险快照已刷新，操作日志可按 requestId 追踪。
失败路径 404 / 409 可复现，错误信息未暴露敏感内容。
```

## 9. 安全与脱敏检查清单

页面检查：

- [ ] 页面只展示 `CUST10000*`、`SAMPLE-*`、`masked`、`sample` 语义数据。
- [ ] 不展示真实手机号、证件号、账号、客户姓名。
- [ ] 错误提示只展示 `message`、`code`、`requestId` 等必要信息。

日志检查：

- [ ] 操作日志字段使用 `errorMessageSample`、`operationDescSample`、`clientIpMasked`。
- [ ] 不记录完整请求体、完整账号、手机号、证件号、密钥。
- [ ] 失败日志不包含 Java 异常堆栈、SQL、连接串。

配置检查：

- [ ] `application.yml` 只包含本地 demo 配置，不包含生产地址或真实密钥。
- [ ] 前端代理只指向 `http://localhost:8080`。
- [ ] 不提交 `.env` 中的真实 token、API key、私钥。

样例数据检查：

- [ ] 客户号使用 `CUST10000*`。
- [ ] 账号使用 `SAMPLE-ACC-****-*`。
- [ ] 手机号和证件号使用 `SAMPLE-MOBILE-*`、`SAMPLE-ID-*`。
- [ ] 备注、风险原因、错误信息字段带 `sample` 语义。

错误提示检查：

- [ ] 400 参数错误不暴露后端实现细节。
- [ ] 404 客户不存在不暴露数据库查询信息。
- [ ] 409 业务冲突只说明业务状态，不泄露内部流程。
- [ ] 500 如出现，应只返回统一错误，不显示堆栈。

密钥和地址检查：

- [ ] 仓库中不出现真实生产域名、内网系统地址、数据库生产地址。
- [ ] 不出现真实 access key、secret、token、私钥。
- [ ] 录屏浏览器地址栏不打开生产系统或内部系统页面。

可选关键词扫描：

```powershell
cd D:\project\ai-study
rg -n "password|secret|token|AKIA|BEGIN PRIVATE KEY|prod|production|身份证|手机号|银行卡|http://" docs poc
```

扫描结果需要人工判断：`localhost`、`SAMPLE-*`、教学说明文字可以保留；真实敏感信息必须移除。

## 10. AI 验收提示词模板

将以下提示词复制到一个全新会话中使用。建议只提供必要文件路径或摘要，不要把真实密钥、真实日志或真实客户数据发给 AI。

```text
你现在是“客户风险评级与账户查询 POC”的独立验收审查员。请站在银行研发项目验收视角，审查当前 POC 是否满足第一阶段课程基线。

背景：
- 项目用于 AI 编程入门教程，不连接真实银行环境。
- 第一阶段是 Spring Boot 3.x 单体后端 + Vue 3 前端 + H2/MySQL 本地样例数据。
- 样例数据只能使用 CUST10000*、SAMPLE-*、masked、sample 语义。
- AI 产出必须被测试、代码审查、人工验收和安全检查接住。

请重点审查：
1. 代码实现是否符合 docs/poc-architecture.md、docs/database-design.md、docs/api-design.md。
2. API 契约是否稳定：统一响应、错误码、分页、X-Request-Id、X-Operator。
3. 自动化测试是否覆盖核心路径：客户列表、参数校验、风险计算、风险重算事务、失败日志。
4. 风险重算是否在同一事务内完成 risk_record 新增、customer 风险快照回写、operation_log 写入。
5. 前端是否严格调用 /api/... 契约，是否正确展示加载、错误和脱敏字段。
6. 安全风险：页面、日志、配置、样例数据、错误提示是否泄露真实账号、手机号、证件号、客户姓名、生产地址、密钥或内部系统地址。
7. 第一阶段边界：是否引入了真实登录、权限、MQ、缓存、微服务、分布式事务等不必要复杂度。

输出格式：
- P0：必须立即修复，否则不能录屏或验收的问题。
- P1：应在验收前修复的问题。
- P2：建议优化或后续课程扩展的问题。
- 已通过项：简要列出有证据的通过点。
- 建议补充测试：只列和当前 POC 强相关的测试。

要求：
- 只列有证据的问题，不要猜测。
- 不要夸大 AI 能力，重点说明哪些地方需要测试和人工验收兜底。
- 如果没有 P0/P1，请明确说明。
```

## 11. 人工验收清单

讲师录屏前逐项勾选：

- [ ] 后端 `mvn test` 通过。
- [ ] 后端 test profile 能启动，`/actuator/health` 返回 `UP`。
- [ ] 前端 `npm run build` 通过。
- [ ] 前端 Vite 能启动，`/dashboard` 可访问。
- [ ] 客户列表分页正常。
- [ ] 客户列表 `riskLevel=HIGH` 筛选正常。
- [ ] 客户列表 `status=ACTIVE` 筛选正常。
- [ ] `CUST100002` 详情页展示基础信息、账户、当前风险、历史风险。
- [ ] 风险重算成功后展示 `transactionEffect` 三项为 `true`。
- [ ] 操作日志可按 `RECALCULATE_RISK` 查询。
- [ ] `CUST999999` 返回并展示 `404001`。
- [ ] `CUST100006` 重算返回并展示 `409001`。
- [ ] HTTP 文件中的核心请求可跑通。
- [ ] 页面、日志、HTTP 请求、终端输出没有真实敏感信息。
- [ ] 录屏讲解中明确 test profile + H2 是默认演示路径，MySQL 只是本地可选验证。

## 12. 常见失败和兜底方案

| 问题 | 表现 | 处理 |
| --- | --- | --- |
| MySQL 未启动 | 默认 profile 启动失败，连接 `localhost:3306` 报错 | 课程录屏切回 `mvn spring-boot:test-run -Dspring-boot.run.profiles=test`；MySQL 只作为可选本地验证 |
| 8080 端口占用 | 后端启动失败，提示端口被占用 | 关闭占用进程；录屏不建议临时换端口，避免前端代理和 HTTP 文件不一致 |
| 5173 端口占用 | Vite 自动切换端口或启动失败 | 可接受 Vite 换端口，但录屏时明确实际访问地址；优先关闭占用进程 |
| 前端代理失败 | 页面报 Network Error 或 404 | 先查后端健康检查；再查 `vite.config.ts` 的 `/api -> http://localhost:8080`；浏览器 Network 看请求路径 |
| H2 profile 与 MySQL profile 差异 | H2 通过但 MySQL 启动或 SQL 行为异常 | 以 H2 作为录屏默认路径；MySQL 差异单独记录为本地环境验证问题，必要时补 MySQL 复验 |
| `npm audit` 提示 | 安装或审计报告漏洞提示 | 不执行 `npm audit fix --force` 破坏锁文件；记录风险，后续统一升级依赖 |
| Vite chunk warning | 构建提示 chunk 超过建议体积 | 当前 ECharts + Element Plus 组合可接受；说明为 POC 构建提示，不影响功能验收 |
| 风险重算结果重复增加历史 | 多次点击导致历史记录变多 | 这是写操作预期效果；需要恢复初始数据时重启 test profile |
| 错误页面没有数据 | 访问不存在客户或后端未启动 | 区分业务错误和环境错误；先确认健康检查，再看前端错误组件 |

## 13. 录制讲解顺序建议

1. 开场说明：AI 写完代码不代表完成，验收要覆盖测试、接口、页面、日志和安全。
2. 展示测试分层图或表格，说明本节不是单纯跑命令。
3. 启动后端 test profile，说明 H2 内存库和脱敏样例数据。
4. 运行或展示 `mvn test` 结果，讲 4 个后端测试类覆盖点。
5. 启动前端 Vite，打开 `/dashboard`。
6. 在 `/customers` 演示分页、`HIGH` 风险筛选、`ACTIVE` 状态筛选。
7. 打开 `/customers/CUST100002`，讲详情页多接口聚合。
8. 触发风险重算，重点讲 `transactionEffect` 三项。
9. 到 `/operation-logs` 按 `RECALCULATE_RISK` 追踪日志。
10. 用 HTTP 文件演示 404、409、参数校验失败。
11. 做安全脱敏检查：页面、日志、配置、错误提示。
12. 收束：AI 是工程搭档，最终责任仍由测试、审查和人工验收承接。

## 14. 主控窗口验收清单

- [ ] 本文覆盖测试验收目标和边界。
- [ ] 本文覆盖单元测试、集成测试、API 测试、前端构建、浏览器联调、AI 审查、人工验收、安全脱敏检查。
- [ ] 本文区分后端 test profile、后端默认 MySQL profile、前端 Vite。
- [ ] 本文明确只使用 `CUST10000*`、`SAMPLE-*`、`masked`、`sample` 语义数据。
- [ ] 本文覆盖已有后端测试类和核心验证点。
- [ ] 本文覆盖客户列表、客户详情、账户、当前风险、风险历史、风险重算、操作日志、404、409、参数校验。
- [ ] 本文覆盖仪表盘、客户列表、客户详情、风险重算弹窗、操作日志、错误展示。
- [ ] 本文明确风险重算的三项事务效果。
- [ ] 本文覆盖页面、日志、配置、样例数据、错误提示、密钥和生产地址检查。
- [ ] 本文提供 AI 验收提示词模板。
- [ ] 本文提供讲师录屏前人工验收清单。
- [ ] 本文提供常见失败和兜底方案。
- [ ] 本文提供录制讲解顺序建议。
- [ ] `docs/http/customer-risk-poc.http` 可用于 IDE REST Client 复验核心接口。
- [ ] 本次子窗口没有修改后端代码、前端代码或数据库脚本。

## 给主控窗口的简短汇报

本次产出两份测试验收材料：`docs/test-acceptance-material.md` 和 `docs/http/customer-risk-poc.http`。材料覆盖后端单元测试、集成测试、API 验收、前端构建、浏览器联调、AI 审查、人工验收、安全脱敏检查和录屏兜底方案，重点补齐风险重算事务验收与 404 / 409 / 参数校验失败路径。

当前内容足够支撑后续第 9 课或验收复盘课录制，也能给主控窗口提供可复验质量基线。本子窗口只整理材料，未实际执行 `mvn test`、后端启动、前端构建或浏览器联调；这些命令依赖本机 Java、Maven、Node、端口和可选 MySQL 环境，文中已按 test profile + H2 作为默认演示路径说明。
