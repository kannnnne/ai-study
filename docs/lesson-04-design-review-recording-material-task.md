# 第 4 课设计评审材料子任务

## 子任务目标

产出第 4 课“用 AI 做系统设计和评审”的可录制材料，演示如何把第 3 课收敛后的 POC 需求转换为架构、数据库、API、安全和测试设计，并让 AI 做设计自查与主控验收。

本子任务只整理录屏材料、提示词、口播稿和讲义，不改 POC 后端代码、不改前端代码、不调整数据库脚本。

建议新增：

- `docs/lesson-04-design-review-recording-material.md`

## 输入基线

必须阅读：

- `README.md`
- `docs/course-completion-roadmap.md`
- `docs/course-blueprint.md`
- `docs/lesson-plan.md`
- `docs/ai-dev-workflow.md`
- `docs/lesson-03-requirement-analysis-recording-material.md`
- `docs/poc-architecture.md`
- `docs/database-design.md`
- `docs/api-design.md`
- `docs/frontend-page-design.md`
- `docs/backend-implementation-plan.md`
- `docs/test-acceptance-material.md`

## 子窗口提示词

```text
你现在是 AI 教程项目的“第 4 课设计评审材料子窗口”。请只负责整理第 4 课“用 AI 做系统设计和评审”的录屏材料、提示词、口播稿和讲义，不改后端代码、不改前端代码、不调整数据库脚本。

课程背景：
- 这是面向赞同科技银行解决方案研发同学的 AI 编程入门教程。
- 第 3 课已经演示如何从粗需求收敛出 POC 边界和验收标准。
- 第 4 课要演示如何把需求转换为可开发设计，并让 AI 帮忙做设计评审。
- 设计重点是：第一阶段单体 POC 先跑通，第二阶段再演进微服务；不要一开始设计过重。

请先阅读：
- README.md
- docs/course-completion-roadmap.md
- docs/course-blueprint.md
- docs/lesson-plan.md
- docs/ai-dev-workflow.md
- docs/lesson-03-requirement-analysis-recording-material.md
- docs/poc-architecture.md
- docs/database-design.md
- docs/api-design.md
- docs/frontend-page-design.md
- docs/backend-implementation-plan.md
- docs/test-acceptance-material.md

输出要求：

1. 新增 `docs/lesson-04-design-review-recording-material.md`，内容至少包括：
   - 本课标题和一句话目标。
   - 本课要解决的问题：需求之后为什么要先设计和评审，而不是直接编码。
   - 推荐时长和章节安排，建议 30 到 40 分钟。
   - 设计输入材料清单：第 3 课需求分析结果、POC 架构、数据库设计、API 设计、前端页面设计、后端实现设计、测试验收材料。
   - 第一轮系统设计提示词：
     - 要求 AI 基于已收敛需求设计第一阶段单体 POC。
     - 输出模块划分、数据流、数据库表、API、前端页面、异常和日志、安全脱敏、测试策略。
     - 明确第一阶段不引入真实登录、权限、MQ、缓存、分布式事务、真实银行系统。
   - 第二轮设计评审提示词：
     - 要求 AI 以 P0/P1/P2 输出设计风险。
     - 检查是否过度设计、是否遗漏验收、是否违反安全边界、是否和 API/数据库设计冲突。
   - 第三轮主控裁决提示词：
     - 让 AI 把设计收敛成第一阶段必须做、第二阶段再做、明确不做三类。
   - 录屏操作步骤：
     - 展示第 3 课需求边界。
     - 展示 `docs/poc-architecture.md`。
     - 展示 `docs/database-design.md` 和 `docs/api-design.md`。
     - 新开设计评审子会话。
     - 输入系统设计提示词。
     - 人工点评是否过度设计。
     - 输入设计评审提示词。
     - 沉淀主控裁决。
   - 讲解重点：
     - 设计不是越复杂越好。
     - 第一阶段单体优先，微服务后置。
     - 设计必须可验收。
     - 银行场景要提前设计日志、脱敏、错误码和操作追踪。
   - 常见错误：
     - 一开始就拆微服务。
     - 设计没有验收标准。
     - 数据库、API、前端字段不一致。
     - 忽略错误码、日志和 requestId。
     - 把真实登录、权限和生产系统接入放进入门 POC。
   - 半口语口播稿。
   - 讲义精简版。
   - 课后练习：让学员把第 3 课自己的小需求转成一个设计提示词，并要求 AI 输出设计评审风险。
   - 主控验收清单。

2. 风格要求：
   - 贴近真实项目设计评审，不要把设计讲成架构炫技。
   - 强调 AI 可以辅助列方案和查风险，但架构边界由人拍板。
   - 面向银行研发场景，必须强调安全、脱敏、日志、错误码、requestId、操作追踪。
   - 语言通俗，适合有研发经验但 AI 使用较少的同学。

3. 不要引入真实客户数据、内部系统地址、密钥或生产配置。

最后给主控窗口一段简短汇报，说明：
- 产出了哪些文件。
- 是否覆盖系统设计提示词、设计评审提示词、主控裁决提示词和录屏步骤。
- 是否足够支撑第 4 课录制。
```

## 主控验收重点

- 是否承接第 3 课需求分析结果。
- 是否讲清第一阶段单体 POC 和第二阶段微服务演进的边界。
- 是否覆盖架构、数据库、API、前端、异常、日志、安全脱敏和测试策略。
- 是否包含 AI 设计评审提示词，并按 P0/P1/P2 输出风险。
- 是否强调设计要可验收，而不是炫复杂架构。
- 是否为第 5 课后端开发材料铺垫。
