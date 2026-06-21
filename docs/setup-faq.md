# Claude Code + DeepSeek 环境安装 FAQ

## 1. 先判断是哪类问题

| 类别 | 典型表现 | 优先检查 |
| --- | --- | --- |
| Node/npm 问题 | `node` 或 `npm` 不存在，版本过低 | Node 是否安装，PATH 是否生效，版本是否满足官方要求 |
| 安装包下载问题 | `npm install` 超时、证书错误、registry 不通 | npm 源、公司网络、证书、代理、权限 |
| CLI 路径问题 | 安装成功但 `claude` 不存在 | npm global bin 目录是否在 PATH |
| API 认证问题 | 提示 key/token 无效 | token 是否正确、是否过期、是否复制了空格 |
| API endpoint 问题 | 请求 404、模型不存在、协议不兼容 | `ANTHROPIC_BASE_URL`、模型名、DeepSeek 当前文档 |
| 网络出口问题 | 所有人都请求超时或连接失败 | 公司网络策略、防火墙、是否需要内部网关 |
| PowerShell 问题 | 编码乱码、路径空格、环境变量失效 | 使用 `$env:` 临时变量，路径加引号，必要时换 WSL2 |
| WSL2 问题 | WSL 不能启动或网络不同 | WSL 是否安装、虚拟化是否开启、Windows 与 WSL 网络策略 |

## 2. npm 安装失败怎么办

处理顺序：

1. 确认 Node 和 npm 可用：

```bash
node -v
npm -v
```

2. 确认公司网络是否允许访问 npm registry。
3. 若公司有内部 npm 源，按公司内部规范配置。
4. 若使用网盘分发安装包，需确认许可证、版本、安全扫描和更新机制。
5. 不要把 npm 下载失败误判为 DeepSeek API 失败。

## 3. `claude --version` 成功，但对话失败怎么办

这通常说明 CLI 安装基本成功，问题更可能在模型访问链路。

检查顺序：

1. `ANTHROPIC_BASE_URL` 是否为 DeepSeek 当前官方文档里的 Anthropic-compatible endpoint。
2. token/key 是否正确，是否过期。
3. 模型名是否为 DeepSeek 当前文档支持的模型。
4. 公司网络是否允许访问外部 API。
5. 是否存在代理、证书或防火墙限制。

## 4. `ANTHROPIC_AUTH_TOKEN` 和 `ANTHROPIC_API_KEY` 用哪个

DeepSeek 当前 Claude Code 集成文档使用 `ANTHROPIC_AUTH_TOKEN`。部分资料或旧配置可能提到 `ANTHROPIC_API_KEY`。

课程口径：

- 录屏前以 DeepSeek 当前官方文档为准。
- 不把变量名凭记忆写死。
- 不在仓库、截图、录屏中展示真实 token。

## 5. base URL 配错会怎样

常见表现：

- 请求 404。
- 协议不兼容。
- 模型不存在。
- CLI 能启动但无法完成对话。

处理：

- 复核 DeepSeek Anthropic API 文档。
- 不要把 OpenAI-compatible endpoint 和 Anthropic-compatible endpoint 混用。
- 如果公司未来提供内部网关，以公司网关文档为准。

## 6. 没有代理怎么办

先确认公司是否允许直接访问：

- npm registry。
- Claude Code 官方包源。
- DeepSeek API endpoint。

如果不允许：

- 安装包可以考虑公司网盘或内部源分发。
- API 访问需要公司网络策略或内部网关支持。
- 课程录屏可以使用统一演示机器、截图备份或讲师已验证环境。

注意：安装包分发不能解决 API 访问问题。

## 7. Windows 到底用 PowerShell 还是 WSL2

建议：

- 能装 WSL2：优先 WSL2。
- 不能装 WSL2：PowerShell 可以先体验。
- 长期做 Java、Maven、npm、Git、AI CLI 联动：WSL2 更稳定。

PowerShell 风险：

- 路径和空格问题。
- 编码问题。
- npm global PATH 问题。
- 环境变量只对当前窗口生效。
- 公司权限限制。

## 8. 公司电脑权限不足怎么办

可能表现：

- 不能安装 Node。
- 不能安装 WSL2。
- 不能写 npm global 目录。
- 不能配置系统环境变量。

兜底：

- 申请公司统一安装包或软件白名单。
- 使用用户级安装目录。
- 使用统一演示机器。
- 先看录屏和截图，等环境权限解决后再跟做。

## 9. 录屏时如何避免泄露

录屏前检查：

- 终端历史。
- 环境变量输出。
- 浏览器地址栏。
- API key 管理页面。
- 代理配置页面。
- `.env`、配置文件、截图。

录屏中只展示：

- `<your-deepseek-api-key>`。
- `<deepseek-model-from-current-docs>`。
- `https://api.deepseek.com/anthropic` 这类官方公开 endpoint。

不要展示：

- 真实 token。
- 真实代理地址。
- 真实内部网关地址。
- 生产系统地址。
- 真实客户数据。

## 10. 讲师录屏前复核清单

- [ ] Claude Code 官方安装方式是否变化。
- [ ] Node.js 最低版本要求是否变化。
- [ ] DeepSeek Anthropic-compatible endpoint 是否变化。
- [ ] DeepSeek 推荐模型名是否变化。
- [ ] 环境变量名称是否变化。
- [ ] 公司网络是否允许访问 npm 和 DeepSeek API。
- [ ] 录屏环境是否隐藏 token、内部地址和真实账号。
- [ ] 准备截图备份和失败兜底话术。
