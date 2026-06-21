# 前端代码实现子任务

## 子任务目标

创建第一阶段 Vue 3 前端，实现“客户风险评级与账户查询 POC”的企业后台操作台页面，并对接已实现的 `poc/backend` 后端 API。

## 输入基线

实现前必须阅读：

- `docs/frontend-page-design.md`
- `docs/api-design.md`
- `docs/backend-code-implementation-acceptance.md`
- `poc/backend`

## 输出位置

建议创建：

```text
poc/frontend
```

## 子窗口提示词

```text
你现在是 AI 教程项目的前端代码实现子窗口。请在当前工作区创建第一阶段 Vue 3 前端代码，只实现第一阶段企业后台操作台，不做真实登录、权限、导出、批量导入、删除。

请使用 frontend-design skill 的思路实现：这是银行研发培训用企业后台，不是营销页。界面要克制、专业、清晰，避免泛泛模板感。不要使用大渐变营销风格。

项目背景：
- 后端第一阶段已经实现，代码位于 `poc/backend`。
- 前端需要对接 `docs/api-design.md` 中的 API。
- 技术栈：Vue 3、TypeScript、Vite、Element Plus、ECharts。
- 所有展示数据必须使用脱敏样例字段，保留 `masked` / `sample` 语义。

请先阅读：
- docs/frontend-page-design.md
- docs/api-design.md
- docs/backend-code-implementation-acceptance.md
- poc/backend

实现要求：
1. 在 `poc/frontend` 下创建 Vue 3 + TypeScript + Vite 项目。
2. 使用 Element Plus 作为基础组件库。
3. 使用 ECharts 实现仪表盘风险分布和客户状态统计。
4. 实现页面：仪表盘、客户列表、客户详情、风险重算弹窗、操作日志页。
5. 路由：`/dashboard`、`/customers`、`/customers/:customerNo`、`/operation-logs`，默认跳转 `/dashboard`。
6. 实现 API client，统一处理 `ApiResponse<T>`、错误提示、`X-Request-Id`、`X-Operator`。
7. 严格按 `docs/api-design.md` 调用 API，不新增后端契约。
8. 重算成功展示 `transactionEffect`，重算失败展示 `code`、`message`、`requestId`。
9. 不展示数据库自增 `id`，不展示真实敏感信息。
10. 补充基础构建验证，至少保证 `npm run build` 通过。

验收标准：
- `npm install` 和 `npm run build` 成功，或说明失败原因。
- 页面能完成客户查询、详情查看、风险重算、日志查看主流程。
- API 字段与 `docs/api-design.md` 对齐。
- 视觉是企业后台操作台，不是营销页。
- 不引入真实登录、权限、导出、批量导入、删除。
- 适合录制第 6 课“前端页面和接口联调”。
```

## 主控验收重点

- 是否严格对齐 `docs/frontend-page-design.md` 和 `docs/api-design.md`。
- 是否能构建通过。
- 页面是否能支撑课程录屏主流程。
- 是否有加载、空数据、错误、重算中、重算成功、重算失败状态。
- 是否保持脱敏和安全边界。

