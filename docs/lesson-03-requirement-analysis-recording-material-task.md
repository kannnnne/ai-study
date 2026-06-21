# 第 3 课需求分析材料子任务

## 子任务目标

产出第 3 课“用 AI 做 POC 需求分析”的可录制材料，演示如何从一句粗需求开始，让 AI 帮助澄清用户角色、功能范围、边界、不做什么、业务流程、非功能要求、待确认问题和验收标准。

本子任务只整理录屏材料、口播稿、提示词和讲义，不改 POC 后端代码、不改前端代码、不调整数据库脚本。

建议新增：

- `docs/lesson-03-requirement-analysis-recording-material.md`

## 输入基线

必须阅读：

- `README.md`
- `docs/course-completion-roadmap.md`
- `docs/course-blueprint.md`
- `docs/lesson-plan.md`
- `docs/ai-dev-workflow.md`
- `docs/poc-project-plan.md`
- `docs/poc-architecture.md`
- `docs/example-library.md`
- `docs/subsession-prompts.md`
- `docs/lesson-00-recording-material.md`
- `docs/lesson-01-workflow-recording-material.md`，如果该文件已经存在

## 子窗口提示词

```text
你现在是 AI 教程项目的“第 3 课需求分析材料子窗口”。请只负责整理第 3 课“用 AI 做 POC 需求分析”的录屏材料、提示词、口播稿和讲义，不改后端代码、不改前端代码、不调整数据库脚本。

课程背景：
- 这是面向赞同科技银行解决方案研发同学的 AI 编程入门教程。
- 第 0 课讲课程导入和预期管理。
- 第 1 课讲 AI 编程基础概念和工作流。
- 第 3 课要演示：不要一上来让 AI 写代码，而是先让 AI 帮忙澄清需求、边界和验收标准。
- 演示案例是“客户风险评级与账户查询 POC”，服务于银行研发场景下 AI 辅助开发流程。

请先阅读：
- README.md
- docs/course-completion-roadmap.md
- docs/course-blueprint.md
- docs/lesson-plan.md
- docs/ai-dev-workflow.md
- docs/poc-project-plan.md
- docs/poc-architecture.md
- docs/example-library.md
- docs/subsession-prompts.md
- docs/lesson-00-recording-material.md
- docs/lesson-01-workflow-recording-material.md，如果该文件已经存在；如果不存在，参考 docs/lesson-01-workflow-recording-material-task.md

输出要求：

1. 新增 `docs/lesson-03-requirement-analysis-recording-material.md`，内容至少包括：
   - 本课标题和一句话目标。
   - 本课要解决的问题：从粗需求到可开发任务，为什么要先做需求分析。
   - 推荐时长和章节安排，建议 25 到 35 分钟。
   - 演示输入的粗需求：
     `我要做一个客户风险评级与账户查询 POC，用于演示银行研发场景下 AI 辅助开发流程。`
   - 第一轮需求分析提示词：
     - 要求 AI 先不要写代码。
     - 输出用户角色、功能范围、不做什么、业务流程、非功能要求、待确认问题、验收标准。
     - 明确使用脱敏样例数据，不接真实银行环境。
   - 第二轮追问提示词：
     - 让 AI 收敛 POC 边界。
     - 区分第一阶段单体 POC 和第二阶段微服务演进。
     - 让 AI 明确最小可运行闭环。
   - 第三轮验收标准提示词：
     - 让 AI 输出需求验收清单。
     - 区分需求验收、设计验收、开发验收、测试验收、安全验收。
   - 录屏操作步骤：
     - 打开主控文档。
     - 新开需求分析子会话。
     - 输入粗需求。
     - 让 AI 输出需求分析。
     - 人工指出不合理或过大的范围。
     - 让 AI 收敛边界。
     - 把结果沉淀成文档或主控验收基线。
   - 讲解重点：
     - AI 第一次输出通常会偏大，需要人收敛。
     - “不做什么”与“做什么”同样重要。
     - 银行场景必须提前写安全和脱敏边界。
     - 验收标准要写在开发前。
   - 常见错误：
     - 粗需求后直接要求写代码。
     - 不定义用户角色。
     - 不写非功能要求。
     - 不写不做什么。
     - 没有待确认问题。
     - 验收标准太虚。
   - 半口语口播稿。
   - 讲义精简版。
   - 课后练习：让学员把自己项目里的一个小需求改写成需求分析提示词，并输出验收标准。
   - 主控验收清单。

2. 风格要求：
   - 贴近真实项目需求澄清过程。
   - 不夸大 AI，一定强调 AI 输出要由人收敛和确认。
   - 面向银行研发场景，必须强调真实客户数据、生产地址、密钥、内部系统信息不能进入提示词。
   - 语言通俗，适合有研发经验但 AI 使用较少的同学。

3. 不要引入真实客户数据、内部系统地址、密钥或生产配置。

最后给主控窗口一段简短汇报，说明：
- 产出了哪些文件。
- 是否覆盖粗需求输入、需求分析提示词、收敛边界、验收标准和录屏步骤。
- 是否足够支撑第 3 课录制。
```

## 主控验收重点

- 是否清楚演示了从一句粗需求到需求分析结果的过程。
- 是否明确 AI 不能直接进入编码。
- 是否覆盖用户角色、功能范围、不做什么、业务流程、非功能要求、待确认问题和验收标准。
- 是否能体现主控会话 / 子会话工作流。
- 是否强调银行场景安全与脱敏边界。
- 是否能承接第 1 课工作流，并为第 4 课设计评审铺垫。
