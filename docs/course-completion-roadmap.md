# AI 教程完成路线图

## 1. 主控目标

本项目最终目标不是单纯产出几节视频，而是形成一套可在团队内部推广的 AI 辅助研发入门课程包。

交付目标分三层：

| 层级 | 目标 | 用途 | 当前判断 |
| --- | --- | --- | --- |
| P0 | 领导/骨干试看版 | 先证明课程价值和演示效果 | 基本具备 |
| P1 | 内部试讲版 | 给小范围同学完整试讲并收集反馈 | 还差 1 到 2 轮 |
| P2 | 正式发布版 | 面向更大范围推广，有完整视频、文档、练习和 FAQ | 还差 12 到 18 轮 |

当前主控优先级：先完成 P1 内部试讲版，不急着一次性做完 P2 正式发布版。

## 2. 当前已完成资产

| 类型 | 已完成内容 | 位置 |
| --- | --- | --- |
| 课程基线 | 课程蓝图、分集大纲、交付物规划、领导汇报口径 | `docs/course-blueprint.md`、`docs/lesson-plan.md`、`docs/deliverables-and-launch-plan.md`、`docs/leadership-brief.md` |
| POC 设计 | 架构、数据库、API、前端页面设计、后端实现设计 | `docs/poc-architecture.md`、`docs/database-design.md`、`docs/api-design.md`、`docs/frontend-page-design.md`、`docs/backend-implementation-plan.md` |
| POC 代码 | Spring Boot 后端、Vue 前端、Flyway 样例数据 | `poc/backend`、`poc/frontend` |
| 联调验收 | 前后端联调闭环验收记录 | `docs/fullstack-integration-acceptance.md` |
| 录屏材料 | 第 0、1、2、3、4、5、6、9 课录屏材料 | `docs/lesson-00-recording-material.md`、`docs/lesson-01-workflow-recording-material.md`、`docs/lesson-02-setup-recording-material.md`、`docs/lesson-03-requirement-analysis-recording-material.md`、`docs/lesson-04-design-review-recording-material.md`、`docs/lesson-05-backend-recording-material.md`、`docs/lesson-06-recording-material.md`、`docs/lesson-09-acceptance-recording-material.md` |

## 3. P0 试看版

目标：拿给领导或技术骨干试看，验证方向是否值得继续投入。

| 任务 | 状态 | 产出 |
| --- | --- | --- |
| 第 0 课导入材料 | 已完成 | `docs/lesson-00-recording-material.md` |
| 第 6 课联调材料 | 已完成 | `docs/lesson-06-recording-material.md` |
| 可运行 POC | 已完成 | `poc/backend`、`poc/frontend` |
| 试看说明和反馈表 | 待补充 | 建议新增 `docs/pilot-preview-brief.md` |

P0 还差 1 轮以内。若时间紧，可以直接用第 0 课 + 第 6 课试看。

## 4. P1 内部试讲版任务主线

P1 目标：形成 6 到 8 节能连起来试讲的课程材料，让小范围同学能够看懂、跟做、反馈问题。

建议主线如下：

| 顺序 | 子任务 | 状态 | 目标产出 | 验收口径 |
| --- | --- | --- | --- | --- |
| 1 | 测试验收材料 | 已完成 | `docs/test-acceptance-material.md`、`docs/http/customer-risk-poc.http` | 能支撑第 9 课和录屏前复验 |
| 2 | 环境安装课材料 | 已完成 | `docs/lesson-02-setup-recording-material.md`、`docs/setup-faq.md` | Windows、macOS、WSL2 路径清楚，网络限制有兜底 |
| 3 | 第 1 课工作流材料 | 已完成 | `docs/lesson-01-workflow-recording-material.md` | 讲清 AI、提示词、上下文、Agent、会话拆分 |
| 4 | 第 3 课需求分析材料 | 已完成 | `docs/lesson-03-requirement-analysis-recording-material.md` | 能从粗需求推到边界、流程和验收标准 |
| 5 | 第 4 课设计评审材料 | 已完成 | `docs/lesson-04-design-review-recording-material.md` | 能讲清架构、数据库、API、设计评审 |
| 6 | 第 5 课后端开发材料 | 已完成 | `docs/lesson-05-backend-recording-material.md` | 能讲清后端实现、测试、异常、日志 |
| 7 | 第 9 课验收复盘材料 | 已完成 | `docs/lesson-09-acceptance-recording-material.md` | 能讲清 AI 验收、人工验收、安全验收 |
| 8 | 试讲包和反馈表 | 待派发 | `docs/internal-pilot-plan.md`、反馈表模板 | 能组织 3 到 5 人小范围试讲 |

P1 预计还需要 1 到 2 轮。每一轮只处理一个子任务，避免上下文混乱。

## 5. P2 正式发布版任务主线

P2 目标：从试讲材料升级为正式内部课程包。

| 模块 | 任务 | 预计轮次 | 是否现在做 |
| --- | --- | --- | --- |
| 微服务演进 | Gateway、Nacos、customer-service、risk-service 设计和代码 | 3 到 5 轮 | 暂缓 |
| 全套讲义 | 每节课精简讲义、课后练习、提示词模板 | 3 到 5 轮 | P1 后做 |
| 环境手册 | 安装包、内网、代理、FAQ、错误截图 | 1 到 2 轮 | P1 中先做关键版 |
| 推广材料 | 领导汇报 PPT、试看反馈总结、推广计划 | 1 到 2 轮 | P0/P1 后做 |
| 总验收 | 全仓库文档整理、README、发布清单、敏感信息复查 | 1 到 2 轮 | 发布前做 |

P2 预计还需要 12 到 18 轮，但不建议现在全部展开。

## 6. 每轮任务状态流转

每个子任务统一按以下状态推进：

```text
待派发 -> 已派发 -> 子窗口回传 -> 主控验收 -> 修订/通过 -> 提交推送 -> 派发下一轮
```

主控窗口职责：

- 维护总目标和任务顺序。
- 派发子窗口提示词。
- 验收子窗口产物。
- 判断是否需要返工。
- 验收通过后提交并推送 GitHub。
- 决定下一轮任务。

子窗口职责：

- 只处理一个明确子任务。
- 尽量不碰无关文件。
- 最后给主控窗口简短汇报。

## 7. 每轮验收标准

每轮回传后，主控至少检查：

| 检查项 | 说明 |
| --- | --- |
| 范围 | 是否只完成本轮任务，没有扩散 |
| 交付物 | 是否生成了指定文件 |
| 可录制性 | 是否能直接用于视频录制或讲义 |
| 一致性 | 是否与课程主线、POC 设计、API 契约一致 |
| 安全性 | 是否没有真实客户数据、密钥、生产地址 |
| 可执行性 | 命令、步骤、验收清单是否能照着做 |
| 仓库状态 | 验收通过后再提交并推送 |

## 8. 上下文控制原则

为避免轮次太多导致上下文丢失，后续只依赖少量主控文档：

| 用途 | 主要文档 |
| --- | --- |
| 看总目标 | `docs/course-completion-roadmap.md` |
| 看当前状态 | `docs/master-task-board.md` |
| 看课程结构 | `docs/lesson-plan.md` |
| 看交付物 | `docs/deliverables-and-launch-plan.md` |
| 看 POC 边界 | `docs/poc-architecture.md` |
| 看 AI 工作流 | `docs/ai-dev-workflow.md` |

每次开新子窗口时，不要把所有历史都塞进去，只给：

1. 本轮目标。
2. 必读文档清单。
3. 明确输出文件。
4. 不做什么。
5. 主控验收标准。

## 9. Token 节省策略

后续高效推进时遵循：

- 主控窗口只做目标、拆分、验收、状态记录，不在主控里展开长篇内容。
- 子窗口只处理单一主题，不跨主题聊天。
- 子窗口回传使用“文件路径 + 覆盖点 + 验证结果 + 待确认问题”的短格式。
- 主控验收优先看差异文件和验收清单，不重复阅读全文。
- 已完成的基线文档不反复复制，统一用文件路径引用。
- 每完成一轮就更新 `docs/master-task-board.md`，必要时更新本路线图。
- 验收通过才提交 GitHub，派发阶段可以先不提交。

## 10. 下一轮建议

当前已完成：第 9 课验收复盘材料。

当前下一轮：试讲包和反馈表子窗口。

第 9 课验收复盘材料已通过主控验收后：

1. 更新 `docs/master-task-board.md` 和本路线图。
2. 提交并推送 GitHub。
3. 下一轮优先派发“试讲包和反馈表子窗口”。

原因：第 9 课负责把前面开发、测试、联调和安全检查收束成验收方法，完成后就可以组织小范围内部试讲。
