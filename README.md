# AI 编程入门教程项目

这是一套面向公司技术同学的 AI 编程入门教程规划。教程以视频为主、文字为辅，核心目标是让银行项目环境下的研发同学能够低门槛搭建 AI 编程工具链，并跟随课程从 0 完成一个最小可运行 Java 微服务 POC。

## 教程定位

- 面向对象：有开发经验，但较少接触 AI 编程、Agent 工具或大模型应用的技术同学。
- 主要工具链：Claude Code + DeepSeek，辅以 Codex、Trae、Qoder 等工具经验对比。
- 课程风格：少讲玄学，多做演示；少堆概念，多给可复制流程。
- 业务背景：结合银行解决方案研发场景，包括需求分析、架构设计、接口梳理、前后端开发、单测生成、文档整理、问题排查等。
- 难度边界：入门到能用，不追求大模型原理深挖和 Agent 框架研发。

## 预期成果

- 一套可录制的视频课程大纲。
- 每节课配套文字讲义、命令清单、操作截图或录屏素材。
- 一个从 0 搭建的完整 POC 案例，帮助同学理解 AI 如何融入真实研发流程。
- 一份面向领导的课程价值说明，体现培训目标、组织收益和讲师能力。
- 一份讲师自学路线，用这次教程反向系统化补齐 AI、LLM、Agent、提示词和工程实践知识。

## 文档结构

- [课程蓝图](./docs/course-blueprint.md)
- [分集大纲](./docs/lesson-plan.md)
- [案例库](./docs/example-library.md)
- [录制与交付流程](./docs/recording-workflow.md)
- [主控窗口工作台](./docs/master-control.md)
- [主控任务看板](./docs/master-task-board.md)
- [POC 项目路线](./docs/poc-project-plan.md)
- [POC 架构设计基线](./docs/poc-architecture.md)
- [后端第一阶段最小实现设计](./docs/backend-implementation-plan.md)
- [后端代码实现子任务](./docs/backend-code-implementation-task.md)
- [后端代码实现主控验收记录](./docs/backend-code-implementation-acceptance.md)
- [前端页面设计子任务](./docs/frontend-page-design-task.md)
- [前端页面设计基线](./docs/frontend-page-design.md)
- [前端代码实现子任务](./docs/frontend-code-implementation-task.md)
- [前端代码实现主控验收记录](./docs/frontend-code-implementation-acceptance.md)
- [前后端联调验收子任务](./docs/fullstack-integration-acceptance-task.md)
- [前后端联调主控验收记录](./docs/fullstack-integration-acceptance.md)
- [AI 研发工作流](./docs/ai-dev-workflow.md)
- [环境安装与配置](./docs/toolchain-setup.md)
- [领导汇报口径](./docs/leadership-brief.md)
- [子窗口提示词模板](./docs/subsession-prompts.md)
- [课程交付物与开展方式](./docs/deliverables-and-launch-plan.md)

## 当前建议路线

第一阶段先做一套“从 0 到 1 用 AI 搭建最小可运行 POC”的课程，每节视频控制在 15 到 30 分钟。课程主线不是零散介绍工具，而是完整演示一个带前端、后端、数据库和基础微服务治理能力的 Java POC 如何通过 AI 协作完成。

本窗口作为主控窗口，负责总体规划、拆分子任务、生成子窗口提示词、验收子任务结果和维护课程主线。具体的前端设计、后端架构、数据库建模、环境安装、讲稿编写等工作可以拆到不同会话中完成。
