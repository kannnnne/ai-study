# 主控任务看板

## 当前阶段

阶段：课程设计与 POC 设计基线阶段。

目标：先把课程主线、POC 架构、数据库、API、前后端边界、测试验收和录制材料设计清楚，再进入代码实现。

## 任务总览

| 序号 | 子任务 | 状态 | 产出 | 主控验收 |
| --- | --- | --- | --- | --- |
| 1 | POC 架构设计 | 已完成 | `docs/poc-architecture.md` | 已通过 |
| 2 | 数据库设计 | 已完成 | `docs/database-design.md`、`docs/db-migration-draft/*.sql` | 已通过 |
| 3 | API 设计 | 已完成 | `docs/api-design.md` | 已通过 |
| 4 | 后端最小实现设计 | 已完成 | `docs/backend-implementation-plan.md` | 已通过 |
| 5 | 前端页面设计 | 已完成 | `docs/frontend-page-design.md` | 已通过 |
| 6 | 测试验收设计 | 待派发 | 测试矩阵、AI 验收清单、人工验收清单 | 待验收 |
| 7 | 环境安装样片材料 | 待派发 | Windows、macOS、WSL2 安装讲义和录屏脚本 | 待验收 |
| 8 | 课程第 0 课讲稿 | 待派发 | 导入课口播稿、课件大纲、录屏提示 | 待验收 |
| 9 | 后端代码实现 | 已完成 | `poc/backend` 可运行 Spring Boot 单体后端 | 已通过 |
| 10 | 前端代码实现 | 已完成 | `poc/frontend` 可运行 Vue 3 前端 | 已通过 |
| 11 | 前后端联调验收 | 已完成 | `docs/fullstack-integration-acceptance.md` | 已通过 |
| 12 | 第 6 课录屏材料 | 已完成 | `docs/lesson-06-recording-material.md` | 已通过 |

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
