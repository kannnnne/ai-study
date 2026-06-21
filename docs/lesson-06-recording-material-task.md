# 第 6 课录屏材料子任务

## 子任务目标

把已通过主控验收的前后端联调闭环，转换成可直接录制的视频材料。

课程主题：

```text
第 6 课：前端页面和接口联调
```

目标不是继续开发功能，而是产出讲师录制需要的材料：

- 视频脚本。
- 半口语口播稿。
- 录屏操作步骤。
- 命令清单。
- 提示词和讲解点。
- 失败兜底方案。
- 课后练习。

## 输入基线

必须阅读：

- `docs/lesson-plan.md`
- `docs/frontend-page-design.md`
- `docs/frontend-code-implementation-acceptance.md`
- `docs/fullstack-integration-acceptance.md`
- `docs/api-design.md`
- `poc/backend`
- `poc/frontend`

## 子窗口提示词

```text
你现在是 AI 教程项目的第 6 课录屏材料子窗口。请只负责把已完成的前后端联调闭环整理成可录制视频材料，不写代码，不改项目。

课程背景：
- 这是一套面向银行解决方案研发同学的 AI 编程入门教程。
- 课程主线是用 Claude Code + DeepSeek 从 0 搭建一个最小可运行 Java 微服务 POC。
- 第 6 课主题：前端页面和接口联调。
- 前后端联调已通过主控验收。

请先阅读：
- docs/lesson-plan.md
- docs/frontend-page-design.md
- docs/frontend-code-implementation-acceptance.md
- docs/fullstack-integration-acceptance.md
- docs/api-design.md
- poc/backend
- poc/frontend

输出要求：
1. 视频标题和一句话目标。
2. 本节课适合放在整套课程中的位置说明。
3. 预计时长和章节划分，建议 20 到 30 分钟。
4. 录制前准备清单。
5. 启动命令清单：
   - 后端 test profile 启动命令
   - 前端 Vite 启动命令
   - 构建或测试命令
6. 完整录屏操作步骤：
   - 打开 `/dashboard`
   - 讲风险分布和客户状态统计
   - 进入 `/customers`
   - 演示 HIGH / ACTIVE 筛选
   - 打开 `/customers/CUST100002`
   - 讲客户详情多接口聚合
   - 触发风险重算
   - 展示 `transactionEffect`
   - 进入 `/operation-logs`
   - 筛选 `RECALCULATE_RISK`
   - 演示 `/customers/CUST999999` 和 `CUST100006` 错误场景
7. 半口语口播稿，要求像真实技术负责人给同事培训，不要太书面。
8. 每个演示点要说明：
   - AI 在前面帮我们做了什么
   - 人需要验收什么
   - 这个点在真实项目中的意义
9. 失败兜底方案：
   - 后端 8080 起不来
   - 前端 5173 起不来
   - API 代理失败
   - 风险重算失败
   - 页面空数据
   - MySQL 没有准备
10. 安全和脱敏提醒。
11. 课后练习。
12. 本节课讲师检查清单。
13. 可放到讲义里的精简版文字材料。

风格要求：
- 通俗，但不要幼稚。
- 不夸大 AI。
- 主动强调 AI 输出必须验收。
- 重点体现“前端不改 API，只消费契约；联调不是点点页面，而是验证研发闭环”。
- 明确说明 test profile 是为了课程演示方便，真实项目应准备稳定数据库环境。

最后请给主控窗口一段简短汇报，说明这份材料是否已经可以用于录制第 6 课。
```

## 主控验收重点

- 是否能直接指导录屏。
- 是否包含启动命令和失败兜底。
- 是否覆盖成功主流程和错误场景。
- 是否能体现课程方法论：AI 生成之后必须联调和验收。
- 是否保留银行研发安全和脱敏边界。
- 是否适合 20 到 30 分钟视频。

