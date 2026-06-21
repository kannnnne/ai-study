# 第 2 课录屏材料：Claude Code + DeepSeek 环境搭建

## 主控验收结论

本录屏材料用于第 2 课“Claude Code + DeepSeek 环境搭建”，可作为内部试讲版安装课脚本。

通过口径：

- 覆盖 macOS、Windows PowerShell、Windows WSL2 三条路径。
- 明确推荐顺序：macOS 优先；Windows 上 WSL2 优先于纯 PowerShell；CLI 优先于桌面端。
- 把“安装包下载”和“模型 API 访问”拆开讲，适合公司网络受限场景。
- 明确所有真实 token、API key、代理地址、生产地址、内部系统地址都不能入仓、截图或录屏。
- 对可能变化的安装命令标注“以当前官方文档为准 / 讲师录屏前复核 / 需公司网络验证”。

参考资料：

- Claude Code 官方设置文档：<https://code.claude.com/docs/en/setup>
- Claude Code npm 包：<https://www.npmjs.com/package/@anthropic-ai/claude-code>
- DeepSeek Claude Code 集成文档：<https://api-docs.deepseek.com/quick_start/agent_integrations/claude_code>
- DeepSeek Anthropic API 文档：<https://api-docs.deepseek.com/guides/anthropic_api>

## 1. 标题与一句话目标

标题：第 2 课：Claude Code + DeepSeek 环境搭建。

一句话目标：让学员能理解并搭起 Claude Code + DeepSeek 的基本使用环境，同时知道安装失败、网络不通、API 配错和安全泄露分别怎么判断。

## 2. 本课解决什么问题

面向学员：

- 我到底要装哪些东西。
- Windows、macOS、WSL2 应该选哪条路径。
- Claude Code 装好了以后，DeepSeek 怎么接进去。
- 没有代理、公司网络受限、API 不通时怎么判断问题在哪。
- API key、token、内部地址、真实数据怎么保护。

面向领导和团队推广：

- 降低同学各自摸索安装环境的成本。
- 统一推荐路径和安全边界。
- 把“能不能安装”和“能不能访问模型”拆开治理，便于后续试点推广。
- 为后续课程中的需求分析、代码生成、联调验收提供一致工具底座。

## 3. 时长与章节安排

建议时长：25 到 35 分钟。

| 章节 | 内容 | 时长 |
| --- | --- | --- |
| 1 | 开场：这节课不是炫命令，是搭工具底座 | 2 分钟 |
| 2 | 环境路线选择：macOS、Windows、WSL2 怎么选 | 4 分钟 |
| 3 | Claude Code 安装前置：终端、Node.js、npm | 5 分钟 |
| 4 | DeepSeek API 配置和安全保管 | 6 分钟 |
| 5 | macOS / Linux / WSL2 示例流程 | 5 分钟 |
| 6 | Windows PowerShell 示例流程和风险 | 4 分钟 |
| 7 | 无代理、内网限制和安装包分发兜底 | 5 分钟 |
| 8 | 常见失败定位和收束 | 4 分钟 |

## 4. 环境路线选择原则

推荐顺序：

1. macOS：终端、Node/npm、环境变量体验相对稳定，适合讲师录屏。
2. Windows + WSL2：更接近 Linux 命令习惯，适合 Java 后端同学长期使用 CLI。
3. Windows PowerShell：适合无法安装 WSL2 或只做轻量体验的同学，但路径、编码、权限和环境变量更容易出问题。

原则：

- CLI 优先于桌面端，因为本课程要演示文件读写、命令执行、测试、构建和 Git 提交。
- WSL2 优先于 PowerShell，因为很多开源工具链、脚本和 AI CLI 文档默认偏 Linux/macOS。
- 公司网络受限时，先分清两个问题：
  - 安装包下载：npm、Node、Claude Code 包能不能下载。
  - 模型 API 访问：Claude Code 启动后能不能访问 DeepSeek Anthropic-compatible endpoint。
- 安装成功不等于模型可用。能运行 `claude` 只能证明 CLI 在本机可执行，不代表 API key、base URL、模型名、网络出口都正确。

## 5. 录制前准备清单

- 讲师录屏前复核 Claude Code 官方安装文档。
- 讲师录屏前复核 DeepSeek Claude Code 集成文档中的环境变量名称、模型名和 endpoint。
- 准备一个只用于录屏的 DeepSeek API key，录屏时用占位符展示，不展示真实值。
- 准备一个干净目录，例如 `D:\project\ai-study-demo` 或 `~/project/ai-study-demo`。
- 关闭或遮挡浏览器里的真实账号、公司内部系统页面、真实代理配置和 token 管理页面。
- 如果现场网络不稳定，准备截图或文字备份，说明“这一步在公司网络下需要验证”。
- 准备 `docs/setup-faq.md` 作为失败兜底材料。

## 6. Claude Code 安装说明

官方文档当前说明 Claude Code 可以通过 npm 安装，并要求 Node.js 18 或更高版本。录屏前必须以官方文档为准复核。

通用检查：

```bash
node -v
npm -v
```

Node 版本低于官方要求时，先升级 Node，再安装 Claude Code。

Claude Code 安装示例，录屏前复核：

```bash
npm install -g @anthropic-ai/claude-code
claude --version
```

讲解口径：

- 这一步解决的是“本机有没有 Claude Code CLI”。
- 如果 npm 下载失败，优先判断是 npm registry、公司网络、证书、权限还是代理问题。
- 不建议在没有确认原因时反复换命令。
- 如果公司统一分发安装包或配置内部 npm 源，应以公司内部规范为准。

## 7. DeepSeek 配置说明

DeepSeek 官方文档提供了 Anthropic API 格式兼容 endpoint，并给出 Claude Code 集成示例。当前官方示例中 base URL 为：

```text
https://api.deepseek.com/anthropic
```

注意：DeepSeek 文档中的模型名和环境变量可能随版本变化，录屏前必须复核官方文档。

Linux / macOS / WSL2 示例，真实 key 不要出现在录屏里：

```bash
export ANTHROPIC_BASE_URL=https://api.deepseek.com/anthropic
export ANTHROPIC_AUTH_TOKEN=<your-deepseek-api-key>
export ANTHROPIC_MODEL=<deepseek-model-from-current-docs>
```

Windows PowerShell 示例，真实 key 不要出现在录屏里：

```powershell
$env:ANTHROPIC_BASE_URL="https://api.deepseek.com/anthropic"
$env:ANTHROPIC_AUTH_TOKEN="<your-deepseek-api-key>"
$env:ANTHROPIC_MODEL="<deepseek-model-from-current-docs>"
```

讲解口径：

- `ANTHROPIC_BASE_URL` 决定 Claude Code 请求哪个 Anthropic-compatible endpoint。
- token/key 只存在本机环境变量或安全凭据管理里，不写入 Git 仓库。
- 有些资料可能使用 `ANTHROPIC_API_KEY`，但 DeepSeek 当前 Claude Code 集成文档使用 `ANTHROPIC_AUTH_TOKEN`；录屏前以 DeepSeek 当前官方文档为准。
- 模型名不要凭记忆写死，录屏前按 DeepSeek 文档复核。

## 8. macOS / Linux / WSL2 录屏命令清单

以下命令用于教学示例，录屏前按官方文档和公司网络环境复核。

```bash
node -v
npm -v
npm install -g @anthropic-ai/claude-code
claude --version

export ANTHROPIC_BASE_URL=https://api.deepseek.com/anthropic
export ANTHROPIC_AUTH_TOKEN=<your-deepseek-api-key>
export ANTHROPIC_MODEL=<deepseek-model-from-current-docs>

mkdir -p ~/project/ai-study-demo
cd ~/project/ai-study-demo
claude
```

验证思路：

- `claude --version` 能执行：CLI 安装基本可用。
- `claude` 能进入交互：工具本地可启动。
- 发送一个无敏感信息的小问题：验证模型访问是否可用。
- 如果 CLI 可启动但模型请求失败，优先排查 base URL、token、模型名、网络出口。

## 9. Windows PowerShell 路径

适用场景：

- 学员不能安装 WSL2。
- 只做工具体验，不做复杂 shell 脚本。
- 公司电脑权限允许安装 Node/npm 和 npm global package。

示例命令，录屏前复核：

```powershell
node -v
npm -v
npm install -g @anthropic-ai/claude-code
claude --version

$env:ANTHROPIC_BASE_URL="https://api.deepseek.com/anthropic"
$env:ANTHROPIC_AUTH_TOKEN="<your-deepseek-api-key>"
$env:ANTHROPIC_MODEL="<deepseek-model-from-current-docs>"

mkdir D:\project\ai-study-demo
cd D:\project\ai-study-demo
claude
```

风险提醒：

- PowerShell 的执行策略、编码、路径空格、管理员权限、npm global 路径都可能影响体验。
- 环境变量用 `$env:` 设置只对当前窗口生效，关闭窗口后会失效。
- 不要为了省事把 token 写进 `.ps1` 脚本并提交到仓库。

## 10. Windows WSL2 路径

推荐原因：

- 更接近 Linux/macOS 命令习惯。
- 更适合后续 Java、Maven、npm、Git、CLI 工具联动。
- 更容易复用课程中的 shell 命令和开源社区排错经验。

前置检查：

```powershell
wsl --list --verbose
```

进入 WSL 后：

```bash
node -v
npm -v
npm install -g @anthropic-ai/claude-code
claude --version
```

常见问题：

- WSL2 未安装或未启用虚拟化。
- WSL 与 Windows 的文件路径混用，例如 `/mnt/c/...` 和 `D:\...`。
- 公司安全软件限制 WSL 网络访问。
- Windows 代理设置未自动进入 WSL。

讲解口径：

- 如果后续要长期做 AI 编程，Windows 同学优先建议把 WSL2 配好。
- 但 WSL2 本身也需要公司电脑权限和网络策略支持，不能强行假设每个人都能装。

## 11. 项目级配置和规则文件

全局配置解决“工具能不能跑”，项目级配置解决“在这个项目里应该怎么合作”。

项目规则文件可以写：

- 技术栈：JDK 21、Spring Boot 3.x、Vue 3、MySQL 8、Flyway。
- 代码边界：不要引入真实登录、权限、MQ、缓存等第一阶段不需要的内容。
- 安全边界：不使用真实客户数据、真实账号、生产地址、密钥。
- 工作流：先分析、再计划、再实现、再测试和验收。
- Git 规则：验收通过后再提交。
- 录屏规则：命令、token、内部地址要遮挡。

示例口径：

```text
CLAUDE.md 不是写给模型看的魔法咒语，它更像项目协作说明书。
它告诉 AI：这个项目是什么、技术栈是什么、哪些事不要做、验收标准是什么。
```

## 12. 无代理 / 内网限制兜底

先拆问题：

| 问题 | 表现 | 兜底 |
| --- | --- | --- |
| 安装包下载失败 | npm install 超时或证书错误 | 使用公司允许的 npm 源、离线包、网盘分发；需由公司网络策略确认 |
| CLI 已安装但 API 不通 | `claude` 能启动，但请求模型失败 | 检查 base URL、token、模型名、网络出口、防火墙 |
| 不能访问外部 API | 所有人都请求失败 | 需要内部网关或统一演示机器；本课程只能标注待公司环境确认 |
| 个人没有代理 | 下载或 API 均失败 | 不把代理作为默认前提；优先准备截图演示、统一机器演示或集中安装包 |

安装包是否可以上传网盘：

- 可以作为公司内部兜底方案讨论，但必须确认许可证、版本一致性、安全扫描和更新机制。
- 网盘安装包只能解决“下载不到安装包”，不能解决“模型 API 访问不了”。
- 如果公司未来提供内部 AI 网关，应由统一网关配置替代个人 token。

## 13. 常见失败和兜底

详见 `docs/setup-faq.md`。

录屏时建议只演示 2 到 3 个最常见失败：

- npm 安装失败：说明是安装包下载问题。
- API key 无效：说明是认证问题。
- base URL 配错：说明是模型访问配置问题。

不要在安装课里临场排查过深网络问题。排查不出来时，用 FAQ 收束。

## 14. 安全提醒

必须强调：

- 不上传真实客户数据。
- 不展示真实 token、API key、代理地址。
- 不把生产地址、内部系统地址、数据库连接串写进提示词。
- 不把 `.env`、本地配置、截图中的敏感信息提交到仓库。
- 录屏前检查终端历史、浏览器地址栏、Network 面板和环境变量输出。

可以口播：

```text
AI 工具能不能用是一回事，能不能安全地用是另一回事。
银行项目里最重要的边界，是不要把真实客户数据、生产配置和密钥带进外部模型。
```

## 15. 半口语口播稿

大家好，这节是第 2 课，我们来讲 Claude Code + DeepSeek 的环境搭建。

先说清楚，这节课不是为了证明某个命令一定能在所有机器上一遍成功。公司电脑、网络策略、Windows 权限、npm 源、API 出口都可能不一样。所以我们更重要的目标，是把环境问题拆清楚：到底是工具没装上，还是工具装上了但访问不了模型；是下载包的问题，还是 API key、base URL、模型名的问题。

我建议大家优先按这个顺序理解环境：如果你用 macOS，终端体验一般最顺；如果你用 Windows，长期做 AI 编程更推荐 WSL2；如果暂时只能用 PowerShell，也可以先跑起来，但要接受路径、编码、权限和环境变量会多一些坑。

Claude Code 是跑在终端里的 AI 编程工具。安装它之前，先确认 Node 和 npm 可用。官方文档当前说明可以通过 npm 安装 Claude Code，并要求 Node.js 18 或更高版本。这里的命令录屏前我会按官方文档复核，大家课后也要以当前官方文档为准。

装好 Claude Code 只代表本机有这个 CLI。接下来要让它使用 DeepSeek，就要配置 DeepSeek 的 Anthropic-compatible endpoint。DeepSeek 官方文档里给了 base URL 和环境变量示例。这里最重要的是不要把真实 API key 放进仓库，也不要在录屏里展示。课堂里我会用占位符演示。

如果你执行 `claude --version` 成功，但对话失败，那就说明安装可能没问题，问题更可能在模型访问上。这个时候依次查 base URL、token、模型名、网络出口。如果 npm install 一开始就失败，那就是安装包下载链路的问题。两类问题要分开看。

在公司没有统一代理或外部 API 出口的情况下，我们不能假设所有人都能直接访问。可以考虑内部网盘分发安装包、统一演示机器、截图演示，或者后续由公司提供内部网关。但要注意，安装包分发只能解决下载问题，不能解决模型 API 访问问题。

最后再强调安全边界：不要把真实客户数据、账号、手机号、身份证号、生产地址、密钥、token 发给外部模型。AI 是研发辅助工具，不是安全边界的替代品。后面的课程里，我们会一直按这个原则来做需求、设计、开发和验收。

## 16. 讲义精简版

第 2 课要点：

- 推荐路径：macOS 优先；Windows 推荐 WSL2；PowerShell 可用于轻量体验。
- 安装问题和模型访问问题要分开判断。
- Claude Code 本地可运行，不代表 DeepSeek API 一定可访问。
- DeepSeek 配置重点是 base URL、token、模型名和网络出口。
- 安装包可以考虑内部分发，但不能解决 API 访问。
- token、真实客户数据、生产地址不能进入仓库、截图、录屏和提示词。
- 所有安装命令录屏前以官方文档为准复核。

## 17. 课后练习

1. 在自己的机器上检查 `node -v` 和 `npm -v`，记录版本。
2. 尝试安装或检查 Claude Code CLI，记录是否能执行 `claude --version`。
3. 用占位符写出自己的 DeepSeek 环境变量配置，不填真实 key。
4. 判断一个失败案例属于哪类问题：
   - npm 下载失败。
   - CLI 能启动但模型请求失败。
   - API key 无效。
   - base URL 配错。
5. 写一条自己的项目级 AI 使用规则，例如“不上传真实客户数据，不提交 token”。

## 18. 失败兜底方案

如果录屏现场失败：

- CLI 安装失败：切到截图和 FAQ，说明这是安装包下载问题。
- API 请求失败：保留失败现场，讲解排查顺序，必要时切到截图备份。
- WSL2 不可用：改用 PowerShell 路径演示，并说明 WSL2 需公司电脑权限支持。
- 公司网络访问外部 API 失败：不硬排，标注“需公司网络验证或内部网关支持”。
- 命令变化：以官方文档为准，录屏中说明工具链会更新，课程关注的是配置方法和排错思路。

## 19. 主控验收清单

- [ ] 覆盖第 2 课标题和一句话目标。
- [ ] 覆盖学员视角和团队推广视角。
- [ ] 覆盖 macOS、Windows PowerShell、Windows WSL2。
- [ ] 明确 macOS 优先、WSL2 优先于 PowerShell、CLI 优先于桌面端。
- [ ] 明确安装包下载和模型 API 访问是两个问题。
- [ ] 覆盖 DeepSeek API key、base URL、模型名和安全保管。
- [ ] 覆盖 Claude Code 全局配置和项目级规则文件。
- [ ] 覆盖无代理 / 内网限制兜底。
- [ ] 覆盖常见失败和兜底。
- [ ] 没有真实 API key、token、代理地址、内部网关地址、生产系统地址。
- [ ] 不把不确定安装命令写成永久有效铁律。
- [ ] 足够支撑第 2 课录制。

## 给主控窗口的简短汇报

第 2 课环境安装材料已整理完成，产出 `docs/lesson-02-setup-recording-material.md`，并配套 `docs/setup-faq.md`。材料覆盖 macOS、Windows PowerShell、Windows WSL2、DeepSeek 配置、Claude Code 配置、无代理/内网限制、安装包分发、API 访问排障、安全提醒和失败兜底。

需要讲师录屏前复核：Claude Code 当前官方安装命令、DeepSeek 当前环境变量名称和模型名、公司网络是否允许 npm 下载和访问 DeepSeek API。材料足够支撑第 2 课录制。
