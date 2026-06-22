# AI 教程讲课 PPT 生成子任务

## 子任务目标

生成一份用于内部讲课、试看和试讲汇报的 PowerPoint，帮助讲师把当前 AI 编程入门课程讲清楚：为什么做、讲什么、怎么学、如何用 Claude Code + DeepSeek 搭环境、如何用 AI 做需求 / 设计 / 开发 / 联调 / 验收，以及课程对团队研发效率、质量和风险控制的价值。

本子任务需要实际生成 PPTX，而不是只写大纲。

建议输出：

- `outputs/ai-course-p1-internal-training.pptx`

## 输入基线

必须阅读：

- `README.md`
- `docs/course-completion-roadmap.md`
- `docs/course-blueprint.md`
- `docs/lesson-plan.md`
- `docs/deliverables-and-launch-plan.md`
- `docs/leadership-brief.md`
- `docs/pilot-preview-brief.md`
- `docs/internal-pilot-plan.md`
- `docs/lesson-00-recording-material.md`
- `docs/lesson-01-workflow-recording-material.md`
- `docs/lesson-02-setup-recording-material.md`
- `docs/lesson-03-requirement-analysis-recording-material.md`
- `docs/lesson-04-design-review-recording-material.md`
- `docs/lesson-05-backend-recording-material.md`
- `docs/lesson-06-recording-material.md`
- `docs/lesson-09-acceptance-recording-material.md`
- `docs/test-acceptance-material.md`
- `docs/fullstack-integration-acceptance.md`

## 子窗口提示词

```text
你现在是 AI 教程项目的“讲课 PPT 生成子窗口”。请生成一份可直接用于讲课、试看和内部试讲启动会的 PowerPoint。

请先阅读：
- docs/course-ppt-generation-task.md
- README.md
- docs/course-completion-roadmap.md
- docs/course-blueprint.md
- docs/lesson-plan.md
- docs/deliverables-and-launch-plan.md
- docs/leadership-brief.md
- docs/pilot-preview-brief.md
- docs/internal-pilot-plan.md
- docs/lesson-00-recording-material.md
- docs/lesson-01-workflow-recording-material.md
- docs/lesson-02-setup-recording-material.md
- docs/lesson-03-requirement-analysis-recording-material.md
- docs/lesson-04-design-review-recording-material.md
- docs/lesson-05-backend-recording-material.md
- docs/lesson-06-recording-material.md
- docs/lesson-09-acceptance-recording-material.md
- docs/test-acceptance-material.md
- docs/fullstack-integration-acceptance.md

输出要求：
1. 使用 Presentations skill 生成真实 `.pptx` 文件，建议输出到：
   - `outputs/ai-course-p1-internal-training.pptx`
2. 不要只写 Markdown 大纲，不要只给文本。
3. 不改后端代码、不改前端代码、不提交 Git。
4. PPT 语言使用中文。
5. 风格定位：
   - 企业内部技术培训。
   - 面向银行解决方案研发团队。
   - 专业、克制、清晰、有技术可信度。
   - 不要营销风，不要花哨渐变，不要堆满文字。
   - 可以使用蓝灰、深青、绿色等稳重科技色，但避免紫色炫光风。
6. 建议 18 到 25 页，内容结构建议如下：
   - 封面：AI 辅助研发入门课程。
   - 为什么现在要做这门课：内网限制、学习门槛、团队效率、质量和风险控制。
   - 课程面向谁：Java 后端、前端、测试、技术骨干、AI 初学同学。
   - 课程不是什么：不是大模型原理课、不是算法课、不是绕过安全流程。
   - 课程交付物总览：视频、文档、POC、提示词、验收清单、试讲反馈。
   - 学习路径：第 0、1、2、3、4、5、6、9 课如何串起来。
   - 工具体系：Claude Code + DeepSeek 的定位和边界。
   - AI 辅助研发工作流：需求分析 -> 设计评审 -> 计划拆分 -> 开发 -> 测试 -> 联调 -> 验收。
   - 主控会话和子会话协作方式。
   - POC 场景：客户风险评级与账户查询。
   - POC 架构：前端、后端、数据库、Flyway、API、日志。
   - 后端开发课重点：Spring Boot 3.x、JDK 21、统一响应、错误码、requestId、风险重算事务。
   - 前端联调课重点：仪表盘、客户列表、详情、风险重算、操作日志。
   - 验收复盘课重点：AI 验收、人工验收、安全脱敏、事务副作用。
   - 安全边界：不上传真实客户数据、不使用生产地址、不泄露密钥、不绕过合规流程。
   - 对学员的价值：会搭环境、会提需求、会拆任务、会验收 AI 产物。
   - 对团队和管理的价值：统一方法、降低试错成本、提高研发质量、沉淀组织资产。
   - 内部试讲安排：试看组合、3 到 5 人试讲、反馈表。
   - 成功标准：能复现、能讲清、能验收、能安全推广。
   - 下一步：组织试讲，收集反馈，再决定 P2 正式发布版。
7. 每页需要有清晰标题和讲课要点，不要写成长篇论文。
8. 每页尽量附 1 到 3 句讲师备注或口播提示，便于讲课。
9. 需要做渲染/预览 QA，检查是否有文字重叠、裁剪、标题换行、页面过密。
10. 最终回传时说明：
    - PPT 输出路径。
    - 页数。
    - 覆盖了哪些课程主线。
    - 做过哪些 QA。
    - 有哪些待主控确认的问题。

特别注意：
- 不要引入真实客户数据、内部系统地址、密钥或生产配置。
- POC 风险规则只作为教学样例，不代表真实银行业务规则。
- 如果没有公司模板，就自行生成一套专业内训风格；不要停下来问模板。
```

## 主控验收重点

- 是否生成真实 PPTX 文件。
- 是否覆盖课程背景、目标、交付物、学习路径、工具体系、工作流、POC、开发、联调、验收、安全边界、试讲安排和下一步。
- 是否适合领导 / 技术骨干试看，也适合内部讲课开场使用。
- 是否页面清晰、不过密、没有明显重叠或裁剪。
- 是否有讲师口播提示。
- 是否避免真实客户数据、密钥、生产地址和内部系统细节。
