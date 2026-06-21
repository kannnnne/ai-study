# 第 1 课工作流材料子任务

## 子任务目标

产出第 1 课“AI 编程基础概念和工作流”的可录制材料，帮助学员建立正确的 AI 辅助研发方法：先理解 AI、LLM、提示词、上下文、Agent 和工具调用，再掌握主控会话 / 子会话拆分、需求分析、设计评审、计划拆解、测试验收和人工把关。

本子任务只整理录屏材料和讲义，不改 POC 后端代码、不改前端代码、不调整数据库脚本。

建议新增：

- `docs/lesson-01-workflow-recording-material.md`

## 输入基线

必须阅读：

- `README.md`
- `docs/course-completion-roadmap.md`
- `docs/course-blueprint.md`
- `docs/lesson-plan.md`
- `docs/ai-dev-workflow.md`
- `docs/master-control.md`
- `docs/master-task-board.md`
- `docs/subsession-prompts.md`
- `docs/lesson-00-recording-material.md`
- `docs/test-acceptance-material.md`

## 子窗口提示词

```text
你现在是 AI 教程项目的“第 1 课工作流材料子窗口”。请只负责整理第 1 课“AI 编程基础概念和工作流”的录屏材料、口播稿和讲义，不改后端代码、不改前端代码、不调整数据库脚本。

课程背景：
- 这是面向赞同科技银行解决方案研发同学的 AI 编程入门教程。
- 第 0 课已经讲清课程导入和预期管理。
- 第 1 课要解决“为什么不能一上来就让 AI 写完整系统”的问题。
- 学员需要理解 AI 辅助研发的基本概念和正确流程，而不是只学几个提示词技巧。

请先阅读：
- README.md
- docs/course-completion-roadmap.md
- docs/course-blueprint.md
- docs/lesson-plan.md
- docs/ai-dev-workflow.md
- docs/master-control.md
- docs/master-task-board.md
- docs/subsession-prompts.md
- docs/lesson-00-recording-material.md
- docs/test-acceptance-material.md

输出要求：

1. 新增 `docs/lesson-01-workflow-recording-material.md`，内容至少包括：
   - 本课标题和一句话目标。
   - 本课要解决的问题：AI 是什么、能做什么、不能替代什么、怎么安全地进入研发流程。
   - 推荐时长和章节安排，建议 20 到 30 分钟。
   - 基础概念通俗解释：
     - AI / 大模型 / LLM。
     - 提示词不是咒语，而是任务说明、上下文、约束和验收标准。
     - 上下文窗口是什么，为什么会跑偏。
     - Agent 可以理解为会分步骤、会调用工具、会检查结果的工作流。
     - 工具调用、文件读写、命令执行、浏览器验收分别意味着什么。
   - 正确工作流：
     - 需求分析。
     - 设计和评审。
     - 拆分计划。
     - 测试和验收标准。
     - 开发。
     - AI 验收。
     - 人工验收。
     - 安全运维验收。
   - 主控会话和子会话分工：
     - 主控负责目标、拆分、验收、路线图。
     - 子会话负责单点产出。
     - 一个会话不要塞多个不相关任务。
     - 上下文跑偏时新开会话。
   - 结合本项目的真实例子：
     - POC 架构设计子窗口。
     - 数据库设计子窗口。
     - API 设计子窗口。
     - 后端/前端/联调/录屏材料子窗口。
     - 主控如何验收和提交。
   - 好提示词的结构：
     - 背景。
     - 目标。
     - 输入材料。
     - 输出格式。
     - 约束边界。
     - 验收标准。
   - 常见错误：
     - 一句话让 AI 做完整系统。
     - 一个会话塞太多任务。
     - 不跑测试就相信 AI。
     - 上传真实数据、token、生产地址。
     - 上下文跑偏仍然硬聊。
   - 录屏展示建议：
     - 展示 `docs/ai-dev-workflow.md`。
     - 展示 `docs/course-completion-roadmap.md`。
     - 展示 `docs/master-task-board.md`。
     - 展示一个子任务提示词结构。
   - 半口语口播稿。
   - 讲义精简版。
   - 课后练习：让学员把一个真实小需求改写成适合 AI 执行的任务说明。
   - 主控验收清单。

2. 风格要求：
   - 通俗、克制、可信。
   - 不讲玄学，不夸大 AI，不说 AI 替代程序员。
   - 要让有 Java / 银行项目经验但 AI 使用较少的同学听得懂。
   - 重点强调：AI 是研发搭档，最终责任由人和工程流程承担。

3. 不要引入真实客户数据、内部系统地址、密钥或生产配置。

最后给主控窗口一段简短汇报，说明：
- 产出了哪些文件。
- 是否覆盖基础概念、工作流、会话拆分和提示词结构。
- 是否足够支撑第 1 课录制。
```

## 主控验收重点

- 是否讲清 AI / LLM / 提示词 / 上下文 / Agent / 工具调用。
- 是否讲清完整 AI 辅助研发工作流。
- 是否讲清主控会话和子会话分工。
- 是否用本项目现有子任务作为真实例子。
- 是否强调测试、人工验收和安全边界。
- 是否能承接第 0 课，并为第 2 课环境安装、第 3 课需求分析铺垫。
