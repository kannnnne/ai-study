# 前端代码实现主控验收记录

## 验收结论

前端代码实现通过主控验收，可以作为第一阶段 POC 前端基线，并可用于后续前后端联调、测试验收和第 6 课录屏。

## 验收范围

代码位置：

```text
poc/frontend
```

主控检查内容：

- Vue 3 + TypeScript + Vite + Element Plus + ECharts 项目结构。
- 路由是否覆盖 `/dashboard`、`/customers`、`/customers/:customerNo`、`/operation-logs`。
- 页面是否覆盖仪表盘、客户查询、客户详情、风险重算弹窗、操作日志查询。
- API client 是否统一处理 `ApiResponse<T>`、`X-Request-Id`、`X-Operator` 和错误展示。
- 是否严格调用 `docs/api-design.md` 中的 `/api/...` 契约。
- 是否保留 `masked` / `sample` 脱敏字段语义。
- 是否守住第一阶段边界，不引入真实登录、权限、导出、批量导入、删除。
- 构建是否通过。

## 验证结果

已在主控窗口复跑：

```bash
npm run build
```

结果：

```text
vue-tsc --noEmit && vite build
✓ built
```

构建提示：

- Vite chunk size warning：当前 ECharts + Element Plus 打包体积较大，第一阶段 POC 可接受。
- Rollup 对 `@vueuse/core` 的 PURE 注释有清理提示，不影响产物。

## 主控观察

- `vite.config.ts` 已配置 `/api` 代理到 `http://localhost:8080`。
- API client 会生成前端请求号并传递 `X-Request-Id`，同时传递 `X-Operator`。
- 风险重算弹窗展示 `transactionEffect`，能支撑课程讲解“同一事务内写风险记录、回写快照、写操作日志”。
- 页面展示保留 `mobileMasked`、`idNoMasked`、`accountNoMasked`、`riskReasonSample`、`errorMessageSample` 等字段语义。
- 仪表盘基于客户分页数据做 POC 级轻量聚合，符合主控裁决。
- 边界检查未发现真实登录、权限、导出、批量导入、删除等第一阶段外能力。

## 未验证项

- 本次未启动后端 + 前端做浏览器端完整联调。
- 后续需要进行前后端联调验收，验证客户查询、详情、风险重算、日志追踪在浏览器中真实可操作。
- `npm install` 报告的依赖审计提示暂未修复；未执行 `npm audit fix --force`，避免破坏当前教学依赖锁。

## 后续建议

下一步建议派发前后端联调验收子窗口：

- 启动后端 test 或 MySQL profile。
- 启动前端 Vite dev server。
- 在浏览器中验证完整路径：仪表盘 -> 客户列表 -> 客户详情 -> 风险重算 -> 操作日志。
- 记录可用于第 6 课录屏的操作路径和问题清单。

