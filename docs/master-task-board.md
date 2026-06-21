# 主控任务看板

## 当前阶段

阶段：P1 内部试讲版补齐阶段。

目标：在已有可运行 POC、第 0 课和第 6 课样片基础上，补齐测试验收、环境安装、关键课程录屏材料和试讲包，形成可小范围内部试讲的第一版课程。

主控总路线图：`docs/course-completion-roadmap.md`

当前优先级：先完成 P1 内部试讲版，不急着一次性展开 P2 正式发布版。

## 任务总览

| 序号 | 子任务 | 状态 | 产出 | 主控验收 |
| --- | --- | --- | --- | --- |
| 1 | POC 架构设计 | 已完成 | `docs/poc-architecture.md` | 已通过 |
| 2 | 数据库设计 | 已完成 | `docs/database-design.md`、`docs/db-migration-draft/*.sql` | 已通过 |
| 3 | API 设计 | 已完成 | `docs/api-design.md` | 已通过 |
| 4 | 后端最小实现设计 | 已完成 | `docs/backend-implementation-plan.md` | 已通过 |
| 5 | 前端页面设计 | 已完成 | `docs/frontend-page-design.md` | 已通过 |
| 6 | 测试验收材料 | 已完成 | `docs/test-acceptance-material.md`、`docs/http/customer-risk-poc.http` | 已通过 |
| 7 | 环境安装课材料 | 已完成 | `docs/lesson-02-setup-recording-material.md`、`docs/setup-faq.md` | 已通过 |
| 8 | 试看说明和反馈表 | 待派发 | `docs/pilot-preview-brief.md` | 待验收 |
| 14 | 第 1 课工作流材料 | 已完成 | `docs/lesson-01-workflow-recording-material.md` | 已通过 |
| 15 | 第 3 课需求分析材料 | 已完成 | `docs/lesson-03-requirement-analysis-recording-material.md` | 已通过 |
| 16 | 第 4 课设计评审材料 | 已完成 | `docs/lesson-04-design-review-recording-material.md` | 已通过 |
| 17 | 第 5 课后端开发材料 | 已完成 | `docs/lesson-05-backend-recording-material.md`、`docs/lesson-05-backend-recording-material-task.md` | 已通过 |
| 18 | 第 9 课验收复盘材料 | 已完成 | `docs/lesson-09-acceptance-recording-material.md`、`docs/lesson-09-acceptance-recording-material-task.md` | 已通过 |
| 9 | 后端代码实现 | 已完成 | `poc/backend` 可运行 Spring Boot 单体后端 | 已通过 |
| 10 | 前端代码实现 | 已完成 | `poc/frontend` 可运行 Vue 3 前端 | 已通过 |
| 11 | 前后端联调验收 | 已完成 | `docs/fullstack-integration-acceptance.md` | 已通过 |
| 12 | 第 6 课录屏材料 | 已完成 | `docs/lesson-06-recording-material.md` | 已通过 |
| 13 | 第 0 课录屏材料 | 已完成 | `docs/lesson-00-recording-material.md` | 已通过 |

## 已完成基线

- 课程蓝图：`docs/course-blueprint.md`
- 分集大纲：`docs/lesson-plan.md`
- 主控窗口工作台：`docs/master-control.md`
- POC 项目路线：`docs/poc-project-plan.md`
- POC 架构设计基线：`docs/poc-architecture.md`
- AI 研发工作流：`docs/ai-dev-workflow.md`
- 环境安装与配置：`docs/toolchain-setup.md`
- 交付物与开展方式：`docs/deliverables-and-launch-plan.md`

## 当前主控裁决

- 数据库：MySQL 8。
- ORM：MyBatis Plus。
- 数据库迁移：Flyway。
- 第一阶段认证：固定操作人或 `X-Operator`。
- 第二阶段数据库：先共享库分表，不做独立库。
- 前端：Vue 3 + TypeScript + Vite + Element Plus + ECharts。

## 下一步

数据库设计子窗口已回传并通过主控验收。后续后端子窗口可直接基于数据库设计和 Flyway 草案推进，但需要注意主控补充裁决：第一阶段风险评级优先验收等级和流程，不优先强断言所有初始化样例分数。

数据库设计子窗口：

- Thread ID：`019ee89a-d115-7a53-ba36-8029e25da25f`

API 设计子窗口已回传并通过主控验收。后续后端、前端和测试子窗口均以 `docs/api-design.md` 为接口契约基线。

API 设计子窗口：

- Thread ID：`019ee8b2-eb0e-7b91-b139-f6ece80d5b73`

下一步建议派发后端最小实现设计子窗口，先输出 Spring Boot 单体模块结构、依赖清单、开发步骤、测试计划和课程录屏步骤，再进入代码实现。

后端最小实现设计子窗口：

- Thread ID：`019ee8c5-2b26-7ad2-a308-77f7c41cd115`

后端最小实现设计子窗口已回传并通过主控验收。下一步建议进入后端代码实现阶段：创建 `backend` Maven 项目，复制 Flyway SQL，按 `docs/api-design.md` 和 `docs/backend-implementation-plan.md` 实现第一阶段单体 POC。

后端代码实现子任务提示词已准备：

- `docs/backend-code-implementation-task.md`

后端代码实现子窗口：

- Thread ID：`019ee8d6-e697-70d2-937d-5c991678a168`

后端代码实现已回传并通过主控验收：

- 代码目录：`poc/backend`
- 验收记录：`docs/backend-code-implementation-acceptance.md`
- 复验命令：`mvn test`
- 复验结果：9 tests, 0 failures, 0 errors

下一步建议派发前端页面设计子窗口，基于 `docs/api-design.md` 和 `poc/backend` 输出 Vue 3 页面结构、视觉方案、组件拆分和联调计划。

前端页面设计子任务提示词已准备：

- `docs/frontend-page-design-task.md`

前端页面设计子窗口：

- Thread ID：`019ee9b9-3c59-72d3-82b4-d786800e4b72`

前端页面设计已回传并通过主控验收：

- 设计基线：`docs/frontend-page-design.md`
- 代码实现任务提示词：`docs/frontend-code-implementation-task.md`

下一步建议派发前端代码实现子窗口，创建 `poc/frontend` 并实现第一阶段页面。

前端代码实现子窗口：

- Thread ID：`019ee9bd-6095-7211-a1a2-5e84cbbaff70`

前端代码实现已回传并通过主控验收：

- 代码目录：`poc/frontend`
- 验收记录：`docs/frontend-code-implementation-acceptance.md`
- 复验命令：`npm run build`
- 复验结果：构建通过

下一步建议派发前后端联调验收子窗口，验证浏览器完整操作闭环。

前后端联调验收子任务提示词已准备：

- `docs/fullstack-integration-acceptance-task.md`

前后端联调验收子窗口：

- Thread ID：`019ee9d8-6a95-7e63-b187-f6959890e7d2`

前后端联调验收已回传并通过主控验收：

- 验收记录：`docs/fullstack-integration-acceptance.md`
- 后端复验：`mvn test` 通过，9 tests, 0 failures, 0 errors
- 前端复验：`npm run build` 通过
- 主流程：仪表盘 -> 客户列表 -> 客户详情 -> 风险重算 -> 操作日志追踪通过

下一步建议派发第 6 课录屏材料子窗口，输出前端联调课的口播稿、录屏步骤和失败兜底方案。

第 6 课录屏材料子任务提示词已准备：

- `docs/lesson-06-recording-material-task.md`

第 6 课录屏材料子窗口：

- Thread ID：`019ee9e4-3e00-7bd3-a12e-7fc009d7c556`

第 6 课录屏材料已回传并通过主控验收：

- 录屏材料：`docs/lesson-06-recording-material.md`
- 覆盖内容：视频脚本、口播稿、启动命令、录屏路径、失败兜底、安全脱敏、课后练习、讲师检查清单

下一步建议派发第 0 课录屏材料子窗口，先补齐课程导入样片；或者派发测试验收材料子窗口，补齐 HTTP 文件和完整验收清单。

第 0 课录屏材料子任务提示词已准备：

- `docs/lesson-00-recording-material-task.md`

第 0 课录屏材料子窗口：

- Thread ID：`019eea2e-41d9-7802-8945-01084e5ed7e2`

第 0 课录屏材料已回传并通过主控验收：

- 录屏材料：`docs/lesson-00-recording-material.md`
- 覆盖内容：开场口播、完整口播、课程交付物、领导/学员视角、讲师定位、安全边界、失败兜底、讲义精简版

当前样片组合建议：

- 第 0 课：课程导入和预期管理
- 第 6 课：前端页面和接口联调

这两节可以先给领导或技术骨干试看，分别展示课程价值和实际闭环效果。

测试验收材料子任务提示词已准备：

- `docs/test-acceptance-material-task.md`

测试验收材料子窗口已回传并通过主控验收：

- `docs/test-acceptance-material.md`
- `docs/http/customer-risk-poc.http`

覆盖内容：后端测试矩阵、API 验收矩阵、前端人工验收矩阵、风险重算事务验收、安全脱敏检查、AI 验收提示词、人工验收清单、失败兜底和 HTTP 请求样例。

该材料可以支撑后续第 9 课“AI 验收、人工验收和安全运维验收”的录制，也可以作为讲师每次录屏前的复验清单。

下一步建议派发环境安装课材料子窗口，优先解决学员能否在 Windows、macOS、WSL2 上把 Claude Code + DeepSeek 跑起来的问题。

环境安装课材料子任务提示词已准备：

- `docs/lesson-02-setup-recording-material-task.md`

环境安装课材料已通过主控验收：

- `docs/lesson-02-setup-recording-material.md`
- `docs/setup-faq.md`

覆盖内容：macOS、Windows PowerShell、Windows WSL2、DeepSeek 配置、Claude Code 配置、无代理 / 内网限制、安装包分发、API 访问排障、安全提醒和失败兜底。

下一步建议派发第 1 课 AI 编程基础概念和工作流材料子窗口，补齐学员在安装前必须先理解的工作方式、会话拆分和验收边界。

第 1 课工作流材料子任务提示词已准备：

- `docs/lesson-01-workflow-recording-material-task.md`

第 1 课工作流材料已通过主控验收：

- `docs/lesson-01-workflow-recording-material.md`

覆盖内容：AI / LLM / 提示词 / 上下文 / Agent / 工具调用、完整 AI 辅助研发工作流、主控会话和子会话分工、好提示词结构、常见错误、口播稿、讲义和课后练习。

下一步建议派发第 3 课需求分析材料子窗口，演示如何从一句粗需求开始，让 AI 帮助澄清边界、流程和验收标准。

第 3 课需求分析材料子任务提示词已准备：

- `docs/lesson-03-requirement-analysis-recording-material-task.md`

第 3 课需求分析材料已通过主控验收：

- `docs/lesson-03-requirement-analysis-recording-material.md`

覆盖内容：粗需求输入、第一轮需求分析提示词、第二轮边界收敛提示词、第三轮分层验收标准提示词、录屏步骤、讲解重点、常见错误、半口语口播稿、讲义和课后练习。

下一步建议派发第 4 课设计评审材料子窗口，把已收敛需求转换为架构、数据库、API 和评审清单。

第 4 课设计评审材料子任务提示词已准备：

- `docs/lesson-04-design-review-recording-material-task.md`

第 4 课设计评审材料已通过主控验收：

- `docs/lesson-04-design-review-recording-material.md`

覆盖内容：系统设计提示词、设计评审提示词、主控裁决提示词、录屏步骤、第一阶段单体与第二阶段微服务边界、常见设计错误、口播稿、讲义和课后练习。

下一步建议派发第 5 课后端开发材料子窗口，把设计基线转成 Spring Boot 后端开发录屏脚本。

第 5 课后端开发材料子任务提示词已准备：

- `docs/lesson-05-backend-recording-material-task.md`

第 5 课后端开发材料子窗口派发目标：

- `docs/lesson-05-backend-recording-material.md`

第 5 课后端开发材料已回传并通过主控验收：

- 录屏材料：`docs/lesson-05-backend-recording-material.md`
- 子任务提示词：`docs/lesson-05-backend-recording-material-task.md`
- 覆盖内容：后端项目骨架、Flyway、分层实现、统一响应、错误码、`X-Request-Id` / `X-Operator`、风险重算事务、自动化测试、AI 提示词、常见错误、口播稿、讲义、练习和主控验收清单
- 主控验收：确认能支撑第 5 课 35 到 45 分钟录制；敏感信息扫描只命中安全提醒文本，没有发现真实客户数据、密钥或生产地址

下一步建议派发第 9 课验收复盘材料子窗口，把测试验收材料、联调闭环和安全检查整理成可录制课程。

第 9 课验收复盘材料子任务提示词已准备：

- `docs/lesson-09-acceptance-recording-material-task.md`

第 9 课验收复盘材料子窗口派发目标：

- `docs/lesson-09-acceptance-recording-material.md`

第 9 课验收复盘材料已回传并通过主控验收：

- 录屏材料：`docs/lesson-09-acceptance-recording-material.md`
- 子任务提示词：`docs/lesson-09-acceptance-recording-material-task.md`
- 覆盖内容：验收分层模型、录屏操作步骤、后端 `mvn test`、前端 `npm run build`、HTTP 复验、浏览器主流程、风险重算事务副作用、错误场景、requestId 追踪、安全脱敏检查、AI 验收提示词、人工验收清单、失败兜底、口播稿、讲义和课后练习
- 主控验收：确认能支撑第 9 课 30 到 40 分钟录制；敏感信息扫描命中均为安全提醒和检查命令，没有发现真实客户数据、密钥或生产地址

下一步建议派发试讲包和反馈表子窗口，整理 P1 内部试讲组织方案、试看材料组合、反馈表和讲师录制前检查清单。
