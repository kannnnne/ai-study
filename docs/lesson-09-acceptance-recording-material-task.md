# 第 9 课验收复盘材料子任务

## 子任务目标

产出第 9 课“AI 验收、人工验收和安全复盘”的可录制材料，演示一个 AI 辅助开发 POC 完成后，如何从测试、接口、页面、事务、安全、运维和需求目标几个层面做收口验收。

本子任务只整理录屏材料、提示词、口播稿、讲义和验收清单，不改后端代码、不改前端代码、不调整数据库脚本、不新增测试代码。

建议新增：

- `docs/lesson-09-acceptance-recording-material.md`

## 输入基线

必须阅读：

- `README.md`
- `docs/course-completion-roadmap.md`
- `docs/course-blueprint.md`
- `docs/lesson-plan.md`
- `docs/ai-dev-workflow.md`
- `docs/test-acceptance-material.md`
- `docs/http/customer-risk-poc.http`
- `docs/fullstack-integration-acceptance.md`
- `docs/backend-code-implementation-acceptance.md`
- `docs/frontend-code-implementation-acceptance.md`
- `docs/lesson-05-backend-recording-material.md`
- `docs/lesson-06-recording-material.md`
- `docs/poc-architecture.md`
- `docs/api-design.md`
- `poc/backend`
- `poc/frontend`

## 子窗口提示词

```text
你现在是 AI 教程项目的“第 9 课验收复盘材料子窗口”。请只负责整理第 9 课“AI 验收、人工验收和安全复盘”的录屏材料、提示词、口播稿和讲义，不改后端代码、不改前端代码、不调整数据库脚本、不新增测试代码。

课程背景：
- 这是面向赞同科技银行解决方案研发同学的 AI 编程入门教程。
- 第 5 课已经讲清后端最小服务开发，第 6 课已经讲清前后端联调闭环。
- 第 9 课要把“AI 做完之后怎么验收”讲清楚，强调不能只看页面能点、代码能跑，而要从契约、事务、日志、安全、需求和录屏可复现性做系统性收口。
- 本课定位为 P1 内部试讲版的收束课之一，帮助学员形成可复用的验收习惯。

请先阅读：
- README.md
- docs/course-completion-roadmap.md
- docs/course-blueprint.md
- docs/lesson-plan.md
- docs/ai-dev-workflow.md
- docs/test-acceptance-material.md
- docs/http/customer-risk-poc.http
- docs/fullstack-integration-acceptance.md
- docs/backend-code-implementation-acceptance.md
- docs/frontend-code-implementation-acceptance.md
- docs/lesson-05-backend-recording-material.md
- docs/lesson-06-recording-material.md
- docs/poc-architecture.md
- docs/api-design.md
- poc/backend
- poc/frontend

输出要求：

1. 新增 `docs/lesson-09-acceptance-recording-material.md`，内容至少包括：
   - 本课标题和一句话目标。
   - 本课要解决的问题：AI 生成和联调完成后，如何证明“真的完成了”。
   - 推荐时长和章节安排，建议 30 到 40 分钟。
   - 录制前准备：
     - 后端 test profile + H2。
     - 前端 Vite。
     - HTTP 请求样例。
     - 浏览器主流程。
     - 已有验收记录。
   - 验收分层模型：
     - 需求验收。
     - 设计验收。
     - API 契约验收。
     - 后端自动化测试验收。
     - 前端构建和页面验收。
     - 前后端联调验收。
     - 事务和数据副作用验收。
     - 日志和 requestId 追踪验收。
     - 安全脱敏验收。
     - 课程录屏可复现性验收。
   - 录屏操作步骤：
     - 展示 `docs/test-acceptance-material.md`。
     - 展示 `docs/http/customer-risk-poc.http`。
     - 展示或运行后端 `mvn test`。
     - 展示前端 `npm run build` 或已有构建验收记录。
     - 演示浏览器主流程：Dashboard -> 客户列表 -> 客户详情 -> 风险重算 -> 操作日志。
     - 演示错误场景：客户不存在、关闭客户重算。
     - 演示按 requestId 追踪问题。
     - 演示安全脱敏检查。
   - AI 验收提示词模板：
     - 让 AI 按需求验收。
     - 让 AI 按 API 契约验收。
     - 让 AI 按事务副作用验收。
     - 让 AI 做安全脱敏审查。
     - 让 AI 生成复盘报告。
   - 人工验收清单：
     - 命令是否可复现。
     - 页面主流程是否可复现。
     - 错误码和 requestId 是否展示。
     - 风险重算三项事务效果是否成立。
     - 日志是否不泄露敏感信息。
     - 是否没有真实客户、账号、手机号、证件号、密钥、生产地址。
   - 失败兜底方案：
     - MySQL 不可用。
     - 后端测试失败。
     - 前端构建失败。
     - 浏览器联调失败。
     - requestId 对不上。
     - 安全扫描发现疑似敏感信息。
   - 常见误区：
     - 只看页面不看接口。
     - 只跑成功路径不跑失败路径。
     - 只看返回结果不验事务副作用。
     - 只让 AI 自评不做人工验收。
     - 忘记检查脱敏和日志。
   - 半口语口播稿。
   - 讲义精简版。
   - 课后练习：让学员用 AI 对一个已完成小功能做验收复盘。
   - 主控验收清单。
2. 风格要求：
   - 面向有 Java / 前端基础但 AI 使用经验较少的银行研发同学。
   - 强调“AI 验收是辅助，最终责任在人”。
   - 强调银行研发场景里的审计、日志、错误码、requestId、脱敏、安全边界。
   - 内容要能直接支撑录屏，不要只写抽象原则。
3. 不要引入真实客户数据、内部系统地址、密钥或生产配置。

最后给主控窗口一段简短汇报，说明：
- 产出了哪些文件。
- 是否覆盖验收分层、录屏步骤、AI 提示词、人工清单和失败兜底。
- 是否足够支撑第 9 课录制。
- 是否有待主控确认的问题。
```

## 主控验收重点

- 是否承接第 5 课后端开发和第 6 课联调闭环。
- 是否讲清 AI 验收、人工验收、安全验收的分工。
- 是否覆盖后端测试、前端构建、浏览器联调、错误场景、事务副作用、日志追踪和脱敏检查。
- 是否包含可直接录制的操作步骤、口播稿、讲义、练习和失败兜底。
- 是否避免真实客户数据、密钥、生产地址和内部系统细节。
