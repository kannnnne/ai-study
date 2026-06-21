# 第 6 课录屏材料：前端页面和接口联调

## 主控验收结论

本录屏材料通过主控窗口验收，可以用于录制第 6 课。

通过理由：

- 覆盖视频目标、课程位置、时长章节、启动命令和录制前准备。
- 包含完整录屏路径：仪表盘 -> 客户查询 -> 详情聚合 -> 风险重算 -> 日志追踪 -> 错误处理。
- 包含半口语口播稿，适合真实技术培训场景。
- 明确每个演示点中 AI 做了什么、人要验收什么、真实项目中的意义。
- 包含失败兜底方案、安全脱敏提醒、课后练习和讲师检查清单。
- 未要求修改代码，基于已通过主控验收的前后端联调结果整理。

## 1. 标题与目标

标题：第 6 课：前端页面和接口联调

一句话目标：带学员跑通“仪表盘 -> 客户查询 -> 详情聚合 -> 风险重算 -> 日志追踪 -> 错误处理”的前后端研发闭环。

## 2. 课程位置

本节适合放在后端、数据库测试数据、API 契约完成之后，微服务拆分之前。

现有 `docs/lesson-plan.md` 中类似内容列为“第 7 课：前端页面设计和联调”，但本次录制口径按当前课程推进称为“第 6 课”。讲课时建议强调：这节不是单纯“点页面”，而是验证前端是否严格消费 API 契约，并把查询、事务、日志、错误、安全连成闭环。

## 3. 时长与章节

建议 24 到 28 分钟。

| 章节 | 内容 | 建议时长 |
| --- | --- | --- |
| 1 | 课程定位和联调原则 | 3 分钟 |
| 2 | 启动后端 test profile 与前端 Vite | 4 分钟 |
| 3 | `/dashboard` 仪表盘讲解 | 4 分钟 |
| 4 | `/customers` 筛选与列表契约 | 4 分钟 |
| 5 | `/customers/CUST100002` 详情多接口聚合 | 5 分钟 |
| 6 | 风险重算、`transactionEffect` 和日志追踪 | 6 分钟 |
| 7 | 错误场景、安全脱敏和真实项目提醒 | 4 分钟 |

## 4. 录制前准备

- 关闭无关窗口、聊天工具、真实生产系统页面。
- 浏览器准备两个标签：`http://127.0.0.1:5173/dashboard`、`http://localhost:8080/actuator/health`。
- 终端准备两个窗口：后端 `poc/backend`，前端 `poc/frontend`。
- 确认 8080 和 5173 未被占用。
- 说明本次使用 test profile，是为了课程演示方便；真实项目应准备稳定数据库、固定测试数据和可重复环境。
- 准备讲解关键词：API 契约、统一响应、`X-Request-Id`、`X-Operator`、`masked/sample`、`transactionEffect`、操作日志。

## 5. 启动命令清单

后端 test profile：

```powershell
cd D:\project\ai-study\poc\backend
mvn spring-boot:test-run -Dspring-boot.run.profiles=test
```

前端 Vite：

```powershell
cd D:\project\ai-study\poc\frontend
npm run dev -- --host 127.0.0.1
```

构建或测试：

```powershell
cd D:\project\ai-study\poc\backend
mvn test

cd D:\project\ai-study\poc\frontend
npm run build
```

补充检查：

```powershell
Invoke-WebRequest http://localhost:8080/actuator/health
```

## 6. 完整录屏操作步骤

### 6.1 打开 `/dashboard`

讲风险分布、客户状态统计、总客户 6、高风险 2、冻结 / 关闭 2、最近日志 5。

说明仪表盘当前是 POC 级前端轻量聚合，数据来自 `GET /api/customers?pageNo=1&pageSize=100`，不是生产统计方案。

AI 做了什么：帮我们生成页面、图表、API client 和聚合展示。

人验收什么：数字是否来自契约字段、图表是否误导、是否新增了不该有的统计接口。

真实意义：前端可以做展示聚合，但生产统计必须有明确口径和后端能力。

### 6.2 进入 `/customers`

演示 `riskLevel = HIGH`，看到 `Total 2`；再演示 `status = ACTIVE`，看到 `Total 4`。

AI 做了什么：根据 API 契约生成筛选表单、表格、分页、风险 / 状态标签。

人验收什么：筛选参数是否严格对应 `GET /api/customers`，没有绕过契约或前端写死。

真实意义：联调要验证“用户操作 -> 请求参数 -> 后端结果 -> 页面状态”整条链。

### 6.3 打开 `/customers/CUST100002`

讲客户详情多接口聚合：客户基础信息、账户列表、当前风险、历史风险记录分别来自多个接口。

AI 做了什么：把详情页拆成多个 API 调用和多个展示区域。

人验收什么：加载、错误、空数据是否可解释；字段是否保留 `mobileMasked`、`accountNoMasked`、`riskReasonSample`。

真实意义：真实系统的详情页往往不是一个接口解决，联调要看并发加载、部分失败和契约稳定性。

### 6.4 触发风险重算

点击“风险重算”，确认 `reasonSample`、`ruleCode = DEMO_RULE_V1`，点击“触发重算”。

展示 `transactionEffect`：

- `riskRecordInserted=true`
- `customerRiskSnapshotUpdated=true`
- `operationLogInserted=true`

AI 做了什么：生成弹窗、请求体、成功结果展示和静默刷新。

人验收什么：不是只看“成功 toast”，而是看事务三件事是否都返回并能追踪。

真实意义：风险重算是写操作，联调重点是事务效果、审计日志和重复提交控制。

### 6.5 进入 `/operation-logs`

筛选 `operationType = RECALCULATE_RISK`，展示成功和失败日志。

AI 做了什么：生成日志查询页、筛选项和结果表格。

人验收什么：`requestId`、`operator`、`targetBizNo`、`result`、`errorMessageSample` 是否能追踪刚才操作。

真实意义：联调闭环必须能解释“页面发生了什么”，日志不是上线后才补的。

### 6.6 错误场景一：`/customers/CUST999999`

展示 `客户不存在`、`code: 404001`、`requestId`。

AI 做了什么：统一错误展示组件。

人验收什么：错误不泄露 SQL、堆栈、连接串，且能给测试人员稳定断言。

真实意义：失败路径是一等公民，不能只录成功路径。

### 6.7 错误场景二：`CUST100006`

打开或筛选 `CUST100006`，触发风险重算。

展示关闭状态客户不允许重算，`code: 409001`，并回到日志页查失败日志。

AI 做了什么：把业务状态冲突接入前端错误展示。

人验收什么：`409001` 是否符合 API 设计，失败日志是否脱敏记录。

真实意义：银行系统里状态控制很常见，前端不能靠猜，要消费后端明确业务码。

## 7. 半口语口播稿

这一节我们不再只看后端接口，也不只看前端页面。我们要把它们连起来，看一个最小研发闭环是不是真的跑通。

先说一个原则：前端不改 API，只消费契约。我们前面已经有 `docs/api-design.md`，里面定义了 `/api/customers`、风险重算、操作日志这些接口。前端要做的是按契约请求、按统一响应展示、按错误码处理，不是在页面里临时补业务规则。

这里我用 test profile 启动后端。它用 H2 内存库和 Flyway 样例数据，适合课程录制，大家不用先准备 MySQL。但真实项目不能这么随意，应该有稳定数据库环境、固定测试数据和可重复的初始化流程。

现在打开仪表盘。这里的风险分布和客户状态统计，是前端基于样例客户分页数据做的轻量聚合。这个在 POC 里可以接受，但生产里要非常谨慎，统计口径通常应该由后端或数仓能力明确提供。

接着看客户列表。我们筛 HIGH，再筛 ACTIVE。这里不是为了证明下拉框能点，而是验证前端传参、后端筛选、分页总数、表格展示这几件事是一致的。

打开 `CUST100002`。详情页背后不是一个接口，它聚合了客户基础信息、账户列表、当前风险、历史风险记录。AI 帮我们把页面和接口调用串起来，但我们要验收字段有没有漂移，错误有没有处理，脱敏字段有没有被保留。

现在触发风险重算。这个点最重要。我们不只看“重算成功”，还要看 `transactionEffect`。它告诉我们风险记录插入、客户风险快照回写、操作日志写入都发生了。也就是说，页面上的一次点击，后端事务和审计链路都能解释。

最后去操作日志，按 `RECALCULATE_RISK` 筛选。这里能看到成功和失败日志。真实项目里，研发、测试、运维排问题都离不开这个链路。没有日志追踪，联调就只是“看起来能点”。

再看两个错误：不存在客户 `CUST999999` 返回 `404001`；关闭客户 `CUST100006` 重算返回 `409001`。这些错误页面要能展示，但不能暴露 SQL、堆栈、真实账号、真实手机号。AI 能帮我们把错误组件做出来，但安全边界必须人来验。

所以这节课的核心不是“AI 做了一个前端”，而是：AI 帮我们加速搭出页面和调用链，人负责验收契约、事务、日志、错误和脱敏。联调不是点点页面，联调是在验证研发闭环。

## 8. 失败兜底方案

后端 8080 起不来：

- 先查端口占用。
- 换端口不建议录屏临时做，优先停止占用进程。
- 确认命令在 `poc/backend` 下执行。
- 使用 test profile，不要误连 MySQL。

前端 5173 起不来：

- 确认在 `poc/frontend` 下执行。
- 如端口占用，可临时让 Vite 自动换端口，但录屏口播要说明实际地址。

API 代理失败：

- 检查 `vite.config.ts` 中 `/api -> http://localhost:8080`。
- 浏览器 Network 看请求是否打到 5173 后被代理。
- 后端健康检查先通过。

风险重算失败：

- 先确认客户不是 `CUST100006` 这种关闭状态。
- 如果是 `409001`，它本身就是演示场景。
- 如果是 500，切到日志和终端看后端错误。

页面空数据：

- 确认后端是 test profile，Flyway 样例数据已加载。
- 刷新 `/dashboard` 和 `/customers`。
- 不要把空数据解释成真实客户为空。

MySQL 没有准备：

- 课程录制用 test profile。
- 真实项目应准备 MySQL、库 `ai_study_risk_poc`、账号权限、Flyway 初始化和可重复样例数据。

## 9. 安全和脱敏提醒

- 只展示 `CUST10000*`、`SAMPLE-*`、`demo-admin`、`demo-query`。
- 不展示真实手机号、证件号、账号、客户姓名、生产地址、密钥。
- 字段名保留 `mobileMasked`、`idNoMasked`、`accountNoMasked`、`clientIpMasked`、`*Sample`。
- 错误页面只展示 `message`、`code`、`requestId`，不展示 SQL、堆栈、连接串。
- `X-Operator` 只是教学模拟，不代表真实认证授权。

## 10. 课后练习

1. 用浏览器 Network 记录一次 `HIGH` 筛选请求，写出请求参数和响应字段。
2. 找到风险重算请求，说明 `reasonSample`、`ruleCode` 和 `transactionEffect` 的含义。
3. 手动访问 `CUST999999`，记录错误码、HTTP 状态码、`requestId`。
4. 在操作日志中筛选 `RECALCULATE_RISK`，区分成功日志和失败日志。
5. 思考：如果第二阶段拆成 gateway、customer-service、risk-service，为什么前端仍应保持 `/api/...` 不变。

## 11. 讲师检查清单

- [ ] 后端 test profile 可启动，健康检查通过。
- [ ] 前端 Vite 可启动，`/dashboard` 可访问。
- [ ] `npm run build` 通过；`mvn test` 通过。
- [ ] `/dashboard` 数据符合验收记录。
- [ ] `/customers` 的 HIGH、ACTIVE 筛选结果符合预期。
- [ ] `/customers/CUST100002` 能展示详情、账户、当前风险、历史风险。
- [ ] 风险重算能展示三项 `transactionEffect=true`。
- [ ] `/operation-logs` 能筛 `RECALCULATE_RISK`。
- [ ] `CUST999999`、`CUST100006` 错误场景可复现。
- [ ] 录屏中没有真实敏感信息和无关内部系统。

## 12. 讲义精简版

本节课演示前后端联调闭环：前端不改 API，只消费 `docs/api-design.md` 中的 `/api/...` 契约。录制时使用后端 test profile，借助 H2 内存库和 Flyway 样例数据快速启动；真实项目应准备稳定 MySQL 环境。

演示路径：`/dashboard` 看风险分布和客户状态统计；`/customers` 演示 HIGH、ACTIVE 筛选；`/customers/CUST100002` 查看详情多接口聚合；触发风险重算并检查 `transactionEffect`；到 `/operation-logs` 按 `RECALCULATE_RISK` 追踪日志；最后演示 `CUST999999` 和 `CUST100006` 的统一错误响应。

本节重点：AI 可以帮助生成页面、接口调用、错误展示和日志查询，但人必须验收 API 契约、事务效果、错误码、日志追踪和脱敏安全。联调不是点页面，而是验证从用户操作到后端事务再到审计日志的研发闭环。

