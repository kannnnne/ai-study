# 附录: 提示词模板库

> 收录34条精选提示词模板，覆盖需求、设计、编码、测试、Review、排障、项目管理、Agent长任务等8大场景。所有模板均可直接复制使用，基于Java/Spring Boot技术栈。

---

## 快速索引

| 编号 | 名称 | 类别 |
|------|------|------|
| 01 | 需求澄清 | 需求类 |
| 02 | 需求转功能清单 | 需求类 |
| 03 | 需求转用户故事 | 需求类 |
| 04 | 用户故事验收标准生成 | 需求类 |
| 05 | 技术方案生成 | 设计类 |
| 06 | 技术方案评审 | 设计类 |
| 07 | API设计 (RESTful) | 设计类 |
| 08 | 数据库设计 (DDL生成) | 设计类 |
| 09 | 接口Mock数据生成 | 设计类 |
| 10 | Controller生成 | 编码类 |
| 11 | Service生成 | 编码类 |
| 12 | DAO/Repository生成 | 编码类 |
| 13 | DTO/VO生成 | 编码类 |
| 14 | 参数校验补全 | 编码类 |
| 15 | 异常处理补全 | 编码类 |
| 16 | 日志补全 | 编码类 |
| 17 | 权限校验补全 | 编码类 |
| 18 | 单元测试生成 (JUnit 5 + Mockito) | 测试类 |
| 19 | 接口测试用例生成 | 测试类 |
| 20 | Postman Collection生成 | 测试类 |
| 21 | 代码Review（安全检查+性能+规范） | Review类 |
| 22 | 技术方案Review | Review类 |
| 23 | 测试用例Review | Review类 |
| 24 | 异常堆栈分析 | 排障类 |
| 25 | 日志异常模式分析 | 排障类 |
| 26 | SQL慢查询分析 | 排障类 |
| 27 | JVM问题分析 | 排障类 |
| 28 | 会议纪要整理 | 项目管理类 |
| 29 | 项目周报生成 | 项目管理类 |
| 30 | 项目风险识别 | 项目管理类 |
| 31 | 版本发布说明生成 | 项目管理类 |
| 32 | Claude Code 多步骤开发任务 | Agent长任务类 |
| 33 | Claude Code 代码分析任务 | Agent长任务类 |
| 34 | Claude Code 重构任务 | Agent长任务类 |

---

## 一、需求类

### 01 需求澄清

**类别**: 需求

**使用场景**: 收到模糊或一句话需求时，通过结构化提问帮助业务方明确真实意图和边界条件。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 原始需求描述文字，或与业务方的聊天记录摘要。

---

**提示词正文**:

```
你是一位资深业务分析师，擅长通过结构化提问澄清需求。请阅读以下需求描述，然后通过一系列有针对性的问题帮助我明确需求边界。

原始需求:
[粘贴原始需求描述]

请按以下维度逐一提问，每个维度1-2个问题，避免一次性抛出所有问题：

1. 用户与角色：谁会用这个功能？是否有不同的角色和权限需求？
2. 核心流程：用户的主要操作路径是什么？正常流程和异常流程分别是什么？
3. 数据范围：涉及哪些数据？数据从哪来、存哪里、给谁用？
4. 非功能需求：对性能、安全、可用性有什么具体要求？
5. 验收标准：什么情况下这个功能算"做完了"？

每轮只给我2-3个问题，等我回答后再继续下一轮。最后生成一份需求澄清记录。
```

---

**期望输出**:

```
第1轮 - 用户与角色：
Q1: 这个功能的最终使用者是谁？是内部员工、外部客户还是第三方系统？
Q2: 不同角色的操作权限是否有差异？比如普通用户和管理员看到的数据范围是否不同？

（等你回答后继续）
```

---

**注意事项**:

1. 如果需求涉及老系统改造，在输入准备中额外附上现有系统的接口文档或表结构。
2. 与业务方沟通前，建议先将提示词中的"角色"调整为匹配你所在行业的术语（如"柜员/主管"、"医生/护士"等）。
3. 最后生成的需求澄清记录应直接可用于技术方案评审。

---

### 02 需求转功能清单

**类别**: 需求

**使用场景**: 将PRD或需求文档中的描述性语言转化为结构化的功能清单，便于评估工作量和分配任务。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 需求文档内容（PRD、需求规格说明、用户故事列表等）。

---

**提示词正文**:

```
你是一位经验丰富的Java后端技术负责人。请分析以下需求文档，生成一份结构化的功能清单。

需求文档:
[粘贴需求文档全文]

输出要求：
1. 用表格列出所有功能点，表格包含以下列：
   - 功能编号（格式: FEAT-001, FEAT-002...）
   - 功能名称
   - 功能描述（一句话）
   - 优先级（P0-核心/P1-重要/P2-锦上添花）
   - 涉及模块（如: 用户模块、订单模块、支付模块）
   - 预估复杂度（高/中/低）
   - 依赖项（依赖的其他功能编号，无则填"无"）

2. 在表格后，用Mermaid flowchart画出各功能模块之间的依赖关系图。

3. 对每个P0功能，单独写2-3句实现要点。

4. 识别表格中所有"依赖项"为"无"的功能点，将它们列为第一阶段可并行开发的任务。
```

---

**期望输出**:

```
| 功能编号 | 功能名称 | 功能描述 | 优先级 | 涉及模块 | 预估复杂度 | 依赖项 |
|----------|----------|----------|--------|----------|------------|--------|
| FEAT-001 | 用户注册 | 支持手机号+验证码注册 | P0 | 用户模块 | 中 | 无 |
| FEAT-002 | 用户登录 | 支持密码登录和短信验证码登录 | P0 | 用户模块 | 中 | FEAT-001 |
...
```

---

**注意事项**:

1. 如果需求文档很长（超过5000字），建议分成多个部分分别处理，最后汇总。
2. 预估复杂度仅供参考，最终以团队技术评审结果为准。
3. 依赖关系图用Mermaid格式输出，可直接粘贴到支持Mermaid的Markdown编辑器中渲染。

---

### 03 需求转用户故事

**类别**: 需求

**使用场景**: 将PRD中的功能描述转化为敏捷开发中的标准用户故事格式，便于Scrum团队理解和估算。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 功能需求描述，最好包含角色和操作场景。

---

**提示词正文**:

```
你是一位敏捷教练，擅长编写高质量的用户故事。请将以下功能需求转化为标准用户故事。

功能需求:
[粘贴功能需求描述]

输出要求：
1. 每个用户故事使用标准格式：
   作为 <角色>，我希望 <功能>，以便 <价值>。

2. 每个故事附带：
   - 故事编号（US-001, US-002...）
   - 故事点数估算（1/2/3/5/8/13，参考Fibonacci）
   - 相关场景（Gherkin格式Given/When/Then，至少2个场景）
   - 技术备注（如果是后端开发需关注的技术点）

3. 按功能模块分组，每组内的故事按优先级从高到低排列。

4. 如果存在跨模块的史诗级需求，请明确指出并说明拆分为哪些用户故事。
```

---

**期望输出**:

```
## 用户模块

### US-001: 手机号注册
作为 新用户，我希望 使用手机号和验证码完成注册，以便 快速创建账户开始使用系统。

- 故事点数: 5
- 场景1:
  Given 用户未注册
  When 用户输入有效手机号并点击"获取验证码"
  Then 系统发送6位数字验证码到该手机号
- 场景2:
  Given 用户已获取验证码且未过期
  When 用户输入正确的验证码和密码并提交
  Then 系统创建账户并返回JWT Token
...
```

---

**注意事项**:

1. 故事点数估算假设1点约等于半天工作量，可根据团队实际调整。
2. Gherkin场景要覆盖正常流程和核心异常流程（至少各1个）。
3. 技术备注中要特别标注涉及事务、并发、安全等需要重点设计的内容。

---

### 04 用户故事验收标准生成

**类别**: 需求

**使用场景**: 为已有的用户故事补充详细的验收标准（Acceptance Criteria），确保开发与测试对"完成"的理解一致。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 用户故事内容和相关的业务规则描述。

---

**提示词正文**:

```
你是一位质量保证专家。请为以下用户故事生成详细的验收标准。

用户故事:
[粘贴用户故事]

补充业务规则（如有）:
[粘贴业务规则]

输出要求：
1. 验收标准使用Checklist格式，每项以" - [ ] "开头。

2. 覆盖以下维度：
   - 功能验收：正常流程的每步操作和预期结果
   - 异常验收：至少3种异常输入或边界情况
   - 权限验收：不同角色访问时应有不同表现（如适用）
   - 非功能验收：响应时间、并发、数据一致性要求（如适用）

3. 每条验收标准必须可测试、可量化，避免模糊描述如"体验良好"、"响应快"。

4. 最后用一段话总结：什么条件全部满足时，这个用户故事可以被标记为"已完成"。
```

---

**期望输出**:

```
## US-001: 手机号注册 - 验收标准

### 功能验收
- [ ] 输入合法手机号（11位，1开头），点击"获取验证码"，接口返回成功，用户收到短信
- [ ] 60秒内重复点击"获取验证码"，按钮置灰不可点击，显示倒计时
- [ ] 输入正确验证码+符合规则的密码（8-20位，含字母和数字），提交后注册成功，返回token

### 异常验收
- [ ] 输入已被注册的手机号，提示"该手机号已注册，请直接登录"
- [ ] 输入错误验证码，提示"验证码错误，请重新输入"，剩余尝试次数减1
- [ ] 连续5次输入错误验证码，该验证码失效，需重新获取

### 完成定义
当所有功能验收和异常验收项全部通过，且接口响应时间P99 < 500ms时，本用户故事可标记为完成。
```

---

**注意事项**:

1. 验收标准是测试用例编写的直接输入，标准越详细，测试用例编写越高效。
2. 非功能验收标准需要与团队确认实际SLA要求，不要凭感觉写。
3. 如果该用户故事涉及外部系统调用（如短信服务），务必在异常验收中包含外部服务不可用的场景。

---

## 二、设计类

### 05 技术方案生成

**类别**: 设计

**使用场景**: 给定需求和技术约束，自动生成包含系统架构、核心流程、存储设计和接口设计的完整技术方案。

**适用工具**: Claude Code / ChatGPT

**输入准备**: 清晰的功能需求列表、技术栈约束、性能指标要求。

---

**提示词正文**:

```
你是一位Java后端架构师，精通Spring Boot/Spring Cloud微服务架构和MySQL/Redis技术栈。请根据以下输入生成完整的技术方案。

项目背景:
[简要说明项目背景和目标]

功能需求:
[粘贴功能清单或需求描述]

技术约束:
- 语言: Java 17
- 框架: Spring Boot 3.x + Spring Cloud
- 数据库: MySQL 8.0 + Redis 7.x
- 消息队列: RocketMQ（如需要）
- 部署: Kubernetes + Docker

非功能需求:
- 接口响应时间: P99 < 500ms
- 系统可用性: 99.9%
- 并发支持: 峰值QPS 2000

请生成技术方案，包含以下章节：

1. 系统架构概述（附Mermaid C4架构图-系统上下文和容器层）
2. 模块划分与职责说明
3. 核心业务流程（选2-3个核心流程，以序列图说明）
4. 数据库设计思路（ER关系要点，不需要完整DDL）
5. 接口设计概要（核心API列表）
6. 关键设计决策（为什么选A不选B，列举3-5个决策点）
7. 风险与缓解措施（至少3条）
```

---

**期望输出**:

```
## 1. 系统架构概述

### 上下文图
...Mermaid C4 Context图...

### 模块划分

| 模块 | 职责 | 内部组件 | 依赖服务 |
|------|------|----------|----------|
| user-service | 用户注册、登录、认证 | Controller/Service/DAO | Redis(缓存)、SMS(短信) |
| order-service | 订单创建、查询、状态管理 | Controller/Service/DAO | user-service、MQ |

## 5. 关键设计决策

### 决策1：验证码存储选择Redis而非数据库
- 理由：验证码有TTL要求，Redis原生支持过期，避免定时清理任务
- 风险：Redis故障导致无法注册 → 缓解：短信服务兜底降级

...
```

---

**注意事项**:

1. 技术方案应包含足够的架构图，Mermaid格式方便团队协作修改。
2. "关键设计决策"是方案评审中讨论最集中的部分，务必写清楚A/B方案的优劣对比。
3. 如果项目涉及金融交易或敏感数据，需额外增加安全设计和数据脱敏方案。

---

### 06 技术方案评审

**类别**: 设计

**使用场景**: 评审一份已有的技术方案，从架构合理性、安全性、性能、可维护性等维度找出潜在问题。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 完整的技术方案文档。

---

**提示词正文**:

```
你是一位资深技术评审专家，曾主导过多个日均千万级请求的金融系统架构设计。请评审以下技术方案，找出问题并给出改进建议。

技术方案:
[粘贴完整技术方案]

评审维度（每个维度给出0-10分，并说明理由和改进建议）:

1. 架构合理性
   - 模块划分是否合理？边界是否清晰？
   - 服务拆分粒度是否恰当（不过粗也不过细）？
   - 技术选型是否与需求匹配？

2. 数据一致性
   - 分布式事务如何处理？
   - 是否存在数据不一致的风险点？
   - 缓存与数据库的一致性策略是否可靠？

3. 安全性
   - 认证授权机制是否完善？
   - 敏感数据是否做了脱敏和加密？
   - 是否存在常见安全漏洞（SQL注入、XSS、CSRF等）？

4. 性能与扩展性
   - 是否能满足目标QPS？
   - 瓶颈在哪里？如何水平扩展？
   - 缓存策略是否合理？

5. 可维护性与可观测性
   - 日志、指标、链路追踪是否完整？
   - 异常处理是否统一？
   - 部署和回滚方案是否清晰？

总结：给出方案的整体成熟度评级（可开发/需修改后可开发/需重新设计），以及最需要优先解决的Top 3问题。
```

---

**期望输出**:

```
## 评审结果总览

| 维度 | 评分 | 状态 |
|------|------|------|
| 架构合理性 | 7/10 | 需修改 |
| 数据一致性 | 5/10 | 存在问题 |
| 安全性 | 6/10 | 需修改 |
| 性能与扩展性 | 8/10 | 基本通过 |
| 可维护性 | 7/10 | 需修改 |

## Top 3问题

1. 【数据一致性-严重】方案中订单创建和库存扣减未使用分布式事务或最终一致性方案，高并发下必然出现超卖
   - 建议：引入RocketMQ事务消息或Seata AT模式

...
```

---

**注意事项**:

1. 评审时要具体指出方案中的问题位置（章节/段落），不要泛泛而谈。
2. 评分标准要一致：8分以上为基本通过，6-7分需修改后可开发，5分及以下为需重新设计。
3. 对"安全"和"数据一致性"维度的问题，优先级应调到最高。

---

### 07 API设计 (RESTful)

**类别**: 设计

**使用场景**: 根据功能需求生成符合RESTful规范的API接口设计，包含路径、方法、请求参数、响应格式和错误码。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 功能描述、涉及的数据实体、业务规则说明。

---

**提示词正文**:

```
你是一位API设计专家，严格遵循RESTful规范。请根据以下信息设计完整的API接口。

业务描述:
[粘贴业务描述]

涉及实体及属性:
[列出实体和关键字段]

设计约束:
- 使用RESTful风格，资源命名使用复数名词
- 版本前缀: /api/v1
- 统一响应格式: {"code": 200, "message": "success", "data": {...}}
- 分页参数使用 page（从1开始）和 size（默认20，最大100）
- 鉴权方式: Bearer JWT

请为每个接口生成以下内容：
1. 接口名称和用途
2. HTTP方法和完整URL路径
3. 请求参数（Path/Query/Body），标注必填和可选
4. 请求示例（curl命令）
5. 成功响应示例
6. 常见错误码和触发条件（至少3种）

请覆盖标准CRUD操作，以及至少2个非CRUD的业务接口（如状态变更、批量操作、统计查询等）。
```

---

**期望输出**:

```
## 用户管理 API

### 1. 创建用户

- 方法: POST
- 路径: /api/v1/users
- 请求体:
{
  "username": "zhangsan",
  "phone": "13800138000",
  "password": "Abc@1234",
  "email": "zhangsan@example.com"
}
- curl示例:
curl -X POST https://api.example.com/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{"username":"zhangsan","phone":"13800138000","password":"Abc@1234","email":"zhangsan@example.com"}'
- 成功响应 (201):
{"code": 201, "message": "创建成功", "data": {"id": 10001, "username": "zhangsan"}}
- 错误码:
  - 400: 参数校验失败（手机号格式错误/密码强度不足）
  - 409: 用户名或手机号已存在
  - 500: 服务器内部错误

...
```

---

**注意事项**:

1. 接口返回的敏感字段（密码、身份证号等）务必在响应示例中标注"不返回"或给出脱敏后的值。
2. 错误码设计建议使用项目内部的统一错误码规范，不要每次生成不同格式。
3. RESTful接口中的非CRUD操作（如`POST /orders/{id}/cancel`），需要特别注明幂等性要求。

---

### 08 数据库设计 (DDL生成)

**类别**: 设计

**使用场景**: 根据实体描述生成完整的MySQL DDL语句，包含表结构、索引、约束和注释。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 实体字段清单、业务关系描述、预估数据量级。

---

**提示词正文**:

```
你是一位数据库设计专家，擅长MySQL高性能表设计。请为以下业务实体生成完整的DDL语句。

业务实体:
[描述各实体及其属性，如：用户表包含用户名、手机号、密码哈希、邮箱、注册时间等]

数据量级预估:
- 用户表: 100万行
- 订单表: 每天新增1万行，保留3年

数据库版本: MySQL 8.0
字符集: utf8mb4
排序规则: utf8mb4_unicode_ci
存储引擎: InnoDB

输出要求：
1. 每个表生成完整DDL（CREATE TABLE语句），包含：
   - 所有字段及其类型、长度、是否可空、默认值
   - 主键定义
   - 必要的NOT NULL约束
   - 每个字段的COMMENT注释

2. 为每个表设计索引：
   - 主键索引（必选）
   - 唯一索引（根据业务唯一性要求）
   - 普通索引（基于查询场景，每个索引附注释说明其服务的查询SQL模式）
   - 避免冗余索引（如已有联合索引(a,b)，不再单独建(a)索引）

3. 表与表之间标注外键关系（用注释说明，不生成物理外键约束）。

4. 如果有分库分表需求，请说明分片键选择和分片策略。

5. 最后生成E-R关系的Mermaid erDiagram图。
```

---

**期望输出**:

```sql
-- ===== 用户表 =====
CREATE TABLE `t_user` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `phone` VARCHAR(20) NOT NULL COMMENT '手机号',
    `password_hash` VARCHAR(128) NOT NULL COMMENT '密码哈希值(BCrypt)',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1-正常 2-禁用 3-注销',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`) COMMENT '用户名唯一',
    UNIQUE KEY `uk_phone` (`phone`) COMMENT '手机号唯一',
    KEY `idx_status_created` (`status`, `created_at`) COMMENT '按状态和时间范围查询用户列表'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
```

---

**注意事项**:

1. 不生成物理外键是互联网项目的常见实践，由应用层保证数据一致性。如你的项目需要物理外键，请明确告知AI。
2. 索引设计建议附上其服务的SQL查询模式，方便后续优化时判断索引是否仍有价值。
3. 对于超大表（亿级），提醒AI考虑分区策略，并在DDL中体现。

---

### 09 接口Mock数据生成

**类别**: 设计

**使用场景**: 前端需要并行开发时，根据API设计快速生成Mock接口的JSON响应数据。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: API接口文档（请求参数和响应字段定义）。

---

**提示词正文**:

```
你是一位前后端协作专家。请根据以下API设计，生成Mock响应数据，供前端开发使用。

API设计:
[粘贴API定义，包括接口路径、方法、请求参数、响应字段说明]

输出要求：
1. 为每个接口生成3组Mock响应数据：
   - 正常返回：真实且多样化的数据（不要全用"张三"、"123456"这种无意义值）
   - 空数据返回：列表为空时的响应
   - 异常返回：常见的错误响应（参数错误、无权限、资源不存在）

2. 对于列表接口，生成至少5条不同的记录。

3. 数据要求：
   - 中文名使用真实姓名风格（如：王建国、李明华），但必须是虚构的
   - 手机号使用符合格式但明显不合法的号码（如：138****0001格式）
   - ID使用自增风格的数字（10001起）
   - 日期使用2025年内合理分布的日期
   - 金额使用合理但虚构的数值

4. 同时生成一个mock-rules.json文件，描述如何用Mock工具（如Mock.js或MSW）动态生成这些数据，给出规则表达式。
```

---

**期望输出**:

```json
// GET /api/v1/users?page=1&size=5 - 正常返回
{
  "code": 200,
  "message": "success",
  "data": {
    "total": 128,
    "page": 1,
    "size": 5,
    "list": [
      {
        "id": 10001,
        "username": "wangjianguo",
        "phone": "138****1234",
        "email": "wangjianguo@example.com",
        "status": 1,
        "createdAt": "2025-01-15 10:30:00"
      },
      ...
    ]
  }
}
```

---

**注意事项**:

1. Mock数据中的时间字段建议使用相对日期（如"当前时间-3天"），避免因时间过期导致前端判断逻辑异常。
2. 异常返回的错误码必须与后端约定的错误码规范一致。
3. 如果API涉及文件上传/下载，Mock数据中应说明如何模拟文件响应（Content-Type、Content-Disposition等）。

---

## 三、编码类 (Java/Spring Boot)

### 10 Controller生成

**类别**: 编码

**使用场景**: 根据API设计文档快速生成Spring Boot Controller代码，包含完整的参数校验和Swagger注解。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: API接口定义（路径、方法、参数、响应），以及项目工程结构信息。

---

**提示词正文**:

```
你是一位Java Spring Boot后端开发专家。请根据以下API定义生成完整的Controller代码。

API定义:
[粘贴API接口定义]

项目技术栈:
- Spring Boot 3.x
- Java 17
- 使用 @RestController + @RequestMapping
- 参数校验使用 Jakarta Validation (@Valid, @NotBlank等)
- API文档使用 Springdoc OpenAPI (io.swagger.v3.oas.annotations)
- 统一返回: Result<T> 包装类，包含 code/message/data 字段

约束要求：
1. Controller只负责参数接收、校验、调用Service、返回结果，不包含任何业务逻辑。
2. 所有接口方法必须添加：
   - @Operation(summary = "功能说明") 注解
   - @Parameter 注解描述关键参数
   - @ApiResponse 注解描述可能返回的错误码
3. 使用 @Valid 或 @Validated 对请求体进行校验，Controller层不写if判断校验。
4. 分页参数使用自定义 PageRequest 类（包含 page/size/sort），不使用Spring Data的Pageable。
5. 需要鉴权的接口添加自定义 @RequirePermission 注解（不要使用Spring Security注解，保持框架无关性）。
6. 异常直接抛出，统一由全局异常处理器（@RestControllerAdvice）处理。

请同时生成对应的Controller类文件和该类需要的自定义注解定义（如果没有的话）。
```

---

**期望输出**:

```java
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "用户管理", description = "用户CRUD操作接口")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "创建用户")
    @ApiResponse(responseCode = "201", description = "创建成功")
    @ApiResponse(responseCode = "400", description = "参数校验失败")
    @ApiResponse(responseCode = "409", description = "用户名或手机号已存在")
    public Result<UserVO> createUser(@Valid @RequestBody CreateUserRequest request) {
        return Result.success(userService.createUser(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询用户详情")
    @Parameter(name = "id", description = "用户ID", required = true, example = "10001")
    @ApiResponse(responseCode = "404", description = "用户不存在")
    @RequirePermission("user:read")
    public Result<UserVO> getUser(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }
}
```

---

**注意事项**:

1. Controller方法命名建议遵循RESTful规范：getXxx / listXxx / createXxx / updateXxx / deleteXxx。
2. `@RequirePermission`注解如果项目中已有权限框架，替换为项目实际的权限注解。
3. `Result<T>`包装类需要与项目中已有定义一致，如果项目没有则先让AI生成通用Result类。

---

### 11 Service生成

**类别**: 编码

**使用场景**: 输入业务逻辑描述，生成完整的Service实现代码，包含事务处理、异常处理和必要的业务校验。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 业务逻辑描述、涉及的数据表或实体、相关DAO/Repository接口定义。

---

**提示词正文**:

```
你是一位Java Spring Boot后端开发专家。请根据以下业务描述生成完整的Service层代码。

业务描述:
[详细描述业务逻辑，包括正常流程、异常判断、数据校验规则]

已有的DAO接口:
[粘贴DAO/Repository接口方法签名，或描述可用的数据访问方法]

技术栈:
- Spring Boot 3.x + Java 17
- MyBatis-Plus 3.5+（DAO层）
- Spring事务管理

约束要求：
1. Service接口和实现类分离（Interface + @Service实现）。
2. 所有写操作（增删改）使用 @Transactional(rollbackFor = Exception.class)。
3. 业务异常统一使用自定义 BusinessException，包含错误码和消息。
   - 不要在Service中写 try-catch 吞异常，除非是明确需要兜底或转换的场景。
4. 每个方法内部按此顺序组织代码：
   - 参数校验（业务层面的校验，非注解校验）
   - 业务规则校验（查库确认状态、唯一性等）
   - 数据库操作
   - 外部服务调用（如有，放到最后以缩短事务时长）
   - 构建返回对象
5. 禁止在Service中直接操作 HttpServletRequest/HttpServletResponse。
6. 禁止拼接SQL字符串，所有数据库操作通过DAO接口完成。
7. 涉及金额计算的业务，在注释中标注使用BigDecimal，并在代码中体现。
```

---

**期望输出**:

```java
public interface UserService {
    UserVO createUser(CreateUserRequest request);
    UserVO getUserById(Long id);
    PageResult<UserVO> listUsers(UserPageRequest request);
}

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO createUser(CreateUserRequest request) {
        // 1. 业务规则校验
        UserDO existByUsername = userDao.selectByUsername(request.getUsername());
        if (existByUsername != null) {
            throw new BusinessException(ErrorCode.USERNAME_DUPLICATE);
        }
        UserDO existByPhone = userDao.selectByPhone(request.getPhone());
        if (existByPhone != null) {
            throw new BusinessException(ErrorCode.PHONE_DUPLICATE);
        }

        // 2. 数据库操作
        UserDO user = new UserDO();
        user.setUsername(request.getUsername());
        user.setPhone(request.getPhone());
        user.setPasswordHash(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        user.setStatus(UserStatusEnum.ACTIVE.getCode());
        userDao.insert(user);

        // 3. 构建返回对象
        return UserConvertor.toVO(user);
    }
}
```

---

**注意事项**:

1. 业务异常码（ErrorCode）建议使用枚举统一管理，让AI生成时一并给出枚举定义。
2. 如果项目使用MapStruct做对象转换，在提示词中声明，让AI生成Convertor时使用MapStruct而非手动set。
3. 读操作（查询）不需要加`@Transactional`，可以在提示词中明确，让AI只在写操作上加事务。

---

### 12 DAO/Repository生成

**类别**: 编码

**使用场景**: 根据数据库表结构生成MyBatis-Plus或JPA的DAO/Repository层代码。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 数据库DDL语句（CREATE TABLE），或完整的表字段定义。

---

**提示词正文**:

```
你是一位Java数据访问层开发专家，精通MyBatis-Plus和高性能SQL编写。请根据以下表结构生成完整的DAO层代码。

表DDL:
[粘贴CREATE TABLE语句]

ORM框架: MyBatis-Plus 3.5+
Java 17

输出要求：
1. 为每个表生成：
   - 实体类（DO, extends BaseEntity 如果项目有公共基类）
   - Mapper接口（extends BaseMapper<T>）
   - 自定义XML映射文件（仅针对非MyBatis-Plus内置方法的自定义SQL）

2. 实体类要求：
   - 使用 @TableName 指定表名
   - 使用 @TableId(type = IdType.AUTO) 标注主键策略
   - 使用 @TableField 映射下划线字段到驼峰属性，fill字段使用FieldFill策略
   - 日期字段使用 LocalDateTime 类型
   - 枚举字段使用自定义 TypeHandler 映射

3. 为以下常见查询场景生成自定义SQL（放在XML中）：
   - 按多个条件动态查询（使用MyBatis动态SQL的<where>/<if>标签）
   - 批量插入（<foreach>标签）
   - 带锁查询（SELECT ... FOR UPDATE，用于悲观锁场景）
   - 按时间范围的分页查询

4. 每个Mapper XML的namespace正确对应Mapper接口全限定名。
5. 公共BaseEntity（如果有）包含 id, createTime, updateTime, isDeleted 字段。
```

---

**期望输出**:

```java
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_user")
public class UserDO extends BaseEntity {

    @TableField("username")
    private String username;

    @TableField("phone")
    private String phone;

    @TableField("password_hash")
    private String passwordHash;

    @TableField("email")
    private String email;

    @TableField("status")
    private Integer status;
}

@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

    List<UserDO> selectByCondition(@Param("query") UserQuery query);

    int batchInsert(@Param("list") List<UserDO> list);

    UserDO selectByIdForUpdate(@Param("id") Long id);
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
  "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.example.dao.mapper.UserMapper">

    <select id="selectByCondition" resultType="com.example.dao.entity.UserDO">
        SELECT * FROM t_user
        <where>
            <if test="query.username != null and query.username != ''">
                AND username LIKE CONCAT('%', #{query.username}, '%')
            </if>
            <if test="query.status != null">
                AND status = #{query.status}
            </if>
            <if test="query.startTime != null">
                AND created_at >= #{query.startTime}
            </if>
            <if test="query.endTime != null">
                AND created_at &lt;= #{query.endTime}
            </if>
        </where>
        ORDER BY created_at DESC
    </select>

    <insert id="batchInsert">
        INSERT INTO t_user (username, phone, password_hash, email, status, created_at, updated_at)
        VALUES
        <foreach collection="list" item="item" separator=",">
            (#{item.username}, #{item.phone}, #{item.passwordHash},
             #{item.email}, #{item.status}, NOW(), NOW())
        </foreach>
    </insert>

    <select id="selectByIdForUpdate" resultType="com.example.dao.entity.UserDO">
        SELECT * FROM t_user WHERE id = #{id} FOR UPDATE
    </select>

</mapper>
```

---

**注意事项**:

1. MyBatis-Plus的BaseMapper已经内置了常用CRUD方法（insert/updateById/selectById等），不需要在XML中重复定义。
2. 逻辑删除字段（isDeleted）如果在BaseEntity中定义了，建议使用MyBatis-Plus的@TableLogic注解。
3. FOR UPDATE查询要注释清楚使用场景和风险（长事务导致锁等待），防止被滥用。

---

### 13 DTO/VO生成

**类别**: 编码

**使用场景**: 根据API接口的请求参数和响应字段定义，生成对应的DTO/VO类，以及对象转换器。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: API接口定义（请求体和响应体字段列表），或Controller中的方法签名和注释。

---

**提示词正文**:

```
你是一位Java代码生成专家。请根据以下API定义生成完整的DTO/VO类和转换器。

API定义:
[粘贴API接口列表，包含请求参数字段和响应字段]

技术栈:
- Java 17
- 使用 Lombok (@Data, @Builder, @NoArgsConstructor, @AllArgsConstructor)
- 使用 Jakarta Validation 进行参数校验注解
- 转换器使用 MapStruct

约束要求：
1. 类命名规范：
   - 请求体: XxxRequest（如CreateUserRequest、UserPageRequest）
   - 响应体: XxxVO（如UserVO、UserListVO）
   - 内部DTO: XxxDTO（用于Service层之间的数据传递）

2. 请求类字段必须添加校验注解：
   - 字符串字段: @NotBlank (非空字符串) 或 @NotNull (可为空串)
   - 整数/长整型: @NotNull + @Min/@Max（如有范围要求）
   - 邮箱: @Email
   - 手机号: @Pattern(regexp = "^1[3-9]\\d{9}$")
   - 日期: 使用 @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") 配合LocalDateTime

3. 校验注解必须配置 message 属性，使用中文错误提示。

4. 响应VO类中，敏感字段（密码、身份证等）不包含在场。

5. 生成 MapStruct 转换器接口：
   - 命名: XxxConvertor
   - 使用 @Mapper(componentModel = "spring") 注解
   - 包含 DO -> VO 和 Request -> DO 的转换方法

6. 禁止在Request/VO中引入Spring MVC相关类（如HttpServletRequest），保持POJO纯粹。
```

---

**期望输出**:

```java
@Data
public class CreateUserRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度需在3-50个字符之间")
    private String username;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 20, message = "密码长度需在8-20个字符之间")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d).+$", message = "密码必须包含字母和数字")
    private String password;

    @Email(message = "邮箱格式不正确")
    private String email;
}

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
    private Long id;
    private String username;
    private String phone;
    private String email;
    private Integer status;
    private LocalDateTime createdAt;
}

@Mapper(componentModel = "spring")
public interface UserConvertor {
    UserVO toVO(UserDO user);
    List<UserVO> toVOList(List<UserDO> users);
    UserDO toDO(CreateUserRequest request);
}
```

---

**注意事项**:

1. MapStruct的`componentModel = "spring"`确保转换器可以被`@Autowired`注入，与Spring DI配合。
2. Request中的校验message要用中文，因为最终会返回给前端展示给用户。
3. 如果项目不使用MapStruct，可以用BeanUtils或手动转换；但建议在提示词中保留MapStruct，它编译期生成代码，性能优于反射。

---

### 14 参数校验补全

**类别**: 编码

**使用场景**: 给已有的Request/DTO类补全Jakarta Validation参数校验注解，并定义校验分组。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 现有的Request类代码，以及字段的业务校验规则。

---

**提示词正文**:

```
你是一位Java代码审查和增强专家。请为以下Java Request类补全参数校验注解。

现有代码:
[粘贴Request类代码]

补充的业务校验规则:
[描述每个字段的校验规则，如：手机号11位/状态只能1或2/金额>0]

校验框架: Jakarta Validation (jakarta.validation.constraints)

约束要求：
1. 为每个字段添加合适的校验注解，不允许任何应该校验的字段裸着。
2. 使用 groups 区分创建和更新场景：
   - CreateGroup: 所有必填字段不能为空
   - UpdateGroup: 只校验显式传入的字段（允许部分更新）
   - 主键ID字段使用 @NotNull(groups = UpdateGroup.class)
3. 自定义校验场景请给出校验注解的完整定义（@Constraint + Validator实现类）。
4. 在类级别添加至少一个自定义校验（如：开始时间必须早于结束时间，使用@ValidTimeRange注解）。
5. 校验注解的message统一使用中文，并保持风格一致（用"xxx不能为空"而非"xxx is required"）。
```

---

**期望输出**:

```java
@Data
public class CreateOrderRequest {

    @NotNull(message = "用户ID不能为空", groups = {CreateGroup.class})
    private Long userId;

    @NotBlank(message = "收货地址不能为空", groups = {CreateGroup.class})
    @Size(max = 200, message = "收货地址不能超过200字")
    private String address;

    @NotEmpty(message = "订单明细不能为空", groups = {CreateGroup.class})
    @Size(min = 1, max = 50, message = "订单明细数量需在1-50之间")
    @Valid  // 级联校验
    private List<OrderItemRequest> items;

    @NotNull(message = "订单总金额不能为空", groups = {CreateGroup.class})
    @DecimalMin(value = "0.01", message = "订单总金额必须大于0")
    private BigDecimal totalAmount;

    @Size(max = 500, message = "备注不能超过500字")
    private String remark;
}

@Data
public class OrderItemRequest {

    @NotNull(message = "商品ID不能为空", groups = {CreateGroup.class})
    private Long productId;

    @NotNull(message = "购买数量不能为空", groups = {CreateGroup.class})
    @Min(value = 1, message = "购买数量最少为1")
    @Max(value = 999, message = "购买数量最多为999")
    private Integer quantity;
}
```

---

**注意事项**:

1. 嵌套对象（如`List<OrderItemRequest>`）必须加`@Valid`才能触发级联校验，这是常见的遗漏点。
2. 自定义校验注解需要同步给出ConstraintValidator实现类，否则编译不通过。
3. groups机制需要Controller层的`@Validated(CreateGroup.class)`配合使用，务必在提示词中提醒或让AI同步生成Controller注解。

---

### 15 异常处理补全

**类别**: 编码

**使用场景**: 为Spring Boot项目建立或补全全局异常处理机制，包括自定义异常类、错误码枚举和全局异常处理器。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 项目中已有的异常处理代码，或项目的业务模块列表（用于生成错误码）。

---

**提示词正文**:

```
你是一位Java异常处理设计专家。请为Spring Boot项目生成完整的全局异常处理方案。

项目信息:
- Spring Boot 3.x
- 模块列表: [列出业务模块，如用户模块、订单模块、支付模块]

现有代码（如有）:
[粘贴现有异常处理相关代码]

约束要求：
1. 生成以下文件：

   a) 错误码枚举 ErrorCode（按模块分组）:
   - 格式: 模块前缀(2位) + 错误序号(3位)
   - 如用户模块: USER_001("用户名已存在"), USER_002("用户不存在")
   - 如订单模块: ORDER_001("订单不存在"), ORDER_002("订单状态不允许此操作")

   b) 业务异常类 BusinessException:
   - 包含 ErrorCode 和可选的detail字段（用于附加调试信息）
   - 构造函数: BusinessException(ErrorCode) / BusinessException(ErrorCode, String detail)

   c) 全局异常处理器 GlobalExceptionHandler (@RestControllerAdvice):
   - 处理 BusinessException → 返回对应的错误码和消息
   - 处理 MethodArgumentNotValidException → 返回参数校验失败详情（列出所有字段错误）
   - 处理 BindException → 同上
   - 处理 HttpMessageNotReadableException → 返回"请求体格式错误"
   - 处理 AccessDeniedException → 返回403
   - 处理 Exception → 兜底处理，返回500 + 通用错误消息，同时在日志中记录完整堆栈
   - 每个处理方法中使用 log.error/info 记录日志

2. 敏感信息控制：
   - 生产环境（spring.profiles.active=prod）下，500错误不返回detail和堆栈信息
   - 开发环境下面向开发者返回detail

3. 统一返回 Result<T> 包装类：
   - code: Integer (错误码编号)
   - message: String (错误信息)
   - data: T (数据)
   - 静态工厂方法: Result.success(T data), Result.fail(ErrorCode errorCode)
```

---

**期望输出**:

```java
public enum ErrorCode {

    // 用户模块
    USERNAME_DUPLICATE(10001, "用户名已存在"),
    USER_NOT_FOUND(10002, "用户不存在"),
    PHONE_DUPLICATE(10003, "手机号已被注册"),
    PASSWORD_ERROR(10004, "密码错误"),

    // 订单模块
    ORDER_NOT_FOUND(20001, "订单不存在"),
    ORDER_STATUS_ERROR(20002, "订单状态不允许此操作"),
    ORDER_AMOUNT_MISMATCH(20003, "订单金额不一致"),

    // 通用
    PARAM_ERROR(90001, "参数校验失败"),
    SYSTEM_ERROR(99999, "系统繁忙，请稍后重试");

    private final Integer code;
    private final String message;
}

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.info("业务异常: code={}, message={}", e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ":" + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.info("参数校验失败: {}", msg);
        return Result.fail(ErrorCode.PARAM_ERROR.getCode(), msg);
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return Result.fail(ErrorCode.SYSTEM_ERROR.getCode(), ErrorCode.SYSTEM_ERROR.getMessage());
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return new Result<>(code, message, null);
    }
}
```

---

**注意事项**:

1. 错误码设计中，模块前缀建议用数字而非字母，方便排序和检索。10000-19999给用户模块，20000-29999给订单模块，以此类推。
2. `MethodArgumentNotValidException`只在使用`@Valid`校验请求体时抛出；如果参数来自URL Query String，校验失败抛出的是`BindException`，两个都要处理。
3. 全局异常处理器必须在Spring Boot能扫描到的包路径下，否则不生效。

---

### 16 日志补全

**类别**: 编码

**使用场景**: 为已有Service/Controller代码补全关键日志，建立运维期可观测性。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 需要补全日志的Java代码文件。

---

**提示词正文**:

```
你是一位Java可观测性专家。请为以下代码补全日志，使其满足生产环境排障需要。

现有代码:
[粘贴Java类代码]

技术栈:
- 日志框架: SLF4J + Logback
- 使用 Lombok @Slf4j 注解

日志补全原则：
1. 每个public方法入口打印一条INFO日志，包含方法名和关键参数（敏感参数需脱敏处理）。
2. 每个public方法出口（正常返回时）打印一条INFO日志，包含方法名和执行耗时（毫秒）。
3. 每个调用外部服务（HTTP/RPC/MQ/DB超时查询）的地方，前后各一条INFO日志，记录调用耗时。
4. 每个关键业务判断分支，记录DEBUG日志说明进入哪个分支。
5. 每个异常捕获点，打印ERROR日志，包含完整的入参信息（便于复现）和异常堆栈。
6. 涉及金额、状态变更等关键操作，打印INFO日志记录变更前后的值。

脱敏规则：
- 手机号: 保留前3后4，中间4位用****替换（如138****1234）
- 身份证: 保留前3后4
- 密码/Token: 完全不输出
- 银行卡号: 保留后4位

请直接在原代码中插入日志语句，保持原有逻辑不变。代码注释中标注你添加的每行日志的目的。
```

---

**期望输出**:

```java
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderDao orderDao;

    public OrderVO createOrder(CreateOrderRequest request) {
        long start = System.currentTimeMillis();
        // 日志目的：记录创建订单入口，手机号需脱敏
        log.info("创建订单开始, userId={}, phone={}",
                request.getUserId(), maskPhone(request.getPhone()));

        // 查询商品信息 - 日志目的：记录外部调用
        long dbStart = System.currentTimeMillis();
        List<ProductDO> products = productDao.selectByIds(request.getProductIds());
        log.info("查询商品信息完成, productIds={}, count={}, cost={}ms",
                request.getProductIds(), products.size(), System.currentTimeMillis() - dbStart);

        if (products.isEmpty()) {
            // 日志目的：记录业务异常分支
            log.info("商品不存在, productIds={}", request.getProductIds());
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        OrderDO order = buildOrder(request, products);
        orderDao.insert(order);
        // 日志目的：记录关键数据变更前后的状态
        log.info("订单创建成功, orderId={}, amount={}, status=CREATED", order.getId(), order.getAmount());

        // 日志目的：记录方法出口和耗时
        log.info("创建订单完成, orderId={}, cost={}ms", order.getId(), System.currentTimeMillis() - start);
        return OrderConvertor.toVO(order);
    }

    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 7) return "***";
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}
```

---

**注意事项**:

1. 日志量不是越多越好，INFO级别应聚焦于"排障时需要的关键信息"。DEBUG级别可以放更多细节，生产环境默认关闭。
2. 外部调用日志的耗时记录是建立SLA监控的基础，建议后续将这些耗时指标上报到监控系统（如Prometheus）。
3. 敏感信息脱敏函数应放在统一的工具类中，避免每个Service各自实现一套。

---

### 17 权限校验补全

**类别**: 编码

**使用场景**: 为Controller接口添加基于RBAC或自定义注解的权限校验逻辑。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 需要添加权限的Controller代码，以及项目的角色-权限矩阵定义。

---

**提示词正文**:

```
你是一位Java安全开发专家。请为以下Controller接口补全权限校验机制。

现有Controller代码:
[粘贴Controller代码]

角色与权限矩阵:
- 管理员(ADMIN): 拥有全部权限
- 运营(OPERATOR): 可查看和编辑，不可删除
- 普通用户(USER): 仅可查看自己的数据

安全设计要求：
1. 使用自定义注解 @RequirePermission(value = "权限标识") 标注每个接口所需的权限。
2. 生成完整的权限定义常量类 PermissionConstants，按模块列出所有权限标识。
3. 生成权限校验AOP切面 PermissionAspect：
   - 拦截 @RequirePermission 注解
   - 从SecurityContext获取当前用户角色和权限列表
   - 判断是否拥有所需权限，无权限抛出 AccessDeniedException
   - 支持"或"逻辑: @RequirePermission(value = {"user:create", "user:admin"}) 表示满足任一即可
4. 生成数据权限过滤逻辑：
   - 对于"仅可查看自己的数据"这种需求，在Service层通过AOP实现 @DataScope 注解
   - @DataScope(userField = "userId") 自动在查询条件中追加 userId = 当前用户ID

5. 同时生成一个 SecurityContext 工具类：
   - 从ThreadLocal（真正的请求上下文在拦截器中设置）获取当前用户信息
   - 提供 getCurrentUserId(), getCurrentRoles(), hasPermission(String perm) 方法

安全约束：
- 权限判断只能使用AOP，不能在Controller或Service中手写if判断
- 禁止通过"字符串判断角色名"的方式做权限控制（如 if(role.equals("admin")) ）
- 权限标识使用 module:action 格式（如 user:create, order:delete）
```

---

**期望输出**:

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequirePermission {
    String[] value();  // 满足任一即可
}

@Aspect
@Component
@RequiredArgsConstructor
public class PermissionAspect {

    @Around("@annotation(requirePermission)")
    public Object checkPermission(ProceedingJoinPoint pjp, RequirePermission requirePermission) throws Throwable {
        Set<String> userPermissions = SecurityContext.getCurrentPermissions();
        String[] requiredPerms = requirePermission.value();

        boolean hasAny = Arrays.stream(requiredPerms).anyMatch(userPermissions::contains);
        if (!hasAny) {
            throw new BusinessException(ErrorCode.ACCESS_DENIED);
        }

        return pjp.proceed();
    }
}

public final class PermissionConstants {
    public static final String USER_CREATE = "user:create";
    public static final String USER_UPDATE = "user:update";
    public static final String USER_DELETE = "user:delete";
    public static final String USER_READ = "user:read";

    public static final String ORDER_CREATE = "order:create";
    public static final String ORDER_DELETE = "order:delete";
}
```

---

**注意事项**:

1. 权限AOP的切入点必须在Controller方法上，因为AOP执行顺序是Spring代理决定的，需确保权限AOP在事务AOP之前执行。
2. SecurityContext中的ThreadLocal在请求结束后必须清理，否则在Tomcat线程池复用场景下会导致数据串了。让AI同时生成清理拦截器。
3. 如果需要更细粒度的数据权限（如"只能看自己部门的数据"），推荐使用MyBatis-Plus的插件机制在SQL层拦截，而不是在Service层逐条判断。

---

## 四、测试类

### 18 单元测试生成 (JUnit 5 + Mockito)

**类别**: 测试

**使用场景**: 为Service层方法生成完整的单元测试，使用Mockito模拟外部依赖，覆盖正常和异常场景。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 待测试的Service类代码和其依赖的DAO/外部服务接口。

---

**提示词正文**:

```
你是一位Java单元测试专家，精通JUnit 5和Mockito。请为以下Service方法生成完整的单元测试。

待测试代码:
[粘贴Service类完整代码]

测试框架:
- JUnit 5 (org.junit.jupiter)
- Mockito 5 (org.mockito)
- AssertJ (org.assertj.core.api.Assertions) - 用于流畅断言

约束要求：
1. 测试类命名: 被测类名 + Test (如 UserServiceImplTest)
2. 使用 @ExtendWith(MockitoExtension.class)
3. 被测对象用 @InjectMocks，依赖用 @Mock
4. 测试方法命名: should_预期行为_when_条件()
5. 每个测试方法使用 Given-When-Then 结构，并在代码中用注释标注

6. 覆盖场景（每个方法至少包括）：
   - 正常场景 (Happy Path): 正常输入，验证输出是否符合预期
   - 异常场景1: 依赖抛出异常时，验证是否按预期处理
   - 异常场景2: 输入不满足业务规则时，验证是否抛出正确的BusinessException
   - 边界场景: 空列表、最大值、最小值等

7. Mock规则：
   - 不Mock被测Service本身的方法调用
   - Mock所有DAO和外部服务依赖
   - 使用 ArgumentCaptor 捕获传给DAO的参数进行验证
   - 使用 verify(mock, times(n)) 验证方法调用次数

8. 断言要求：
   - 对返回值做完整字段断言，不允许只断言 notNull
   - 对异常使用 assertThatThrownBy 断言异常类型和消息
   - 每个测试方法不超过30行

9. 不依赖Spring容器（纯单元测试，不加载ApplicationContext），不连接真实数据库。
```

---

**期望输出**:

```java
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("创建用户 - 正常场景")
    void should_createUserSuccessfully_when_validRequest() {
        // Given
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testuser");
        request.setPhone("13800138000");
        request.setPassword("Abc@1234");

        when(userDao.selectByUsername("testuser")).thenReturn(null);
        when(userDao.selectByPhone("13800138000")).thenReturn(null);

        // When
        UserVO result = userService.createUser(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.getPhone()).isEqualTo("13800138000");

        ArgumentCaptor<UserDO> captor = ArgumentCaptor.forClass(UserDO.class);
        verify(userDao, times(1)).insert(captor.capture());
        assertThat(captor.getValue().getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("创建用户 - 用户名已存在时抛异常")
    void should_throwException_when_usernameDuplicate() {
        // Given
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("existinguser");
        request.setPhone("13800138000");

        when(userDao.selectByUsername("existinguser")).thenReturn(new UserDO());

        // When & Then
        assertThatThrownBy(() -> userService.createUser(request))
                .isInstanceOf(BusinessException.class)
                .extracting("code")
                .isEqualTo(ErrorCode.USERNAME_DUPLICATE.getCode());

        verify(userDao, never()).insert(any());
    }
}
```

---

**注意事项**:

1. 单元测试不依赖Spring容器意味着启动快（几百个测试秒级完成），这是区分"单元测试"和"集成测试"的核心。
2. `assertThatThrownBy` 是 AssertJ 的断言方法，比 JUnit 原生的 `assertThrows` 更流畅，建议统一使用。
3. 禁止在单元测试中使用 `Thread.sleep()`、`@DirtiesContext` 等影响测试速度的做法。

---

### 19 接口测试用例生成

**类别**: 测试

**使用场景**: 根据API设计生成结构化的接口测试用例文档，覆盖功能、异常、边界、权限等方面。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: API接口定义（路径、参数、响应格式、错误码）。

---

**提示词正文**:

```
你是一位API测试专家。请根据以下API接口定义生成完整的接口测试用例。

API定义:
[粘贴API接口文档]

测试用例要求：
1. 使用表格格式，包含以下列：
   - 用例编号 (TC-001, TC-002...)
   - 用例名称
   - 前置条件
   - 测试步骤
   - 请求参数（具体值，非描述）
   - 预期响应码 (HTTP状态码)
   - 预期响应体（关键字段和值）
   - 优先级 (P0/P1/P2)

2. 每个接口覆盖以下测试场景：
   - 功能测试（至少2个正常场景）
   - 参数校验测试（必填参数缺失、格式错误、边界值）
   - 业务逻辑测试（至少2个违背业务规则的场景）
   - 权限测试（不同角色访问、无权限访问）
   - 并发测试思路（说明如何用工具验证并发问题，不要求写具体脚本）

3. 对写操作（POST/PUT/DELETE），额外包含：
   - 幂等性测试（重复提交是否安全）

4. 测试数据必须具体，写清楚每个参数填入什么值。例如：
   - 正确：phone=13800138001
   - 错误：phone=正确的手机号
```

---

**期望输出**:

```
## POST /api/v1/users - 创建用户

| 用例编号 | 用例名称 | 前置条件 | 请求参数 | 预期响应码 | 预期响应体 |
|----------|----------|----------|----------|------------|------------|
| TC-001 | 正常创建用户 | 手机号13800138001未注册 | POST /api/v1/users body:{"username":"test01","phone":"13800138001","password":"Abc@1234","email":"test@test.com"} | 201 | code=201,data.id不为空 |
| TC-002 | 用户名为空 | 无 | Body中去掉username字段 | 400 | code=90001,message包含"用户名不能为空" |
| TC-003 | 手机号格式错误 | 无 | phone="12345" | 400 | code=90001,message包含"手机号格式不正确" |
| TC-004 | 手机号已注册 | 数据库中存在phone=13800138001 | Body中phone="13800138001" | 409 | code=10003,message="手机号已被注册" |
| TC-005 | 密码不含数字 | 无 | password="abcdefgh" | 400 | code=90001,message包含"密码必须包含字母和数字" |
| TC-006 | 无权限访问 | 发起请求时不带Authorization头 | 正常请求体 | 401 | - |
| TC-007 | 普通用户无创建用户权限 | 使用普通用户Token | 正常请求体 | 403 | code=90003 |

### 并发测试思路
- 使用JMeter设置10个线程并发请求，所有线程使用相同的手机号
- 预期：只有1个请求成功（返回201），其余9个返回409（手机号已注册）
- 验证点：数据库中该手机号只对应一条用户记录
```

---

**注意事项**:

1. 接口测试用例和单元测试用例是不同的：接口测试关注HTTP层的输入输出，单元测试关注代码逻辑。两者应互补而非替代。
2. 并发测试建议用JMeter或wrk等专业工具，不建议在JUnit中模拟并发，结果不可靠。
3. P0用例应在CI流水线中每次提交自动执行，P1用例可在每日构建中执行。

---

### 20 Postman Collection生成

**类别**: 测试

**使用场景**: 根据API设计生成可直接导入Postman的Collection JSON文件，方便手动测试和团队共享。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 完整的API接口列表（路径、方法、Header、请求Body、预期响应）。

---

**提示词正文**:

```
你是一位API调试工具专家。请根据以下API定义生成一个完整的Postman Collection JSON文件。

API定义:
[粘贴所有API接口定义]

生成要求：
1. 生成标准Postman Collection v2.1格式的JSON文件。

2. Collection结构：
   - name: 项目名称API
   - description: API使用说明（中文）
   - 按模块建Folder（如用户管理、订单管理）
   - 每个Folder下包含该模块的所有接口

3. 每个请求包含：
   - name: 接口名称（中文）
   - method: HTTP方法
   - url: 使用Postman变量 {{baseUrl}} + 路径
   - headers: Content-Type, Authorization（使用{{token}}变量）
   - body: 完整的JSON示例（raw模式）
   - 至少一个Example响应（200成功示例）

4. 创建Collection级别的变量：
   - baseUrl: http://localhost:8080
   - token: <登录后获取的JWT Token>

5. 在Collection根级添加Pre-request Script（可选）:
   - 如果所有接口都需要token，添加自动获取token并设置的脚本

6. 在Collection根级添加Tests脚本（可选）:
   - 自动解析响应并验证code字段是否等于200

请输出完整的、可被Postman直接导入的JSON文件内容。
```

---

**期望输出**:

```json
{
  "info": {
    "name": "电商系统 API",
    "description": "## 电商系统API接口集合\n\n模块：用户管理、订单管理\n\n使用前请先在环境变量中设置 baseUrl 和 token",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "variable": [
    {"key": "baseUrl", "value": "http://localhost:8080"},
    {"key": "token", "value": ""}
  ],
  "item": [
    {
      "name": "用户管理",
      "item": [
        {
          "name": "创建用户",
          "request": {
            "method": "POST",
            "header": [
              {"key": "Content-Type", "value": "application/json"},
              {"key": "Authorization", "value": "Bearer {{token}}"}
            ],
            "url": {
              "raw": "{{baseUrl}}/api/v1/users",
              "host": ["{{baseUrl}}"],
              "path": ["api", "v1", "users"]
            },
            "body": {
              "mode": "raw",
              "raw": "{\n  \"username\": \"testuser\",\n  \"phone\": \"13800138001\",\n  \"password\": \"Abc@1234\",\n  \"email\": \"test@example.com\"\n}"
            }
          },
          "response": [
            {
              "name": "创建成功",
              "status": "Created",
              "code": 201,
              "body": "{\"code\":201,\"message\":\"创建成功\",\"data\":{\"id\":10001,\"username\":\"testuser\"}}"
            }
          ]
        }
      ]
    }
  ]
}
```

---

**注意事项**:

1. Postman Collection JSON中不要包含真实的生产环境地址和Token，使用变量替代。
2. Collection较大时（超过50个接口），建议分模块导出为多个Collection文件，方便管理。
3. 如果项目使用OpenAPI/Swagger文档，可以直接用Swagger URL导入Postman，不需要手写Collection。

---

## 五、Review类

### 21 代码Review（安全检查+性能+规范）

**类别**: Review

**使用场景**: 对一段Java代码进行全面审查，从安全、性能、代码规范、可维护性等多维度给出改进建议。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 需要Review的Java代码和相关业务上下文说明。

---

**提示词正文**:

```
你是一位资深Java代码审查专家，曾在阿里巴巴（P9级）担任技术评审委员会成员。请从以下维度审查这段代码。

代码:
[粘贴Java代码]

业务上下文（如有）:
[简要描述这段代码在整体系统中的角色]

审查维度：

一、安全检查（Weight: 40%）
- SQL注入：是否使用了参数化查询？禁止任何字符串拼接SQL
- XSS：返回给前端的用户输入是否做了转义
- 敏感信息：日志中是否打印了密码、Token、身份证号等
- 权限：是否有越权风险？是否校验了数据归属
- 输入校验：是否对所有外部输入做了校验（不仅是前端校验）
- 文件操作：路径是否可被遍历攻击

二、性能（Weight: 30%）
- 数据库：是否存在N+1查询、全表扫描、缺失索引
- 缓存：高频查询是否加上缓存
- 并发：分布式锁、本地锁使用是否正确，是否存在死锁风险
- 内存：是否有大对象、内存泄漏隐患

三、代码规范（Weight: 20%）
- 命名：类名、方法名、变量名是否表意
- 注释：关键业务逻辑是否有注释，复杂算法是否有说明
- 异常：是否合理处理异常，是否有吞异常的情况
- 事务：事务边界是否合理，写操作是否有事务保护

四、可维护性（Weight: 10%）
- 方法是否过长（超过80行建议拆分）
- 是否有违反单一职责的类
- 是否过度设计（不必要的抽象层、工厂模式等）

输出格式：
1. 总体评分: X/100
2. 每个维度单独评分和简要说明
3. 严重问题（必须修复）- 红色标注，附修复代码
4. 建议改进（建议修复）- 黄色标注
5. 优化建议（锦上添花）- 绿色标注
```

---

**期望输出**:

```
## 审查报告

### 总体评分: 62/100

| 维度 | 得分 | 说明 |
|------|------|------|
| 安全 | 45/40 | SQL拼接问题严重 |
| 性能 | 12/30 | 存在N+1查询 |
| 规范 | 3/20 | 命名不规范，无注释 |
| 可维护性 | 2/10 | 方法过长 |

### 🔴 严重问题

1. [安全-SQL注入] 第23行: `String sql = "SELECT * FROM users WHERE phone = '" + phone + "'";`
   - 风险: 直接拼接用户输入到SQL中，存在SQL注入风险
   - 修复:
   @Select("SELECT * FROM t_user WHERE phone = #{phone}")
   UserDO selectByPhone(@Param("phone") String phone);

### 🟡 建议改进

2. [性能-N+1查询] 第45-48行: 循环内查询数据库
   - 修复: 先收集所有ID，一次批量查询
   ```java
   List<Long> ids = orders.stream().map(Order::getProductId).collect(Collectors.toList());
   Map<Long, Product> productMap = productDao.selectByIds(ids).stream()
           .collect(Collectors.toMap(Product::getId, Function.identity()));
   ```

### 🟢 优化建议

3. 第67行可使用MapStruct替代手动set转换，减少样板代码且编译期校验类型匹配。
```

---

**注意事项**:

1. 对安全问题（SQL注入、XSS、敏感信息泄露），任何一条存在都应直接判定"不可发布"，需修复后再提测。
2. 输出中的修复代码必须是可直接替换使用的，不能是伪代码或描述。
3. 如果审查的是多文件变更，先让AI识别文件间的依赖关系，再逐文件审查。

---

### 22 技术方案Review

**类别**: Review

**使用场景**: 对技术方案进行全面评审，从架构合理性、技术选型、可落地性等角度进行审查。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 完整的技术方案文档。

---

**提示词正文**:

```
你是一位技术方案评审专家，有10年以上大型分布式系统架构经验。请评审以下技术方案。

技术方案:
[粘贴技术方案全文]

评审标准（每个维度0-10分）：

1. 需求理解准确性
   - 是否完整覆盖了所有业务需求？
   - 有没有遗漏的隐含需求（如审计、对账、监管要求）？

2. 架构设计合理性
   - 模块边界划分是否合理？
   - 服务间通信方式（同步/异步）选择是否恰当？
   - 数据归属是否明确？是否存在数据耦合？

3. 技术选型恰当性
   - 所选技术是否与团队能力匹配？
   - 是否考虑了技术成熟度和社区活跃度？
   - 是否有过度引入新技术的倾向？

4. 可落地性
   - 是否有明确的实施步骤和里程碑？
   - 工作量估算是否合理？
   - 风险点和应急预案是否覆盖？

5. 非功能需求满足度
   - 性能指标是否有量化目标和验证方案？
   - 安全设计是否考虑了认证、授权、审计、数据加密？
   - 可用性方案是否包含容灾和降级？

输出格式：
1. 五维度评分汇总表
2. 每个维度的详细评审意见（好的地方和不足的地方分列）
3. TOP 5 必须修改的问题（编号+问题描述+修改建议）
4. 评审结论：通过 / 有条件通过 / 不通过
```

---

**期望输出**:

```
## 评审结论: 有条件通过

### 评分汇总

| 维度 | 评分 | 状态 |
|------|------|------|
| 需求理解 | 8/10 | 较好 |
| 架构设计 | 6/10 | 需改进 |
| 技术选型 | 7/10 | 合理 |
| 可落地性 | 5/10 | 需重点优化 |
| 非功能需求 | 4/10 | 严重不足 |

### TOP 5 必须修改的问题

1. 【架构设计】订单模块和库存模块间的库存扣减使用了同步HTTP调用，在高并发下存在以下问题：
   - 库存扣减失败导致订单创建失败，没有补偿机制
   - 两个服务之间形成强依赖，降低系统可用性
   建议: 改为异步消息（MQ）实现最终一致性，订单模块发送"订单创建成功"事件，库存模块消费并扣减库存。建立补偿表处理扣减失败的回滚。

2. 【非功能需求】方案中未涉及幂等性设计，前端重复提交或MQ重复消费会导致重复创建订单
   建议: 引入唯一请求ID机制（可使用订单号预生成），在数据库层通过唯一索引保证幂等
...
```

---

**注意事项**:

1. 技术方案评审的"可落地性"维度经常被忽视。一个好的方案必须包含实施计划、风险预案和回滚方案。
2. 不通过的情况通常出现在：核心技术选型错误、数据一致性方案严重缺失、安全设计完全空白。
3. 评审时对"好用但团队不熟悉"的技术，建议标注风险但不直接否决。技术可以学，架构失误代价更大。

---

### 23 测试用例Review

**类别**: Review

**使用场景**: 审查已有的测试用例，检查覆盖度是否足够、是否包含边界条件、是否测试了关键异常场景。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 测试用例列表（或测试代码），以及对应的接口/功能定义。

---

**提示词正文**:

```
你是一位测试架构师。请审查以下测试用例是否充分覆盖了对应功能。

被测功能:
[粘贴功能描述和接口定义]

现有测试用例:
[粘贴测试用例列表或测试代码]

审查维度：

1. 覆盖度分析:
   - 正常场景是否覆盖了所有主要业务路径？
   - 异常场景是否覆盖了：参数校验失败、业务规则冲突、外部依赖故障？
   - 边界值：字符串长度边界、数值范围边界、空列表、null值？
   - 组合场景：关键字段的不同组合是否都测到了？

2. 测试数据质量:
   - 测试数据是否真实（用具体值而非"正确值"描述）？
   - 是否使用了生产环境不可能出现的数据？
   - 数据之间是否存在逻辑矛盾？

3. 测试独立性:
   - 测试用例之间是否有依赖（某个用例的执行结果影响另一个）？
   - 是否依赖测试执行顺序？

4. 遗漏场景识别:
   - 列出所有应该测但未测的场景
   - 按优先级（高/中/低）分类
   - 每个遗漏场景给出测试思路

输出格式:
1. 总体评分（XX/100）
2. 覆盖度矩阵（功能×场景的网格，标注哪些已覆盖、哪些缺失）
3. 遗漏的高优测试场景（具体可执行的测试步骤）
4. 现有用例的问题点（如有）
```

---

**期望输出**:

```
## 审查结论

### 总体评分: 65/100

### 覆盖度矩阵

| 功能场景 | 正常场景 | 参数校验 | 业务冲突 | 并发 | 权限 |
|----------|----------|----------|----------|------|------|
| 创建用户 | ✅ 2个 | ✅ 3个 | ✅ 1个 | ❌ 缺失 | ❌ 缺失 |
| 查询用户 | ✅ 2个 | ⚠️ 不完整 | ✅ 1个 | -- | ❌ 缺失 |
| 删除用户 | ✅ 1个 | ❌ 缺失 | ❌ 缺失 | -- | ❌ 缺失 |

### 遗漏的高优测试场景

1. [高-并发] 并发创建同一手机号用户
   步骤: 使用2个线程同时调用createUser接口，传入相同手机号
   预期: 一个返回201，一个返回409，数据库中只有一条记录

2. [高-权限] 无token访问需要鉴权的接口
   步骤: 不带Authorization头调用GET /api/v1/users/1
   预期: 返回401
```

---

**注意事项**:

1. 测试用例审查中最常见的问题是"只测了正常场景"，异常和边界测试严重不足。
2. 测试数据如果有逻辑矛盾（如开始时间晚于结束时间），说明测试设计者对业务理解不足，这类用例的可信度低。
3. 依赖执行顺序的测试用例必须重构，它们通常是被测试代码本身有状态问题（static变量、全局缓存等）的信号。

---

## 六、排障类

### 24 异常堆栈分析

**类别**: 排障

**使用场景**: 收到生产环境异常堆栈信息后，快速分析根本原因和修复方案。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 完整的异常堆栈信息、相关的业务上下文描述、对应的代码（如有）。

---

**提示词正文**:

```
你是一位Java生产故障排查专家。请分析以下异常堆栈，定位根因并给出修复方案。

异常堆栈:
[粘贴完整堆栈信息，包括Caused by链]

发生时间: [如果能提供]
影响范围: [描述影响了哪些用户或功能]
相关代码: [粘贴异常抛出位置附近的代码]

分析要求：

1. 根因分析:
   - 用通俗语言解释：发生了什么？
   - 逐层解读堆栈：每一层Caused by说明了什么？
   - 直接原因是什么？根本原因是什么？
   - 为什么在测试环境没有出现（如果是生产特有故障）？

2. 影响评估:
   - 故障影响范围：影响了多少用户、什么功能
   - 数据完整性问题：是否有脏数据产生
   - 连锁反应：是否会引发下游故障

3. 修复方案（分三步）:
   - 止血方案（立即执行，最快恢复服务）：如重启、切流量、功能降级开关
   - 临时方案（1-2天内上线）：如数据修复脚本、增加参数校验兜底
   - 根治方案（迭代计划）：如重构异常处理逻辑、增加自动化测试

4. 预防措施:
   - 如何避免同类问题再次发生？
   - 需要补充哪些监控报警？
   - 是否需要增加测试用例？
```

---

**期望输出**:

```
## 根因分析

### 发生了什么？
在处理订单支付回调时，因为空指针异常导致支付成功的订单状态未正确更新为"已支付"，用户看到订单状态仍为"待支付"但已扣款。

### 堆栈解读
NullPointerException at OrderService.handlePaymentCallback(OrderService.java:89)
→ paymentRecord.getAmount() 时 paymentRecord 为 null
→ PaymentDao.selectByOrderId() 未找到记录（因为支付记录写入和回调到达存在时序竞争）
→ 支付回调到达时，支付记录的异步写入尚未完成

直接原因: paymentRecord 为 null，未做判空处理
根本原因: 支付服务采用了"先发回调、后写记录"的时序，存在竞争条件

### 为什么测试环境没发现？
测试环境的支付回调是同步模拟的，不存在时序竞争。生产环境网络延迟导致回调先于记录写入到达。

## 修复方案

### 止血方案（已执行）
- 手动修正了故障时段内的23笔异常订单状态
- 增加订单状态监控大盘，异常状态实时告警

### 临时方案
- 在handlePaymentCallback中增加null判断，paymentRecord为null时不抛异常，改为延迟1秒后重试3次
- 增加日志记录重试次数和最终结果

### 根治方案
- 修改支付模块的数据写入时序：先写记录、后发回调（或改为事务内同步完成）
- 引入本地消息表，确保回调处理的最终一致性
```

---

**注意事项**:

1. 堆栈分析最重要的是区分"直接原因"和"根本原因"。直接原因是代码里的null判空缺失，根本原因可能是架构设计中的时序问题。
2. "止血方案"必须是可以立即执行的，不能是需要改代码发版的操作。重启、切流量、开功能开关是常用止血手段。
3. 如果堆栈信息中包含敏感数据（如用户手机号），务必在分析前脱敏处理。

---

### 25 日志异常模式分析

**类别**: 排障

**使用场景**: 给出一段生产环境的应用日志，让AI识别异常模式和潜在风险。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 应用日志片段（可包含INFO/WARN/ERROR行），以及系统架构背景。

---

**提示词正文**:

```
你是一位日志分析专家。请分析以下生产环境日志片段，识别异常模式和潜在风险。

系统信息:
- 应用: Spring Boot 订单服务
- 时间段: [时间段说明]
- 部署: 3实例K8s集群

日志内容:
[粘贴日志，至少50行]

分析要求：

1. 异常模式识别:
   - 找出所有ERROR和WARN行，解释每条的含义
   - 识别重复出现的异常（统计出现频率和分布）
   - 识别异常之间的因果关系（哪些WARN是后续ERROR的前兆？）

2. 时间线分析:
   - 按时间顺序排列关键事件
   - 标注故障的起点和升级点
   - 估算故障持续时间

3. 指标异常:
   - 是否存在某类操作耗时突然增加？
   - 是否存在某接口调用量突增或突降？
   - 数据库连接池、线程池是否有异常？

4. 关联推断:
   - 结合多个日志行推断完整的事故链路
   - 标注你需要但日志中没有的信息（建议后续补充日志点）

5. 行动建议:
   - 立即需要关注的问题
   - 需要添加的监控报警规则
   - 日志格式或内容的改进建议
```

---

**期望输出**:

```
## 日志分析报告

### 异常模式识别

1. **高频ERROR: Redis连接超时** (出现23次, 14:05-14:12)
   - 堆栈: `RedisConnectionFailureException: Cannot connect to redis`
   - 位置: UserService.getUserCache()
   - 影响: 用户查询接口降级为直接查DB，P99响应时间从50ms升至800ms

2. **连锁WARN: 数据库连接池耗尽** (出现5次, 14:08-14:10)
   - 日志: `HikariPool - Connection is not available, request timed out after 30000ms`
   - 因果: Redis超时导致请求全部穿透到DB，瞬间打满连接池

### 时间线分析
14:05:01 - 首次Redis连接超时
14:05:30 - Redis超时频率升至每秒3次
14:07:00 - 开始出现DB慢查询（P99 > 2s）
14:08:15 - 数据库连接池告警
14:10:00 - 第一个HTTP 502返回给用户
14:12:00 - Redis恢复连接，服务逐渐恢复

故障持续时间: 约7分钟

### 行动建议
1. [立即] Redis增加主从切换自动故障转移（Sentinel/Cluster）
2. [立即] 缓存降级逻辑增加断路器：连续超时N次后直接查DB，跳过Redis
3. [短期] 数据库连接池最大连接数从20调至50，增加排队的容忍度
4. [长期] 日志中增加Redis健康检查探针日志，每30秒打印连接状态
```

---

**注意事项**:

1. 日志分析的质量取决于日志本身的完整度。如果分析中发现关键信息缺失，务必在报告中指出，推动团队完善日志打点。
2. 时间线分析需要日志中有准确的时间戳。如果日志时间戳格式不一致（多时区、不同精度），先做归一化。
3. 关联推断需要你提供系统架构背景，否则AI只能做独立行分析。务必在"系统信息"部分描述清楚服务间调用关系。

---

### 26 SQL慢查询分析

**类别**: 排障

**使用场景**: 给出一条慢SQL及其EXPLAIN结果，分析性能瓶颈和优化方案。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 慢SQL语句、EXPLAIN输出、表结构和索引信息、数据量级。

---

**提示词正文**:

```
你是一位MySQL性能优化专家。请分析以下慢查询，给出优化方案。

慢查询SQL:
[粘贴SQL语句]

EXPLAIN输出:
[粘贴EXPLAIN结果]

表结构:
[粘贴相关表的DDL]

数据量级:
- 表A: 约500万行
- 表B: 约1000万行
- 表C: 约50万行

MySQL版本: 8.0
当前执行时间: [如有]

分析要求：

1. 问题诊断:
   - EXPLAIN中type列是什么？是否使用了索引？
   - key列显示使用了哪个索引？是否是最优选择？
   - rows估算是否合理？扫描了多少行？
   - Extra列中是否出现了Using filesort、Using temporary等危险信号？

2. 根因分析:
   - 为什么这个SQL慢？（索引缺失/索引选择错误/写法问题/数据量问题）
   - 业务场景分析：这个SQL的业务意图是什么？是否可以换个方式实现？

3. 优化方案（按优先级排列）:
   - 方案A: 加索引（给出具体DDL，含索引名和字段）
   - 方案B: 改写SQL（给出优化后的SQL）
   - 方案C: 架构层优化（如增加缓存层、读写分离、分表）
   - 每个方案附预估提升效果和风险

4. 验证方法:
   - 如何验证优化是否生效？（EXPLAIN对比、实际执行时间对比）
   - 上线后的监控指标建议
```

---

**期望输出**:

```sql
## 原始SQL
SELECT o.id, o.order_no, o.amount, u.username, u.phone
FROM t_order o
LEFT JOIN t_user u ON o.user_id = u.id
WHERE o.status = 1
  AND o.created_at >= '2025-06-01'
  AND o.created_at < '2025-07-01'
  AND u.status = 1
ORDER BY o.created_at DESC
LIMIT 20;
```

```
## EXPLAIN分析

| table | type | key | rows | Extra |
|-------|------|-----|------|-------|
| o     | ALL  | NULL | 4,800,000 | Using where; Using filesort |
| u     | eq_ref | PRIMARY | 1 | Using where |

问题诊断:
- t_order表全表扫描(type=ALL)，扫描480万行
- 无索引覆盖 WHERE+ORDER BY 组合，触发了Using filesort
- LEFT JOIN后被u.status=1过滤，导致LEFT JOIN形同虚设

## 优化方案

### 方案A: 添加联合索引（推荐，预计提升90%+）
ALTER TABLE t_order ADD INDEX idx_status_created (status, created_at DESC);
- 效果: type 从 ALL 降为 range，rows 从480万降为预估30万内
- 风险: 低，该索引同时服务于status筛选和created_at排序
```

---

**注意事项**:

1. `EXPLAIN ANALYZE`（MySQL 8.0.18+）比传统`EXPLAIN`更准确，因为它实际执行SQL并收集真实数据。尽量提供`EXPLAIN ANALYZE`的结果。
2. 加索引不是永远的正解。写多读少的表，过多索引会降低写入性能。需要结合业务的读写比例判断。
3. 如果EXPLAIN中出现了"Using join buffer"，说明JOIN的驱动表没有走索引，通常意味着SQL写法或索引设计有问题。

---

### 27 JVM问题分析

**类别**: 排障

**使用场景**: 分析JVM GC日志或OOM dump，诊断内存泄漏、GC频繁、CPU飙高等问题。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: JVM参数配置、GC日志片段、OOM时的heap dump摘要、系统资源使用情况。

---

**提示词正文**:

```
你是一位JVM性能调优专家。请分析以下JVM问题。

问题描述:
[描述现象：如"服务运行2天后CPU飙升至100%，重启后恢复"、"Full GC频繁导致STW时间超过5秒"等]

JVM参数:
-Xms2g -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200

GC日志:
[粘贴关键GC日志片段]

系统资源:
- CPU: 4核
- 内存: 8GB
- 堆外内存使用: [如果知道]

分析要求：

1. 症状确认:
   - GC频率是否异常？（Minor GC / Full GC 间隔）
   - 单次GC耗时是否过长？
   - 堆内存使用曲线是否合理？

2. 根因分析:
   - 内存泄漏：哪些对象占据了最多的堆空间？（如能提供jmap或heap dump摘要）
   - GC策略不当：当前GC算法和参数是否适合业务场景？
   - 堆大小是否合理：Xms/Xmx配置是否需要调整？

3. 优化方案:
   - JVM参数调整建议（给出具体参数和值）
   - 代码层面的优化建议（如减少大对象创建、对象池化）
   - 架构层面的建议（如分离内存密集型和CPU密集型服务）

4. 内存泄漏排查（如果适用）:
   - 给出使用MAT或jprofiler分析heap dump的具体步骤
   - 指出可能泄漏的类路径和引用链
```

---

**期望输出**:

```
## 分析报告

### 症状确认
- Minor GC频率: 每8秒一次（正常应为30-60秒）
- Full GC频率: 每5分钟一次（正常应数小时或数天一次）
- 单次Full GC耗时: 3.2秒（P99），已超过-XX:MaxGCPauseMillis=200ms目标
- 老年代使用率: 持续增长，Full GC后仅回收15%，确认内存泄漏

### 根因分析
**内存泄漏**: ThreadLocal未清理导致

从heap dump分析，发现大量 `com.example.common.RequestContext` 对象被 ThreadLocal 持有。
请求处理完成后未调用 ThreadLocal.remove()，而Tomcat线程池复用线程，导致老年代持续堆积。

泄漏链路:
Thread → ThreadLocalMap → RequestContext (1.2MB每个) × 200线程 = 240MB无法回收

### 优化方案

1. [立即] 代码修复:
// 在所有Filter或Interceptor的afterCompletion中增加
@Override
public void afterCompletion(...) {
    RequestContextHolder.clear(); // 必须清理ThreadLocal
}

2. [短期] JVM参数优化（缓解症状）:
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=/var/log/app/oom.hprof
-XX:+PrintGCDetails
-XX:+PrintGCDateStamps
-Xloggc:/var/log/app/gc-%t.log

3. [长期] 监控告警:
- 添加Prometheus JVM Exporter，监控堆使用率
- 设置告警规则：老年代使用率 > 80% 持续5分钟 → 通知 + 自动dump

### 验证方法
1. 修复后观察Full GC频率是否恢复到数小时一次
2. 压测12小时后检查老年代使用率是否稳定在70%以下
```

---

**注意事项**:

1. ThreadLocal内存泄漏是Java Web应用中最常见的内存泄漏类型，根本原因是Tomcat线程池复用+未清理ThreadLocal。
2. 不要一上来就调JVM参数。先确认是否是代码层面的问题（内存泄漏、大对象频繁创建），82%的JVM问题根源在代码。
3. 生产环境建议配置`-XX:+HeapDumpOnOutOfMemoryError`，这会在OOM时自动生成dump文件，是事后分析的关键证据。

---

## 七、项目管理类

### 28 会议纪要整理

**类别**: 项目管理

**使用场景**: 将会议录音转文字或会议速记整理为结构化的会议纪要。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 会议原始记录（速记文本、录音转写文本或关键讨论要点）。

---

**提示词正文**:

```
你是一位项目经理，擅长整理会议纪要。请将以下会议原始记录整理为结构化纪要。

会议基本信息:
- 会议主题: [填写]
- 日期: [填写]
- 参与人: [填写]

原始记录:
[粘贴会议讨论内容]

输出要求：
1. 会议纪要包含以下章节：

   **一、会议结论**
   用3-5条简洁的陈述总结会议达成的关键决定。每条一句话。

   **二、讨论要点**
   按议题分小节，每个小节包含：
   - 议题名称
   - 讨论内容摘要（客观记录，不做评价）
   - 分歧与共识

   **三、待办事项（Action Items）**
   表格格式:
   | 编号 | 任务 | 负责人 | 截止日期 | 备注 |

   **四、遗留问题**
   本次会议讨论过但未达成结论的问题，以及后续跟进方式。

2. 整理原则：
   - 去除口语化表达和重复内容
   - 保留所有决策点和分歧点
   - 行动项必须明确到具体人和具体日期
   - 技术讨论保留术语原文（不强行翻译成中文）
```

---

**期望输出**:

```
## 会议纪要: 订单系统性能优化方案讨论
- 日期: 2025-07-01
- 参与人: 张工(后端TL)、李工(DBA)、王工(运维)、赵工(测试)

### 一、会议结论
1. 优先实施Redis缓存方案，预期将订单查询P99从800ms降至50ms以内
2. 数据库读写分离推迟到下个迭代，先观察缓存方案的效果
3. 7月10日前完成缓存压测，确认Redis集群规模
4. 运维在7月5日前完成Redis集群搭建（3主3从）

### 二、讨论要点

**议题1: 缓存策略选择**
- 讨论内容: 张工提出使用Redis缓存热点订单（最近30天），李工建议用Caffeine本地缓存+Redis二级缓存
- 分歧与共识: 讨论后一致同意先用单层Redis，降低复杂度。二级缓存留到Q4再评估

### 三、待办事项

| 编号 | 任务 | 负责人 | 截止日期 | 备注 |
|------|------|--------|----------|------|
| AI-01 | Redis集群搭建(3主3从) | 王工 | 7月5日 | 配置哨兵模式 |
| AI-02 | 代码改造：订单查询接入Redis | 张工 | 7月8日 | 含缓存预热和降级逻辑 |
| AI-03 | 缓存压测（模拟生产流量） | 赵工 | 7月10日 | 需要运维协助搭建压测环境 |

### 四、遗留问题
1. 缓存穿透防护方案（布隆过滤器 vs 空值缓存）未达成一致 → 张工7月3日前给出技术对比文档
2. 大促期间缓存的数据量预估 → 产品经理确认促销范围后补充
```

---

**注意事项**:

1. 行动项必须有负责人和截止日期，缺少任何一个都等于"没有行动项"。
2. "遗留问题"很重要，它们往往是下次会议的第一个议题。写清楚跟进方式，避免问题沉底。
3. 如果原始记录中有情绪化或非建设性的讨论内容，应不录入纪要。

---

### 29 项目周报生成

**类别**: 项目管理

**使用场景**: 根据本周的工作日志或Git提交记录，生成结构化的项目周报。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 本周完成的工作项列表、遇到的问题、下周计划。

---

**提示词正文**:

```
你是一位技术项目经理。请根据以下信息生成一份专业的项目周报。

项目信息:
- 项目名称: [填写]
- 项目阶段: [如: 开发阶段/测试阶段/上线支持]
- 本周周期: [如: 2025年6月25日 - 7月1日]

本周工作内容:
[描述完成的工作项，包括已上线的功能、已解决的Bug、完成的技术任务等]

遇到的问题:
[描述阻碍、风险、技术难题等]

下周计划:
[描述下周计划开展的工作]

周报格式：

一、本周概要
   1-2句话总结本周最重要的进展

二、本周完成
   按类别列出已完成工作:
   - 功能开发: [已完成的上线/提测功能]
   - Bug修复: [已修复的Bug及编号]
   - 技术优化: [完成的技术改造/重构]
   - 其他: [文档、评审、支持等]

三、关键指标 (如适用)
   - 代码提交次数
   - 新增/关闭的Bug数
   - 接口响应时间变化
   - 测试覆盖率变化

四、风险与阻塞
   表格格式: | 风险/阻塞 | 影响 | 责任人 | 应对措施 | 状态 |

五、下周计划
   按优先级列出

六、需要的支持
   需要其他团队或上级协助的事项

写作要求:
- 使用客观专业的语言，避免"我觉得"、"大概"等不确定表述
- 数字要具体，不要用"很多"、"一些"
- 风险项不要隐瞒，如实反映
```

---

**期望输出**:

```
## 项目周报: 订单系统重构

**项目**: 电商订单系统
**周期**: 2025年6月25日 - 7月1日
**报告人**: 张工

### 一、本周概要
本周完成了订单查询性能优化和支付回调幂等性改造，订单查询P99从800ms降至45ms，已通过压测验证。支付回调重复通知问题得到根本解决。

### 二、本周完成

**功能开发**
- [x] 订单列表Redis缓存方案上线，覆盖创建时间30天内订单
- [x] 支付回调幂等性改造完成，使用唯一索引 + 状态机防重

**Bug修复**
- [x] #1423 订单详情页面展示金额精度问题（BigDecimal舍入模式修正）
- [x] #1428 退款订单状态流转异常（新增退款中→退款成功/失败的状态机约束）

**技术优化**
- [x] 订单表添加联合索引(idx_status_created)，慢查询TOP1消除
- [x] 接入Prometheus JVM监控，新增GC频率和堆使用率大盘

### 三、关键指标

| 指标 | 本周 | 上周 | 变化 |
|------|------|------|------|
| 代码提交 | 23次 | 18次 | +27% |
| 新增Bug | 2个 | 5个 | -60% |
| 关闭Bug | 4个 | 3个 | +33% |
| 订单查询P99 | 45ms | 800ms | -94% |
| 测试覆盖率 | 78% | 72% | +6% |

### 四、风险与阻塞

| 风险/阻塞 | 影响 | 责任人 | 应对措施 | 状态 |
|-----------|------|--------|----------|------|
| Redis集群单点（仅1个哨兵） | 哨兵故障时无法自动切换 | 王工 | 7月5日前增加2个哨兵节点 | 处理中 |
| 产品未确认大促时间 | 无法评估缓存数据量和预热方案 | 产品经理 | 已催办，预计7月3日回复 | 跟进中 |

### 五、下周计划
1. 完成退款流程性能优化（目标P99 < 200ms）
2. 实现订单导出功能（Excel格式，支持1万条）
3. 配合QA完成回归测试

### 六、需要的支持
- 需要运维在7月5日前完成Redis哨兵节点扩容
```

---

**注意事项**:

1. 周报中的关键指标应和历史数据对比（环比），单纯给一个数字没有参考价值。
2. "风险与阻塞"部分要客观，不要为了"好看"而隐瞒问题。隐藏的风险最终都会变成事故。
3. "需要的支持"部分要明确具体的人和事，不要让读者猜测"这是在跟谁说"。

---

### 30 项目风险识别

**类别**: 项目管理

**使用场景**: 在项目启动或迭代计划阶段，系统性地识别潜在风险并制定应对措施。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 项目背景、技术方案、团队情况、时间计划。

---

**提示词正文**:

```
你是一位资深技术项目经理兼风险管理者。请基于以下项目信息，系统性地识别潜在风险。

项目信息:
- 项目目标: [描述]
- 技术方案概要: [简述]
- 团队情况: [团队规模、核心成员经验、是否远程]
- 时间计划: [里程碑和截止日期]
- 依赖方: [需要对接的外部团队或系统]

识别维度（逐个维度扫描风险）:

1. 技术风险:
   - 新技术引入：团队对选型技术栈的熟悉程度
   - 技术复杂度：是否存在团队经验不足的领域
   - 技术债务：是否在旧系统上改造，旧代码质量如何
   - 第三方依赖：外部API/SDK的稳定性和SLA
   - 数据迁移：是否涉及存量数据，迁移方案是否可靠

2. 进度风险:
   - 需求变更频率：历史上项目需求的稳定性
   - 关键路径：是否存在单一阻塞点
   - 人力投入：是否有人员兼职、可能的请假周期
   - 外部交付物：依赖方是否会按时交付

3. 人员风险:
   - Bus Factor: 是否有关键知识只有1人掌握
   - 人员稳定性: 核心成员是否有离职风险
   - 团队协作: 是否分布式团队，时区差影响

4. 质量风险:
   - 测试资源: 是否有独立的QA团队
   - 上线窗口: 是否有充分的测试和灰度时间

输出格式：
- 风险登记表: | 编号 | 风险类别 | 风险描述 | 概率(高/中/低) | 影响(高/中/低) | 风险等级 | 应对措施 | 责任人 |
- 按风险等级从高到低排序
- TOP 3风险单独详细说明（根因+连锁影响+具体应对计划）
```

---

**期望输出**:

```
## 项目风险登记表

| 编号 | 类别 | 风险描述 | 概率 | 影响 | 等级 | 应对措施 | 责任人 |
|------|------|----------|------|------|------|----------|--------|
| R-01 | 技术 | 团队对RocketMQ无生产经验，消息可靠性难以保证 | 高 | 高 | 🔴极高 | 安排MQ专项培训+外部顾问支持 | 张工 |
| R-02 | 进度 | 支付渠道接口文档未提供，预计延迟2周 | 高 | 高 | 🔴极高 | 先基于MOCK开发，文档到后补齐联调 | 李工 |
| R-03 | 人员 | 后端核心开发李工下月休产假，关键模块无人接手 | 高 | 高 | 🔴极高 | 本周起安排王工参与代码评审和结对编程 | 张工 |
| R-04 | 技术 | 旧订单表5000万行数据迁移，停机窗口可能不够 | 中 | 高 | 🟠高 | 采用双写+灰度切换方案，避免全量停机 | 赵工(DBA) |

### TOP 3 风险详细分析

**R-01: RocketMQ经验不足**
- 根因: 团队之前全用RabbitMQ，RocketMQ的事务消息、顺序消息机制差异大
- 连锁影响: 消息丢失 → 订单状态不同步 → 客户投诉 → 人工对账成本↑
- 应对计划:
  1. 本周完成RocketMQ核心技术内训
  2. 搭建本地环境完成消息可靠性验证（发送+消费全链路）
  3. 引入消息表记录每条消息的发送和消费状态，支持人工回放
  4. 关键业务消息（支付/退款）上线前必须通过混沌测试
```

---

**注意事项**:

1. 风险评估要诚实。团队对新技术"不熟"就是高风险，不要写成"需要一定学习成本"来弱化风险等级。
2. 风险应对措施不能是"加强沟通"、"多加关注"这种虚的表述。必须是具体的、可执行的动作。
3. 人员风险（Bus Factor、关键人休假）在技术项目中最容易被忽视，但影响往往最大。

---

### 31 版本发布说明生成

**类别**: 项目管理

**使用场景**: 根据Git提交记录或已完成的功能/Bug列表，生成版本发布说明（Release Notes）。

**适用工具**: Claude Code / ChatGPT / Codex

**输入准备**: 版本号、Git提交历史摘要、功能列表、Bug修复列表、已知问题列表。

---

**提示词正文**:

```
你是一位技术文档编写专家。请根据以下信息生成版本发布说明（Release Notes）。

版本信息:
- 版本号: [如: v2.3.0]
- 发布日期: [如: 2025-07-15]
- 上一版本: [如: v2.2.1]

变更内容:

新增功能:
[列出新功能]

Bug修复:
[列出已修复的Bug]

优化改进:
[列出性能优化、体验改进等]

已知问题:
[列出已发现但未修复的问题]

不兼容变更 (Breaking Changes):
[如有，列出与上一版本不兼容的变更]

发布说明格式：

# Release vX.Y.Z (YYYY-MM-DD)

## 🚀 新功能
- [功能1]: 简要描述
- [功能2]: 简要描述

## 🐛 Bug修复
- [#编号] 问题描述 → 修复描述

## ⚡ 性能优化
- 优化项1: 描述 + 效果数据

## ⚠️ 不兼容变更
- 变更描述 + 迁移指南（如有）

## 🔧 升级指南
- 升级步骤1
- 升级步骤2

## 📝 已知问题
- 问题描述 + 绕过方案（如有）

## 📦 变更文件清单
- 变更文件列表（或commit范围）

---
*发布负责人: [姓名]*
*审核人: [姓名]*

写作要求:
- 功能描述面向使用者（可能是产品或运营），避免纯技术术语
- Bug修复每项附issue编号
- 性能优化附量化数据（P99从多少降至多少），不要只说"性能提升"
- 不兼容变更必须用醒目标识，并提供迁移步骤
```

---

**期望输出**:

```
# Release v2.3.0 (2025-07-01)

## 🚀 新功能
- 订单列表支持按商品名称模糊搜索
- 新增订单批量导出功能，支持Excel格式，单次最多导出1万条
- 退款流程新增"部分退款"支持，可对单个订单中的多个商品分别退款

## 🐛 Bug修复
- [#1245] 修复订单详情页金额精度丢失问题：BigDecimal使用ROUND_HALF_UP舍入模式
- [#1251] 修复退款成功但订单状态未更新的问题：完善了状态机流转规则
- [#1258] 修复并发创建用户时偶发的唯一索引冲突

## ⚡ 性能优化
- 订单查询接口P99响应时间从800ms降至45ms（-94%），通过添加联合索引和Redis缓存实现
- 订单列表导出接口改为异步处理，大文件导出不再阻塞请求线程

## ⚠️ 不兼容变更
**订单查询接口返回字段变更**
- `GET /api/v1/orders/{id}` 返回体中 `refundAmount` 字段类型从 `Double` 变更为 `String`（元，保留2位小数）
- 迁移: 客户端需将 `refundAmount` 的解析从数字类型改为字符串类型

## 🔧 升级指南
1. 执行数据库迁移脚本: `db/migration/V2.3.0__add_order_index.sql`
2. 更新Redis配置: 新增 `spring.redis.order-cache.ttl=1800`
3. 确保客户端已适配 `refundAmount` 字段的类型变更

---

*发布负责人: 张工*
*审核人: 李工*
```

---

**注意事项**:

1. 不兼容变更（Breaking Changes）必须放在最醒目的位置，并提供迁移指南。这是Release Notes最重要的部分。
2. 性能优化的效果数据必须真实，不要夸大。上线前用压测数据，上线后用生产监控数据。
3. 已知问题要写"绕过方案"，不要让用户发现Bug后不知如何应对。

---

## 八、Agent长任务类

### 32 Claude Code 多步骤开发任务

**类别**: Agent长任务

**使用场景**: 在Claude Code中启动一个需要多步骤、多文件修改的复杂开发任务。

**适用工具**: Claude Code

**输入准备**: 清晰的任务描述、技术约束、相关的文件路径。

---

**提示词正文**:

```
我需要你完成以下开发任务。这是一个多步骤任务，请先确认你理解每个步骤，然后按顺序执行。每完成一步，向我汇报进展。

## 任务：为订单模块增加退款流程

### 前置条件
- 项目路径: /home/user/project/order-service
- 技术栈: Spring Boot 3.x + MyBatis-Plus + MySQL
- 订单表: t_order (id, order_no, user_id, amount, status, created_at)
- 已有代码:
  - OrderController.java (src/main/java/com/example/controller/)
  - OrderService.java 和 OrderServiceImpl.java (src/main/java/com/example/service/)
  - OrderDao.java (src/main/java/com/example/dao/)

### 步骤1: 数据库变更
- 创建退款表 t_refund (id, order_id, refund_amount, reason, status, created_at)
- 创建退款明细表 t_refund_item (id, refund_id, product_id, quantity, amount)
- 在 t_order 表新增 refund_status 字段(TINYINT, 默认0)

### 步骤2: 实体与DAO层
- 创建 RefundDO, RefundItemDO
- 创建 RefundMapper, RefundItemMapper (继承BaseMapper)
- 在 OrderMapper 增加 updateRefundStatus 方法

### 步骤3: DTO和VO
- 创建 CreateRefundRequest (含参数校验)
- 创建 RefundVO
- 创建 RefundConvertor (MapStruct)

### 步骤4: Service层
- 创建 RefundService 接口和 RefundServiceImpl
- 退款方法包含:
  - 校验订单状态（必须为已支付）
  - 校验退款金额不超过订单金额
  - 更新订单退款状态
  - 创建退款记录和明细
  - 使用 @Transactional
  - 异常抛出 BusinessException

### 步骤5: Controller层
- 创建 RefundController
- POST /api/v1/orders/{orderId}/refund - 申请退款
- GET /api/v1/refunds/{id} - 查询退款详情
- 添加参数校验和权限注解

### 步骤6: 单元测试
- 创建 RefundServiceImplTest
- 覆盖: 正常退款、订单状态不符、退款金额超限

### 约束
- 所有生成的代码必须遵循项目现有的代码风格
- 使用 Lombok (@Data, @Slf4j, @RequiredArgsConstructor)
- 异常使用 BusinessException，错误码在 ErrorCode 枚举中新增
- 禁止修改与本次任务无关的已有代码
- 每个Java文件必须包含完整的 package 声明和 import

请开始执行。先阅读现有的订单模块代码，确认代码风格后再动手。
```

---

**期望输出**:

```
我已经阅读了现有的订单模块代码，理解了以下代码风格:
- Controller 使用 @RestController + @RequiredArgsConstructor + @Tag
- Service 采用接口+实现分离模式
- DO类继承BaseEntity（含id, createTime, updateTime, isDeleted）
- 统一返回 Result<T>
- 校验使用 Jakarta Validation，注解message使用中文

现在开始执行步骤1...

[步骤1完成] 已生成DDL脚本，文件: src/main/resources/db/migration/V1.1__add_refund.sql
[步骤2完成] 已创建RefundDO, RefundItemDO, RefundMapper, RefundItemMapper, 并在OrderMapper中新增updateRefundStatus方法
...
[步骤6完成] 已创建RefundServiceImplTest，3个测试用例全部通过

所有步骤完成。以下是变更文件总结:
- 新增5个Java文件
- 修改1个Java文件（OrderMapper.java）
- 新增1个SQL迁移脚本
```

---

**注意事项**:

1. 多步骤任务最关键的是"先读后写"。让Claude Code在修改代码前先阅读现有代码，理解代码风格和项目约定。
2. 每步完成后要求汇报进展，便于你及时纠正方向偏差。不要让Agent闷头干到底。
3. "禁止修改与本次任务无关的已有代码"这条约束非常重要，能防止Agent做超出范围的"优化"。

---

### 33 Claude Code 代码分析任务

**类别**: Agent长任务

**使用场景**: 启动Claude Code对未知代码库进行全面分析，包括架构、模块依赖、关键路径梳理。

**适用工具**: Claude Code

**输入准备**: 项目根目录路径、分析目标说明。

---

**提示词正文**:

```
我需要对一个陌生的项目进行全面分析。请按以下要求系统性地完成。

## 项目信息
- 项目路径: /home/user/project/order-service
- 技术栈: Spring Boot + MyBatis-Plus + MySQL
- 分析目标: 理解整体架构、核心业务流程、数据模型，识别技术债务和潜在风险

## 分析步骤

### 第1步: 项目结构梳理
- 读取 build.gradle (或 pom.xml)，列出所有依赖及其用途
- 读取 application.yml/application.properties (所有环境)，理解配置项
- 列出所有模块/包的目录结构和职责划分
- 识别是否存在多模块结构

### 第2步: 核心业务流程
- 找到所有 Controller 类，列出所有对外接口（生成接口清单表格）
- 追踪2-3个核心业务接口的完整调用链路:
  Controller → Service → DAO → SQL
- 画出核心流程的调用时序（用Mermaid）

### 第3步: 数据模型分析
- 列出所有数据库表及其用途
- 分析表之间的关联关系（外键、逻辑关联）
- 识别字段命名不一致、冗余字段、缺失索引等问题
- 评估数据量级和增长趋势（从配置和代码注释中推断）

### 第4步: 异常和边界处理
- 搜索所有 Exception 类的定义，理解异常体系
- 检查全局异常处理器的覆盖范围
- 找出现有代码中可能吞异常、未处理异常的地方

### 第5步: 技术债务识别
- 识别以下模式:
  - TODO/FIXME/HACK 注释
  - 被注释掉的代码块
  - 方法超过80行
  - 参数超过5个的方法
  - 重复代码模式（跨文件）
  - 硬编码的配置值
- 给出技术债务清单，按严重性排序

### 第6步: 安全审计摘要
- 搜索可能的安全问题: SQL拼接、密码明文、日志打印敏感信息、缺失鉴权
- 标注发现的问题和具体位置（文件+行号）

## 输出要求
- 每步完成输出阶段性报告
- 最终生成一份完整的《项目分析报告》，包含以上所有章节
- 所有结论附具体证据（文件名+行号），不写"可能""似乎"等不确定表述
```

---

**期望输出**:

```
## 项目分析报告: order-service

### 第1步: 项目结构梳理 ✅
已完成。

order-service/
├── src/main/java/com/example/
│   ├── controller/    # REST接口层 (5个Controller, 23个接口)
│   ├── service/       # 业务逻辑层 (接口+实现分离)
│   ├── dao/
│   │   ├── mapper/    # MyBatis Mapper接口
│   │   └── entity/    # DO实体类
│   ├── model/
│   │   ├── request/   # 请求DTO
│   │   └── vo/        # 响应VO
│   ├── config/        # 配置类
│   └── common/        # 通用工具和异常
└── src/main/resources/
    ├── mapper/        # MyBatis XML
    └── db/migration/  # Flyway迁移脚本

依赖清单:
- Spring Boot 3.2.0 (Web, Validation, AOP)
- MyBatis-Plus 3.5.5 (ORM)
- MySQL Connector 8.0.33 (数据库驱动)
- Redis / Redisson 3.23.0 (分布式锁)
- Flyway 9.22.0 (数据库迁移)
...

（继续输出其余步骤的报告内容）
```

---

**注意事项**:

1. 这种分析任务对token消耗很大。如果项目代码量超过5万行，建议分模块多次分析。
2. 分析过程中发现的问题，暂时不要修复。这个任务是"分析"不是"修复"，先完整理解全貌再行动。
3. 分析报告应结构化、有索引，方便后续查询。不要让报告变成一段连续的文字难以定位。

---

### 34 Claude Code 重构任务

**类别**: Agent长任务

**使用场景**: 使用Claude Code对已有代码进行安全的重构，确保行为不变的前提下改善代码质量。

**适用工具**: Claude Code

**输入准备**: 需要重构的代码文件路径、重构目标说明、现有测试路径。

---

**提示词正文**:

```
我需要你对以下代码进行安全重构。重构的黄金法则是：不改变任何外部行为。

## 重构目标
- 文件路径: src/main/java/com/example/service/OrderServiceImpl.java
- 当前问题: 该类超过800行，包含太多职责，难以测试和维护
- 重构目标:
  1. 按职责拆分为多个小Service:
     - OrderQueryService: 订单查询相关方法
     - OrderCreateService: 订单创建相关方法
     - OrderStatusService: 订单状态流转相关方法
  2. 消除重复代码（该类中有3处相似的参数校验逻辑）
  3. 提取公共的校验逻辑到 OrderValidator 工具类
  4. 方法长度控制在50行以内

## 现有测试
- 测试文件: src/test/java/com/example/service/OrderServiceImplTest.java
- 该文件包含42个测试用例，必须全部保持通过

## 重构约束

### 安全约束（违反任何一条立即停止）
1. 每次修改后必须运行所有相关测试：`./gradlew test --tests "*OrderService*"`
2. 不要修改任何public方法的签名（方法名、参数列表、返回值类型）
3. 不要修改数据库操作的行为（SQL、事务边界）
4. 不要修改异常的类型和抛出条件
5. 不要修改任何注解的配置（@Transactional、@Cacheable等）

### 操作流程
第1步: 阅读当前代码和测试，理解所有public方法的行为
第2步: 提出拆分方案，说明哪些方法移到哪个新Service，列出理由
第3步: 等我确认拆分方案后，逐步执行
第4步: 每提取一个方法或创建一个新类，立即运行测试
第5步: 如果测试失败，立即回滚上一步修改，分析原因后再尝试

### 禁止事项
- 禁止同时修改多个文件（除非是配套的新Service接口+实现类）
- 禁止使用IDE的自动重构功能（必须逐行人工审查）
- 禁止引入新的第三方依赖
- 禁止修改 import 语句的顺序（避免无意义的diff）

## 输出
- 每步完成后输出: 做了什么、测试结果（通过/失败）、变更的文件列表
- 全部完成后输出: 重构前后对比总结（行数变化、类数量、测试覆盖保持情况）
```

---

**期望输出**:

```
## 重构报告

### 第1步: 代码分析 ✅
阅读了OrderServiceImpl.java (847行) 和 OrderServiceImplTest.java (42个测试)

现有方法清单 (16个public方法):
- createOrder: 创建订单 (152行) → 属于订单创建职责
- getOrderById: 查询单个订单 → 属于查询职责
- listOrders: 列表查询 → 属于查询职责
- cancelOrder: 取消订单 → 属于状态管理职责
- confirmOrder: 确认订单 → 属于状态管理职责
...

### 第2步: 拆分方案（待确认）
OrderQueryService (接口+实现)
├── getOrderById(id)
├── listOrders(request)
├── getOrderCount(query)
└── getOrderStatistics(request)

OrderCreateService (接口+实现)
├── createOrder(request)
├── validateOrderRequest(request)
└── calculateOrderAmount(items)

OrderStatusService (接口+实现)
├── cancelOrder(orderId, reason)
├── confirmOrder(orderId)
├── shipOrder(orderId, trackingNo)
└── completeOrder(orderId)

请确认这个拆分方案...

[用户确认后继续...]

### 全部完成 ✅

重构前后对比:
| 指标 | 重构前 | 重构后 |
|------|--------|--------|
| 类数量 | 1个Service实现 | 3个Service实现+1个Validator |
| 最大类行数 | 847行 | 320行 |
| 最大方法行数 | 152行 | 48行 |
| 重复代码块 | 3处(参数校验) | 0处(提取为OrderValidator) |
| 测试用例 | 42个 | 45个(新增3个针对新Validator) |
| 测试状态 | 42通过 | 45通过 |
```

---

**注意事项**:

1. 重构的基本单元是"一次修改+一次测试"。不要攒一堆修改再跑测试，出错了难以定位是哪个改动导致。
2. 提取的新类和方法命名要表意。重构的目的之一就是提高可读性，命名不清的重构比不重构更糟糕。
3. 如果重构过程中发现原有测试覆盖不足，应先补充测试（保证现有行为被覆盖），再进行重构。不要在"没有测试网"的情况下重构。

---

## 附录：提示词使用指南

### 如何选择工具

| 场景 | 推荐工具 | 理由 |
|------|----------|------|
| 单次问答（生成代码、分析问题） | ChatGPT / Codex | 交互快、成本低 |
| 多文件修改、长步骤任务 | Claude Code | 可直接操作文件系统、持续上下文 |
| 代码Review、PR审查 | Codex CLI / Claude Code | 可读取完整仓库上下文 |
| 技术方案评审、架构分析 | Claude / ChatGPT | 推理能力强、输出结构化质量高 |

### 提示词优化原则

1. **角色设定要具体**: "你是Java专家"不如"你是阿里巴巴P9级Java后端专家，精通Spring Boot和MyBatis-Plus"。
2. **约束要前置**: 把禁止事项和强制要求放在提示词前面，而不是末尾。AI对前置信息的关注度更高。
3. **输出格式要明确**: 给出具体输出模板（表格、代码块、标题层级），能显著提高输出质量。
4. **给正例和反例**: 在关键约束处给出"这样做"和"不这样做"的例子，效果远好于纯文字描述。

### 安全底线

所有编码类提示词均内置了安全约束（禁止SQL拼接、敏感信息脱敏、输入校验等），在生产环境使用这些代码前，仍需通过Code Review和SAST扫描验证。

---

> **版本**: v1.0
> **最后更新**: 2025-07-01
> **维护者**: 企业IT AI落地实战手册编写组
