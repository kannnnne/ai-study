# POC 项目路线

## 项目定位

课程演示项目建议从 0 开始录制，用 AI 协助搭建一个完整的最小可运行 Java 微服务 POC。这个 POC 不追求业务复杂，而是追求工程链路完整，让学员看到 AI 如何参与需求、设计、开发、测试、验收和文档沉淀。

## 推荐业务场景

建议选择“客户风险评级与账户查询 POC”。

原因：

- 银行解决方案研发同学容易理解。
- 可以覆盖前端、后端、数据库、接口、校验、权限、日志和测试。
- 不需要真实客户数据，容易构造脱敏样例。
- 可以自然引出金额、日期、敏感信息、接口响应码等银行研发常见风险。

## 最小功能范围

### 用户侧功能

- 客户列表查询。
- 客户详情查看。
- 客户风险等级展示。
- 客户账户余额展示。
- 简单筛选：客户号、客户名称、风险等级。

### 管理侧功能

- 新增或修改客户基础信息。
- 触发风险等级重新计算。
- 查看操作日志。

### 后端能力

- REST API。
- 参数校验。
- 统一响应结构。
- 统一异常处理。
- 数据库访问。
- 基础日志。
- 单元测试和接口测试。
- OpenAPI 文档。

### 微服务能力

第一版不建议一上来拆太多服务，避免课程复杂度失控。可以采用两阶段：

第一阶段：

- 单体 Spring Boot 3.x 服务。
- MySQL 或 PostgreSQL。
- 前端单页应用。

第二阶段：

- 拆成 customer-service、risk-service、gateway。
- 引入 Spring Cloud、Nacos 注册配置、OpenFeign 或 RestClient。
- 演示 AI 如何协助单体拆微服务。

## 技术栈建议

### 后端

- JDK 21。
- Spring Boot 3.x。
- Spring Web。
- Spring Validation。
- Spring Data JPA 或 MyBatis Plus，优先选择团队更熟悉的一种。
- Flyway 或 Liquibase，做数据库初始化和版本管理。
- JUnit 5、Mockito、Testcontainers 可选。
- OpenAPI / Swagger。

### 微服务

- Spring Cloud。
- Nacos 服务注册和配置管理。
- Spring Cloud Gateway。
- OpenFeign 或 Spring 6 RestClient。

### 前端

- Vue 3 + TypeScript + Vite，适合多数 Java 团队上手。
- Element Plus 或 Naive UI，偏企业后台风格。
- ECharts，用于风险分布或账户统计图。
- API 类型可以用 OpenAPI 生成，或先手写轻量 client。

### 数据库

- MySQL 8 或 PostgreSQL。
- 本地开发使用 Docker Compose。
- 初始化脚本放在 `db/migration` 或 `docker/init`。

## 推荐目录结构

```text
ai-bank-poc/
  README.md
  CLAUDE.md
  docker-compose.yml
  docs/
    requirements.md
    architecture.md
    api-design.md
    test-plan.md
  backend/
    customer-service/
    risk-service/
    gateway-service/
  frontend/
  scripts/
```

## 课程分阶段录制

### 阶段 1：让 AI 做需求分析

- 给 AI 一个粗略目标。
- 要求 AI 追问问题。
- 产出需求文档、范围边界和验收标准。

### 阶段 2：让 AI 做架构设计

- 选择技术栈。
- 设计模块边界。
- 设计数据库表。
- 设计 API。
- 做架构评审。

### 阶段 3：搭建后端最小服务

- 创建 Spring Boot 项目。
- 连接数据库。
- 实现客户查询。
- 增加统一响应和异常处理。
- 写测试。

### 阶段 4：搭建前端页面

- 用 frontend-design skill 指导页面设计。
- 实现列表、详情、筛选和基础图表。
- 联调后端接口。

### 阶段 5：引入微服务治理

- 拆分服务。
- 接入 Nacos。
- 增加 gateway。
- 演示 AI 辅助迁移和回归测试。

### 阶段 6：系统验收和课程复盘

- AI 自测。
- 人工验收。
- 需求验收。
- 设计验收。
- 架构验收。
- 安全运维验收。

## Skill 和插件使用建议

| 场景 | 推荐能力 | 用法 |
| --- | --- | --- |
| 前端页面设计 | frontend-design skill | 让 AI 先确定界面定位、视觉风格、组件布局，再编码 |
| 银行接口设计 | downstream-integration skill | 用于 DTO、接口字段、报文映射、校验规则和设计文档 |
| GitHub PR/CI | github 插件相关 skill | 后续演示 PR 审查、CI 失败修复、发布流程 |
| 文档整理 | documents skill | 需要 Word 版讲义或正式交付文档时使用 |
| PPT 制作 | presentations skill | 制作领导汇报或课程课件 |
| 表格材料 | spreadsheets skill | 统计课程计划、任务矩阵、验收清单 |
| 图片素材 | imagegen skill | 需要课程封面、示意图、概念图时使用 |

## 课程重点

这个 POC 不是为了证明 AI 一次能写完整系统，而是要展示一套可控的协作方式：人负责目标、边界、评审和验收，AI 负责加速分析、生成、修改和整理。

