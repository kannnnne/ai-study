# 前后端联调验收子任务

## 子任务目标

验证第一阶段 POC 是否形成可录屏演示的完整闭环：

```text
仪表盘 -> 客户列表 -> 客户详情 -> 风险重算 -> 操作日志追踪
```

本任务只做联调验收和问题记录，不新增业务功能。

## 输入基线

验收前必须阅读：

- `docs/api-design.md`
- `docs/backend-code-implementation-acceptance.md`
- `docs/frontend-code-implementation-acceptance.md`
- `docs/frontend-page-design.md`
- `poc/backend`
- `poc/frontend`

## 推荐验收方式

优先使用本地可运行环境：

1. 启动后端。
2. 启动前端 Vite dev server。
3. 用浏览器访问前端。
4. 按录屏路径手工验证。

如果本机没有 MySQL 8，可先使用后端 test profile 或补充一个临时联调用 H2 profile，但不得改变主线设计。如果需要新增联调用配置，必须说明原因、范围和是否会影响课程材料。

## 子窗口提示词

```text
你现在是 AI 教程项目的前后端联调验收子窗口。请只做联调验收、问题定位和必要的小修复建议；不要新增业务功能，不要改变 API 契约，不要引入登录、权限、导出、批量导入、删除。

项目背景：
- 后端第一阶段已实现：`poc/backend`
- 前端第一阶段已实现：`poc/frontend`
- 前端 Vite 已配置 `/api` 代理到 `http://localhost:8080`
- 目标是验证课程录屏路径能否跑通：仪表盘 -> 客户列表 -> 客户详情 -> 风险重算 -> 操作日志追踪。

请先阅读：
- docs/api-design.md
- docs/backend-code-implementation-acceptance.md
- docs/frontend-code-implementation-acceptance.md
- docs/frontend-page-design.md
- poc/backend
- poc/frontend

验收要求：
1. 运行后端测试：`mvn test`。
2. 运行前端构建：`npm run build`。
3. 尝试启动后端和前端。
4. 在浏览器中验证页面主流程：
   - 打开 `/dashboard`
   - 查看风险分布和客户状态统计
   - 进入 `/customers`
   - 按风险等级或状态筛选
   - 进入一个客户详情，例如 `CUST100002`
   - 查看基础信息、账户、当前风险、历史风险
   - 触发风险重算
   - 确认页面展示 `transactionEffect`
   - 进入 `/operation-logs`
   - 按 `operationType=RECALCULATE_RISK` 查询日志
5. 验证错误场景：
   - 查询不存在客户详情或手动访问 `/customers/CUST999999`
   - 风险重算关闭状态客户，例如 `CUST100006`
   - 页面应展示 `code/message/requestId`
6. 检查脱敏展示：
   - 不出现真实手机号、真实证件号、真实账号、生产地址或密钥。
   - 页面字段保留 `masked` / `sample` 语义。
7. 记录浏览器验收结果、问题清单和建议修复项。

输出要求：
1. 启动方式和实际使用的 profile。
2. 后端测试结果。
3. 前端构建结果。
4. 浏览器主流程验收结果。
5. 错误场景验收结果。
6. 安全脱敏检查结果。
7. 发现的问题，按 P0/P1/P2 排序。
8. 是否建议通过主控验收。
9. 适合第 6 课录屏的操作路径。

如果需要修改代码：
- 只能做联调必要的小修复。
- 修改后必须重新运行相关验证。
- 必须说明修改原因和影响范围。
```

## 主控验收重点

- 后端测试和前端构建是否仍通过。
- 浏览器是否能跑通完整主流程。
- 错误场景是否能展示 `code/message/requestId`。
- 风险重算后是否能在操作日志中追踪。
- 页面是否仍守住脱敏和第一阶段边界。
- 是否形成可直接用于第 6 课录屏的操作路径。

