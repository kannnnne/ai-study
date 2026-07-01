# 第18章 Java 后端专用 AI 工作流

> **目标读者**：Java 后端开发者，使用 Spring Boot + MyBatis-Plus 技术栈，希望将 AI 深度嵌入日常开发工作流。
> **本章回答的核心问题**：每个具体的 Java 后端开发任务，AI 能帮到什么程度？完整 prompt 怎么写？人会检查什么？

---

## 18.1 为什么 Java 后端特别适合 AI 辅助

Java 后端开发有三个特点让 AI 辅助的效果远超其他领域：

1. **强类型 + 编译期检查**。Controller → Service → DAO → Entity 的分层结构是天然的类型约束，AI 生成的代码一旦类型不匹配，编译器直接报错——不需要人肉排查。
2. **模式高度固定**。DTO 转 Entity、分页查询、参数校验、全局异常处理——每个 Java 项目的写法大同小异，AI 不需要"理解业务"就能产出 80% 正确率的代码。
3. **自动化验收门槛低**。`mvn test`，`mvn verify`，JaCoCo 覆盖率，Checkstyle 检查——生成的代码能不能用，跑一遍就知道了。

但 AI 在 Java 后端中也有致命短板：**事务边界判断**、**并发安全处理**、**业务规则的隐含假设**。本章每个工作流都标注了"常见 AI 错误"，处理这些短板就是人的核心价值。

---

## 18.2 Java 项目 CLAUDE.md 黄金模板

所有工作流的前提是 AI 理解你的项目上下文。下面是一个为 Spring Boot + MyBatis-Plus 项目优化的 CLAUDE.md 模板，直接复制到项目根目录。

```markdown
# <项目名>

<一句话描述项目业务领域>

## 技术栈

- Java 17, Spring Boot 3.x, Maven 3.9+
- ORM: MyBatis-Plus 3.5+, 禁止手写 SQL 拼接，用 LambdaQueryWrapper
- 数据库: MySQL 8.0
- 缓存: Redis (Spring Cache + @Cacheable)
- 权限: Spring Security + @PreAuthorize
- 日志: SLF4J + Logback
- 测试: JUnit 5 + Mockito 5 + Spring MockMvc
- 工具: Lombok, MapStruct, Hutool

## 项目分层

```
controller/   → 只做参数校验 + 调用 Service，不写业务逻辑
service/      → 接口定义
service/impl/ → 业务逻辑 + 事务管理
mapper/       → MyBatis-Plus BaseMapper，不写 SQL 除非复杂查询
entity/       → 数据库实体，@TableName @TableId @TableField
model/dto/    → 请求体，用 @Valid 做基础校验
model/vo/     → 响应体，禁止直接返回 Entity
model/query/  → 查询条件对象
config/       → Spring 配置类
common/       → 全局异常处理、统一返回体、工具类
```

## 代码规范

- 所有 Controller 返回 `Result<T>` 而不是裸数据或 `ResponseEntity`
- 所有 Service 方法加 `@Transactional(rollbackFor = Exception.class)`
- 禁止在 Controller 中写业务逻辑，禁止在 Service 中处理 HttpServletRequest
- Entity 和 DTO 的转换用 MapStruct，禁止手动 set/get
- 日志用 `log.info/warn/error`，不用 `System.out`
- 所有 SQL 关键词大写：`SELECT`, `WHERE`, `LEFT JOIN`
- 数据库字段命名用 snake_case，Java 字段用 camelCase

## 数据库约定

- 所有表必须有 `id`, `create_time`, `update_time`, `is_deleted` 四个字段
- 主键策略：`IdType.ASSIGN_ID`（雪花算法）
- 逻辑删除：`@TableLogic` + `is_deleted`
- 索引命名：`idx_<表名缩写>_<字段名>`
- 唯一约束命名：`uk_<表名缩写>_<字段名>`
- 禁止使用物理外键，关联关系在应用层维护

## 测试规范

- 单元测试类名：`<被测类名>Test`
- 方法命名：`<方法名>_<场景>_<预期结果>`
- MockMvc 测试用 `@AutoConfigureMockMvc` + `@SpringBootTest`
- 测试数据用 `@Sql` 初始化，测试后自动回滚
- 目标：Service 层分支覆盖率 > 80%

## AI 工作流约束

- 生成任何代码后，先跑 `mvn compile` 确认编译通过
- 修改 Service 层后，跑对应的单元测试
- 新增 Controller 后，检查 Swagger 文档是否正确渲染
- 新增数据库操作后，确认事务注解是否正确
- 任何涉及 SQL 的改动，在 review 阶段用 EXPLAIN 验证
```

这个模板放在项目根目录后，每次 AI 会话开始时它会自动理解你的分层结构、命名约定和技术约束——不需要每次都重新解释。

---

## 18.3 Java 后端 AI 开发速查表

| 任务 | 提示词章节 | 预计 AI 耗时 | 人工审查重点 |
|------|-----------|-------------|-------------|
| 新增 REST API 接口 | 18.4.1 | 3-5 分钟 | DTO 字段是否齐全、事务边界、权限注解 |
| 新增数据库表 | 18.4.2 | 2-3 分钟 | 索引设计、字段类型选择、分表策略 |
| 新增业务 Service | 18.4.3 | 5-8 分钟 | 事务传播行为、并发安全、业务规则正确性 |
| 增加参数校验 | 18.4.4 | 2-3 分钟 | 校验规则的业务含义、自定义校验器逻辑 |
| 增加权限校验 | 18.4.5 | 2-4 分钟 | 权限粒度是否合理、越权风险 |
| 增加操作日志 | 18.4.6 | 2-3 分钟 | 日志脱敏、日志级别、关键字段覆盖 |
| 增加全局异常处理 | 18.4.7 | 3-5 分钟 | 异常分类是否合理、异常信息是否泄露敏感数据 |
| 生成单元测试 | 18.4.8 | 3-5 分钟 | 断言是否有效、Mock 是否正确、是否 green-wash |
| 生成接口测试 | 18.4.9 | 4-6 分钟 | 测试数据隔离、请求构造完整性、清理逻辑 |
| 修复 Bug | 18.4.10 | 5-10 分钟 | 是否修了根因而非症状、是否引入新问题 |
| 重构代码 | 18.4.11 | 5-10 分钟 | 行为是否真的未变、测试是否仍然通过 |
| 优化 SQL | 18.4.12 | 4-7 分钟 | EXPLAIN 验证、索引有效性、数据量考量 |
| 排查线上问题 | 18.4.13 | 5-15 分钟 | 根因是否真正找到、修复方案的副作用 |

## 18.4 十三个核心工作流

### 18.4.1 工作流一：新增 REST API 接口

**适用场景**

产品给出明确的接口需求：资源名称、字段列表、CRUD 操作。需要从零产出 Controller + Service + DTO + VO + 单元测试的完整链路。

**输入材料**

- 接口需求描述（文字或 Markdown 格式）：包含资源名、字段名和类型、字段含义
- 项目的 CLAUDE.md（包含分层规范和命名约定）
- 可选的：Swagger / OpenAPI 文档片段
- 可选的：相关的已有 Entity 或表结构

**上下文要求**

CLAUDE.md 中必须包含：项目分层结构、统一返回体 `Result<T>` 的定义、MapStruct 的使用约定、Controller 和 Service 的命名规范。

**提示词（完整可复制）**

```
你是一个资深 Java 后端开发工程师。请根据以下需求，在本项目中新增一个完整的 REST API 接口。

## 业务需求

资源名：<资源名称，如 Order / Product / User>
需要的操作：<列出需要的 CRUD 操作，如 分页查询、根据ID查询、新增、修改、删除>

字段列表：
| 字段名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | String | 是 | 名称，最大50字符 |
| status | Integer | 是 | 状态：0-禁用 1-启用 |
| ... | ... | ... | ... |

## 生成要求

1. 严格遵循项目的分层结构：Controller → Service 接口 → Service Impl → Mapper → Entity → DTO/VO
2. Controller 只做参数校验和调用 Service，返回 `Result<T>` 统一响应体
3. DTO 类使用 `@Valid` 注解 + Jakarta Validation 约束（@NotBlank, @NotNull, @Size 等）
4. VO 类禁止直接返回 Entity，用 MapStruct 做转换
5. 分页查询使用 MyBatis-Plus 的 `Page<T>` + `LambdaQueryWrapper`
6. 所有 Service 方法加 `@Transactional(rollbackFor = Exception.class)`
7. Entity 包含 id（雪花ID）、createTime、updateTime、isDeleted
8. 生成对应的单元测试（JUnit 5 + Mockito），覆盖正常路径和异常路径
9. 生成后自己跑一遍 `mvn compile`，确保编译通过
10. Controller 方法上添加 Swagger/OpenAPI 注解（@Operation, @ApiModel 等）

## 特别注意

- 新增操作不需要传 id（后端自动生成）
- 修改操作必须校验 id 非空，并且数据存在
- 删除操作用逻辑删除（设置 is_deleted=1），不用物理删除
- 分页查询的排序字段必须做白名单校验，防止 SQL 注入
- 所有时间字段使用 LocalDateTime，序列化为 "yyyy-MM-dd HH:mm:ss"

请开始生成代码，每个文件单独列出，包含完整的 import 语句。
```

**Agent 执行步骤**

1. 读取项目的 CLAUDE.md 和已有的 Entity、Mapper、Controller 示例代码，了解命名风格和包结构。
2. 创建 Entity 类，添加 `@TableName`、`@TableId`、`@TableField`、`@TableLogic` 注解。
3. 创建 DTO 类（SaveDTO、UpdateDTO、QueryDTO）和 VO 类，添加校验注解和 Swagger 注解。
4. 创建 Mapper 接口，继承 `BaseMapper<Entity>`。
5. 创建 Service 接口，定义方法签名。
6. 创建 ServiceImpl，实现业务逻辑：分页查询用 Page + LambdaQueryWrapper，新增用 MapStruct 转换 DTO → Entity，修改前先查存在性。
7. 创建 Controller，注入 Service，每个方法调用 Service 并返回 Result。
8. 生成单元测试，Mock Service 依赖，验证 Controller 和 Service 的正确性。
9. 执行 `mvn compile` 确认编译通过，有错误自动修复。

**人工检查点**

- Controller 和 Service 生成完毕后：检查 API 路径是否合理（如 `/api/v1/orders`）、HTTP 方法是否正确（GET 查询、POST 新增、PUT 修改、DELETE 删除）。
- 单元测试生成后：检查 Mock 是否合理、断言是否有效（不是只调用方法而不验证返回值）。
- 全部完成后：检查 Swagger 文档是否正确渲染。

**输出验收清单**

- [ ] Controller 返回 `Result<T>` 而非裸对象
- [ ] DTO 包含完整的 `@Valid` 校验注解
- [ ] VO 不是直接返回 Entity
- [ ] Service 有 `@Transactional(rollbackFor = Exception.class)`
- [ ] 分页查询使用了 LambdaQueryWrapper 而非字符串拼接
- [ ] 排序字段做了白名单校验
- [ ] 删除操作用逻辑删除而非物理删除
- [ ] 修改操作校验了 id 非空和数据存在性
- [ ] 单元测试覆盖了正常路径和至少一个异常路径
- [ ] `mvn compile` 通过，无编译错误

**常见 AI 错误**

1. **忘记 `@Transactional`**。AI 经常在 ServiceImpl 中忘记添加事务注解，尤其是修改和删除操作。
2. **分页查询不做排序白名单校验**。AI 可能直接把前端传来的 sort 字段拼到 SQL 中，存在 SQL 注入风险。
3. **VO 直接返回 Entity**。AI 可能图省事直接在 Controller 中返回 Entity 对象，导致敏感字段（如 isDeleted、内部状态码）泄露到前端。
4. **修改操作不校验数据存在性**。AI 可能直接用 `updateById`，不先查一下数据是否存在，update 返回 0 也当成功。
5. **MapStruct 转换方法不完整**。AI 生成的 MapStruct 接口可能遗漏了某些字段的映射关系。

---

### 18.4.2 工作流二：新增数据库表

**适用场景**

根据业务需求设计一张新表，产出完整的 DDL 语句（含索引、自增策略、审计字段），同时生成对应的 Entity 类和 Mapper 接口。

**输入材料**

- 字段需求和业务描述
- 项目 CLAUDE.md 中的数据库约定（审计字段、命名规范、索引命名）
- 预估数据量和查询模式（用于索引设计）
- 可选的：ER 图或关联表信息

**上下文要求**

CLAUDE.md 中必须包含：审计字段约定（id、create_time、update_time、is_deleted）、索引命名规范（idx_、uk_ 前缀）、主键策略、字段命名风格。

**提示词（完整可复制）**

```
你是一个资深 DBA 兼 Java 后端开发。请根据以下需求设计数据库表并生成 DDL + Entity + Mapper。

## 表设计需求

表名：<表名，英文 snake_case>
业务描述：<这张表干什么用，一句话说清楚>

字段列表：
| 字段名(中文) | 字段名(英文) | 类型要求 | 是否必填 | 默认值 | 说明 |
|-------------|-------------|---------|---------|-------|------|
| 名称 | name | 字符串，最长100 | 是 | - | 显示名称 |
| 状态 | status | 整数 | 是 | 1 | 0-禁用 1-启用 |
| ... | ... | ... | ... | ... | ... |

## 查询模式（用于索引设计）

- 高频查询1：按 status + create_time 倒序分页查询
- 高频查询2：按 name 模糊查询
- 是否需要唯一约束：<如果 name 必须唯一则说明>

## 项目数据库约定

- 所有表必须包含 id（BIGINT, 雪花ID）, create_time（DATETIME）, update_time（DATETIME）, is_deleted（TINYINT, 逻辑删除）
- 主键用 IdType.ASSIGN_ID
- 索引命名：idx_<表名>_<字段名>
- 唯一约束：uk_<表名>_<字段名>
- 字段类型参考：金额用 DECIMAL(18,2)，状态用 TINYINT，文本用 VARCHAR(n)，长文本用 TEXT
- 存储引擎 InnoDB，字符集 utf8mb4，排序规则 utf8mb4_general_ci

## 生成要求

1. 完整 DDL 语句（CREATE TABLE），包含所有字段、索引、注释
2. 每个字段添加 COMMENT 注释（中文）
3. 根据查询模式设计合理索引，不要过度索引
4. 生成对应的 Entity 类（使用 Lombok @Data, MyBatis-Plus 注解）
5. 生成对应的 Mapper 接口（继承 BaseMapper）
6. 预估 1000 万数据量下的索引效果，给出 EXPLAIN 验证建议
```

**Agent 执行步骤**

1. 根据字段需求确定 MySQL 数据类型（VARCHAR 长度、DECIMAL 精度、TINYINT vs INT）。
2. 设计 DDL，包含主键、审计字段、业务字段，每个字段加 COMMENT。
3. 根据查询模式设计索引：高选择性字段建普通索引，高频组合查询建联合索引，唯一字段加唯一约束。
4. 生成 Entity 类，字段名用 camelCase，`@TableField` 指定映射的 snake_case 列名。
5. 生成 Mapper 接口，继承 `BaseMapper<Entity>`。
6. 输出 DDL 执行建议和 1000 万数据量下的 EXPLAIN 验证方案。

**人工检查点**

- DDL 生成后：检查字段类型是否合理（尤其是金额、枚举值、时间精度）。
- 索引设计后：用脑内执行计划验证——按你已知的查询模式，索引是否能覆盖 WHERE + ORDER BY？
- Entity 生成后：确认 `@TableLogic` 注解在 isDeleted 字段上。

**输出验收清单**

- [ ] DDL 包含 id、create_time、update_time、is_deleted 四个审计字段
- [ ] 所有字段都有 COMMENT 注释（中文）
- [ ] 索引数量合理（不超过 5-6 个，不为每个字段都建索引）
- [ ] 唯一约束有实际业务含义，不是随便加的
- [ ] varchar 长度经过思考（不是都写 255）
- [ ] 金额字段用 DECIMAL 而非 FLOAT/DOUBLE
- [ ] 状态字段用 TINYINT 而非 INT
- [ ] Entity 的 @TableField 正确映射了 snake_case 列名
- [ ] Mapper 正确继承 BaseMapper
- [ ] DDL 指定了 InnoDB + utf8mb4

**常见 AI 错误**

1. **为所有字段建索引**。AI 经常给每个字段都加一个单列索引，完全不考虑实际查询模式和数据分布。
2. **VARCHAR 长度随便写**。name 写 VARCHAR(255)，description 也写 VARCHAR(255)，不加区分。
3. **金额用 DOUBLE**。AI 不理解金额精度要求，用浮点类型导致计算误差。
4. **索引顺序不对**。联合索引 `(status, create_time)` 写成 `(create_time, status)`，导致按 status 筛选时无法使用索引。
5. **忘记 InnoDB/字符集**。DDL 末尾没有 `ENGINE=InnoDB DEFAULT CHARSET=utf8mb4`。

---

### 18.4.3 工作流三：新增业务 Service

**适用场景**

实现一个复杂的业务逻辑方法，涉及多表操作、事务管理、缓存更新、外部服务调用等。典型的例子：下单扣库存、审批流状态变更、对账逻辑。

**输入材料**

- 详细的业务规则描述（越具体越好，包括异常分支）
- 涉及的 Entity 和 Mapper 列表
- 外部依赖的接口定义（如消息队列、第三方 API）
- 项目的 CLAUDE.md（事务约定、日志规范）
- 可选的：现有的同类 Service 作为风格参考

**上下文要求**

CLAUDE.md 中必须包含：事务传播行为约定、日志级别使用规范、异常处理约定、缓存使用方式。

**提示词（完整可复制）**

```
你是一个资深 Java 后端开发工程师。请实现以下业务 Service 方法。

## 业务描述

Service 类：<Service 接口的全限定类名>
方法签名：<完整方法签名，如 public Result<OrderVO> createOrder(CreateOrderDTO dto)>

业务规则：
1. <规则1：校验逻辑>
2. <规则2：数据操作逻辑>
3. <规则3：异常处理逻辑>
4. <规则4：后续动作（发消息、清缓存等）>

涉及的数据库表：
- 表A：<读操作 / 写操作 / 行锁>
- 表B：<读操作 / 写操作 / 行锁>
- ...

外部依赖：
- <Redis：缓存什么 key>
- <MQ：发送什么消息到哪个 Topic>
- <第三方API：调用什么接口，失败怎么处理>

## 项目规范

- 事务注解：@Transactional(rollbackFor = Exception.class)
- 日志：log.info 记录关键步骤，log.error 记录异常
- 异常：使用项目统一的 BusinessException，不要抛裸 RuntimeException
- 缓存：用 Spring Cache 注解（@Cacheable / @CacheEvict / @CachePut）
- 禁止在 Service 中使用 HttpServletRequest / HttpServletResponse

## 特别注意

- 多表写操作必须在一个事务内完成
- 对于并发敏感的操作（如扣库存），考虑乐观锁或悲观锁
- 调用外部服务前先做所有业务校验，减少无效的外部调用
- 外部服务调用失败时，明确回滚策略：只回滚本事务还是需要补偿
- 每个分支路径添加日志，便于排查问题

## 生成要求

1. 完整的 ServiceImpl 方法实现，包含所有 import 语句
2. 在方法前用 JavaDoc 注释说明业务逻辑步骤
3. 每个可能抛出异常的地方，用明确的 BusinessException 并给出错误码
4. 关键操作前后添加 log.info
5. 生成对应的单元测试，覆盖正常路径和所有异常路径
6. 用 @MockBean 或 @Mock 模拟外部依赖

请开始实现。
```

**Agent 执行步骤**

1. 分析业务规则，识别核心流程和边界条件。
2. 用 JavaDoc 注释列出实现步骤（先写注释骨架，再填代码）。
3. 实现主流程：参数校验 → 业务规则校验 → 数据操作 → 缓存更新 → 外部通知。
4. 在每个校验失败点抛出 `BusinessException`，附带错误码和错误信息。
5. 关键步骤前后加 `log.info`，异常用 `log.error` 记录完整堆栈。
6. 生成单元测试，Mock 所有 Mapper 和外部依赖，逐场景验证。

**人工检查点**

- 事务边界是否正确：哪些操作应该在同一个事务中？外部调用是否应该在事务外？
- 并发安全：扣库存、改余额这类操作是否有并发控制（乐观锁/分布式锁）？
- 业务规则是否正确：校验顺序是否合理？异常分支是否覆盖全？

**输出验收清单**

- [ ] 方法上有 `@Transactional(rollbackFor = Exception.class)`
- [ ] 事务内没有远程调用（RPC、HTTP、MQ 发送）
- [ ] 所有校验失败都抛出 `BusinessException`，不抛裸 `RuntimeException`
- [ ] 并发敏感操作有锁机制（乐观锁版本号或 SELECT ... FOR UPDATE）
- [ ] 关键步骤有 `log.info`，异常有 `log.error`
- [ ] 方法有完整的 JavaDoc 注释
- [ ] 没有直接使用 `HttpServletRequest` 或 `HttpServletResponse`
- [ ] 单元测试覆盖了正常路径和所有业务异常路径
- [ ] 外部依赖的 Mock 正确设置了行为
- [ ] `mvn test` 针对该 Service 的测试全部通过

**常见 AI 错误**

1. **事务中包含远程调用**。AI 把发 MQ 消息或调 HTTP 接口写在事务方法里，导致事务时间过长或外部失败时回滚逻辑混乱。
2. **不处理并发**。对于库存扣减、余额变更，AI 直接 `updateById` 不做版本号或锁控制。
3. **异常吞没**。AI 在 catch 块中只打日志不重新抛出，导致事务不回滚。
4. **校验顺序倒置**。先调外部服务再做本地校验，浪费外部调用。
5. **BusinessException 不带错误码**。直接 `throw new BusinessException("失败了")`，没有错误码区分不同异常场景。

---

### 18.4.4 工作流四：增加参数校验

**适用场景**

现有接口参数校验不足，需要补充 `@Valid` 注解的基础校验（非空、长度、格式）和自定义业务规则校验（如手机号格式、枚举值范围、关联数据是否存在）。

**输入材料**

- 需要加校验的 Controller 方法及其 DTO 类
- 各字段的校验规则（必填/可选、长度限制、格式要求、业务规则）
- 项目中已有的自定义校验器（如 `@EnumValid`、`@Phone` 等）
- 统一异常处理中对 `MethodArgumentNotValidException` 的处理方式

**上下文要求**

CLAUDE.md 中应包含：使用的校验框架（Jakarta Validation / Hibernate Validator）、统一异常处理中校验异常的返回格式。

**提示词（完整可复制）**

```
你是一个 Java 后端开发工程师。请为以下接口增加完整的参数校验。

## 当前代码

DTO 类路径：<DTO 的全限定类名>
Controller 方法：<方法签名>

## 校验规则

对以下字段增加校验：

| 字段名 | 校验规则 |
|--------|---------|
| name | @NotBlank, 长度 1-50 |
| phone | @NotBlank, 手机号格式（自定义校验） |
| email | @NotBlank, 邮箱格式 @Email |
| age | @NotNull, @Min(0), @Max(150) |
| status | @NotNull, 必须为 0 或 1 中的一个 |
| type | @NotBlank, 必须是 ['A', 'B', 'C'] 中的一个 |
| orderId | 关联校验：如果 type='A' 则必填，否则可以为空 |

## 需要实现的自定义校验器

如果项目中没有合适的校验注解，请创建：

1. @Phone 注解 + PhoneValidator：验证中国大陆手机号（1 开头的 11 位数字）
2. @EnumValid 注解 + EnumValidator：验证字段值在指定枚举范围内
3. @ConditionalNotNull 注解 + ConditionalNotNullValidator：根据另一个字段的值决定当前字段是否必填

## 项目规范

- Controller 方法的 DTO 参数前加 @Valid 或 @Validated
- 统一异常处理器中处理 MethodArgumentNotValidException，返回字段级错误信息
- 自定义校验器放在 common/validator 包下
- 校验失败的错误信息用中文，方便前端展示

## 生成要求

1. 在 DTO 字段上添加对应的校验注解
2. 实现需要的自定义校验器（注解类 + 实现类）
3. 在 Controller 方法参数前添加 @Valid
4. 生成校验器的单元测试
5. 确保统一异常处理器能正确捕获和格式化校验异常

请开始实现。
```

**Agent 执行步骤**

1. 读取现有 DTO 和 Controller 代码。
2. 在 DTO 字段上添加 `@NotBlank`、`@NotNull`、`@Size`、`@Email`、`@Pattern` 等标准注解。
3. 检查项目中是否已有自定义校验器（`@Phone`、`@EnumValid`），没有则创建。
4. 实现自定义校验器的 `ConstraintValidator` 逻辑。
5. 在 Controller 方法参数前添加 `@Valid`。
6. 生成校验器和 Controller 的单元测试。

**人工检查点**

- 自定义校验器实现后：检查正则表达式是否正确、业务逻辑是否与需求一致。
- 全部完成后：用 curl 或 Postman 构造非法参数，验证校验是否生效，错误信息是否清晰。

**输出验收清单**

- [ ] 所有必填字段有 `@NotNull` 或 `@NotBlank`
- [ ] 字符串字段有长度限制 `@Size`
- [ ] Controller 方法参数前有 `@Valid` 注解
- [ ] 自定义校验器有完整的 `isValid` 实现
- [ ] 自定义校验器的注解包含默认错误消息
- [ ] 枚举校验覆盖了所有合法值
- [ ] 关联校验逻辑正确（条件必填）
- [ ] 统一异常处理器能返回字段级错误信息（如 `{"name":"名称不能为空"}`）
- [ ] 单元测试覆盖了每个字段的合法值和非法值
- [ ] 构造非法请求，返回 400 且错误信息清晰

**常见 AI 错误**

1. **只加 `@NotNull` 忘了 `@NotBlank`**。对 String 类型，`@NotNull` 允许空字符串 `""`，需要 `@NotBlank` 才能挡掉空串。
2. **`@Valid` 没加在 Controller 参数上**。DTO 里的注解写了，但 Controller 参数前没加 `@Valid`，校验完全不生效。
3. **正则写错**。`@Phone` 的正则可能允许 `1999999999`（10 位）或 `112345678901`（12 位）。
4. **自定义校验器的 `initialize` 方法是空的**。如果注解有属性（如 `@EnumValid(values = {...})`），必须在 initialize 中解析属性。
5. **校验异常没被统一处理器捕获**。写好了校验逻辑，但统一异常处理器里没处理 `MethodArgumentNotValidException`，前端收到 500 而不是 400。

---

### 18.4.5 工作流五：增加权限校验

**适用场景**

在现有接口上增加基于角色的权限控制（RBAC），使用 Spring Security 的 `@PreAuthorize` 注解或自定义权限表达式。

**输入材料**

- 需要加权限的 Controller 方法列表
- 角色定义：系统的角色枚举（如 ADMIN、MANAGER、USER）及其权限范围
- 权限规则：每个角色能访问哪些接口
- 自定义权限逻辑：如数据级权限（只能查自己部门的数据）

**上下文要求**

CLAUDE.md 中应包含：Spring Security 配置方式、权限校验的注解约定、自定义 PermissionEvaluator 的位置。

**提示词（完整可复制）**

```
你是一个 Java 后端开发工程师，熟悉 Spring Security。请为以下接口增加权限校验。

## 需要加权限的接口

| 接口路径 | HTTP 方法 | 需要的权限 |
|---------|----------|-----------|
| /api/v1/users | GET | ADMIN 或 MANAGER |
| /api/v1/users/{id} | GET | 本人、ADMIN 或 同部门 MANAGER |
| /api/v1/users | POST | ADMIN |
| /api/v1/users/{id} | PUT | ADMIN 或 本人 |
| /api/v1/users/{id} | DELETE | ADMIN |
| /api/v1/orders | GET | 登录即可 |
| /api/v1/orders/{id} | GET | 订单所属人或 ADMIN |

## 角色定义

- ROLE_ADMIN：系统管理员，全部权限
- ROLE_MANAGER：部门经理，管理本部门用户，查看本部门订单
- ROLE_USER：普通用户，管理自己的信息，查看自己的订单

## 需要实现的自定义权限逻辑

1. 数据级权限：MANAGER 只能操作本部门的用户数据
2. 资源归属校验：用户只能修改自己的个人信息（ADMIN 除外）
3. 一个自定义 @CheckOrderOwner 注解：校验当前用户是否是订单的所属人

## 项目规范

- 所有权限校验用 @PreAuthorize 注解，不用代码中的 if-else
- 自定义 PermissionEvaluator 放在 common/security 包下
- 权限表达式引用 Bean：@PreAuthorize("@orderSecurity.checkOrderOwner(#orderId)")
- 权限不足返回 403，统一异常处理器捕获 AccessDeniedException

## 生成要求

1. 在每个 Controller 方法上添加 @PreAuthorize 注解
2. 实现自定义 PermissionEvaluator（如 orderSecurity Bean）
3. 实现 @CheckOrderOwner 或等价的权限检查方法
4. 实现数据级权限过滤（Service 层自动拼接部门条件）
5. 生成权限校验的单元测试和接口测试
6. 确保未授权访问返回 403，错误信息不泄露内部细节

请开始实现。
```

**Agent 执行步骤**

1. 分析接口权限矩阵，确定每个接口需要的 SpEL 表达式。
2. 在 Controller 方法上添加 `@PreAuthorize`，简单权限用 `hasRole()`、`hasAuthority()`。
3. 实现数据级权限：在 Service 中注入 `SecurityContextHolder`，获取当前用户的部门 ID，拼接到查询条件中。
4. 实现资源归属校验：创建 OrderSecurity Bean，提供 `checkOrderOwner(orderId)` 方法。
5. 确保统一异常处理器能返回友好的 403 响应。
6. 生成测试：Mock 不同角色的 SecurityContext，验证权限行为。

**人工检查点**

- 权限矩阵是否完整：有没有接口漏加权限或权限过宽？
- 数据级权限是否正确：Manager 真的只能看到本部门数据吗？分页查询也做了过滤？
- 越权风险：用户 A 能否通过修改 URL 中的 ID 访问用户 B 的数据？

**输出验收清单**

- [ ] 所有需要权限的接口有 `@PreAuthorize` 注解
- [ ] 角色的权限粒度合理（没有过度授权）
- [ ] 数据级权限在 Service 层实现，不在 Controller 层
- [ ] 资源归属校验不依赖前端传来的参数（从 SecurityContext 取当前用户）
- [ ] 权限不足返回 403 和清晰的错误信息
- [ ] 错误信息不泄露内部系统细节（如包名、SQL 信息）
- [ ] 权限相关的 Bean 有单元测试
- [ ] 接口测试覆盖了不同角色的正常和越权场景
- [ ] Spring Security 配置中启用了 `@EnableGlobalMethodSecurity(prePostEnabled = true)`
- [ ] 权限检查不产生 N+1 查询问题

**常见 AI 错误**

1. **用 `hasRole('ADMIN')` 而不是 `hasRole('ROLE_ADMIN')`**。Spring Security 的 `hasRole` 会自动加 `ROLE_` 前缀，如果传了 `'ROLE_ADMIN'` 实际会变成 `ROLE_ROLE_ADMIN`。
2. **数据级权限只过滤了列表查询，忘了详情查询**。Manager 查列表只能看到本部门，但直接访问 `/users/{id}` 可能看到其他部门的人。
3. **在 Controller 层做数据级过滤**。应该在 Service 层统一处理，否则新增接口容易漏掉。
4. **`SecurityContextHolder.getContext()` 在单元测试中返回 null**。需要 Mock SecurityContext 或用 `@WithMockUser` 注解。
5. **权限表达式中的方法参数名写错**。`@PreAuthorize("@checker.check(#id)")` 但方法参数名是 `userId`。

---

### 18.4.6 工作流六：增加操作日志

**适用场景**

为关键操作（新增、修改、删除、审批、导出）增加审计日志，记录操作人、操作时间、操作内容、IP 地址。日志可用于审计追踪和问题排查。

**输入材料**

- 需要记录日志的 Controller 或 Service 方法列表
- 日志需要记录的信息：操作类型、操作对象、操作前后数据对比
- 日志存储方式：存数据库还是发送到日志中心（ELK）
- 项目的 AOP 使用方式（Spring AOP + 自定义注解）

**上下文要求**

CLAUDE.md 中应包含：AOP 日志切面的约定、日志表结构（如果有）、操作日志注解 `@Log` 的定义。

**提示词（完整可复制）**

```
你是一个 Java 后端开发工程师。请为关键业务操作增加操作日志功能。

## 需要记录日志的操作

| 操作 | 接口路径 | 记录内容 |
|------|---------|---------|
| 新增用户 | POST /api/v1/users | 操作人、新增的用户信息 |
| 修改用户 | PUT /api/v1/users/{id} | 操作人、修改前后的字段差异 |
| 删除用户 | DELETE /api/v1/users/{id} | 操作人、被删除的用户 ID |
| 审核通过 | POST /api/v1/orders/{id}/approve | 操作人、订单 ID、审核结果 |
| 导出数据 | GET /api/v1/orders/export | 操作人、导出条件、导出条数 |

## 日志记录方式

- 使用自定义 @Log 注解 + Spring AOP 切面
- 操作日志写入独立的 oper_log 表（异步，不阻塞主流程）
- 日志字段：操作人、操作时间、IP 地址、操作类型、操作模块、操作描述、请求参数、操作结果（成功/失败）、耗时（ms）

## 项目规范

- 注解定义在 common/annotation 包下
- 切面实现用 @Aspect + @Around
- 日志写入使用异步（@Async 或 Event 机制），不影响主流程性能
- 敏感字段（密码、身份证号、手机号）在日志中脱敏
- 获取操作人：从 SecurityContextHolder 取当前登录用户

## 生成要求

1. 定义 @Log 注解：包含操作类型（optType）、操作模块（module）、操作描述（desc）
2. 实现 LogAspect 切面：在 @Around 中拦截带 @Log 的方法
3. 创建 oper_log 表 DDL 和对应的 Entity + Mapper
4. 在切面中异步保存日志（用 @Async + 线程池配置）
5. 实现敏感信息脱敏工具类（手机号 138****1234，身份证号前 3 后 4 保留）
6. 在目标 Controller 方法上添加 @Log 注解
7. 生成切面的单元测试

请开始实现。
```

**Agent 执行步骤**

1. 创建 `@Log` 注解，包含 `optType()`、`module()`、`desc()` 属性。
2. 创建 `LogAspect` 切面，用 `@Around("@annotation(log)")` 拦截方法调用。
3. 在切面中：记录开始时间 → 执行原方法 → 记录结束时间和结果 → 异步保存日志。
4. 创建 `oper_log` 表的 DDL 和对应的 Entity、Mapper、Service。
5. 实现敏感信息脱敏工具类。
6. 在目标方法上添加 `@Log(optType = ..., module = ..., desc = ...)`。
7. 配置异步线程池（`@EnableAsync` + `ThreadPoolTaskExecutor`）。

**人工检查点**

- 脱敏实现后：检查密码、身份证号、手机号是否正确脱敏，银行卡号是否需要脱敏？
- 异步配置后：检查线程池参数是否合理（核心线程数、队列大小、拒绝策略）。
- 全部完成后：实际触发一个操作，检查日志表是否正确写入，脱敏是否生效。

**输出验收清单**

- [ ] @Log 注解包含 optType、module、desc 三个必要属性
- [ ] 切面正确拦截了带 @Log 的方法
- [ ] 日志保存是异步的，不阻塞主流程
- [ ] oper_log 表有完整的审计字段
- [ ] 操作人从 SecurityContext 获取，不是从请求参数
- [ ] IP 地址正确获取（考虑了代理和负载均衡的情况）
- [ ] 敏感字段（密码、手机号、身份证号）已脱敏
- [ ] 日志记录了操作结果（成功/失败）和耗时
- [ ] 异步线程池配置了合理的参数
- [ ] 异步保存失败不影响主流程（catch 异常并打日志）

**常见 AI 错误**

1. **日志保存同步执行**。AI 直接在切面内同步写数据库，每个请求增加几十毫秒的数据库写入延迟。
2. **不脱敏或脱敏不完整**。把完整手机号、身份证号写进日志表，合规风险。
3. **IP 获取方式错误**。直接取 `request.getRemoteAddr()`，没考虑 Nginx 反向代理的情况（应该先取 X-Forwarded-For）。
4. **AOP 切面表达式写错**。切点表达式没有正确匹配到带 `@Log` 的方法，注解加了切面不生效。
5. **异步线程池没配置**。加了 `@Async` 但没配 `ThreadPoolTaskExecutor`，默认用 SimpleAsyncTaskExecutor 为每个任务创建新线程。

---

### 18.4.7 工作流七：增加全局异常处理

**适用场景**

项目缺少统一的异常处理机制，不同模块的异常响应格式不一致。需要建立异常类体系 + `@ControllerAdvice` 全局处理器 + 统一的错误码枚举。

**输入材料**

- 项目中现有的异常处理方式和返回格式
- 需要处理的异常类型：参数校验异常、权限异常、业务异常、系统异常
- 期望的异常响应格式
- 统一的错误码体系

**上下文要求**

CLAUDE.md 中应包含：统一返回体 `Result<T>` 的结构、错误码的分段规划（如 4xxxx 客户端错误，5xxxx 服务端错误）。

**提示词（完整可复制）**

```
你是一个 Java 后端架构师。请为本项目建立完整的全局异常处理体系。

## 当前问题

项目中异常处理混乱：有的 catch 后返回 Result.fail("失败")，有的直接 throw RuntimeException，前端收到的错误信息格式不统一，排查问题困难。

## 期望的异常体系

```
RuntimeException
├── BusinessException（业务异常，返回给用户）
│   ├── DataNotFoundException（数据不存在）
│   ├── DataConflictException（数据冲突，如唯一键重复）
│   ├── PermissionDeniedException（权限不足）
│   └── ParamInvalidException（参数校验失败，非 @Valid）
└── SystemException（系统异常，不返回详情给用户，只记录日志）
    ├── RpcException（远程调用失败）
    ├── DbException（数据库异常）
    └── ConfigException（配置异常）
```

## 错误码规划

- 40000-40999：参数校验错误
- 41000-41999：数据错误（不存在、冲突）
- 42000-42999：权限错误
- 50000-50999：系统内部错误
- 51000-51999：外部依赖错误

## 期望的异常响应格式

```json
{
  "code": 40001,
  "message": "用户名不能为空",
  "detail": null,
  "timestamp": "2026-07-01T10:30:00"
}
```

## 需要处理的异常类型

1. MethodArgumentNotValidException → 提取字段级错误，转成友好提示
2. BindException → 同上
3. ConstraintViolationException → 同上
4. HttpMessageNotReadableException → 请求体格式错误
5. MissingServletRequestParameterException → 缺少必填参数
6. HttpRequestMethodNotSupportedException → 请求方法错误
7. AccessDeniedException → 无权限
8. AuthenticationException → 未登录
9. BusinessException 及其子类 → 返回对应的错误码和信息
10. Exception（兜底） → 记录完整堆栈，返回通用系统错误

## 生成要求

1. 创建完整的异常类体系（exception 包）
2. 创建错误码枚举 ErrorCodeEnum（包含所有错误码）
3. 创建 GlobalExceptionHandler（@RestControllerAdvice）
4. 在处理器中对每种异常类型做精确匹配，返回对应的错误信息
5. BusinessException 的子类携带错误码
6. 系统异常不暴露内部细节给前端（message 返回"系统繁忙，请稍后重试"）
7. 生成 GlobalExceptionHandler 的单元测试
8. 在项目 CLAUDE.md 中补充异常处理规范

请开始实现。
```

**Agent 执行步骤**

1. 创建异常类体系：`BusinessException`（带错误码）→ 四个子异常类。
2. 创建 `SystemException`（带错误码）→ 三个子异常类。
3. 创建 `ErrorCodeEnum` 枚举，覆盖所有错误码和对应的中文描述。
4. 创建 `GlobalExceptionHandler`，对每种异常类型编写 `@ExceptionHandler` 方法。
5. `MethodArgumentNotValidException` 的处理要提取字段级错误信息。
6. 兜底的 `Exception` 处理器记录完整堆栈到日志，返回通用错误信息。
7. 生成单元测试：Mock 不同异常场景，验证返回的 code 和 message。

**人工检查点**

- 异常类体系定义后：检查分类是否合理，是否所有业务场景都能归到某个异常子类。
- 处理器实现后：检查系统异常是否真的不泄露内部信息（DB 连接池耗尽时，不应把 "Connection pool exhausted" 返回给前端）。
- 单元测试后：检查是否覆盖了所有异常类型。

**输出验收清单**

- [ ] BusinessException 有 code 和 message 两个属性
- [ ] ErrorCodeEnum 覆盖了所有业务错误场景
- [ ] GlobalExceptionHandler 使用 @RestControllerAdvice 而非 @ControllerAdvice
- [ ] MethodArgumentNotValidException 返回字段级错误
- [ ] 系统异常对前端返回通用提示，不暴露堆栈
- [ ] log.error 记录了完整堆栈用于排查
- [ ] 兜底 Exception 处理器放在最后（优先级最低）
- [ ] AccessDeniedException 返回 403
- [ ] AuthenticationException 返回 401
- [ ] 单元测试覆盖了所有 @ExceptionHandler 方法

**常见 AI 错误**

1. **异常处理器顺序不对**。如果泛型的 `Exception` 处理器排在具体异常前面，具体异常永远不会被匹配到。
2. **系统异常泄露内部信息**。`DbException` 直接把 SQL 错误消息返回给前端，暴露表名和字段名。
3. **忘记处理 `ConstraintViolationException`**。只在 Controller 参数上用了 `@Validated` 而不是 `@Valid` 时触发这个异常，容易遗漏。
4. **日志级别用错**。业务异常（如数据不存在）用 `log.error` 记录，导致告警泛滥。
5. **错误码不统一**。同一个"数据不存在"场景，`DataNotFoundException` 的 code 是 41001，直接 throw `BusinessException` 的 code 是 41002。

---

### 18.4.8 工作流八：生成单元测试（JUnit 5 + Mockito）

**适用场景**

为已有的 Service 或工具类生成单元测试，覆盖正常路径、异常路径、边界值。适合已有稳定代码的"补测试"场景。

**输入材料**

- 需要测试的类的完整源代码
- 类中依赖的 Mapper/Repository/外部服务接口
- 项目的测试基类（如有）和测试约定
- 可选的：业务规则文档，帮助 AI 理解边界条件

**上下文要求**

CLAUDE.md 中应包含：测试命名规范、Mock 使用约定、断言库偏好（AssertJ vs JUnit 原生）、覆盖率目标。

**提示词（完整可复制）**

```
你是一个 Java 后端测试专家。请为以下 Service 类生成完整的单元测试。

## 被测类

```java
<粘贴完整的 ServiceImpl 源代码>
```

## 项目测试规范

- JUnit 5 + Mockito 5（使用 @ExtendWith(MockitoExtension.class)）
- Mock 依赖用 @Mock，注入被测对象用 @InjectMocks
- 断言使用 AssertJ（assertThat(xxx).isEqualTo(...)）
- 测试方法命名：<方法名>_<场景>_<预期结果>
- 覆盖目标：分支覆盖率 > 80%
- 不使用 @SpringBootTest（单元测试不启动 Spring 容器）
- 测试数据在测试方法内构造，不使用 @Sql

## 需要覆盖的场景

对每个 public 方法，生成以下场景的测试：
1. 正常路径：传入合法参数，验证返回值正确
2. 异常路径：每个 throw 语句对应一个测试
3. 边界值：null、空集合、空字符串、最大值、最小值
4. Mock 验证：验证 Mapper 方法是否以正确的参数被调用

## 生成要求

1. 每个测试方法使用 Given-When-Then 结构，用注释分隔
2. 使用 verify() 验证 Mapper 方法调用次数和参数
3. 使用 ArgumentCaptor 捕获传给 Mapper 的参数并验证
4. 不要生成 green-wash 测试（只调方法不验结果）
5. 每个断言必须验证具体的返回值或副作用
6. 使用 @DisplayName 注解描述测试意图（中文）

请开始生成测试代码。
```

**Agent 执行步骤**

1. 读取被测 Service 的完整源代码，识别所有 public 方法。
2. 分析每个方法的分支逻辑（if-else、switch、try-catch），标记每个分支作为一个测试场景。
3. 对于每个测试场景，确定需要 Mock 的依赖和被 Mock 方法的返回值。
4. 按 Given-When-Then 结构编写测试方法：Given（Mock 设置）→ When（调用方法）→ Then（断言结果 + 验证 Mock 调用）。
5. 运行 `mvn test` 验证测试通过，修复编译和逻辑错误。

**人工检查点**

- 测试生成后：挑 2-3 个核心测试方法检查——Mock 的设置是否合理？断言是否有效？
- 运行覆盖率报告：`mvn test jacoco:report`，检查分支覆盖率是否 > 80%。
- 检查是否有测试只是调用了方法但没做任何断言（green-wash）。

**输出验收清单**

- [ ] 每个测试方法有 @Test 和 @DisplayName 注解
- [ ] 使用 @Mock 而不是 @MockBean（单元测试不启动 Spring 容器）
- [ ] 所有外部依赖（Mapper、Service、RPC）都被 Mock
- [ ] 每个测试方法有明确的断言（assertThat / assertEquals / assertThrows）
- [ ] 异常路径使用了 assertThrows(ExpectedException.class, () -> {...})
- [ ] 使用 verify() 验证 Mapper 方法被正确调用
- [ ] ArgumentCaptor 用于验证传给依赖的参数
- [ ] 没有直接用 new 创建被测对象（用 @InjectMocks）
- [ ] 覆盖率 > 80%（通过 jacoco 报告验证）
- [ ] 所有测试方法独立运行（不依赖执行顺序）

**常见 AI 错误**

1. **Generated green-wash tests**。AI 生成的测试只调用方法，不做任何断言，覆盖率显示 green 但实际没验证任何业务逻辑。
2. **Mock 了不需要 Mock 的对象**。对于纯 POJO 转换（如 MapStruct），不需要 Mock。
3. **用 @MockBean 和 @SpringBootTest 写单元测试**。这是集成测试的写法，单元测试不需要启动 Spring 容器。
4. **Mockito.when() 的参数与实际调用不匹配**。Mock 设置的参数和实际方法调用参数不完全一样，Mockito 返回 null。
5. **异常测试不检查异常消息**。`assertThrows` 只验证了异常类型，没验证异常消息内容，可能抓到错误的异常。

---

### 18.4.9 工作流九：生成接口测试

**适用场景**

为 REST API 生成集成测试，使用 Spring MockMvc 模拟 HTTP 请求，验证完整的请求-响应链路。适合 Controller 开发完成后的冒烟测试和回归测试。

**输入材料**

- Controller 的完整源代码
- 相关的 DTO / VO 类
- API 的 Swagger 文档或接口说明
- 测试数据（数据库中的初始数据或 SQL 初始化脚本）
- 权限规则（不同角色的访问权限）

**上下文要求**

CLAUDE.md 中应包含：测试基类、测试数据初始化方式（@Sql 或 TestDataFactory）、认证测试的处理方式（@WithMockUser）。

**提示词（完整可复制）**

```
你是一个 Java 后端测试专家。请为以下 Controller 生成完整的接口测试（集成测试）。

## 被测 Controller

```java
<粘贴完整的 Controller 源代码>
```

## DTO 定义

```java
<粘贴请求 DTO 的定义>
```

## 接口说明

| 接口 | 方法 | 认证要求 | 说明 |
|------|------|---------|------|
| /api/v1/users | GET | 登录即可 | 分页查询 |
| /api/v1/users/{id} | GET | 登录即可 | 详情查询 |
| /api/v1/users | POST | ADMIN | 新增用户 |
| /api/v1/users/{id} | PUT | ADMIN | 修改用户 |
| /api/v1/users/{id} | DELETE | ADMIN | 删除用户 |

## 项目测试规范

- 使用 @SpringBootTest + @AutoConfigureMockMvc
- 权限测试使用 @WithMockUser(roles = "ADMIN") 或自定义 SecurityContext
- 测试数据用 @Sql 注解初始化（放在 src/test/resources/sql/ 下）
- 测试后数据自动回滚（@Transactional）
- 测试方法命名：<场景>_<预期结果>

## 需要覆盖的场景

对每个接口，生成以下场景的测试：
1. 正常场景：合法请求，验证 200 + 响应体结构
2. 参数校验：非法参数（空名称、超长名称、非法枚举值），验证 400 + 错误信息
3. 权限场景：无权限用户访问，验证 403
4. 数据边界：查询不存在的 ID，验证 404 或空结果
5. 业务规则：重复新增、状态不允许修改等，验证业务错误码

## 生成要求

1. 使用 MockMvc 构造请求，`.perform(get/post/put/delete)`
2. 使用 jsonPath 断言响应字段（如 `jsonPath("$.code").value(200)`）
3. 使用 @Sql 初始化测试数据
4. 每个测试方法独立，不依赖执行顺序
5. 打印请求和响应用于调试（`andDo(print())`）

请开始生成测试代码。
```

**Agent 执行步骤**

1. 分析 Controller 的每个接口方法，识别需要测试的场景。
2. 准备 SQL 初始化脚本：插入必要的测试数据。
3. 编写测试类，对每个接口生成多个测试方法（正常、参数校验、权限、边界）。
4. 使用 MockMvc 的 `.perform()` 构造请求，`.andExpect(jsonPath(...))` 断言响应。
5. 运行测试验证全部通过。

**人工检查点**

- 测试数据准备后：检查 SQL 脚本是否正确，数据之间是否互相冲突。
- 测试运行后：`mvn test` 确认全部通过，检查是否有测试依赖特定执行顺序。
- 权限测试：确认用不同角色跑了相同接口，且结果符合预期。

**输出验收清单**

- [ ] 测试类使用了 @SpringBootTest + @AutoConfigureMockMvc
- [ ] 测试数据通过 @Sql 初始化
- [ ] 测试类标注了 @Transactional 自动回滚
- [ ] 每个接口至少有一个正常场景测试
- [ ] 参数校验场景验证了 400 和具体错误信息
- [ ] 权限场景使用了 @WithMockUser 模拟不同角色
- [ ] 使用了 jsonPath 验证响应体结构
- [ ] 异常场景验证了业务错误码（不仅是 HTTP 状态码）
- [ ] 请求体中正确序列化了 JSON（用 content() + contentType）
- [ ] 所有测试独立可运行（不依赖顺序）

**常见 AI 错误**

1. **测试数据互相污染**。测试 A 修改了数据库记录，测试 B 依赖该记录的原始值，导致测试 B 时过时不过。
2. **忘记设置 Content-Type**。POST/PUT 请求缺少 `contentType(MediaType.APPLICATION_JSON)`，Spring 无法解析请求体。
3. **jsonPath 路径写错**。响应体是 `{"data": {"name": "xxx"}}`，jsonPath 写成了 `$.name` 而不是 `$.data.name`。
4. **MockMvc 不打印结果**。测试失败时没有 `.andDo(print())`，排查问题只能靠猜。
5. **@WithMockUser 的 roles 参数自动加 ROLE_ 前缀**。`@WithMockUser(roles = "ADMIN")` 实际权限是 `ROLE_ADMIN`，如果 `@PreAuthorize` 里写的是 `hasRole('ADMIN')` 才对。

---

### 18.4.10 工作流十：修复 Bug

**适用场景**

收到 Bug 报告（含错误描述、复现步骤、堆栈信息），需要定位根因、修复代码、编写回归测试、验证修复效果。

**输入材料**

- Bug 描述：发生了什么、期望什么、什么条件下触发
- 复现步骤（如有）
- 错误日志 / 堆栈信息
- 相关代码路径
- 可选的：相关数据的状态（数据异常导致的 Bug）

**上下文要求**

CLAUDE.md 中应包含：调试命令、日志查看方式、本地环境启动命令。

**提示词（完整可复制）**

```
你是一个 Java 后端调试专家。请分析以下 Bug 并修复。

## Bug 描述

**问题现象**：<用户看到什么，如"新增订单时偶尔返回 500">
**期望行为**：<应该怎样，如"正常返回 200 和订单信息">
**触发条件**：<什么时候发生，如"订单金额超过 100 万时">
**影响范围**：<影响多少用户 / 多少次请求>
**复现步骤**：
1. <步骤1>
2. <步骤2>
3. <观察到的错误>

## 错误信息

```
<粘贴完整的堆栈信息>
```

## 相关代码

```java
<粘贴相关的 Controller/Service/Mapper 代码>
```

## 修复要求

1. **先分析根因**：不要直接改代码，先定位堆栈中真正出错的那一行，分析为什么出错
2. **给出根因分析**：说明为什么代码会走到这个错误路径，是代码 bug 还是数据问题还是设计缺陷
3. **给出修复方案**：说明怎么修，为什么这样修，有没有副作用
4. **实现修复**：修改代码
5. **编写回归测试**：确保同样的场景不会再出错
6. **输出排查建议**：如果是数据问题，给出数据修复 SQL；如果是并发问题，给出复现条件分析

## 项目规范

- 修复后跑 `mvn test` 确保不会破坏现有测试
- 新增测试覆盖这次 Bug 的场景
- 注释说明修复原因（不是注释掉代码）

请开始分析并修复。
```

**Agent 执行步骤**

1. 解析堆栈信息，定位出错的类、方法、行号。
2. 阅读相关代码，分析执行路径：什么条件下会走到这个错误点？
3. 给出根因分析：是代码逻辑错误、空指针问题、并发问题、还是数据异常？
4. 给出修复方案：修改代码的具体方式和理由。
5. 实现修复，确保修改最小化（不顺便重构无关代码）。
6. 编写回归测试：精确重现 Bug 场景，验证修复后不再出错。
7. 运行 `mvn test` 确认所有测试通过。

**人工检查点**

- 根因分析后：确认 AI 的分析是否正确，是否真的找到了根本原因而不是表面症状。
- 修复方案确认：确认修复不会引入新问题，尤其是在并发、事务、缓存场景下。
- 回归测试验证：确认测试真的能复现原始 Bug。

**输出验收清单**

- [ ] 根因分析准确，定位到了具体出错的代码行
- [ ] 修复方案最小化（没有顺带重构无关代码）
- [ ] 修复代码有完整的注释说明修复原因
- [ ] 新增回归测试覆盖 Bug 场景
- [ ] `mvn test` 全部通过，包括历史测试和新增测试
- [ ] 如果是空指针问题，考虑了上游数据可能为空的兜底方案
- [ ] 如果是并发问题，修复不是简单加 synchronized
- [ ] 如果是数据问题，输出了数据修复 SQL 和预防方案
- [ ] 分析报告包含 Bug 的根因分类（代码/数据/设计/并发）
- [ ] 没有注释掉报错代码来绕过问题

**常见 AI 错误**

1. **只修症状不修根因**。看到 NullPointerException 就在前面加 if(x != null)，不去查为什么 x 会是 null。
2. **顺手大重构**。修一个空指针同时"顺便优化"了方法结构、改了变量名、拆了方法——引入新 Bug 的风险远大于收益。
3. **try-catch 吞异常**。用 try-catch 包裹报错代码但不处理异常，让 Bug 从 500 变成静默失败。
4. **不写回归测试**。修完代码就算完，下次类似的场景还会出错。
5. **并发问题用 synchronized 简单粗暴解决**。可能导致性能大幅下降，更好的方案是乐观锁或分布式锁。

---

### 18.4.11 工作流十一：重构代码

**适用场景**

代码能正常运行但结构不理想：方法太长、类职责不清、命名不准确、重复代码多。需要在保证行为不变的前提下改善代码结构。

**输入材料**

- 需要重构的代码（完整的文件内容）
- 重构目标：提取方法、提取类、改善命名、消除重复
- 相关的单元测试（重构的安全网）
- 项目的 CLAUDE.md（编码规范和命名约定）

**上下文要求**

CLAUDE.md 中应包含：代码规范（方法长度上限、命名风格）、分层约定。

**提示词（完整可复制）**

```
你是一个 Java 后端代码重构专家。请对以下代码进行安全重构。

## 重构代码

```java
<粘贴需要重构的完整文件>
```

## 重构目标

- <目标1：如 ServiceImpl 的 processOrder 方法 200 行，需要拆成多个小方法>
- <目标2：如 OrderHelper 名字太模糊，改为更具体的名字>
- <目标3：如 3 个 Service 中都有相同的日期格式化逻辑，提取到公共工具类>

## 重构原则

1. **行为不变**：重构后所有现有单元测试必须通过
2. **每次一小步**：每次只做一种重构（先提取方法，测试通过后再提取类）
3. **不混入新功能**：重构期间不修改任何业务逻辑
4. **保留原有测试**：不要修改测试内容，除非是因为接口签名变化

## 具体操作（按顺序）

### 第一步：提取方法
- 将 processOrder 方法中"校验订单"的逻辑提取为 validateOrder 方法
- 将"计算金额"的逻辑提取为 calculateAmount 方法
- 将"保存订单"的逻辑提取为 saveOrder 方法
- 每个提取的方法不超过 30 行

### 第二步：改善命名
- OrderHelper → OrderPriceCalculator（更准确描述职责）
- temp → 改为有意义的变量名
- flag → 改为 isOverdue 或具体的布尔变量名

### 第三步：消除重复
- 3 处日期格式化的 yyyy-MM-dd HH:mm:ss 提取为常量
- 3 处相同的权限检查逻辑提取到 SecurityUtils

## 验证要求

重构完每一步后执行：
1. `mvn compile` 确认编译通过
2. `mvn test` 确认所有测试通过
3. 对比重构前后的 diff，确认没有改动业务逻辑

请开始重构，每一步完成后报告结果。
```

**Agent 执行步骤**

1. 分析代码结构，识别可以提取的方法、需要改善的命名、重复代码。
2. 按优先级排序：先提取方法，再改善命名，最后消除重复。
3. 每一步：修改代码 → `mvn compile` → `mvn test` → 确认通过 → 进入下一步。
4. 如果某一步测试失败，立即回滚该步修改，分析原因。
5. 全部完成后输出重构总结：做了什么、为什么做、效果如何。

**人工检查点**

- 每个重构步骤完成后：检查测试是否全部通过，diff 是否只包含结构变化。
- 提取方法后：检查新方法的命名是否准确、参数是否合理、职责是否单一。
- 全部完成后：用 IDE 的 compare 功能对比重构前后的逻辑，确保没有意外修改。

**输出验收清单**

- [ ] 重构后所有单元测试通过（包括未修改过的测试）
- [ ] 提取的方法有明确的单一职责
- [ ] 每个方法不超过 30 行（不包括注释）
- [ ] 变量和方法命名有具体含义，不含 temp/flag/data 等模糊词汇
- [ ] 重复代码已提取到公共方法或工具类
- [ ] 没有修改任何业务逻辑
- [ ] 没有新增功能（连日志都不加）
- [ ] 类名准确描述类的职责
- [ ] 重构步骤可追溯（建议每步一个 commit）
- [ ] JaCoCo 覆盖率没有下降

**常见 AI 错误**

1. **重构 + 修 Bug 混在一起**。说是重构，顺手修了 3 个 Bug、改了 2 个业务规则——导致行为变化无法追溯。
2. **过度提取**。把 2 行的 setter 也提取成方法，反而降低可读性。
3. **提取方法的参数顺序不合理**。传了 5 个参数，但其中 3 个可以包装成一个参数对象。
4. **改命名只改类名不改引用**。在 IDE 中重构可以用 Rename，AI 直接改代码容易漏掉某个引用导致编译失败。
5. **重构后测试不跑**。改完代码就认为没问题，结果某个隐含依赖的测试挂掉了。

---

### 18.4.12 工作流十二：优化 SQL

**适用场景**

发现慢查询（通过慢查询日志、APM、用户反馈），需要分析慢查询原因、优化 SQL 和索引、验证优化效果。

**输入材料**

- 原始 SQL 语句
- 表结构（DDL + 已有索引）
- 数据量（近似行数）
- EXPLAIN 输出（执行计划）
- 查询的业务场景（是否允许范围查询、是否需要精确分页）
- 可选的：慢查询日志中的执行时长

**上下文要求**

CLAUDE.md 中应包含：MyBatis-Plus 的 SQL 编写约定、索引命名规范。

**提示词（完整可复制）**

```
你是一个 MySQL 性能优化专家。请分析以下慢查询并给出优化方案。

## 慢查询信息

**原始 SQL**：
```sql
<粘贴原始 SQL>
```

**执行时长**：<如 平均 3.5s，最高 12s>
**调用频率**：<如 每分钟 200 次>
**表数据量**：<如 orders 表 500 万行，order_items 表 2000 万行>

## 表结构

```sql
<粘贴相关表的 DDL（SHOW CREATE TABLE）>
```

## EXPLAIN 输出

```sql
<粘贴 EXPLAIN 的输出>
```

## 业务约束

- <约束1：如 查询只查近 3 个月的数据>
- <约束2：如 分页最多翻到第 100 页>
- <约束3：如 status 字段只有 3 个值：0 10% 1 80% 2 10%>

## 优化要求

1. **分析执行计划**：指出 EXPLAIN 中的问题点（type、key、rows、Extra）
2. **给出优化方案**：
   - 是否需要新增/修改索引？
   - SQL 写法是否可以优化？
   - 是否需要分表 / 数据归档？
3. **给出优化后的 SQL 和 DDL**
4. **用 EXPLAIN 验证**：给出优化后执行计划的预期效果
5. **风险评估**：改索引对写入性能的影响、锁表风险
6. **给出 MyBatis-Plus 版本**：如果项目用 MyBatis-Plus，给出等价的 LambdaQueryWrapper 写法

## 常见优化方向

- 索引失效：函数包裹字段、隐式类型转换、LIKE '%xxx'
- 回表优化：覆盖索引 vs 回表查询
- 大分页优化：延迟关联（先用覆盖索引查 ID，再通过 ID 回表）
- JOIN 优化：小表驱动大表、子查询改 JOIN
- 排序优化：利用索引排序避免 filesort

请开始分析并输出优化方案。
```

**Agent 执行步骤**

1. 分析 EXPLAIN 输出：type 是否是 ALL/index（全表扫描），key 是否为空（未使用索引），rows 是否过大，Extra 是否有 Using filesort/Using temporary。
2. 分析 WHERE 条件：哪些字段可以走索引、是否因函数/运算导致索引失效。
3. 设计优化方案：新增索引、改写 SQL、考虑覆盖索引减少回表。
4. 输出优化后的 SQL + 创建索引的 DDL。
5. 给出优化后 EXPLAIN 的预期效果。
6. 在 MyBatis-Plus 项目中给出等价的 LambdaQueryWrapper 写法。

**人工检查点**

- 索引方案验证：新增索引后，在测试环境用真实数据量跑 EXPLAIN，确认执行计划变化符合预期。
- 风险评估：在低峰期执行 DDL 还是在主库直接执行？索引大小是否合理？
- 优化后验证：在测试环境实际执行优化后的 SQL，确认执行时间确实下降。

**输出验收清单**

- [ ] 分析了 EXPLAIN 的所有关键字段（type、key、rows、Extra）
- [ ] 指出了索引失效的具体原因（如类型转换、函数包裹、前缀模糊）
- [ ] 给出了明确的索引优化建议（DDL 可直接执行）
- [ ] 给出了优化后的 SQL 或等价 LambdaQueryWrapper 写法
- [ ] 预估了优化后的执行时间
- [ ] 评估了索引对写入性能的影响
- [ ] 如果有大分页场景，给出了延迟关联方案
- [ ] 考虑了现有索引是否可以复用（避免冗余索引）
- [ ] 给出了 ONLINE DDL 的建议（ALGORITHM=INPLACE 避免锁表）
- [ ] 如果用 MyBatis-Plus，代码中避免了字符串 SQL 拼接

**常见 AI 错误**

1. **不管数据分布就建索引**。status 字段只有 3 个值且分布均匀（各 33%），建索引不如全表扫描——但 AI 不管这些直接建议建索引。
2. **冗余索引**。已经存在联合索引 `(a, b)`，AI 还建议建单列索引 `(a)`。
3. **大分页优化不彻底**。AI 把 `LIMIT 100000, 20` 改成了子查询但还是回表了。正确做法是覆盖索引取 ID 列表再回表。
4. **不考虑 ONLINE DDL**。MySQL 5.6+ 支持 `ALGORITHM=INPLACE` 加索引不锁表，但 AI 的 DDL 没指定。
5. **优化后 SQL 语义变了**。为了优化性能改了 JOIN 类型或 WHERE 条件，导致结果集跟原来不一样。

---

### 18.4.13 工作流十三：排查线上问题

**适用场景**

收到线上告警（5xx 错误率飙升、接口超时、OOM、CPU 飙高），需要根据日志和监控快速定位根因、给出修复建议。

**输入材料**

- 错误日志片段（关键堆栈和时间点）
- 监控截图或描述：CPU、内存、GC、线程数、连接池
- 异常发生时间段
- 最近的变更记录（代码发布、配置变更、数据迁移）
- 可选的：JVM dump 分析、慢 SQL 日志

**上下文要求**

CLAUDE.md 中应包含：日志系统（ELK/日志文件路径）、监控平台（Prometheus/Grafana）、部署架构。

**提示词（完整可复制）**

```
你是一个 Java 后端线上排障专家。请分析以下线上问题，定位根因并给出修复方案。

## 问题概述

**时间**：<问题发生时间，如 2026-07-01 14:30 - 14:45>
**现象**：
- <现象1：如 /api/v1/orders 接口 P99 耗时从 200ms 飙升到 8s>
- <现象2：如 5xx 错误率从 0.01% 飙升到 15%>
- <现象3：如 应用服务器 CPU 从 30% 飙升到 95%>

**触发条件**：<如果有规律，如 每天下午 2:30 触发，大促期间触发>

## 错误日志

```
<粘贴关键的错误日志>
```

## 监控数据

- CPU 使用率：<正常 30% → 异常 95%>
- JVM 内存：<堆内存 4G → 使用 3.8G，GC 频繁>
- 数据库连接池：<活跃连接 80/100，等待队列 50>
- 线程数：<正常 200 → 异常 800>
- 慢 SQL 数量：<正常 5/min → 异常 200/min>

## 最近变更

- <变更1：如 上线了新版本 v2.3.1>
- <变更2：如 修改了 Redis 超时时间从 500ms 到 2s>
- <变更3：如 批量导入了 50 万条数据>

## 排查要求

1. **时间线分析**：建立问题发生前后的时间线，关联日志、监控、变更
2. **根因推断**：基于日志和监控，推断最可能的根因（不要给多个"可能"，给出最可能的 1 个）
3. **验证方法**：说明如何在线上验证推断是否正确（不要重启、不要加日志打印）
4. **临时止血方案**：如果问题正在发生，给出最快的止血操作
5. **永久修复方案**：给出代码或配置层面的修复
6. **预防措施**：如何避免同类问题再次发生

## 分析框架

按以下顺序排查：
1. 是否有明确异常的日志？（NullPointer、SQLException、OOM）
2. 慢 SQL？检查数据库连接池和慢查询日志
3. 外部依赖超时？（Redis、MQ、RPC）
4. 资源耗尽？（线程池满、连接池满、内存泄漏）
5. 流量突增？（QPS 是否异常飙升）
6. GC 问题？（Full GC 频繁、STW 时间长）

请开始排查。
```

**Agent 执行步骤**

1. 解析错误日志，提取关键信息：异常类型、出错的类和方法、堆栈调用链。
2. 分析监控数据：找出资源瓶颈（CPU/内存/连接池/线程数哪个先到瓶颈）。
3. 关联变更记录：问题时段附近是否有代码发布或配置变更。
4. 按分析框架逐层排查，给出最可能的根因。
5. 给出临时止血方案（如限流、降级、回滚）和永久修复方案。
6. 如有排查中需要的查询命令（如查看连接池状态、JVM 线程 dump），一并给出。

**人工检查点**

- 根因推断后：确认推断是否真的符合所有现象——很多人给第一个看起来合理的原因就停止排查了。
- 止血方案确认：止血方案是否有副作用？限流会不会影响核心业务？
- 修复方案确认：修复方案是否真的解决了根因，还是只缓解了症状？

**输出验收清单**

- [ ] 建立了问题发生时间线（日志 + 监控 + 变更的关联）
- [ ] 给出了最可能的根因（1 个，不是多个"可能"）
- [ ] 给出了在线验证方法（不依赖重启或加日志）
- [ ] 给出了临时止血方案（可立即执行）
- [ ] 给出了永久修复方案（包含代码或配置修改）
- [ ] 分析了修复方案的副作用和风险
- [ ] 给出了预防措施（监控告警规则、代码规范、压测方案）
- [ ] 排查过程有明确的逻辑链，不是猜的
- [ ] 如果需要数据修复，给出了修复 SQL
- [ ] 总结了本次故障的时间线文档模板

**常见 AI 错误**

1. **直接跳到代码修复不看监控**。看到 NullPointerException 就开始改代码，没注意到真正的根因是连接池耗尽导致返回 null。
2. **给一堆"可能"没有结论**。列出 5 种可能原因但不做判断，排查价值为零。
3. **止血方案太激进**。建议"kill 所有长事务"或"重启数据库"，不考虑业务影响。
4. **忽视变更关联**。问题是代码变更引入的，AI 忽略了变更记录直接去查 JVM 参数。
5. **修复方案治标不治本**。加了超时时间、加大了连接池，但没解决为什么会有那么多慢 SQL 或慢调用。

---

## 18.5 工作流组合使用指南

实际开发中很少只用到一个工作流。以下是几个常见的组合场景：

### 场景：新模块从零到交付

**涉及的工作流（按顺序）**：
1. 18.4.2 新增数据库表 → 产出 DDL + Entity + Mapper
2. 18.4.1 新增 REST API 接口 → 产出 Controller + Service + DTO + VO
3. 18.4.7 增加全局异常处理 → 统一异常类 + 错误码（如果项目还没有）
4. 18.4.4 增加参数校验 → 给 DTO 加校验注解
5. 18.4.5 增加权限校验 → 给接口加 @PreAuthorize
6. 18.4.6 增加操作日志 → 给关键操作加 @Log
7. 18.4.8 生成单元测试 → Service 层单元测试
8. 18.4.9 生成接口测试 → Controller 集成测试
9. 18.4.12 优化 SQL → 审查生成的 SQL，确保索引合理

### 场景：线上 Bug 紧急修复

**涉及的工作流**：
1. 18.4.13 排查线上问题 → 定位根因
2. 18.4.10 修复 Bug → 代码修复 + 回归测试
3. 18.4.12 优化 SQL → 如果是慢 SQL 导致的

### 场景：存量代码质量提升

**涉及的工作流**：
1. 18.4.11 重构代码 → 改善结构
2. 18.4.8 生成单元测试 → 给重构后的代码补测试
3. 18.4.12 优化 SQL → 审查现有 SQL 的质量

---

## 18.6 本章小结

Java 后端的 AI 辅助开发不是"让 AI 写代码然后人Review"这么简单。真正有效的方式是：

1. **先建上下文**：CLAUDE.md 是 AI 的"入职文档"，不写清楚项目规范，AI 生成的代码需要大量返工。
2. **拆成 13 个标准工作流**：每个工作流有明确的输入、输出、检查点，像工厂流水线一样可重复执行。
3. **人盯关键决策**：事务边界、并发安全、业务规则——这三件事 AI 做不好，需要人盯住。
4. **自动化验收**：编译、测试、覆盖率、Checkstyle——用机器验收 AI 的产出，不要用肉眼一行行看。

最重要的经验是：**AI 的价值不在"替代人写代码"，而在"消除重复性的编码劳动"，让人把精力集中在架构决策、业务理解和疑难问题排查上。**
