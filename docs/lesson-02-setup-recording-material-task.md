# 第 2 课环境安装材料子任务

## 子任务目标

产出第 2 课“Claude Code + DeepSeek 环境搭建”的可录制材料，帮助学员在 Windows、macOS、WSL2 三类常见环境下理解如何安装工具、配置 DeepSeek API、运行 Claude Code，并能判断问题出在安装、网络、模型访问还是项目配置。

本子任务只整理录屏材料和安装讲义，不改 POC 后端代码、不改前端代码、不调整数据库脚本。

建议新增：

- `docs/lesson-02-setup-recording-material.md`
- 如有必要，可新增 `docs/setup-faq.md`

## 输入基线

必须阅读：

- `README.md`
- `docs/course-completion-roadmap.md`
- `docs/course-blueprint.md`
- `docs/lesson-plan.md`
- `docs/toolchain-setup.md`
- `docs/ai-dev-workflow.md`
- `docs/lesson-00-recording-material.md`
- `docs/test-acceptance-material.md`

## 子窗口提示词

```text
你现在是 AI 教程项目的“第 2 课环境安装材料子窗口”。请只负责整理第 2 课“Claude Code + DeepSeek 环境搭建”的录屏材料、安装讲义和失败兜底方案，不改 POC 后端代码、不改前端代码、不调整数据库脚本。

课程背景：
- 这是面向赞同科技银行解决方案研发同学的 AI 编程入门教程。
- 学员多为 Java / 银行项目研发同学，有开发经验，但 AI 编程工具使用经验不一。
- 公司内可能存在网络限制、不能统一使用代理、Windows 环境差异、WSL2 与 PowerShell 差异等问题。
- 本课目标不是保证每个人一次装成功，而是让学员知道推荐路径、配置原则、排错方法和兜底方案。

请先阅读：
- README.md
- docs/course-completion-roadmap.md
- docs/course-blueprint.md
- docs/lesson-plan.md
- docs/toolchain-setup.md
- docs/ai-dev-workflow.md
- docs/lesson-00-recording-material.md
- docs/test-acceptance-material.md

输出要求：

1. 新增 `docs/lesson-02-setup-recording-material.md`，内容至少包括：
   - 本课标题和一句话目标。
   - 面向学员要解决的问题：装什么、怎么配、怎么判断问题、怎么安全使用。
   - 面向领导/团队推广要表达的价值：降低上手门槛、统一工具链、统一安全边界。
   - 推荐时长和章节安排，建议 25 到 35 分钟。
   - 环境路线选择原则：
     - macOS 优先。
     - Windows 推荐 WSL2 优先于纯 PowerShell。
     - CLI 优先于桌面端。
     - 公司网络受限时，先区分“安装包下载”和“模型 API 访问”两个问题。
   - Windows 路径：
     - PowerShell 直接安装路径的适用场景和风险。
     - WSL2 路径的推荐原因、前置检查、常见问题。
   - macOS 路径：
     - 终端、Node/npm、Claude Code、环境变量配置的讲解顺序。
   - DeepSeek 配置：
     - API Key 申请和保管原则。
     - 环境变量配置思路。
     - `ANTHROPIC_BASE_URL`、`ANTHROPIC_API_KEY` 等变量的教学说明。
     - 不要把真实 token 写入仓库、截图或录屏。
   - Claude Code 配置：
     - 全局配置和项目级配置的区别。
     - `CLAUDE.md` 或同类项目规则文件应该写什么。
     - 如何验证工具可用。
   - 无代理/内网限制场景：
     - 安装包能否通过公司网盘分发。
     - 安装成功不等于模型访问成功。
     - 不能访问外部 API 时的替代路径：截图演示、统一机器演示、内部网关待确认。
   - 录屏操作命令清单：
     - macOS 示例。
     - Windows PowerShell 示例。
     - Windows WSL2 示例。
     - 所有命令如果存在版本不确定性，要标注“以当前官方文档为准”或“待公司环境确认”。
   - 常见失败和兜底：
     - npm 安装失败。
     - 网络超时。
     - API key 无效。
     - base URL 配错。
     - PowerShell 编码/路径问题。
     - WSL2 没装好。
     - 公司电脑权限不足。
   - 安全提醒：
     - 不上传真实客户数据。
     - 不展示 token。
     - 不把生产地址、内部系统地址、密钥写进提示词。
   - 半口语口播稿。
   - 讲义精简版。
   - 课后练习。
   - 主控验收清单。

2. 如内容较长，可以新增 `docs/setup-faq.md`，整理安装 FAQ 和排错表。

3. 不要编造不可确认的安装命令。对于可能随时间变化的安装方式，请明确写：
   - “以当前官方文档为准”
   - “需在公司网络环境下验证”
   - “需由讲师录屏前复核”

4. 不要写入真实 API key、真实 token、真实代理地址、真实内部网关地址、生产系统地址。

5. 风格要求：
   - 像真实内部培训的安装课，不要营销化。
   - 对初学者友好，但不要把风险说得过于轻松。
   - 重点讲清楚问题定位方法，而不是只罗列命令。

最后给主控窗口一段简短汇报，说明：
- 产出了哪些文件。
- 是否覆盖 Windows、macOS、WSL2。
- 哪些命令或网络路径需要讲师录屏前复核。
- 是否足够支撑第 2 课录制。
```

## 主控验收重点

- 是否明确环境安装课目标和边界。
- 是否覆盖 Windows、macOS、WSL2 三条路径。
- 是否把安装问题和模型 API 访问问题分开讲。
- 是否有无代理/内网限制兜底方案。
- 是否明确 token、API key、生产地址和真实数据不能进入仓库或录屏。
- 是否没有编造不确定安装命令。
- 是否能直接用于录制第 2 课。
