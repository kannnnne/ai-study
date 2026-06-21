# 第 9 课录屏材料：AI 验收、人工验收和安全复盘

## 1. 标题与目标

标题：第 9 课：AI 验收、人工验收和安全复盘

一句话目标：演示一个 AI 辅助开发 POC 完成后，如何用自动化测试、接口复验、页面操作、事务副作用、日志追踪、安全脱敏和人工清单证明它“真的完成了”。

本课承接：

- 第 5 课：后端最小服务开发。
- 第 6 课：前端页面和接口联调。
- `docs/test-acceptance-material.md`：测试验收基线。
- `docs/http/customer-risk-poc.http`：HTTP 接口复验样例。

本课最重要的一句话：

```text
AI 验收是辅助，最终责任在人。
```

## 2. 本课要解决的问题

AI 可以很快生成代码、页面、测试和文档，但银行研发场景里不能把“AI 说完成了”当成真正完成。本课要解决的问题是：

```text
AI 生成和前后端联调完成后，如何证明这个 POC 真的完成了？
```

这里的“完成”至少包含：

- 需求目标可追溯：客户查询、风险重算、操作日志、错误场景都能对上需求和设计。
- API 契约稳定：路径、字段、统一响应、错误码、分页、`X-Request-Id`、`X-Operator` 不漂移。
- 自动化测试通过：后端 test profile + H2 能复验核心规则、事务和错误码。
- 前端构建通过：TypeScript 和 Vite 打包能证明页面代码基本可交付。
- 浏览器主流程可复现：Dashboard -> 客户列表 -> 客户详情 -> 风险重算 -> 操作日志。
- 事务副作用成立：风险重算后风险历史新增、客户风险快照回写、操作日志写入。
- 失败路径可解释：客户不存在、关闭客户重算、参数校验失败都有稳定错误码和 requestId。
- 日志可追踪：页面、接口响应、操作日志能通过 requestId 串起来。
- 安全脱敏过关：没有真实客户、账号、手机号、证件号、密钥、生产地址或内部系统信息。
- 录屏可复现：讲师能按清单在干净环境中重新跑出结果。

本课不是让 AI 自己给自己打分，而是把 AI 放进一个可验证的工程流程里。

## 3. 推荐时长与章节安排

建议时长：30 到 40 分钟。

| 章节 | 内容 | 建议时长 |
| --- | --- | --- |
| 1 | 开场：AI 写完不等于验收通过 | 3 分钟 |
| 2 | 展示验收材料和测试分层 | 5 分钟 |
| 3 | 后端测试、前端构建和 HTTP 样例 | 7 分钟 |
| 4 | 浏览器主流程人工验收 | 8 分钟 |
| 5 | 风险重算事务和 requestId 追踪 | 6 分钟 |
| 6 | 错误场景和安全脱敏检查 | 5 分钟 |
| 7 | AI 验收提示词、人工清单和失败兜底 | 5 分钟 |

如果现场时间紧，可以压缩为 25 分钟：

```text
验收分层 -> 后端 mvn test -> HTTP 样例 -> 浏览器主流程 -> 风险重算事务 -> 错误和脱敏 -> 人工清单
```

## 4. 录制前准备

### 4.1 默认演示环境

后端默认使用 test profile + H2：

```powershell
cd D:\project\ai-study\poc\backend
mvn spring-boot:test-run -Dspring-boot.run.profiles=test
```

说明：

- 不依赖本机 MySQL。
- H2 内存库会随启动加载测试 Flyway 样例数据。
- 重启后数据恢复初始状态，适合录屏复验。
- 不连接真实银行环境。

前端使用 Vite：

```powershell
cd D:\project\ai-study\poc\frontend
npm run dev -- --host 127.0.0.1
```

浏览器准备：

```text
http://127.0.0.1:5173/dashboard
http://localhost:8080/actuator/health
```

HTTP 请求样例：

```text
docs/http/customer-risk-poc.http
```

已有验收记录：

- `docs/backend-code-implementation-acceptance.md`
- `docs/frontend-code-implementation-acceptance.md`
- `docs/fullstack-integration-acceptance.md`
- `docs/test-acceptance-material.md`

### 4.2 录屏前检查

- [ ] 关闭真实生产系统页面、聊天窗口、邮箱和含敏感信息的终端。
- [ ] 确认后端 8080、前端 5173 未被占用。
- [ ] 确认浏览器只打开课程样例页面。
- [ ] 确认 HTTP 文件里只出现 `CUST10000*`、`CUST999999`、`SAMPLE-*`、`demo-*`。
- [ ] 准备一份可展示的 `mvn test` 通过结果或现场运行命令。
- [ ] 准备一份可展示的 `npm run build` 通过结果或现场运行命令。
- [ ] 准备解释 Vite chunk warning：当前 POC 可接受，不等同功能失败。

## 5. 验收分层模型

本课建议把验收讲成十层，从业务目标一路落到录屏可复现：

| 层次 | 验收问题 | 证据材料 |
| --- | --- | --- |
| 需求验收 | 是否满足客户查询、风险重算、操作日志、错误场景这些目标 | `docs/poc-architecture.md`、`docs/lesson-plan.md`、浏览器主流程 |
| 设计验收 | 是否遵守单体优先、四张表、统一响应、错误码、日志和脱敏边界 | `docs/poc-architecture.md`、`docs/api-design.md` |
| API 契约验收 | 路径、字段、分页、错误码、requestId 是否稳定 | `docs/api-design.md`、`docs/http/customer-risk-poc.http` |
| 后端自动化测试验收 | 风险规则、Mapper、Service 事务、Controller 错误码是否被测试接住 | `mvn test`、`docs/test-acceptance-material.md` |
| 前端构建和页面验收 | TypeScript、Vite 构建、页面加载、错误展示是否成立 | `npm run build`、浏览器页面 |
| 前后端联调验收 | 用户操作、请求参数、响应、页面状态是否能串起来 | `/dashboard`、`/customers`、`/customers/CUST100002` |
| 事务和数据副作用验收 | 风险重算是否新增历史、回写快照、写操作日志 | `transactionEffect`、风险历史、操作日志 |
| 日志和 requestId 追踪验收 | 是否能从页面错误或响应追到操作日志 | 响应体、响应头、`/operation-logs` |
| 安全脱敏验收 | 页面、日志、配置、HTTP 样例是否不泄露敏感信息 | 脱敏清单、关键词扫描、人工检查 |
| 课程录屏可复现验收 | 讲师和学员是否能按命令、HTTP 文件和步骤复现 | 本文、`docs/test-acceptance-material.md` |

讲解口径：

```text
验收不是一个动作，而是一组证据链。自动化测试证明一部分，HTTP 样例证明一部分，浏览器操作证明一部分，日志和安全检查再证明一部分。最后由人把这些证据串起来判断是否通过。
```

## 6. 录屏操作步骤

### 6.1 展示测试验收材料

打开：

```text
docs/test-acceptance-material.md
```

讲解重点：

- 这份材料是第 9 课的质量基线。
- 它覆盖单元测试、集成测试、API 测试、前端构建、浏览器联调、AI 审查、人工验收、安全脱敏检查。
- 本课不是重新写代码，而是按这份材料做复验。

建议口播：

```text
我们先不急着打开页面。真正的验收要先看标准。如果没有验收标准，页面点通了也只是“看起来成功”。这份测试验收材料就是我们今天的复验路线图。
```

### 6.2 展示 HTTP 请求样例

打开：

```text
docs/http/customer-risk-poc.http
```

重点展示：

- `GET /actuator/health`
- `GET /api/customers?pageNo=1&pageSize=10`
- `GET /api/customers/CUST100002`
- `POST /api/customers/CUST100002/risk/recalculate`
- `GET /api/operation-logs?operationType=RECALCULATE_RISK`
- `GET /api/customers/CUST999999`
- `POST /api/customers/CUST100006/risk/recalculate`
- `GET /api/customers?riskLevel=UNKNOWN`

讲解口径：

```text
HTTP 文件的价值是让接口验收可复现。不是今天我在浏览器里点了一次，而是任何同学拿到这份文件，都能按同样 requestId、同样 operator、同样样例客户跑一遍。
```

### 6.3 展示或运行后端 `mvn test`

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

讲解四类测试：

| 测试类 | 讲解点 |
| --- | --- |
| `RiskCalculatorTest` | 样例风险规则能稳定算出低、中、高 |
| `MapperIntegrationTest` | 账户查询和风险历史排序能在 H2 + Flyway 下跑通 |
| `RiskServiceIntegrationTest` | 风险重算三项事务效果和客户不存在失败日志 |
| `ControllerIntegrationTest` | 统一响应、requestId、参数校验、关闭客户冲突 |

如果现场不运行：

```text
这里可以展示主控验收记录里的通过结果，但录制前讲师自己仍应复跑一次。展示历史记录是兜底，不是偷懒跳过验收。
```

### 6.4 展示前端 `npm run build`

命令：

```powershell
cd D:\project\ai-study\poc\frontend
npm run build
```

通过参考：

```text
vue-tsc --noEmit && vite build
built
```

说明：

- `vue-tsc` 通过，说明 TypeScript 层面基本一致。
- `vite build` 产物生成成功，说明页面代码可打包。
- chunk size warning 和部分 Rollup 提示属于当前 POC 可接受提示，不等同功能失败。

讲解口径：

```text
前端验收不能只看页面能打开。构建通过至少说明类型、依赖和打包链路没有明显断裂。对于 POC，构建警告可以记录；对于正式项目，要进入依赖治理和性能优化清单。
```

### 6.5 启动并演示浏览器主流程

后端：

```powershell
cd D:\project\ai-study\poc\backend
mvn spring-boot:test-run -Dspring-boot.run.profiles=test
```

前端：

```powershell
cd D:\project\ai-study\poc\frontend
npm run dev -- --host 127.0.0.1
```

浏览器路径：

```text
/dashboard -> /customers -> /customers/CUST100002 -> 风险重算 -> /operation-logs
```

验收点：

- `/dashboard`：总客户 6、高风险 2、冻结 / 关闭 2、最近日志 5。
- `/customers`：`HIGH` 筛选后 Total 2，`ACTIVE` 筛选后 Total 4。
- `/customers/CUST100002`：基础信息、账户、当前风险、历史风险可展示。
- 风险重算：弹窗或结果区展示 `transactionEffect` 三项为 `true`。
- `/operation-logs`：按 `RECALCULATE_RISK` 能看到成功和失败日志。

讲解口径：

```text
浏览器验收不是“页面好不好看”，而是验证用户操作、请求参数、后端响应、页面状态和操作日志能不能组成一条可解释链路。
```

### 6.6 演示错误场景

错误场景一：客户不存在。

```text
/customers/CUST999999
```

预期：

- HTTP 404。
- 业务码 `404001`。
- 页面展示客户不存在。
- 展示 requestId。
- 不展示 SQL、堆栈、连接串。

错误场景二：关闭客户不允许风险重算。

```text
CUST100006 -> 风险重算
```

预期：

- HTTP 409。
- 业务码 `409001`。
- 页面展示关闭客户不允许触发风险重算。
- 操作日志可查失败记录。

错误场景三：非法风险等级。

```text
GET /api/customers?pageNo=1&pageSize=10&riskLevel=UNKNOWN
```

预期：

- HTTP 400。
- 业务码 `400001`。
- 响应体仍保持统一结构。

讲解口径：

```text
失败路径不是补充项，而是验收的一等公民。银行系统里很多严重问题不是出在成功路径，而是出在状态冲突、参数错误、异常提示和日志泄露。
```

### 6.7 演示按 requestId 追踪

建议用 HTTP 文件里的固定请求号演示，例如：

```text
REQ-DEMO-RISK-RECALCULATE-001
REQ-DEMO-RISK-409-001
REQ-DEMO-CUSTOMER-404-001
```

追踪步骤：

1. 在 HTTP 文件或浏览器 Network 中找到 `X-Request-Id`。
2. 看响应体 `requestId` 是否一致。
3. 到 `/operation-logs` 按 `requestId` 或 `operationType` 查询。
4. 确认 `operator`、`targetBizNo`、`result`、`errorMessageSample` 可解释。

讲解口径：

```text
requestId 的作用是把问题从“用户说页面报错”变成“这一次请求、这个操作人、这个客户号、这条日志”。这就是研发、测试、运维能协作排查的基础。
```

### 6.8 演示安全脱敏检查

页面检查：

- 只展示 `CUST10000*`、`SAMPLE-*`、`masked`、`sample`。
- 不展示真实客户姓名、手机号、证件号、账号。

日志检查：

- 字段使用 `errorMessageSample`、`operationDescSample`、`clientIpMasked`。
- 不展示堆栈、SQL、连接串、请求体全文。

配置和仓库检查：

```powershell
cd D:\project\ai-study
rg -n "password|secret|token|AKIA|BEGIN PRIVATE KEY|prod|production|身份证|手机号|银行卡|http://" docs poc
```

说明：

- 扫描结果必须人工判断。
- `localhost`、`SAMPLE-*`、教学说明文字可以保留。
- 真实密钥、真实生产地址、真实客户信息必须移除。

讲解口径：

```text
安全检查不能只靠关键词扫描。扫描能帮我们发现疑点，但真正判断是否敏感、是否可以出现在课程材料里，仍然要人负责。
```

## 7. AI 验收提示词模板

以下提示词建议放到全新会话中使用。不要把真实客户数据、真实日志、真实密钥或生产地址发给 AI。

### 7.1 让 AI 按需求验收

```text
你现在是客户风险评级与账户查询 POC 的独立需求验收审查员。

请基于以下材料判断当前 POC 是否满足第一阶段课程需求：
- docs/poc-architecture.md
- docs/api-design.md
- docs/test-acceptance-material.md
- docs/fullstack-integration-acceptance.md

需求目标：
1. 支持客户列表、筛选和客户详情。
2. 支持账户列表、当前风险、历史风险。
3. 支持风险重算，并能展示事务效果。
4. 支持操作日志查询和 requestId 追踪。
5. 支持客户不存在、关闭客户重算、参数校验失败等错误场景。
6. 只使用脱敏样例数据，不连接真实银行环境。

请输出：
- 已满足项，必须带证据。
- P0：阻断录屏或验收的问题。
- P1：验收前应修复的问题。
- P2：后续优化建议。
- 需要人工复核的项。

要求：
- 不要猜测，只列有材料依据的问题。
- 明确说明 AI 验收只是辅助，最终仍需人工验收。
```

### 7.2 让 AI 按 API 契约验收

```text
请站在 API 契约审查员角度，检查当前 POC 是否遵守 docs/api-design.md。

重点检查：
1. 路径是否保持 /api/customers、/api/customers/{customerNo}/risk/recalculate、/api/operation-logs 等契约。
2. 成功和失败是否都返回 ApiResponse<T> 统一结构。
3. 错误码是否覆盖 000000、400001、404001、409001、500000。
4. 分页是否使用 pageNo、pageSize、records、total、pages、hasNext。
5. 请求和响应是否保留 X-Request-Id、X-Operator 和 requestId。
6. 字段是否保留 masked / sample 语义。
7. HTTP 样例 docs/http/customer-risk-poc.http 是否覆盖成功、400、404、409。

输出格式：
- 契约一致项。
- 契约漂移风险。
- 建议补充的 HTTP 样例。
- 需要人工确认的问题。
```

### 7.3 让 AI 按事务副作用验收

```text
请只审查风险重算链路，不要扩大到其他功能。

背景：
- 接口为 POST /api/customers/{customerNo}/risk/recalculate。
- 成功对象建议使用 CUST100002。
- 失败对象包括 CUST999999 和 CUST100006。

请检查：
1. 成功时是否新增 risk_record。
2. 成功时是否回写 customer 当前风险快照。
3. 成功时是否写入 operation_log。
4. 响应 data.transactionEffect 三项是否能证明事务效果。
5. 客户不存在是否返回 404001，并写入脱敏失败日志。
6. 关闭客户是否返回 409001，并写入脱敏失败日志。
7. 是否有测试或 HTTP 样例支撑以上判断。

输出：
- 通过项。
- 风险点。
- 建议人工复验步骤。
- 不要建议引入 MQ、分布式事务或真实风控模型。
```

### 7.4 让 AI 做安全脱敏审查

```text
你现在是银行研发场景下的安全脱敏审查助手。

请审查课程 POC 材料和代码是否存在敏感信息风险。范围：
- docs
- poc/backend
- poc/frontend

重点检查：
1. 是否出现真实客户姓名、真实手机号、真实证件号、真实账号。
2. 是否出现真实生产地址、内部系统地址、密钥、token、私钥。
3. 页面、接口、日志、HTTP 样例是否只使用 CUST10000*、SAMPLE-*、masked、sample、demo-*。
4. 错误响应是否可能暴露 SQL、堆栈、连接串或请求体全文。
5. 配置是否只使用本地教学配置。

输出：
- 疑似风险项：文件、位置、原因。
- 可接受项：例如 localhost、SAMPLE-*、教学说明文字。
- 必须人工确认项。
- 录屏前安全检查清单。

要求：
- 不要输出或扩写任何疑似密钥内容。
- 不要建议连接真实环境。
```

### 7.5 让 AI 生成复盘报告

```text
请基于当前验收材料生成一份 POC 验收复盘报告。

输入材料：
- docs/test-acceptance-material.md
- docs/backend-code-implementation-acceptance.md
- docs/frontend-code-implementation-acceptance.md
- docs/fullstack-integration-acceptance.md
- docs/http/customer-risk-poc.http

报告结构：
1. 验收结论。
2. 已验证范围。
3. 自动化测试结果。
4. API 契约和 HTTP 复验结果。
5. 浏览器人工验收结果。
6. 风险重算事务副作用。
7. 错误场景和 requestId 追踪。
8. 安全脱敏检查。
9. 剩余风险和后续建议。

要求：
- 只写有证据的结论。
- 明确说明 AI 验收是辅助，最终责任在人。
- 不引入真实客户数据、生产地址、密钥或内部系统细节。
```

## 8. 人工验收清单

录屏前由讲师逐项勾选：

- [ ] 后端 `mvn test` 通过。
- [ ] 后端 test profile + H2 能启动。
- [ ] `/actuator/health` 返回 `UP`。
- [ ] 前端 `npm run build` 通过。
- [ ] 前端 Vite 能启动，`/dashboard` 可访问。
- [ ] HTTP 文件中的健康检查、客户列表、客户详情可跑通。
- [ ] HTTP 文件中的风险重算成功样例可跑通。
- [ ] HTTP 文件中的 400、404、409 样例可跑通。
- [ ] `/dashboard` 展示总客户 6、高风险 2、冻结 / 关闭 2、最近日志 5。
- [ ] `/customers` 的 `HIGH` 筛选结果为 Total 2。
- [ ] `/customers` 的 `ACTIVE` 筛选结果为 Total 4。
- [ ] `/customers/CUST100002` 展示基础信息、账户、当前风险、历史风险。
- [ ] 风险重算成功后 `transactionEffect` 三项为 `true`。
- [ ] 风险重算后风险历史新增或当前风险快照刷新可观察。
- [ ] 操作日志可按 `RECALCULATE_RISK` 查询。
- [ ] 操作日志可通过 requestId 追踪成功或失败请求。
- [ ] `CUST999999` 展示 `404001` 和 requestId。
- [ ] `CUST100006` 重算展示 `409001` 和 requestId。
- [ ] 错误页面不展示 SQL、堆栈、连接串。
- [ ] 页面、HTTP 文件、终端输出、日志不出现真实客户、账号、手机号、证件号。
- [ ] 仓库中不出现真实密钥、token、私钥、生产地址或内部系统地址。
- [ ] 录屏中明确 test profile + H2 是默认演示路径，MySQL 只是本地可选验证。

人工验收结论模板：

```text
本次第 9 课录屏前验收通过。后端自动化测试、前端构建、HTTP 接口样例、浏览器主流程、风险重算事务副作用、错误场景、requestId 追踪和安全脱敏检查均已覆盖。AI 审查只作为辅助证据，最终验收结论由讲师按人工清单确认。
```

## 9. 失败兜底方案

| 问题 | 表现 | 兜底 |
| --- | --- | --- |
| MySQL 不可用 | 默认 profile 启动失败或连接本地库失败 | 切回 `mvn spring-boot:test-run -Dspring-boot.run.profiles=test`；本课默认演示 H2 |
| 后端测试失败 | `mvn test` 不通过 | 先展示测试失败名称和原因，不硬录通过；可改为讲解验收方法，修复后补录结果 |
| 前端构建失败 | `npm run build` 不通过 | 先看 TypeScript 错误或依赖错误；录屏可展示已有验收记录，但正式录制前必须复验 |
| 浏览器联调失败 | 页面 Network Error、接口 404 或代理异常 | 先查 `/actuator/health`，再查 Vite `/api -> http://localhost:8080` 代理 |
| 8080 端口占用 | 后端启动失败 | 关闭占用进程；不建议临时改后端端口，避免 HTTP 文件和前端代理不一致 |
| 5173 端口占用 | Vite 自动切端口或启动失败 | 可接受临时端口，但口播说明实际访问地址；优先关闭占用进程 |
| requestId 对不上 | 响应体、响应头、操作日志不一致 | 使用 HTTP 文件固定 `X-Request-Id` 复验；区分查询日志本身产生的新 requestId |
| 风险历史越点越多 | 多次风险重算导致历史记录增加 | 这是写操作预期效果；重启 test profile 恢复 H2 初始样例数据 |
| 404 / 409 演示失败 | 选错客户或后端数据状态变化 | 重新使用 `CUST999999` 和 `CUST100006`；必要时重启 test profile |
| 安全扫描发现疑似敏感信息 | rg 扫描命中 password、token、prod 等词 | 人工判断是否为教学文字或本地样例；真实敏感信息必须移除后再录屏 |
| AI 审查输出泛泛而谈 | AI 列出无证据问题或建议扩范围 | 要求 AI 只基于文件证据输出，并按 P0/P1/P2 分类；不采纳无证据建议 |

兜底口径：

```text
验收课里出现失败并不可怕。真正重要的是不要把失败藏起来，而是让学员看到如何定位：先区分环境问题、代码问题、契约问题、数据问题和安全问题，再决定是否修复、记录或切换演示路径。
```

## 10. 常见误区

### 10.1 只看页面，不看接口

页面能点不代表契约稳定。必须看请求路径、参数、响应体、错误码和 requestId。

### 10.2 只跑成功路径，不跑失败路径

成功路径只能证明主流程可用。404、409、400 才能证明错误设计、前端展示和日志追踪是否可靠。

### 10.3 只看返回结果，不验事务副作用

风险重算返回 `success=true` 还不够。必须确认：

- `risk_record` 新增。
- `customer` 当前风险快照回写。
- `operation_log` 写入。

### 10.4 只让 AI 自评，不做人工验收

AI 可以帮助审查和生成清单，但 AI 不能承担最终验收责任。人工验收必须按清单操作、记录结果和判断风险。

### 10.5 忘记检查脱敏和日志

课程材料、截图、终端输出、HTTP 文件和日志都可能泄露信息。录屏前必须检查真实客户、账号、手机号、证件号、密钥、生产地址和内部系统地址。

### 10.6 把 POC 规则讲成真实银行规则

风险评级规则只是教学样例，不代表真实风控、反洗钱、征信或核心账务规则。

## 11. 半口语口播稿

大家好，这节是第 9 课，AI 验收、人工验收和安全复盘。

前面几节课里，我们已经让 AI 辅助完成了后端、前端、接口联调和测试材料。到这里很多同学可能会有一个直觉：页面能打开，接口能返回，AI 也说没问题，那是不是就完成了？

在真实研发里，答案一定是：还不够。尤其在银行研发场景里，完成不是“AI 写完了”，也不是“我点了一下页面没报错”。真正的完成，要能拿出证据：需求对上了，设计没漂，接口契约稳定，测试能跑，页面主流程能复现，失败路径能解释，日志能追踪，敏感信息没有泄露。

所以这节课我们不写新代码，只做验收复盘。大家可以把它理解成一次录屏前的质量闸门。

先打开 `docs/test-acceptance-material.md`。这份材料已经把 POC 的验收分层写清楚了：后端自动化测试、API 测试、前端构建、浏览器联调、AI 审查、人工验收和安全脱敏检查。我们今天就是按这条线走一遍。

这里先强调一个原则：AI 验收是辅助，最终责任在人。AI 很适合帮我们从需求、契约、事务和安全角度找问题，但它只能基于我们提供的上下文判断。它看不到现场环境，也不能替我们确认页面、日志和命令结果。

接着看 HTTP 文件，`docs/http/customer-risk-poc.http`。这里保存了可复现的接口请求。它的价值是让验收不是靠记忆，而是靠样例。健康检查、客户列表、客户详情、风险重算、操作日志、404、409、400 都有固定请求。每个请求都带 `X-Request-Id` 和 `X-Operator`，这样后面才能追日志。

然后看后端测试。我们在 `poc/backend` 下运行 `mvn test`。通过结果是 9 个测试全部通过。这里不是为了炫测试数量，而是看覆盖层次：风险规则、Mapper 查询、风险重算事务、Controller 错误码和 requestId。

后端测试通过以后，再看前端构建。进入 `poc/frontend`，运行 `npm run build`。它会先跑 `vue-tsc`，再跑 Vite build。构建通过说明类型和打包链路没有明显断裂。像 chunk size warning 这类提示，POC 阶段可以记录为可接受提醒，但正式项目要进入性能和依赖治理。

接下来启动后端 test profile 和前端 Vite。这里我们默认用 H2 内存库，而不是 MySQL。原因很简单：课程录制要可复现，不依赖大家本机数据库。MySQL 可以作为本地可选验证，但不能连接真实银行环境。

打开 `/dashboard`。我们先看总客户 6、高风险 2、冻结和关闭 2、最近日志 5。这个页面证明前端能拿到客户数据并做 POC 级展示，但我们也要提醒，生产统计口径不能随便在前端拼。

再进入 `/customers`，筛选 HIGH，看到 Total 2；筛选 ACTIVE，看到 Total 4。这里验收的是用户操作、请求参数、后端筛选和页面展示是否一致。

打开 `CUST100002` 详情。这个页面背后有客户详情、账户列表、当前风险、历史风险多个接口。我们检查的不是页面是否漂亮，而是字段是否对契约，是否保留 `mobileMasked`、`accountNoMasked`、`riskReasonSample` 这些脱敏和样例语义。

现在触发风险重算。这个是整套 POC 最重要的写操作。我们不能只看一个成功提示，要看 `transactionEffect` 三项：风险记录插入、客户风险快照回写、操作日志写入。如果这三项没有证据，重算就没有真正验收通过。

然后进入操作日志，按 `RECALCULATE_RISK` 筛选。这里要把刚才的操作通过 requestId、operator、targetBizNo 和 result 追出来。requestId 的价值就在这里：把页面的一次点击，接口的一次响应，后端的一条日志，串成一条可排查链路。

接下来演示失败路径。访问 `CUST999999`，应该看到客户不存在、`404001` 和 requestId。对 `CUST100006` 触发风险重算，应该看到关闭客户不允许重算、`409001` 和 requestId。非法风险等级则应该返回 `400001`。失败路径一定要验，因为很多真实问题都出现在错误处理和状态冲突上。

最后做安全脱敏检查。页面和日志里只能出现 `CUST10000*`、`SAMPLE-*`、`masked`、`sample`、`demo-*`。不能出现真实客户、真实手机号、真实证件号、真实账号、生产地址、内部系统地址、密钥或 token。关键词扫描可以帮我们发现疑点，但最后仍然要人工判断。

这节课收束一下。AI 在验收里很有用：它可以帮我们按需求、API、事务、安全生成审查清单，也可以帮我们写复盘报告。但是验收责任不能交给 AI。我们要把 AI 输出、自动化测试、HTTP 样例、浏览器操作、日志追踪和安全检查合在一起，最后由人给出结论。

真正的完成，不是 AI 说完成，而是证据链能证明完成。

## 12. 讲义精简版

### 12.1 本课核心

第 9 课把前面开发、测试和联调成果收束成验收方法：

```text
AI 审查 + 自动化测试 + HTTP 复验 + 浏览器人工验收 + 日志追踪 + 安全脱敏 = 可复验完成
```

核心原则：

```text
AI 验收是辅助，最终责任在人。
```

### 12.2 验收十层

| 层次 | 重点 |
| --- | --- |
| 需求验收 | 功能是否满足课程 POC 目标 |
| 设计验收 | 是否遵守单体、四张表、统一响应、安全边界 |
| API 契约验收 | 路径、字段、错误码、分页、requestId |
| 后端测试验收 | `mvn test` 覆盖规则、事务、错误码 |
| 前端构建验收 | `npm run build` 通过 |
| 浏览器联调验收 | Dashboard -> 客户列表 -> 详情 -> 重算 -> 日志 |
| 事务副作用验收 | 新增风险记录、回写快照、写日志 |
| 日志追踪验收 | requestId 可串联请求和操作日志 |
| 安全脱敏验收 | 无真实数据、密钥、生产地址 |
| 录屏可复现验收 | 命令、HTTP 文件、步骤可重复 |

### 12.3 默认命令

后端测试：

```powershell
cd D:\project\ai-study\poc\backend
mvn test
```

后端启动：

```powershell
cd D:\project\ai-study\poc\backend
mvn spring-boot:test-run -Dspring-boot.run.profiles=test
```

前端构建：

```powershell
cd D:\project\ai-study\poc\frontend
npm run build
```

前端启动：

```powershell
cd D:\project\ai-study\poc\frontend
npm run dev -- --host 127.0.0.1
```

### 12.4 必验路径

```text
/dashboard
  -> /customers
  -> /customers/CUST100002
  -> 风险重算
  -> /operation-logs
  -> /customers/CUST999999
  -> CUST100006 风险重算
```

### 12.5 安全底线

- 只使用 `CUST10000*`、`CUST999999`、`SAMPLE-*`、`masked`、`sample`、`demo-*`。
- 不展示真实客户、账号、手机号、证件号、生产地址、内部系统地址、密钥或 token。
- 错误响应不展示 SQL、堆栈、连接串。
- 风险评级规则是教学样例，不是真实风控规则。

## 13. 课后练习

练习目标：让学员用 AI 对一个已完成小功能做验收复盘，而不是继续让 AI 写新功能。

练习任务：

```text
任选一个你已经完成的小功能，使用 AI 生成一份验收复盘清单。

要求 AI 至少检查：
1. 需求是否满足。
2. API 或输入输出契约是否稳定。
3. 成功路径和失败路径是否都有样例。
4. 是否有自动化测试或手工验收步骤。
5. 日志和 requestId 是否能追踪问题。
6. 是否存在敏感信息泄露风险。
7. 哪些结论必须人工确认。

输出格式：
- 已通过项。
- P0 / P1 / P2 问题。
- 人工验收清单。
- 失败兜底方案。
```

练习验收：

- 学员能说清 AI 提出的每个问题是否有证据。
- 学员能区分 AI 建议、测试结果和人工最终结论。
- 学员不会因为 AI 建议而擅自扩大功能范围。

## 14. 主控验收清单

- [x] 本文明确第 9 课标题和一句话目标。
- [x] 本文讲清本课要解决的问题：AI 生成和联调完成后，如何证明真的完成。
- [x] 本文给出 30 到 40 分钟章节安排。
- [x] 本文覆盖录制前准备：后端 test profile + H2、前端 Vite、HTTP 样例、浏览器主流程、已有验收记录。
- [x] 本文覆盖验收分层模型：需求、设计、API、后端测试、前端构建、前后端联调、事务副作用、日志 requestId、安全脱敏、录屏可复现。
- [x] 本文覆盖录屏操作步骤：展示测试材料、HTTP 文件、`mvn test`、`npm run build`、浏览器主流程、错误场景、requestId 追踪、安全检查。
- [x] 本文提供 AI 验收提示词模板：需求验收、API 契约、事务副作用、安全脱敏、复盘报告。
- [x] 本文提供人工验收清单。
- [x] 本文提供失败兜底方案。
- [x] 本文覆盖常见误区。
- [x] 本文提供半口语口播稿。
- [x] 本文提供讲义精简版。
- [x] 本文提供课后练习。
- [x] 本文没有要求修改后端代码、前端代码、数据库脚本或新增测试代码。
- [x] 本文没有引入真实客户数据、内部系统地址、密钥或生产配置。

## 给主控窗口的简短汇报

本次新增 `docs/lesson-09-acceptance-recording-material.md`。

材料覆盖第 9 课“AI 验收、人工验收和安全复盘”的录屏结构、录制前准备、验收分层模型、录屏操作步骤、AI 验收提示词、人工验收清单、失败兜底方案、常见误区、半口语口播稿、讲义精简版、课后练习和主控验收清单。

内容已覆盖验收分层、录屏步骤、AI 提示词、人工清单和失败兜底，重点包含后端 test profile + H2、前端 Vite、HTTP 请求样例、浏览器主流程、风险重算事务三项效果、404 / 409 / 400 错误场景、requestId 追踪和安全脱敏检查。

当前材料足够支撑第 9 课 30 到 40 分钟录制。待主控确认的问题：录制时是否现场实际运行 `mvn test` 和 `npm run build`，还是展示既有主控验收记录并把命令作为复验步骤；是否需要再拆一份“一页式录屏检查表”给讲师打印使用。
