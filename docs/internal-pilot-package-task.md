# 内部试讲包和反馈表子任务

## 子任务目标

产出 P1 内部试讲版的组织材料，让课程可以进入 3 到 5 人小范围试看 / 试讲阶段，并把反馈收集、讲师准备、录制前检查和后续改进闭环整理清楚。

本子任务只整理课程组织材料、反馈表、试看说明和检查清单，不改 POC 后端代码、不改前端代码、不调整课程正文内容、不提交 Git。

建议新增：

- `docs/pilot-preview-brief.md`
- `docs/internal-pilot-plan.md`
- `docs/pilot-feedback-form.md`

## 输入基线

必须阅读：

- `README.md`
- `docs/course-completion-roadmap.md`
- `docs/course-blueprint.md`
- `docs/lesson-plan.md`
- `docs/deliverables-and-launch-plan.md`
- `docs/leadership-brief.md`
- `docs/master-task-board.md`
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
你现在是 AI 教程项目的“内部试讲包和反馈表子窗口”。请只负责整理 P1 内部试讲版的组织材料，不改后端代码、不改前端代码、不调整课程正文内容、不提交 Git。

课程背景：
- 这是面向赞同科技银行解决方案研发同学的 AI 编程入门教程。
- 当前已经有可运行 POC，以及第 0、1、2、3、4、5、6、9 课录屏材料。
- 现在需要把这些材料组织成可给领导 / 技术骨干试看，以及 3 到 5 人小范围内部试讲的课程包。
- 重点不是再写新课程，而是让“怎么试看、怎么试讲、怎么收反馈、怎么判断是否进入正式推广”变清楚。

请先阅读：
- README.md
- docs/course-completion-roadmap.md
- docs/course-blueprint.md
- docs/lesson-plan.md
- docs/deliverables-and-launch-plan.md
- docs/leadership-brief.md
- docs/master-task-board.md
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

1. 新增 `docs/pilot-preview-brief.md`，用于给领导或技术骨干试看，内容至少包括：
   - 试看目标。
   - 建议试看组合：第 0 课 + 第 6 课；可选补看第 5 课或第 9 课。
   - 试看时长安排。
   - 领导关注点：价值、效率、风险控制、团队推广成本、可复制性。
   - 技术骨干关注点：工具可用性、工作流、代码质量、验收方式、落地阻力。
   - 试看后希望得到的反馈问题。
   - 不承诺的边界：不是一次性解决所有 AI 和大模型原理，不直接接真实银行环境，不替代安全和合规流程。
2. 新增 `docs/internal-pilot-plan.md`，用于组织 3 到 5 人内部试讲，内容至少包括：
   - 试讲目标。
   - 适合参与的人群。
   - 试讲前准备。
   - 推荐课程顺序。
   - 每节课的试讲目标、材料路径、预计时长和验收点。
   - 讲师准备清单。
   - 学员准备清单。
   - 试讲当天流程。
   - 试讲后反馈收集和复盘方式。
   - 是否进入正式推广的判断标准。
   - 风险和兜底方案：环境装不上、DeepSeek token 不可用、后端起不来、前端联调失败、学员基础差异大、领导只关心价值不关心细节。
3. 新增 `docs/pilot-feedback-form.md`，作为反馈表模板，内容至少包括：
   - 参与者背景。
   - 环境安装反馈。
   - 课程理解度反馈。
   - POC 跟做反馈。
   - AI 工作流反馈。
   - 验收和安全边界反馈。
   - 对正式推广的建议。
   - 评分项和开放问题。
   - 讲师自评区。
4. 风格要求：
   - 面向公司内部试讲，语气正式但不要官样文章。
   - 明面上以公司价值、团队效率、研发质量和风险控制为主。
   - 兼顾领导视角和技术同学视角，但不要把个人利益说得太直白。
   - 内容要能直接拿去组织试讲，不要只写原则。
5. 不要引入真实客户数据、内部系统地址、密钥或生产配置。

最后给主控窗口一段简短汇报，说明：
- 产出了哪些文件。
- 是否能支撑领导 / 骨干试看和 3 到 5 人内部试讲。
- 是否包含反馈收集和正式推广判断标准。
- 是否有待主控确认的问题。
```

## 主控验收重点

- 是否补齐 P0 试看说明和 P1 内部试讲组织材料。
- 是否明确课程组合、试讲流程、参与角色、时间安排、反馈表和正式推广判断标准。
- 是否兼顾领导价值视角和技术落地视角。
- 是否能直接拿去组织 3 到 5 人小范围试讲。
- 是否避免真实客户数据、密钥、生产地址和内部系统细节。
