# 环境安装与配置

## 目标

让学员在 Windows、macOS 或 WSL2 环境中安装 Claude Code，并通过 DeepSeek API 作为模型后端完成基础使用验证。

## 官方信息来源

本文件需要定期复核，因为 Claude Code 和 DeepSeek API 都会更新。当前依据：

- Claude Code Overview: https://code.claude.com/docs/en/overview
- Claude Code Quickstart: https://code.claude.com/docs/en/quickstart
- Claude Code Advanced Setup: https://code.claude.com/docs/en/setup
- Claude Code Environment Variables: https://code.claude.com/docs/en/env-vars
- Claude Code Authentication: https://code.claude.com/docs/en/authentication
- DeepSeek API Docs: https://api-docs.deepseek.com/
- DeepSeek Anthropic API: https://api-docs.deepseek.com/guides/anthropic_api

## 推荐安装路线

### macOS

官方推荐可使用原生安装：

```bash
curl -fsSL https://claude.ai/install.sh | bash
```

也可以使用 Homebrew：

```bash
brew install --cask claude-code
```

### Windows PowerShell

官方原生安装命令：

```powershell
irm https://claude.ai/install.ps1 | iex
```

### Windows CMD

```bat
curl -fsSL https://claude.ai/install.cmd -o install.cmd && install.cmd && del install.cmd
```

### Windows WinGet

```powershell
winget install Anthropic.ClaudeCode
```

### WSL2

在 Ubuntu 等 WSL2 环境中，按 Linux/macOS 路线安装：

```bash
curl -fsSL https://claude.ai/install.sh | bash
```

## Windows 建议

- 优先推荐 WSL2。
- 如果使用原生 Windows，建议安装 Git for Windows，让 Claude Code 可以使用 Bash 工具。
- 没有 Git Bash 时，Claude Code 会使用 PowerShell，但复杂项目操作体验通常不如类 Unix shell。

## DeepSeek 接入方式

DeepSeek 官方文档说明其 API 兼容 OpenAI 和 Anthropic 格式。用于 Anthropic 生态时，base URL 为：

```text
https://api.deepseek.com/anthropic
```

环境变量示例：

```bash
export ANTHROPIC_BASE_URL=https://api.deepseek.com/anthropic
export ANTHROPIC_API_KEY=你的 DeepSeek API Key
```

PowerShell 示例：

```powershell
$env:ANTHROPIC_BASE_URL="https://api.deepseek.com/anthropic"
$env:ANTHROPIC_API_KEY="你的 DeepSeek API Key"
```

注意：Claude Code 官方认证文档中也提到 `ANTHROPIC_AUTH_TOKEN` 常用于通过 LLM gateway 或 proxy 以 bearer token 方式认证；直接使用 Anthropic API key 时用 `ANTHROPIC_API_KEY`。DeepSeek 官方 Anthropic API 示例使用 `ANTHROPIC_API_KEY`。

## 没有代理怎么办

需要分清两类问题：

### 下载安装问题

如果只有下载安装阶段受限，可以考虑：

- 由有网络权限的同事下载官方安装包或安装脚本。
- 上传到公司允许的网盘或制品库。
- 在培训文档中提供校验方式和版本号。
- 尽量使用官方渠道下载，不要使用来源不明的第三方安装包。

但是要注意：Claude Code 原生安装可能依赖在线下载、更新和登录。如果完全离线，仅靠安装包通常不足以完成后续使用。

### 运行调用问题

如果运行时无法访问 DeepSeek API 或 Claude 相关域名，仅安装成功也不能正常工作。此时需要公司提供一种合规网络路径：

- 允许访问 DeepSeek API。
- 使用公司统一出口。
- 使用内网模型网关。
- 使用公司批准的代理或 API 转发服务。

课程里要明确：安装包解决的是“软件怎么装”，不能解决“模型服务怎么访问”。

## 建议配置 CLAUDE.md

项目根目录放置 `CLAUDE.md`，用于告诉 AI 项目的技术栈、命令、约束和工作流。

示例：

```markdown
# 项目说明

这是一个 AI 银行 POC 教程项目，技术栈为 JDK 21、Spring Boot 3.x、Spring Cloud、Nacos、Vue 3、TypeScript。

## 工作原则

- 修改代码前先分析影响范围。
- 复杂任务必须先输出计划，等待确认后再实现。
- 每次修改后说明变更文件、风险点和验证方式。
- 不要输出或提交任何密钥、真实客户数据、生产地址。
- 涉及金额使用 BigDecimal。
- 涉及日期时间必须说明时区和格式。
- 后端优先补充单元测试或接口测试。
- 前端修改需要检查桌面和移动端基础布局。

## 常用命令

- 后端测试：`./mvnw test`
- 后端启动：`./mvnw spring-boot:run`
- 前端安装：`npm install`
- 前端启动：`npm run dev`
- 前端构建：`npm run build`

## 验收要求

- 代码能编译。
- 核心测试通过。
- API 与文档一致。
- 页面主要流程可操作。
- 日志不打印敏感信息。
```

## 安装课要讲清的原则

- 首选官方文档和官方安装源。
- 安装路径和运行路径分开讲。
- 明确在线安装、离线安装、运行访问是三件事。
- Windows、macOS、WSL2 分开演示。
- 密钥只放本机环境变量或公司批准的密钥管理方案中。
- 录屏时必须隐藏 token、真实账号和内部地址。

