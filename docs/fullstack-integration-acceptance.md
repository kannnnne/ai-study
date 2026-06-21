# 前后端联调主控验收记录

## 验收结论

前后端联调验收通过。第一阶段 POC 已具备课程录屏演示条件，可以支撑第 6 课“前端页面和接口联调”的主流程演示。

## 验收范围

本次验收覆盖：

- 后端：`poc/backend`
- 前端：`poc/frontend`
- 主流程：仪表盘 -> 客户列表 -> 客户详情 -> 风险重算 -> 操作日志追踪
- 错误场景：不存在客户、关闭状态客户风险重算
- 安全检查：脱敏样例数据、无真实敏感信息

## 启动方式

后端默认 profile 指向本机 MySQL：

```text
jdbc:mysql://localhost:3306/ai_study_risk_poc
```

当前机器未启动 MySQL，默认 profile 启动失败属于环境未准备问题。本次浏览器验收实际使用：

```bash
mvn spring-boot:test-run -Dspring-boot.run.profiles=test
```

后端 test profile 使用 H2 内存库 + Flyway 样例数据。

前端启动方式：

```bash
npm run dev -- --host 127.0.0.1
```

Vite `/api` 代理到：

```text
http://localhost:8080
```

验收后本地 8080 / 5173 进程已停止。

## 主控复验结果

后端测试：

```bash
mvn test
```

结果：

```text
Tests run: 9, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

前端构建：

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

## 浏览器主流程验收

子窗口已完成浏览器验收，结果如下：

- `/dashboard` 展示总客户 6、高风险 2、冻结 / 关闭 2、最近日志 5。
- `/customers` 风险等级 HIGH 筛选后 `Total 2`。
- `/customers` 状态 ACTIVE 筛选后 `Total 4`。
- `/customers/CUST100002` 展示基础信息、账户、当前风险、历史风险。
- 风险重算成功后页面展示 `transactionEffect`，三项均为 `true`。
- `/operation-logs` 按 `operationType=RECALCULATE_RISK` 可查到成功和失败日志。

## 错误场景验收

- `/customers/CUST999999` 展示 `客户不存在`、`code: 404001`、`requestId`。
- `CUST100006` 关闭状态客户重算返回并展示 `关闭状态客户不允许触发风险重算`、`code: 409001`、`requestId`。

## 安全脱敏检查

通过：

- 页面和样例数据使用 `SAMPLE-*`、`CUST10000*`、`mobileMasked`、`idNoMasked`、`accountNoMasked`、`clientIpMasked`、`*Sample`。
- 未扫到真实手机号、真实证件号、真实账号、私钥形态、生产地址。
- `application.yml` 中仅有本地 demo MySQL 账号 `risk_poc / risk_poc_demo`。

## 已修复问题

P1 已修复：

- 问题：风险重算成功后父页面刷新会切换到 loading skeleton，导致弹窗卸载，`transactionEffect` 无法留在页面上。
- 修改文件：`poc/frontend/src/views/CustomerDetailView.vue`
- 处理方式：`load(showLoading = true)` 支持静默刷新，重算成功后调用 `load(false)`。
- 影响范围：只影响详情页重算成功后的刷新体验，不改变 API 契约和业务能力。
- 复验结果：前端构建通过，浏览器验收通过。

## 问题清单

P0：无。

P1：无剩余。

P2：

- 默认 profile 依赖本机 MySQL。录屏若不准备 MySQL，建议明确使用 `spring-boot:test-run -Dspring-boot.run.profiles=test`。
- 错误提示展示了 message 文本，但没有字面标签 `message:`；如后续需要更强教学展示，可微调 `ApiErrorAlert` 文案。

## 第 6 课录屏路径

1. 启动后端 test profile 和前端 Vite。
2. 打开 `/dashboard`，讲风险分布、客户状态统计、POC 聚合边界。
3. 进入 `/customers`，演示 HIGH 和 ACTIVE 筛选。
4. 打开 `/customers/CUST100002`，讲客户详情多接口聚合。
5. 点击“风险重算”，展示 `transactionEffect` 三项事务效果。
6. 进入 `/operation-logs`，筛选 `RECALCULATE_RISK`，追踪成功日志。
7. 打开 `/customers/CUST999999` 和对 `CUST100006` 重算，讲统一错误响应和脱敏日志。

## 后续建议

下一步建议进入第 6 课录屏材料准备：

- 录屏脚本。
- 口播稿。
- 操作命令清单。
- 常见问题与失败兜底说明。

也可以先派发测试验收子窗口，补齐 HTTP 文件和完整手工验收清单。

