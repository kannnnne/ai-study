# 第 5 课后端开发材料子任务

## 子任务目标

产出第 5 课“后端最小服务开发”的可录制材料，演示如何基于前面需求和设计基线，让 AI 辅助实现 Spring Boot 3.x 单体后端、Flyway 样例数据、REST API、统一响应、异常处理、风险重算事务和自动化测试。

本子任务只整理录屏材料、提示词、口播稿和讲义，不改 POC 后端代码、不改前端代码、不调整数据库脚本。

建议新增：

- `docs/lesson-05-backend-recording-material.md`

## 输入基线

必须阅读：

- `README.md`
- `docs/course-completion-roadmap.md`
- `docs/course-blueprint.md`
- `docs/lesson-plan.md`
- `docs/ai-dev-workflow.md`
- `docs/lesson-04-design-review-recording-material.md`
- `docs/poc-architecture.md`
- `docs/database-design.md`
- `docs/api-design.md`
- `docs/backend-implementation-plan.md`
- `docs/backend-code-implementation-acceptance.md`
- `docs/test-acceptance-material.md`
- `poc/backend`

## 子窗口提示词

```text
你现在是 AI 教程项目的“第 5 课后端开发材料子窗口”。请只负责整理第 5 课“后端最小服务开发”的录屏材料、提示词、口播稿和讲义，不改后端代码、不改前端代码、不调整数据库脚本。

课程背景：
- 这是面向赞同科技银行解决方案研发同学的 AI 编程入门教程。
- 第 4 课已经讲清设计评审：第一阶段单体 Spring Boot POC，第二阶段再微服务演进。
- 当前仓库已经有可运行后端 `poc/backend`，本课要把“如何用 AI 逐步实现后端”的过程整理成可录制材料。
- 本课重点不是现场从空目录写完所有代码，而是讲清任务拆分、AI 提示词、关键实现点、测试和验收。

请先阅读：
- README.md
- docs/course-completion-roadmap.md
- docs/course-blueprint.md
- docs/lesson-plan.md
- docs/ai-dev-workflow.md
- docs/lesson-04-design-review-recording-material.md
- docs/poc-architecture.md
- docs/database-design.md
- docs/api-design.md
- docs/backend-implementation-plan.md
- docs/backend-code-implementation-acceptance.md
- docs/test-acceptance-material.md
- poc/backend

输出要求：

1. 新增 `docs/lesson-05-backend-recording-material.md`，内容至少包括：
   - 本课标题和一句话目标。
   - 本课要解决的问题：如何让 AI 辅助后端实现，但仍由人控制边界、测试和验收。
   - 推荐时长和章节安排，建议 35 到 45 分钟。
   - 录制前准备：
     - 后端项目位置。
     - JDK 21、Spring Boot 3.x、Maven。
     - 默认演示使用 test profile + H2。
     - MySQL 作为可选本地验证。
   - 后端实现拆分讲解：
     - 项目骨架。
     - Flyway 脚本。
     - entity / mapper / service / controller / dto / assembler 分层。
     - 统一响应 `ApiResponse<T>` 和分页响应。
     - `X-Request-Id` / `X-Operator`。
     - 全局异常处理和错误码。
     - 客户、账户、风险、操作日志模块。
     - 风险重算事务：新增 risk_record、回写 customer 快照、写 operation_log。
   - 后端开发提示词模板：
     - 创建项目骨架提示词。
     - 实现客户查询提示词。
     - 实现风险重算提示词。
     - 补测试提示词。
     - AI 审查提示词。
   - 录屏操作步骤：
     - 展示设计基线。
     - 展示 `poc/backend` 结构。
     - 展示一个 Controller / Service / Mapper。
     - 展示 Flyway 样例数据。
     - 展示风险重算核心代码。
     - 运行或展示 `mvn test`。
     - 说明 H2 test profile 和 MySQL profile 区别。
   - 讲解重点：
     - 先按 API 契约开发，不让 AI 自己改接口。
     - 写业务代码时同步写测试。
     - 事务效果必须能被验收。
     - 日志和错误信息不能泄露敏感内容。
   - 常见错误：
     - AI 自己新增接口或改字段。
     - 忘记统一响应和错误码。
     - 风险重算只返回结果但不写日志。
     - 只跑成功路径，不跑 404 / 409 / 参数校验。
     - 把真实数据库、真实客户数据或生产配置放进课程。
   - 半口语口播稿。
   - 讲义精简版。
   - 课后练习：让学员用 AI 给一个已有 service 补测试或做代码审查。
   - 主控验收清单。

2. 风格要求：
   - 贴近真实 Java 后端开发。
   - 强调 AI 可以辅助生成代码，但不能替代 API 契约、事务设计、测试和人工验收。
   - 面向银行研发场景，必须强调安全、脱敏、日志、错误码、requestId、操作追踪。
   - 语言通俗，适合有 Spring Boot 经验但 AI 使用较少的同学。

3. 不要引入真实客户数据、内部系统地址、密钥或生产配置。

最后给主控窗口一段简短汇报，说明：
- 产出了哪些文件。
- 是否覆盖后端结构、关键实现、提示词、测试和验收。
- 是否足够支撑第 5 课录制。
```

## 主控验收重点

- 是否承接第 4 课设计评审结果。
- 是否覆盖 Spring Boot 后端核心结构和模块。
- 是否讲清 API 契约、统一响应、错误码、requestId、operator、日志和事务。
- 是否突出风险重算三项事务效果。
- 是否覆盖自动化测试和 `mvn test`。
- 是否强调 AI 生成代码必须被测试、审查和人工验收接住。
