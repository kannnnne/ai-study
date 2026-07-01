# AI 工作流标准模板

> 使用说明：本模板用于设计可复用的 AI 辅助开发工作流。使用时复制全文，按照每个章节中的说明填写具体内容。每个工作流最终产出"可复制提示词"，保证不同人使用同一工作流得到一致结果。

---

## 1. 工作流名称

### 说明

给工作流一个明确的名称，方便团队沟通和引用。名称应该能表达"做什么"。

### 模板

```
<场景前缀>_<核心动作>_<产出物>
```

### 示例

```
Java接口开发工作流
API自动化测试工作流
数据库变更审核工作流
技术文档生成工作流
```

---

## 2. 适用场景

### 说明

描述什么情况下应该使用这个工作流。包括触发条件、前置条件、适用项目类型。

### 模板

```markdown
## 触发条件

- <什么情况下触发>
- <什么情况下触发>

## 前置条件

- <使用这个工作流前需要准备好的东西>
- <使用这个工作流前需要准备好的东西>

## 适用项目类型

- <什么样的项目适合>
- <什么样的项目不适合>
```

---

## 3. 输入材料清单

### 说明

列出工作流启动时需要输入的所有材料。每项材料说明格式要求和来源。

### 模板

```markdown
| 序号 | 材料名称 | 是否必填 | 格式要求 | 来源 | 说明 |
|------|----------|----------|----------|------|------|
| 1 | <材料名> | 是/否 | <格式> | <谁提供> | <说明> |
```

---

## 4. 上下文配置

### 说明

在 CLAUDE.md 或 AGENTS.md 中需要预先配置的上下文信息。这些信息确保 AI 在正确的项目约束下工作。

### 模板

```markdown
## CLAUDE.md 中需要配置的内容

```markdown
# 项目技术栈
- Java 8/17
- Spring Boot 2.7.x
- MyBatis-Plus 3.5.x
- MySQL 8.0
- ...

# 项目结构
src/main/java/com/xxx/
  ├── controller/   # 接口层
  ├── service/      # 业务逻辑层
  │   └── impl/     # 实现类
  ├── mapper/       # 数据访问层
  ├── entity/       # 实体类
  ├── dto/          # 数据传输对象
  ├── enums/        # 枚举类
  ├── config/       # 配置类
  └── common/       # 通用工具类

# 编码规范
- 统一响应体：Result<T> (code, message, data)
- 异常处理：@RestControllerAdvice + BusinessException
- 参数校验：JSR-303 @Validated
- 日志框架：Slf4j
- 包命名：com.<company>.<project>.<module>

# 数据库规范
- 表名前缀：t_
- 主键：BIGINT 自增
- 必备字段：created_at, updated_at, deleted(逻辑删除)
- 索引命名：idx_<表名简称>_<字段名>
```
```

---

## 5. 约束条件

### 说明

给 AI 的硬性限制。这些是不可突破的底线规则，AI 在任何情况下都不能违反。

### 模板

```markdown
## 硬约束（不可违反）

1. <硬约束1>
2. <硬约束2>
3. <硬约束3>

## 软约束（建议遵守，有理由可突破）

1. <软约束1>
2. <软约束2>
```

---

## 6. 推荐工具

### 说明

工作流中推荐使用的 AI 工具、IDE 插件、辅助工具。

### 模板

```markdown
| 工具 | 用途 | 必要性 | 说明 |
|------|------|--------|------|
| Claude Code / Cursor | AI 代码生成 | 必须 | 核心工具 |
| Git | 版本控制 | 必须 | 每步变更可追溯 |
| Maven/Gradle | 构建工具 | 必须 | 编译验证 |
| JUnit 5 | 单元测试 | 必须 | 验证代码正确性 |
| Postman / curl | 接口测试 | 建议 | 手动验证 API |
```

---

## 7. 执行步骤

### 说明

工作流的核心部分。每一步包含：步骤编号、步骤名称、输入材料、输出物、检查点、使用的工具。

### 模板

```markdown
### 步骤 N: <步骤名称>

- **输入**: <上一步的输出 + 本步骤需要的额外材料>
- **输出**: <本步骤产出的文件/内容>
- **检查点**: <如何判断这一步做对了>
- **工具**: <本步骤使用的工具>
- **AI 提示词**: <给 AI 的具体指令>
- **预计耗时**: <分钟>
```

---

## 8. 人工检查点

### 说明

在自动流程中设置必须人工确认的节点。AI 不能自行决定通过，必须等待人工。

### 模板

```markdown
| 检查点编号 | 位置（在哪步之后） | 检查内容 | 通过标准 | 不通过怎么办 |
|------------|-------------------|----------|----------|-------------|
| CP-1 | 步骤X之后 | <检查什么> | <怎样算通过> | <不通过时回退到哪步> |
```

---

## 9. 输出物清单

### 说明

工作流最终产出的所有文件列表，含文件路径、用途、验收人。

### 模板

```markdown
| 序号 | 文件 | 用途 | 验收人 |
|------|------|------|--------|
| 1 | src/main/java/.../<ClassName>.java | <用途> | <谁来验收> |
| 2 | src/test/java/.../<TestClass>.java | <用途> | <谁来验收> |
```

---

## 10. 验收标准

### 说明

工作流执行完成后，如何判断产出物合格。

### 模板

```markdown
| 编号 | 验收项 | 验收方法 | 通过标准 |
|------|--------|----------|----------|
| AC-1 | <验收项> | <怎么验证> | <怎样算通过> |
```

---

## 11. 风险点与应对

### 说明

识别工作流执行中可能出问题的环节，提前制定应对方案。

### 模板

```markdown
| 风险 | 可能性 | 影响 | 预防措施 | 应对方案 |
|------|--------|------|----------|----------|
| <风险描述> | 高/中/低 | 高/中/低 | <怎么预防> | <出问题了怎么办> |
```

---

## 12. 可复制提示词

### 说明

将整个工作流浓缩为一段可复制的 AI 提示词。任何人复制这段提示词，粘贴给 AI，就能启动整个工作流。**这是工作流的核心交付物。**

### 模板

```markdown
```text
<完整的 AI 提示词，可直接复制粘贴使用>
```
```

---

---

# 参考示例：Java 接口开发工作流

以下是一个完整填写的参考示例，展示如何使用上述模板设计一个实际的开发工作流。

---

## 1. 工作流名称

```
Java接口开发工作流（基于 Spec）
```

---

## 2. 适用场景

### 触发条件

- 已有一份完成评审的 Spec 文档（参考本手册 Spec 标准模板）
- 需要开发新的 RESTful API 接口或模块
- 涉及数据库表创建、业务逻辑、接口开发的完整流程

### 前置条件

- Spec 文档已完成并通过评审（13个要素齐全）
- 项目环境已搭建（JDK、Maven、IDE 可用）
- CLAUDE.md 已配置项目技术栈和编码规范
- 数据库实例可连接

### 适用项目类型

- Java/Spring Boot 企业后端项目
- 有明确数据模型和业务规则的模块开发
- 不适合：纯前端开发、纯配置变更、简单 Bug 修复

---

## 3. 输入材料清单

| 序号 | 材料名称 | 是否必填 | 格式要求 | 来源 | 说明 |
|------|----------|----------|----------|------|------|
| 1 | Spec 文档 | 是 | Markdown，包含13要素 | 产品/技术负责人 | 业务需求和技术规格的唯一依据 |
| 2 | CLAUDE.md | 是 | Markdown | 项目已有 | 项目级约束：技术栈、编码规范、目录结构 |
| 3 | 数据库连接信息 | 是 | 开发环境配置 | 运维/DBA | 用于创建表和验证 |
| 4 | 已有的通用模块 | 否 | Java 源码 | 项目已有 | Result、BaseEntity、全局异常处理器等 |

---

## 4. 上下文配置

### CLAUDE.md 中需要配置的内容

```markdown
# 项目技术栈
- Java 17
- Spring Boot 3.2.x
- MyBatis-Plus 3.5.x
- MySQL 8.0
- Redis 7.x
- Spring Security + JWT
- Lombok, MapStruct, Hutool

# 项目结构
src/main/java/com/example/project/
  ├── controller/     # 接口层，@RestController
  ├── service/        # 业务逻辑接口
  │   └── impl/       # 业务逻辑实现
  ├── mapper/         # MyBatis-Plus Mapper
  ├── entity/         # 数据库实体，@TableName
  ├── dto/            # 请求体 DTO
  ├── vo/             # 响应体 VO
  ├── enums/          # 枚举类
  ├── config/         # Spring 配置类
  └── common/
      ├── Result.java           # 统一响应 {code, message, data}
      ├── BaseEntity.java       # 实体基类 (createdAt, updatedAt, deleted)
      ├── GlobalExceptionHandler.java  # @RestControllerAdvice
      └── exception/
          └── BusinessException.java   # 业务异常

# 编码规范
- 统一响应体：Result<T>(Integer code, String message, T data)
- 成功：Result.success(data)，失败：Result.fail(errorCode, message)
- 异常处理：@RestControllerAdvice，业务异常抛出 BusinessException
- 参数校验：使用 @Validated + JSR-303 注解，校验失败返回 400
- Controller 层不做业务逻辑，只做参数接收+权限校验+调用 Service
- Service 层接口+实现分离，事务注解加在 ServiceImpl 上
- 日志：private static final Logger log = LoggerFactory.getLogger(XxxServiceImpl.class)
- 禁止在 Mapper 中写复杂业务逻辑

# 数据库规范
- 表名：小写+下划线，前缀 t_
- 字段名：小写+下划线，驼峰自动映射
- 主键：BIGINT AUTO_INCREMENT，字段名 id
- 必备字段：created_at DATETIME, updated_at DATETIME, deleted TINYINT(1) DEFAULT 0
- 索引命名：uk_xxx 唯一索引，idx_xxx 普通索引
- 不使用外键约束，关联关系在应用层维护
```

---

## 5. 约束条件

### 硬约束（不可违反）

1. **Spec 优先**: 一切代码以 Spec 为准，不允许脱离 Spec 自行发挥
2. **字段名一致性**: 数据库字段名、实体字段名、DTO/VO 字段名必须与 Spec 完全一致
3. **状态流转**: 只允许 Spec 中定义的状态转换路径，不允许跳过、逆转、自创
4. **权限检查**: 每个接口必须有权限注解，不允许遗漏
5. **日志审计**: 关键操作（创建、修改、删除、状态变更）必须记录操作日志
6. **不写前端代码**: 只产出后端代码（Entity, Mapper, Service, Controller, Test, Config）

### 软约束（建议遵守，有理由可突破）

1. 优先使用 MyBatis-Plus 自带方法，复杂查询再写自定义 SQL
2. 优先使用 MapStruct 做对象转换，避免手写 BeanUtils.copyProperties
3. 列表查询默认分页，pageSize 上限 100
4. 金额字段使用 Decimal 类型，单位用分

---

## 6. 推荐工具

| 工具 | 用途 | 必要性 | 说明 |
|------|------|--------|------|
| Claude Code / Cursor | AI 代码生成 | 必须 | 基于 Spec 提示词生成代码 |
| IDEA / Eclipse | 代码编写 | 必须 | 编译、运行、调试 |
| Maven | 构建工具 | 必须 | mvn compile 验证编译通过 |
| JUnit 5 + Mockito | 单元测试 | 必须 | mvn test 跑测试 |
| Git | 版本控制 | 必须 | 每步变更独立提交 |
| MySQL Workbench / DBeaver | 数据库管理 | 建议 | 执行 DDL、查看数据 |
| Postman / curl | 接口测试 | 建议 | 启动服务后手动调用验证 |

---

## 7. 执行步骤

### 步骤 1: 输入 Spec，生成 DDL 和实体类

- **输入**: Spec 文档（第6章 数据模型 Spec）
- **输出**:
  - DDL SQL 脚本（放在 `src/main/resources/db/` 下）
  - Entity 实体类（`com.example.project.entity.Xxx.java`）
- **检查点**:
  - DDL 中字段名、类型、长度、注释与 Spec 完全一致
  - DDL 中索引名称、类型、字段与 Spec 完全一致
  - 实体类使用 @TableName、@TableId、@TableField、@TableLogic 注解
  - 实体类继承 BaseEntity（如有）
- **工具**: Claude Code + MySQL Workbench（执行 DDL）
- **AI 提示词**:

```text
基于以下 Spec 中的数据模型定义，生成：
1. 完整的 DDL SQL 脚本（可直接在 MySQL 8.0 执行）
2. 对应的 Java Entity 实体类

【Spec 数据模型内容】

要求：
- 字段名、类型、长度、默认值、注释与 Spec 完全一致
- 索引包含命名、类型、字段
- 实体类使用 Lombok @Data，MyBatis-Plus 注解
- 金额字段使用 BigDecimal
- 包含 created_at, updated_at, deleted 通用字段
- 不写业务逻辑
```

- **预计耗时**: 10 分钟

### 步骤 2: 生成 DTO 和 VO

- **输入**: Spec 文档（第5章 API Spec）
- **输出**:
  - Request DTO（`dto/CreateXxxRequest.java`, `dto/XxxQuery.java` 等）
  - Response VO（`vo/XxxVO.java`, `vo/XxxDetailVO.java` 等）
- **检查点**:
  - 字段名、类型与 Spec 中的请求体/响应体完全一致
  - 使用 JSR-303 校验注解（@NotNull, @NotBlank, @Min, @Max 等）
  - 校验注解的 message 属性使用中文提示
- **工具**: Claude Code
- **AI 提示词**:

```text
基于以下 Spec 中的 API 定义，生成所有请求 DTO 和响应 VO 类。

【Spec API Spec 内容】

要求：
- 类名：XxxRequest / XxxQuery / XxxVO / XxxDetailVO
- 字段名和类型与 API Spec 完全一致
- 请求 DTO 添加 JSR-303 校验注解，message 用中文
- 响应 VO 包含所有 API 定义的返回字段
- 使用 Lombok @Data
```

- **预计耗时**: 15 分钟

### 步骤 3: 生成枚举类

- **输入**: Spec 文档（第8章 状态流转规则 + 第7章 权限规则）
- **输出**: 枚举类（`enums/OrderStatus.java`, `enums/OperateType.java`, `enums/PaymentMethod.java` 等）
- **检查点**:
  - 枚取值与 Spec 完全一致（大写+下划线）
  - 每个枚举包含 code（存储值）、desc（中文描述）
  - 提供静态方法：`fromCode(String code)` 用于反查
- **工具**: Claude Code
- **AI 提示词**:

```text
基于以下 Spec 的状态定义，生成 Java 枚举类。

【Spec 状态定义】

要求：
- 枚取值与 Spec 完全一致
- 每个枚举包含：code(String), desc(String)
- 提供 public static XxxEnum fromCode(String code) 方法
- 使用 Lombok @Getter, @AllArgsConstructor
```

- **预计耗时**: 10 分钟

### 步骤 4: 生成 Mapper 接口和 XML

- **输入**: 步骤1的 Entity 类 + Spec 文档（第6章索引 + 第3章权限的数据范围）
- **输出**:
  - Mapper 接口（`mapper/XxxMapper.java`，继承 BaseMapper<T>）
  - Mapper XML（如有复杂查询，放在 `resources/mapper/XxxMapper.xml`）
- **检查点**:
  - Mapper 接口继承 `BaseMapper<XxxEntity>`
  - 自定义 SQL 涉及的条件字段与 Spec 查询参数一致
  - XML 中 SQL 使用参数化查询（#{}），不使用 ${}
- **工具**: Claude Code
- **AI 提示词**:

```text
基于以下 Entity 类和 Spec 中的查询条件，生成 Mapper 接口。

【Entity 类代码】
【Spec 查询参数定义】

要求：
- Mapper 接口继承 MyBatis-Plus BaseMapper
- 复杂查询（多表、聚合）使用 @Select 注解或 XML
- 所有 SQL 参数使用 #{} 占位符，禁止字符串拼接
- 分页查询使用 MyBatis-Plus Page 对象
```

- **预计耗时**: 15 分钟

### 步骤 5: 生成 Service 接口和实现类

- **输入**: 前面所有步骤的产物 + Spec 全文（尤其是状态流转规则、异常处理规则、权限规则）
- **输出**:
  - Service 接口（`service/XxxService.java`）
  - ServiceImpl 实现类（`service/impl/XxxServiceImpl.java`）
- **检查点**:
  - 每个业务方法对应 Spec 中的一个功能点
  - 状态变更逻辑严格遵循 Spec 中的状态流转矩阵
  - 每个异常场景使用 Spec 定义的错误码和提示语
  - 事务注解 @Transactional 加在写操作方法上
  - 操作日志记录在状态变更方法中
  - 关键操作打 Info 日志，异常打 Error 日志
- **工具**: Claude Code
- **AI 提示词**:

```text
基于完整的 Spec 文档和已有的 Entity/Mapper/DTO，实现 Service 层。

【完整 Spec 文档】
【已有代码】

要求：
- Service 接口定义所有业务方法
- ServiceImpl 实现所有方法
- 状态流转严格按 Spec 的"状态流转矩阵"实现
- 异常处理按 Spec 的"异常处理规则"表使用对应错误码
- 权限检查：数据范围按 Spec 的"数据级权限"SQL 过滤条件
- 写操作加 @Transactional(rollbackFor = Exception.class)
- 状态变更写操作日志到 t_order_log 表（使用日志 Mapper）
- 金额字段使用 BigDecimal，禁止使用 Float/Double 计算
- 外部系统调用（如 WMS）使用独立线程池 + 超时 + 重试
- 日志：Service 入口 Info，异常 Error
- 代码注释：关键业务逻辑加中文注释，说明对应 Spec 的哪个章节
```

- **预计耗时**: 30-45 分钟

### 步骤 6: 生成 Controller

- **输入**: 步骤5的 Service + Spec 文档（第5章 API Spec + 第7章 权限规则）
- **输出**: Controller 类（`controller/XxxController.java`）
- **检查点**:
  - 每个端点路径、HTTP 方法与 Spec 完全一致
  - 每个端点有 @PreAuthorize 权限注解
  - 参数使用 @Validated 或 @Valid 启用校验
  - Controller 不包含任何业务逻辑，只做参数接收+调用 Service+返回 Result
  - 响应格式使用统一 Result 包装
- **工具**: Claude Code
- **AI 提示词**:

```text
基于 Service 接口和 Spec 的 API 定义，生成 Controller 类。

【Service 接口代码】
【Spec API Spec】
【Spec 权限规则】

要求：
- 类注解：@RestController, @RequestMapping("/api/v1/xxx"), @Slf4j
- 每个方法路径和 HTTP 方法与 Spec 完全一致
- 权限注解使用 @PreAuthorize("hasRole('ROLE_XXX')") 或自定义注解
- 请求体参数使用 @Validated 或 @Valid
- Controller 不做任何业务判断，只调用 Service 并包装 Result
- 返回成功：return Result.success(service.xxxMethod(...))
- 不捕获业务异常（由全局异常处理器处理）
```

- **预计耗时**: 15 分钟

### 步骤 7: 生成单元测试

- **输入**: ServiceImpl + Spec 文档（尤其是状态流转规则和验收标准）
- **输出**: 测试类（`src/test/java/.../XxxServiceTest.java`, `XxxControllerTest.java`）
- **检查点**:
  - 每个 Service 方法至少有正常+边界+异常三个测试用例
  - 状态流转的每个合法路径有测试用例
  - 状态流转的每个非法路径有测试用例
  - 每个角色的权限校验有测试用例
  - 使用 @MockBean Mock 外部依赖
  - 运行 `mvn test` 全部通过
- **工具**: Claude Code + Maven
- **AI 提示词**:

```text
基于 ServiceImpl 和 Spec 的状态流转规则、验收标准，生成 JUnit 5 + Mockito 单元测试。

【ServiceImpl 代码】
【Spec 状态流转矩阵】
【Spec 验收标准】

要求：
- 使用 JUnit 5 (@ExtendWith(MockitoExtension.class))
- 使用 @Mock 模拟 Mapper 和其他 Service
- 使用 @InjectMocks 注入被测试的 ServiceImpl
- 测试方法命名：test<方法名>_<场景>_<预期结果>()
- 覆盖：正常场景 + 边界场景(null/空值/最大值) + 异常场景(非法状态/无权限)
- 状态流转：每个合法路径1个用例 + 每个非法路径1个用例
- 使用 BDDMockito 风格：given(...).willReturn(...)  +  then(...).should(...)
```

- **预计耗时**: 20-30 分钟

### 步骤 8: 编译验证 + 测试执行

- **输入**: 全部代码
- **输出**: 编译通过 + 测试通过
- **检查点**:
  - `mvn compile` 编译通过（0 错误）
  - `mvn test` 测试全部通过
  - 如有编译错误，修复后重新编译
- **工具**: Maven
- **预计耗时**: 5-10 分钟

---

## 8. 人工检查点

| 检查点编号 | 位置（在哪步之后） | 检查内容 | 通过标准 | 不通过怎么办 |
|------------|-------------------|----------|----------|-------------|
| CP-1 | 步骤1之后 | DDL 与 Spec 数据模型的一致性 | 字段/类型/长度/索引/注释与 Spec 逐项对比一致 | 修改 DDL，重新执行 |
| CP-2 | 步骤3之后 | 枚举值与 Spec 状态定义的一致性 | 所有状态值、事件值与 Spec 一致，无遗漏 | 补充缺失枚举值 |
| CP-3 | 步骤5之后 | Service 业务逻辑与 Spec 规则的一致性 | 状态流转、异常处理、权限过滤与 Spec 逐项对比一致 | 修正 ServiceImpl 代码 |
| CP-4 | 步骤6之后 | Controller 与 API Spec 的一致性 | 路径/方法/参数/响应格式与 Spec 一致 | 修正 Controller |
| CP-5 | 步骤7之后 | 测试覆盖率 | 状态流转路径100%覆盖，权限矩阵100%覆盖，异常场景覆盖 | 补充测试用例 |
| CP-6 | 步骤8之后 | 编译+测试通过 | mvn compile 0错误 + mvn test 全部通过 | 修复失败项，回到出问题的步骤 |

---

## 9. 输出物清单

| 序号 | 文件 | 用途 | 验收人 |
|------|------|------|--------|
| 1 | `src/main/resources/db/V1.0__create_order_tables.sql` | 建表 DDL | DBA / Tech Lead |
| 2 | `src/main/java/com/example/project/entity/Order.java` | 订单实体 | Tech Lead |
| 3 | `src/main/java/com/example/project/entity/OrderItem.java` | 订单明细实体 | Tech Lead |
| 4 | `src/main/java/com/example/project/entity/OrderLog.java` | 操作日志实体 | Tech Lead |
| 5 | `src/main/java/com/example/project/enums/OrderStatus.java` | 订单状态枚举 | Tech Lead |
| 6 | `src/main/java/com/example/project/enums/OperateType.java` | 操作类型枚举 | Tech Lead |
| 7 | `src/main/java/com/example/project/dto/CreateOrderRequest.java` | 创建订单请求 DTO | Tech Lead |
| 8 | `src/main/java/com/example/project/dto/OrderListQuery.java` | 订单查询请求 DTO | Tech Lead |
| 9 | `src/main/java/com/example/project/dto/CancelOrderRequest.java` | 取消订单请求 DTO | Tech Lead |
| 10 | `src/main/java/com/example/project/vo/OrderVO.java` | 订单列表响应 VO | Tech Lead |
| 11 | `src/main/java/com/example/project/vo/OrderDetailVO.java` | 订单详情响应 VO | Tech Lead |
| 12 | `src/main/java/com/example/project/mapper/OrderMapper.java` | 订单 Mapper | Tech Lead |
| 13 | `src/main/java/com/example/project/mapper/OrderItemMapper.java` | 订单明细 Mapper | Tech Lead |
| 14 | `src/main/java/com/example/project/mapper/OrderLogMapper.java` | 操作日志 Mapper | Tech Lead |
| 15 | `src/main/java/com/example/project/service/OrderService.java` | 订单 Service 接口 | Tech Lead |
| 16 | `src/main/java/com/example/project/service/impl/OrderServiceImpl.java` | 订单 Service 实现 | Tech Lead |
| 17 | `src/main/java/com/example/project/controller/OrderController.java` | 订单 Controller | Tech Lead |
| 18 | `src/test/java/.../service/impl/OrderServiceImplTest.java` | Service 单元测试 | Tech Lead |
| 19 | `src/test/java/.../controller/OrderControllerTest.java` | Controller 单元测试 | Tech Lead |

---

## 10. 验收标准

| 编号 | 验收项 | 验收方法 | 通过标准 |
|------|--------|----------|----------|
| AC-1 | 编译通过 | `mvn compile` | 0 error, 0 warning |
| AC-2 | 测试全部通过 | `mvn test` | 所有用例绿色，覆盖率 > 80% |
| AC-3 | API 路径一致 | Postman 逐接口调用 | 路径、方法、参数格式与 Spec 一致 |
| AC-4 | 状态流转正确 | 逐一触发每个状态变更场景 | 合法路径成功，非法路径返回 409 |
| AC-5 | 权限正确 | 用不同角色 Token 调用同一接口 | 有权限成功，无权限返回 403 |
| AC-6 | 异常处理正确 | 触发各类异常场景（订单不存在、状态不允许等） | 返回对应的业务错误码和中文提示 |
| AC-7 | 日志审计 | 查看数据库 t_order_log 表 | 每次状态变更都有对应记录 |
| AC-8 | 代码风格一致 | 人工浏览所有生成文件 | 命名、注释、结构与项目现有代码风格一致 |

---

## 11. 风险点与应对

| 风险 | 可能性 | 影响 | 预防措施 | 应对方案 |
|------|--------|------|----------|----------|
| AI 生成的字段名与 Spec 不一致 | 中 | 高 | 在提示词中明确强调"字段名与 Spec 完全一致" | 人工检查点 CP-1 逐项对比 |
| AI 遗漏某个状态流转路径 | 中 | 高 | 在提示词中显式列出所有状态流转矩阵 | CP-3 逐路径验证，缺失则追加提示词 |
| AI 生成的代码与现有项目风格不符 | 高 | 中 | CLAUDE.md 中明确编码规范 | CP-6 人工浏览检查，不符合则要求 AI 修改 |
| AI 编造不存在的 API 或类库 | 中 | 高 | 在 CLAUDE.md 中列出可用依赖 | 编译时报错发现，让 AI 用已知依赖替代 |
| 并发场景处理不完善 | 中 | 高 | 在 Spec 的异常处理规则中明确并发处理方案 | Code Review 重点检查乐观锁/幂等逻辑 |
| 工作流步骤过多导致中途放弃 | 高 | 中 | 提供完整提示词一键执行 | 提供"轻量版"跳过部分检查点 |
| AI 把 DTO/VO 的字段名写成驼峰而 Spec 用下划线 | 低 | 中 | Java 规范下驼峰是正常的，但需确认映射 | 数据库字段下划线 + Java 字段驼峰是标准做法，MyBatis-Plus 自动映射，此项风险实际可控 |

---

## 12. 可复制提示词

以下提示词可以直接复制粘贴给 AI，启动整个 Java 接口开发工作流：

```text
你是一个资深 Java/Spring Boot 后端开发工程师。请严格基于以下 Spec 完成一个完整的模块开发。

=== 技术栈 ===
- Java 17
- Spring Boot 3.2.x
- MyBatis-Plus 3.5.x
- MySQL 8.0
- Lombok, MapStruct, Slf4j
- 统一响应体：Result<T>(Integer code, String message, T data)

=== 项目结构 ===
controller/ → service/ → service/impl/ → mapper/ → entity/
dto/ (请求体), vo/ (响应体), enums/ (枚举)

=== Spec 文档 ===
<在此粘贴完整的 Spec 文档，包含13个要素>

=== 执行要求 ===

请严格按以下顺序执行，每一步完成后等待我确认再继续下一步。

## 步骤1：生成 DDL + Entity
- 基于 Spec 第6章"数据模型 Spec"生成完整 DDL SQL
- 生成对应的 Entity 类，使用 MyBatis-Plus 注解
- 字段名/类型/长度/索引与 Spec 完全一致
- 等待我确认

## 步骤2：生成 DTO + VO
- 基于 Spec 第5章"API Spec"生成所有请求 DTO 和响应 VO
- 请求 DTO 添加 JSR-303 校验注解，message 用中文
- 等待我确认

## 步骤3：生成枚举类
- 基于 Spec 第8章"状态流转规则"和第7章"权限规则"生成枚举
- 每个枚举包含 code 和 desc
- 等待我确认

## 步骤4：生成 Mapper 接口
- 继承 MyBatis-Plus BaseMapper
- 复杂查询写自定义 SQL（使用 #{} 参数化）
- 等待我确认

## 步骤5：生成 Service 接口 + ServiceImpl
- 状态流转严格按 Spec 的"状态流转矩阵"
- 异常处理按 Spec 的"异常处理规则"使用对应错误码
- 写操作加 @Transactional
- 状态变更写操作日志
- 等待我确认

## 步骤6：生成 Controller
- 路径、HTTP 方法与 Spec 完全一致
- 权限注解按 Spec 第7章"权限规则"
- Controller 不做业务逻辑
- 等待我确认

## 步骤7：生成单元测试
- JUnit 5 + Mockito
- 覆盖：正常+边界+异常场景
- 状态流转每个合法路径和非法路径都要测试
- 等待我确认

## 硬约束（不可违反）
1. 字段名/枚举值/状态标识与 Spec 完全一致
2. 只允许 Spec 中定义的状态转换路径
3. 每个接口有权限注解
4. 关键操作记录日志
5. 不写前端代码
6. 不使用 ${} 拼接 SQL

## 每步完成后
请列出本步骤生成的所有文件，并说明它们与 Spec 的对应关系，等待我确认后再继续下一步。
```

---

## 快速模式（跳过人工检查点）

如果对 AI 的代码生成质量有信心，可以使用以下快速提示词，让 AI 一次性完成所有步骤：

```text
你是一个资深 Java/Spring Boot 后端开发工程师。请基于以下 Spec 一次性完成整个模块开发。

=== Spec 文档 ===
<在此粘贴完整的 Spec>

=== 技术栈 ===
Java 17 / Spring Boot 3.2.x / MyBatis-Plus 3.5.x / MySQL 8.0

=== 输出物（按此顺序生成）===
1. DDL SQL（可直接执行的建表语句）
2. Entity 实体类（Lombok + MyBatis-Plus 注解）
3. 枚举类（含 code + desc + fromCode 方法）
4. DTO + VO（JSR-303 校验注解）
5. Mapper 接口（继承 BaseMapper）
6. Service 接口 + ServiceImpl（事务 + 状态流转 + 操作日志）
7. Controller（路径/权限与 Spec 一致）
8. 单元测试（JUnit 5 + Mockito，覆盖正常/边界/异常）

=== 硬约束 ===
- 字段名/枚举值/状态标识与 Spec 完全一致
- 状态流转严格按 Spec 的状态流转矩阵
- 异常处理按 Spec 的错误码表
- 权限按 Spec 的权限矩阵
- 关键操作记录日志
- 不使用 ${} 拼接 SQL

请一次性生成所有代码。
```
```
