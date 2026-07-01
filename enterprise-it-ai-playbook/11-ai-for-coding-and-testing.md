# 第11章：AI 如何辅助编码与测试（Java 后端）

> **本章定位**：面向 Java 后端程序员，提供可直接复制的 AI 编码提示词和系统化的质量核查方法。每个场景都经过实际项目验证，不是理论推演，而是踩坑后的沉淀。

---

## 11.1 为什么 Java 后端特别适合 AI 编码

Java 后端的编码工作有三个特点，恰好命中 AI 的优势区间：

1. **模式化程度高**：Controller → Service → DAO 的分层结构在多数项目中高度雷同，AI 对这类模板代码的生成准确率极高。
2. **注解驱动**：Spring Boot 的大量行为由注解声明（`@Transactional`、`@Cacheable`、`@Valid`），AI 只需正确放置注解即可产生可用代码，不需要深度推理。
3. **强类型约束**：Java 的类型系统让 AI 的输出有硬边界——编译不过就是编译不过，不会像动态语言那样把错误藏到运行时。

但优势的反面就是陷阱：AI 对"看起来对但逻辑错"的代码没有任何警觉。下面的每个场景都会标注**常见风险**和**人工检查点**，这些是真正省时间的关键——不检查这些，AI 省下的编码时间会加倍还到调试上。

---

## 11.2 核心原则：描述输入，而不是描述实现

使用 AI 编码有一条铁律，放在最前面：

> **给 AI 看输入和输出，让它自己推导实现；不要告诉 AI "你先做A再做B"。**

错误示范：

```
创建一个 UserController，先注入 UserService，然后写一个 createUser 方法，
方法里调用 userService.create()，返回 ResponseEntity...
```

正确示范：

```
根据以下 API 文档生成 UserController：

## POST /api/users
Request Body: { "username": "string", "email": "string", "password": "string" }
Response 201: { "id": "long", "username": "string", "email": "string" }
校验规则: username 3-20位字母数字，email 合法邮箱格式，password 8位以上
错误码: 400 参数校验失败, 409 username/email 重复
```

原因：当你描述实现时，你已经在替 AI 做它擅长的事。当你描述输入时，你保留了审查 AI 实现质量的能力——这是人应该做的事。

---

## 11.3 十一个 Java 后端 AI 编码场景

### 场景1：Controller 生成

**适用场景**

新建 REST API 端点、重构 Controller 层、为新模块搭建 API 入口。当你有明确的需求文档或接口文档时效果最好。

**输入材料**

- API 接口文档（路径、方法、请求/响应格式、错误码）
- 项目已有的 Controller 示例（用于风格对齐）
- 项目使用的统一返回体类名（如 `R<T>`、`Result<T>`、`ApiResponse<T>`）

**提示词（完整可复制）**

```
你是一个资深 Java Spring Boot 开发工程师。请根据以下 API 规格生成 Controller 代码。

## 项目技术约定
- Spring Boot 版本: 2.7.x / 3.x（根据实际选择）
- 统一返回体: Result<T>，成功用 Result.success(data)，失败用 Result.error(code, message)
- 参数校验: 使用 javax.validation / jakarta.validation 注解
- 异常处理: 业务异常统一抛 BusinessException(code, message)，由全局异常处理器拦截
- 日志框架: Slf4j

## 需要生成的 API

### 1. POST /api/orders
- 描述: 创建订单
- 权限: 需要登录（@RequestHeader("Authorization") token）
- Request Body:
  {
    "productId": "long, 必填",
    "quantity": "int, 必填, 1-999",
    "deliveryAddress": "string, 必填, 1-200字符"
  }
- 正常响应 201:
  {
    "orderId": "long",
    "orderStatus": "PENDING",
    "createdAt": "datetime"
  }
- 错误场景:
  - 400: 参数校验失败
  - 404: productId 对应的商品不存在
  - 409: 库存不足
  - 401: 未登录

### 2. GET /api/orders/{orderId}
- 描述: 查询订单详情
- 权限: 需要登录，只能查自己的订单
- 正常响应 200:
  {
    "orderId": "long",
    "productName": "string",
    "quantity": "int",
    "totalPrice": "BigDecimal",
    "orderStatus": "string",
    "createdAt": "datetime"
  }
- 错误场景:
  - 404: 订单不存在
  - 403: 不是自己的订单

### 3. GET /api/orders
- 描述: 分页查询自己的订单列表
- 权限: 需要登录
- Query参数: page (default 1), size (default 20, max 100), status (可选)
- 正常响应 200: 分页对象 { "records": [...], "total": "long", "page": "int", "size": "int" }

## 生成要求
1. 使用 @RestController 和 @RequestMapping("/api/orders")
2. 每个方法添加 @Operation (Swagger) 注解描述
3. 使用 @Valid 触发 DTO 参数校验
4. 不要在 Controller 里写业务逻辑
5. 请求体和路径参数单独定义 DTO 类（如果项目没有就内联定义）
6. 所有异常通过 throw new BusinessException(...) 抛出，不要 try-catch 包裹
```

**期望输出**

- 一个带有 `@RestController` 和 `@RequestMapping` 注解的类
- 每个接口方法有完整的参数注解（`@RequestBody @Valid`、`@PathVariable`、`@RequestParam`）
- 使用项目统一返回体包装响应
- 权限校验逻辑（从 token 或 SecurityContext 获取当前用户）
- Swagger `@Operation` 注解

**验收标准**

- 编译通过（IDE 无红色波浪线）
- HTTP 方法和路径与规格一致
- 参数校验注解完整（`@NotNull`、`@Min`、`@Max` 等）
- 没有在 Controller 中直接操作数据库或写业务判断
- 异常抛出而不是 return error 对象

**常见风险**

| 风险 | 说明 |
|------|------|
| 返回体不一致 | AI 有时直接 return 业务对象，有时包装 Result，混在一起。必须在 prompt 中固定返回体类型，生成后全文搜索 `return ` 确认一致性 |
| 缺少 `@Valid` | 写了校验注解但没加 `@Valid`，校验不生效 |
| 用 `@RequestParam` 收分页参数 | 应该封装为 PageRequest DTO，否则接口参数列表爆炸 |
| 路径变量名不一致 | `@PathVariable("orderId")` 和 `{orderId}` 不匹配 |

**人工检查点**

1. **权限校验是否遗漏**：只查自己的订单这条规则，AI 容易在列表接口漏掉 userId 过滤
2. **分页上限**：size max 100 是否真的限制了（AI 容易只写注释不做校验）
3. **错误码是否和全局异常处理器的枚举匹配**：如果项目用了 `ErrorCode.ORDER_NOT_FOUND` 这种枚举，AI 必须用对，否则编译不过

---

### 场景2：Service 生成

**适用场景**

新增业务模块、实现复杂业务规则、重构 Service 层逻辑。适合在有了 Entity、DTO 和接口规格后批量生成。

**输入材料**

- 业务流程描述（文字或流程图）
- 涉及的 Entity 类定义
- 项目事务管理约定（声明式 `@Transactional` 还是编程式）
- 项目异常类和错误码枚举

**提示词（完整可复制）**

```
你是一个资深 Java 后端开发。请根据以下业务描述生成 Service 实现。

## 项目约定
- 框架: Spring Boot + MyBatis-Plus
- 事务: 使用 @Transactional(rollbackFor = Exception.class)
- 异常: 业务异常用 BusinessException(ErrorCode.XXX)，ErrorCode 是枚举
- 日志: 使用 @Slf4j，关键操作记 log.info，异常记 log.error
- 不要用  Map<String, Object> 做返回值，定义明确的 VO/DTO

## 已有资源
```java
// ErrorCode 枚举
public enum ErrorCode {
    INSUFFICIENT_STOCK(1001, "库存不足"),
    ORDER_NOT_FOUND(1002, "订单不存在"),
    ORDER_CANNOT_CANCEL(1003, "当前状态不允许取消"),
    PRODUCT_NOT_FOUND(1004, "商品不存在");
}

// 订单状态枚举
public enum OrderStatus {
    PENDING, PAID, SHIPPED, COMPLETED, CANCELLED;
    
    public boolean canCancel() {
        return this == PENDING || this == PAID;
    }
}

// Entity
@Entity
public class Order {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
    private BigDecimal totalPrice;
    private OrderStatus status;
    private LocalDateTime createdAt;
}

@Entity
public class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
}
```

## 业务逻辑：取消订单

1. 根据 orderId 查询订单，不存在抛 ORDER_NOT_FOUND
2. 校验订单是否属于当前用户（userId），不匹配抛 ORDER_NOT_FOUND（不暴露403以避免信息泄露）
3. 校验订单状态是否可以取消（只有 PENDING 和 PAID 可取消），否则抛 ORDER_CANCEL_NOT_ALLOWED
4. 更新订单状态为 CANCELLED
5. 恢复商品库存（product.stock += order.quantity）
6. 如果订单是 PAID 状态，调用退款退款服务 refundService.refund(order)（这是一个接口调用，可能失败，需要处理）
7. 如果退款失败，记录错误日志，将订单状态改为 REFUND_PENDING（人工处理状态），不抛异常
8. 记录操作日志

## 生成要求
- 生成 OrderService 类，方法名 cancelOrder(Long orderId, Long userId)
- 考虑并发：更新库存时使用乐观锁（version字段）或 `UPDATE product SET stock = stock + #{quantity} WHERE id = #{productId}`
- 退款失败不中断主流程，但要留下可追溯的记录
- 方法上标注事务，但退款调用可能是远程RPC，需要评估是否应该放在事务外
```

**期望输出**

- 带有 `@Service` 和 `@Slf4j` 的类
- 方法上有 `@Transactional(rollbackFor = Exception.class)`
- 按步骤实现业务逻辑，每个异常分支用 `throw new BusinessException(ErrorCode.XXX)`
- 库存恢复使用数据库原子操作而非先查后改
- 退款调用有 try-catch 且不影响主事务

**验收标准**

- 所有异常路径都有处理（不要漏掉某个 if 分支没有 return/throw）
- 事务边界合理：退款这类远程调用应该在事务提交后执行还是事务内？需要明确
- 日志位置覆盖了关键状态变更
- 没有潜在的 NPE（如 getOrder() 返回 null 后直接 .getStatus()）

**常见风险**

| 风险 | 说明 |
|------|------|
| 事务范围过大 | 退款 RPC 调用放在 `@Transactional` 方法内，远程调用超时导致数据库连接长时间占用 |
| 先查后改的并发问题 | `product.setStock(product.getStock() + quantity)` 而非 `UPDATE ... SET stock = stock + ?` |
| 异常分支遗漏 | if 只有 happy path，else 分支什么都不做，状态机出现漏洞 |
| 日志泄露敏感信息 | `log.info("退款失败: {}", refundResponse)` 打印了包含银行卡号的完整响应 |

**人工检查点**

1. **事务边界是否合理**：涉及远程调用、消息发送、文件写入的操作，考虑使用 `TransactionSynchronization.afterCommit()` 回调
2. **并发安全**：库存扣减/恢复这类操作必须用数据库原子操作，禁止 Java 层面的 read-check-write
3. **状态机完整性**：画出订单状态流转图，确认没有未处理的非法状态转换
4. **退款幂等性**：如果退款接口被重复调用（重试场景），是否会导致重复退款

---

### 场景3：DTO/VO 生成

**适用场景**

新建 API 需要定义请求体和响应体、前后端联调阶段需要新增或修改 VO 字段、从 Entity 拆分出不同场景的 VO（列表VO、详情VO、编辑VO）。

**输入材料**

- 数据库表结构或 Entity 类
- API 接口文档中的字段需求
- 项目使用的校验框架（javax.validation 或 jakarta.validation）
- 项目使用的 JSON 序列化库（默认 Jackson）

**提示词（完整可复制）**

```
你是一个 Java 后端开发。请根据以下信息生成 DTO 和 VO 类。

## 项目约定
- 使用 Lombok @Data
- 校验注解使用 jakarta.validation.constraints
- JSON 序列化使用 Jackson（@JsonProperty 仅在字段名不一致时使用）
- DTO 类放在 dto.request 包，VO 类放在 dto.response 包
- 日期字段统一使用 LocalDateTime，序列化格式 yyyy-MM-dd HH:mm:ss

## 数据库表结构

```sql
CREATE TABLE t_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    real_name VARCHAR(50),
    id_card VARCHAR(18),
    gender TINYINT COMMENT '0-未知 1-男 2-女',
    status TINYINT COMMENT '0-禁用 1-启用',
    created_at DATETIME,
    updated_at DATETIME
);
```

## API 场景

### 场景A：创建用户 (CreateUserRequest)
- username: 必填，3-20位，只能包含字母数字和下划线
- email: 必填，合法邮箱格式
- phone: 必填，中国大陆手机号格式（1开头的11位数字）
- realName: 必填，2-20个字符
- gender: 非必填，只能是1或2
- 注意：不需要传 idCard、status、createdAt、updatedAt

### 场景B：用户列表项 (UserListVO)
- id, username, email, phone, realName, gender, status, createdAt
- gender 返回时需要转换为中文描述（男/女/未知），原值0,1,2不返回
- createdAt 格式化为 yyyy-MM-dd HH:mm:ss

### 场景C：用户详情 (UserDetailVO)
- 包含 UserListVO 的所有字段
- 追加 idCard（脱敏显示：保留前3后4，其余用*替代）
- 追加 updatedAt

## 生成要求
1. 每个 DTO/VO 独立成一个类文件
2. 校验注解的 message 用中文
3. phone 使用 @Pattern 校验
4. 需要字段脱敏的 VO 类，在 getter 中处理而不是新增字段
5. 列表VO和详情VO是否需要继承关系由你判断
```

**期望输出**

- 5个类：CreateUserRequest、UserListVO、UserDetailVO（如果使用继承，可能还有 BaseUserVO）
- 每个类的字段有完整注解（`@NotNull`、`@NotBlank`、`@Size`、`@Email`、`@Pattern`）
- 脱敏逻辑在 getter 方法中实现
- Jackson 日期格式化注解 `@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")`

**验收标准**

- 校验覆盖所有必填字段和格式要求
- 脱敏字段在序列化时生效（通过 `@JsonFormat` 或自定义序列化器测试）
- 敏感字段（如 idCard）只在详情VO出现，不在列表VO出现
- DTO 类不包含 Entity 的 JPA/MyBatis-Plus 注解（它们不是同一层的东西）

**常见风险**

| 风险 | 说明 |
|------|------|
| DTO 直接继承 Entity | 耦合了持久层注解，字段变更影响面不可控 |
| 脱敏逻辑放在 VO 的 setter | 序列化时 setter 不调用，导致字段不生效；脱敏应该放在 getter 或用 `@JsonSerialize` |
| 列表VO和详情VO完全一样 | AI 偷懒把两者生成成一模一样，实际列表应该更精简 |
| 校验注解版本混乱 | 混合使用 `javax.validation` 和 `jakarta.validation`，Spring Boot 3.x 只能用后者 |

**人工检查点**

1. **敏感字段泄露**：逐字段检查列表VO和导出VO，确保密码哈希、身份证号、银行卡号不会出现在不该出现的地方
2. **校验规则完整度**：email 校验了格式但没校验长度（数据库 varchar(100)），phone 校验了格式但没校验 null
3. **DTO 和 Entity 字段映射关系**：确保改名（real_name → realName）没有遗漏

---

### 场景4：Mapper/Repository 生成

**适用场景**

新建数据表后生成数据访问层代码、需要添加复杂查询方法、从 JPA 迁移到 MyBatis-Plus 或反之。

**输入材料**

- 数据库建表 SQL
- 项目使用的 ORM 框架（MyBatis-Plus / JPA / MyBatis XML）
- 项目 BaseMapper/BaseRepository 基类定义
- 需要支持的业务查询场景

**提示词（完整可复制）**

```
你是一个熟悉 MyBatis-Plus 的 Java 开发。请根据以下表结构生成 Mapper 接口和必要的 XML（如果需要）。

## 项目约定
- ORM: MyBatis-Plus 3.5.x
- Mapper 接口继承 BaseMapper<T>（已提供通用 CRUD）
- 逻辑删除字段: deleted (0-未删除 1-已删除)，使用 @TableLogic
- 分页使用 MyBatis-Plus 的 Page<T> + IPage<T>
- 复杂查询可以写 XML，简单查询用注解 @Select

## 表结构

```sql
CREATE TABLE t_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(32) NOT NULL COMMENT '订单号',
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(200),
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    total_price DECIMAL(12,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    receiver_name VARCHAR(50),
    receiver_phone VARCHAR(20),
    receiver_address VARCHAR(500),
    remark VARCHAR(500),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除',
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    UNIQUE INDEX uk_order_no (order_no)
);
```

## 需要的查询方法

1. 分页查询用户订单：按 userId 过滤，支持 status 可选过滤，按 created_at 倒序
2. 根据订单号查询订单（订单号唯一）
3. 批量查询指定状态、指定时间范围内的订单（用于定时任务处理超时未支付订单）
4. 统计用户各状态订单数量：SELECT status, COUNT(*) FROM ... WHERE user_id = ? GROUP BY status
5. 更新订单状态（乐观锁）：UPDATE ... SET status = ?, updated_at = NOW() WHERE id = ? AND status = ?

## 生成要求
- 生成 OrderMapper 接口
- 方法1和2用注解即可，方法3、4、5写 XML
- 分页查询返回 IPage<Order>，参数用 Page<Order> + 查询条件
- 方法5使用 @Update 注解，不需要 XML
- 涉及用户输入的条件参数，注意 SQL 注入防护
```

**期望输出**

- `OrderMapper` 接口继承 `BaseMapper<Order>`
- 简单查询用 `@Select` 注解
- 复杂查询有对应的 XML 映射文件
- 分页查询的 Page 参数放在第一个参数位（MyBatis-Plus 的分页插件要求）
- 动态 SQL 使用 `<if>` 标签安全拼接

**验收标准**

- 所有查询方法在 MyBatis-Plus 的自动映射规则下字段名能对齐（表字段 order_no → Java 属性 orderNo）
- 没有 SELECT *，明确列出需要的字段
- 统计查询返回的是 `List<Map<String, Object>>` 或专用统计 DTO
- 更新方法使用 `WHERE id = #{id} AND status = #{oldStatus}` 实现乐观锁

**常见风险**

| 风险 | 说明 |
|------|------|
| ${} 和 #{} 混用 | 用户输入进了 `${}`，SQL 注入风险。AI 在处理动态排序字段（ORDER BY）时容易直接 `${sortField}` |
| 逻辑删除条件遗漏 | 查询和更新 SQL 忘了 `AND deleted = 0`，但这个通常由 MyBatis-Plus 自动拼接——如果是手写 XML，必须手动加 |
| 分页无法拦截 | 自定义 XML 的分页查询如果命名不符合 MyBatis-Plus 规范，分页插件不会自动拦截 |
| 大批量 IN 查询 | WHERE id IN (#{ids}) 如果 ids 是一个几千条记录的列表，MySQL 性能严重退化 |

**人工检查点**

1. **SQL 注入**：全文搜索 XML 中的 `${}`，确认只有排序字段、表名这类无法用 `#{}` 的场景才使用，且做了白名单校验
2. **索引命中**：EXPLAIN 检查关键查询是否走了索引（特别是带排序的分页查询）
3. **逻辑删除**：手写 XML 确认加了 `AND deleted = 0`
4. **LIMIT 缺失**：理论上不会返回大量数据的查询（如定时任务批量处理），确认没有忘了 LIMIT

---

### 场景5：参数校验补全

**适用场景**

已有 DTO 类但没有加校验注解、接口参数校验不完整导致脏数据入库、安全审计要求加强输入校验。

**输入材料**

- 现有的 DTO 类代码
- 数据库字段约束（长度、非空、唯一）
- 业务规则文档（哪些字段必填、哪些有格式要求）

**提示词（完整可复制）**

```
请为以下 DTO 类补充完整的参数校验注解。这是一份已有代码，不要修改类结构和已有注解，只添加缺失的校验。

## 项目约定
- 校验框架: jakarta.validation.constraints（Spring Boot 3.x）
- @NotNull: 引用类型非空（String, Integer, BigDecimal等）
- @NotBlank: 字符串非空且不能是纯空格
- @NotEmpty: 集合/数组/字符串非空

## 数据库字段约束
- username: varchar(50), not null, unique
- email: varchar(100)
- phone: varchar(20)
- age: int
- amount: decimal(12,2)
- description: varchar(500)
- tags: 逗号分隔字符串, varchar(200)
- status: varchar(20), 只能是 ACTIVE/INACTIVE/SUSPENDED 之一

## 业务规则
- username: 必填, 2-50字符, 只能是字母、数字、下划线、中文
- email: 选填, 但如果有值必须是合法邮箱
- phone: 必填, 必须是手机号格式 (1[3-9]\d{9})
- age: 选填, 但如果有值必须 1-150
- amount: 必填, 必须 > 0, 小数不超过2位
- description: 选填, 最多500字符
- tags: 选填, 如果有值不能是纯逗号或逗号开头结尾
- status: 必填

## 已有代码
```java
@Data
public class UserSaveRequest {
    private String username;
    private String email;
    private String phone;
    private Integer age;
    private BigDecimal amount;
    private String description;
    private String tags;
    private String status;
}
```

## 生成要求
1. 区分 @NotNull、@NotBlank、@NotEmpty 的使用场景，不要全部用 @NotNull
2. 金额校验使用 @Digits(integer=10, fraction=2) 控制精度
3. 范围校验使用 @Min/@Max（数值）和 @Size（字符串/集合）
4. status 使用枚举校验或用 @Pattern
5. 所有校验注解的 message 用中文描述
6. 加上合适的 group 分组（如果不确定就不用）
```

**期望输出**

- 每个字段按约束加上对应的校验注解
- message 描述清晰，例如 `@NotBlank(message = "用户名不能为空")`
- 金额使用 `@NotNull` + `@DecimalMin("0.01")` + `@Digits(integer=10, fraction=2)`
- status 可以用自定义校验注解或 `@Pattern(regexp = "^(ACTIVE|INACTIVE|SUSPENDED)$")`

**验收标准**

- Controller 方法参数前有 `@Valid` 或 `@Validated`（校验注解加上了但 Controller 没触发，等于白加）
- 每个必填字段都有非空校验
- 手机号、邮箱的正则实际测试通过（不要信任 AI 写的正则，必须实测）
- `@Digits` 的 integer 参数设置合理

**常见风险**

| 风险 | 说明 |
|------|------|
| 正则写错 | AI 生成的正则表达式经常有边界问题，比如 `\d{9}` 写成了 `\d{10}` |
| 用 @NotNull 校验 String | `@NotNull` 对空字符串 "" 不报错，应该用 `@NotBlank` |
| 忘了 controller 加 @Valid | 校验注解加了一堆，Controller 参数没加 `@Valid`，全白费 |
| 嵌套对象校验遗漏 | 如果 DTO 内部还有嵌套对象字段，需要加 `@Valid` 才能级联校验 |

**人工检查点**

1. **正则表达式实测**：写一个简单的单元测试，传入边界值验证正则是否准确
2. **@NotBlank vs @NotNull**：逐字段检查 String 类型是否用了正确的注解
3. **分组校验如果有**：确认 Create 和 Update 场景使用的 group 分组是否正确（Create 时 id 为 null，Update 时 id 不为 null）

---

### 场景6：异常处理补全

**适用场景**

项目缺少统一的异常处理机制、错误码分散在代码各处、API 返回的错误信息不一致。

**输入材料**

- 项目使用的错误码枚举（如果有）
- 需要处理的业务异常场景清单
- 期望的错误响应格式
- 项目已有异常类的定义

**提示词（完整可复制）**

```
请为 Spring Boot 项目生成完整的全局异常处理体系。

## 项目约定
- Spring Boot 3.x
- 统一返回体: Result<T>，结构为 { "code": int, "message": string, "data": T }
- 使用 Slf4j 日志
- 错误码规范: 4xxxx 客户端错误, 5xxxx 服务端错误

## 需要包含的内容

### 1. 错误码枚举（ErrorCode）
定义以下错误码（你可以补充常用的）：
- 40001 参数校验失败
- 40101 未登录
- 40102 token 过期
- 40301 无权限
- 40401 资源不存在
- 40901 数据冲突（如重复提交）
- 42901 请求频繁，请稍后重试
- 50001 服务器内部错误

### 2. 业务异常类（BusinessException）
- 包含 ErrorCode 枚举
- 可选：携带额外的错误详情（如哪个字段校验失败）
- 提供静态工厂方法: BusinessException.of(ErrorCode), BusinessException.of(ErrorCode, String detail)

### 3. 全局异常处理器（GlobalExceptionHandler）
使用 @RestControllerAdvice，处理以下异常：
- MethodArgumentNotValidException（参数校验失败）
  - 需要提取出每个字段的具体错误信息，拼接到 message 中
  - 日志级别: WARN
- ConstraintViolationException（方法级校验失败）
  - 同上，日志级别: WARN
- BusinessException（业务异常）
  - 直接使用异常的 ErrorCode 和 message
  - 如果是 5xxxx 错误码，日志级别: ERROR（打印堆栈）
  - 如果是 4xxxx 错误码，日志级别: WARN（不打印堆栈）
- BindException（参数绑定失败，如类型转换）
  - 返回 40001，日志级别: WARN
- MissingServletRequestParameterException（缺少必填参数）
  - 返回 40001，提示具体缺少哪个参数
- HttpMessageNotReadableException（请求体解析失败）
  - 返回 40001，message: "请求格式错误"
- AccessDeniedException（Spring Security 权限不足）
  - 返回 40301
- Exception（兜底）
  - 返回 50001，message 不要暴露内部错误细节给前端
  - 日志级别: ERROR，完整打印堆栈

### 4. 自定义断言工具类（AssertUtils）
- 静态方法，类似 Spring 的 Assert
- AssertUtils.notNull(obj, ErrorCode.XXX) — 为 null 抛异常
- AssertUtils.isTrue(condition, ErrorCode.XXX) — 条件不成立抛异常
- AssertUtils.notEmpty(str, ErrorCode.XXX) — 字符串为空抛异常

## 生成要求
- 所有类放在 exception 包下
- 异常处理器的方法顺序：从具体到一般（先处理具体异常，最后兜底 Exception）
- 日志需要包含足够的排查信息（请求路径、参数、用户ID），但不打印敏感字段
- 不要捕获 Throwable（Error 类型应该让框架处理）
```

**期望输出**

- 完整的4个类/枚举：ErrorCode、BusinessException、GlobalExceptionHandler、AssertUtils
- GlobalExceptionHandler 至少有 7 个以上的 `@ExceptionHandler` 方法
- MethodArgumentNotValidException 的处理正确提取了 `FieldError` 列表
- 兜底的 Exception 处理方法不会把堆栈信息返回给前端

**验收标准**

- 手动抛一个 BusinessException，调用 API 确认返回格式正确
- 发送一个缺少必填字段的请求，确认返回了具体字段名而非泛泛的"参数错误"
- 触发一个意外的 NullPointerException，确认前端只看到 50001，日志中能看到完整堆栈
- AssertUtils 的方法签名方便业务代码调用（不用每次都 new BusinessException）

**常见风险**

| 风险 | 说明 |
|------|------|
| 兜底异常泄露堆栈 | `e.getMessage()` 直接返回给前端，可能暴露包名、SQL 语句等敏感信息 |
| 异常处理顺序错误 | 把父类异常处理器放在子类前面，导致子类处理器永远不会被调用 |
| 校验失败信息不完整 | MethodArgumentNotValidException 处理时只取了第一个错误，丢掉了其他字段的校验失败信息 |
| 日志重复打印 | 在异常处理器打了日志，业务代码又打了一次，日志里出现两条相同错误 |

**人工检查点**

1. **异常处理器优先级**：Spring 的 `@ExceptionHandler` 以方法参数类型匹配为准，确认具体的子类处理器不会被泛化的父类处理器覆盖
2. **敏感信息泄露**：兜底处理器的返回 message 必须是固定字符串，不能用 `e.getMessage()`
3. **日志级别**：区分业务异常（WARN，不需要人工介入）和系统异常（ERROR，需要人工介入）

---

### 场景7：日志补全

**适用场景**

线上问题排查困难、关键业务操作没有审计日志、日志级别混乱导致要么看不到关键信息要么被刷屏。

**输入材料**

- 关键业务流程的 Service 方法
- 需要审计的操作类型（创建订单、取消订单、退款等）
- 项目的日志格式规范（MDC 中的 traceId、userId 等）

**提示词（完整可复制）**

```
请为以下 Service 代码补充完整的日志。这是已有代码，不要修改业务逻辑，只在适当位置添加日志。

## 日志规范
- 使用 @Slf4j（Lombok）
- 日志级别:
  - INFO: 关键业务操作（创建、取消、状态变更）、对外部系统的调用
  - WARN: 可恢复的异常（重试、降级、参数接近上限）
  - ERROR: 不可恢复的异常（数据库操作失败、第三方调用失败且无法降级）
  - DEBUG: 调试信息（方法入参出参、中间状态），不要在生产环境用 INFO 打这类信息
- 日志格式: "操作描述, key1=value1, key2=value2"
  例如: log.info("取消订单, orderId={}, userId={}, reason={}", orderId, userId, reason)
- 不要在日志中打印:
  - 密码、token、身份证号、银行卡号等敏感信息
  - 大对象（超过100个字符的JSON），截断或用摘要
  - 循环体内（除非循环次数很少且确认需要）
- 异常日志需要包含业务上下文（哪个用户、哪个订单触发的）

## 已有代码

```java
@Service
@Slf4j
public class OrderService {

    private final OrderMapper orderMapper;
    private final ProductMapper productMapper;
    private final PaymentService paymentService;
    private final InventoryService inventoryService;

    @Transactional(rollbackFor = Exception.class)
    public OrderCreateVO createOrder(Long userId, OrderCreateRequest request) {
        Product product = productMapper.selectById(request.getProductId());
        if (product == null) {
            throw new BusinessException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        if (product.getStock() < request.getQuantity()) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK);
        }

        boolean deducted = inventoryService.deductStock(
            request.getProductId(), request.getQuantity());
        if (!deducted) {
            throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK);
        }

        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUserId(userId);
        order.setProductId(request.getProductId());
        order.setQuantity(request.getQuantity());
        order.setUnitPrice(product.getPrice());
        order.setTotalPrice(product.getPrice().multiply(
            new BigDecimal(request.getQuantity())));
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        orderMapper.insert(order);

        CreatePaymentRequest paymentRequest = buildPaymentRequest(order);
        PaymentResult paymentResult = paymentService.createPayment(paymentRequest);

        return buildCreateVO(order, paymentResult);
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId, Long userId) {
        Order order = orderMapper.selectById(orderId);
        if (order == null || !order.getUserId().equals(userId)) {
            throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
        }
        if (!order.getStatus().canCancel()) {
            throw new BusinessException(ErrorCode.ORDER_CANNOT_CANCEL);
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderMapper.updateById(order);

        inventoryService.restoreStock(order.getProductId(), order.getQuantity());

        if (order.getStatus() == OrderStatus.PAID) {
            try {
                paymentService.refund(order);
            } catch (Exception e) {
                order.setStatus(OrderStatus.REFUND_PENDING);
                orderMapper.updateById(order);
            }
        }
    }
}
```

## 生成要求
1. 在方法入口打 INFO 日志，记录关键参数
2. 每个业务校验失败前打 WARN 日志
3. 外部服务调用前后打 INFO 日志（调用前记录请求参数，调用后记录结果摘要）
4. 数据库写操作完成后打 INFO 日志
5. 异常捕获处打 ERROR 日志（含异常堆栈）
6. 方法正常返回前打 INFO 日志（记录返回值关键字段）
7. 注意只在已有代码基础上加日志语句，不修改任何业务逻辑
```

**期望输出**

- 每个方法入口有 INFO 日志（方法名 + 关键入参）
- 每个异常分支前有 WARN/ERROR 日志
- 外部调用（inventoryService、paymentService）有调用前日志
- 重要的数据库写操作（insert、updateById）后有确认日志
- 异常捕获中有完整堆栈的 ERROR 日志

**验收标准**

- 模拟一次完整的创建订单 → 取消订单流程，日志能还原出完整的调用链
- 日志中的关键 ID（orderId、userId、productId）贯穿全部日志，方便 grep 定位
- 没有敏感信息出现在日志中
- 日志级别使用正确：正常业务流程不用 ERROR，系统故障不用 INFO

**常见风险**

| 风险 | 说明 |
|------|------|
| 日志洪水 | AI 为了"完整"给每个 getter/setter 都加了日志，或者循环内打 INFO |
| 性能影响 | 日志参数中的方法调用（如 `log.info("xxx {}", obj.toString())`）即使日志级别关闭也会执行 |
| 敏感信息泄露 | JSON 序列化整个对象打日志，包含了用户密码、手机号等 |
| 日志格式不统一 | 有的用 `key=value`，有的用 `key:value`，有的直接拼字符串，无法用 ELK 结构化解析 |

**人工检查点**

1. **结构化格式**：确认所有日志使用了统一的 `key=value` 格式，便于日志平台解析
2. **日志参数惰性求值**：如果使用了参数化日志（`log.info("{}", expensiveCall())`），确认 expensiveCall() 在日志级别不够时不会执行（实际上 SLF4J 的参数化日志确实会先执行参数求值，这一点很多老手也会搞错——如果入参计算昂贵，需要加 `if (log.isInfoEnabled())` 判断）
3. **traceId 传递**：如果项目使用 MDC 传递 traceId，确认跨线程（`@Async`、线程池）时 traceId 正确传递

---

### 场景8：单元测试生成（JUnit 5 + Mockito）

**适用场景**

为新 Service 编写单元测试、补全已有代码的测试覆盖、重构前的安全网搭建。

**输入材料**

- 需要测试的 Service 类完整代码
- Service 依赖的外部接口（Mapper、RPC Client、MQ Producer 等）
- 项目的测试基类（如果有）

**提示词（完整可复制）**

```
你是一个坚持 TDD 的 Java 开发。请为以下 Service 方法生成完整的单元测试。

## 测试框架
- JUnit 5 (Jupiter)
- Mockito 5.x（使用 @ExtendWith(MockitoExtension.class)）
- AssertJ 断言库（推荐但非必须，JUnit 5 自带断言也可以）

## 测试原则
1. 每个测试方法应该只测一个行为
2. 测试方法命名: should_预期行为_when_触发条件
   例如: should_throwOrderNotFoundException_when_orderIdNotExist
3. Mock 所有外部依赖（Mapper、其他 Service、RPC Client）
4. 使用 @Mock 注解声明 Mock 对象，@InjectMocks 注入被测试对象
5. 每个测试的 Given-When-Then 三段式清晰

## 必须覆盖的测试场景
- 正常流程（Happy Path）
- 每个异常分支（每个 throw BusinessException 的分支）
- 边界值（quantity=1, quantity=999, stock刚好等于quantity）
- 依赖调用失败（Mapper 抛异常、外部 Service 抛异常）
- 并发场景（如果方法涉及状态变更，测试乐观锁失败的情况）

## 被测试代码
```java
@Service
public class OrderService {
    // ... (粘贴完整的 Service 代码)
}
```

## 生成要求
1. 生成完整的测试类，包括类注解和 import
2. 使用 @Nested 对内聚的测试场景分组（如 CancelOrderTests, CreateOrderTests）
3. 每个 Mock 的 when-thenReturn 要合理（返回值字段要填全，不要只 set ID）
4. 验证 Mock 调用次数：用 verify(mock, times(1)).method()
5. 异常测试使用 assertThrows，并验证异常的错误码
6. 不要使用  ArgumentMatchers.any()  偷懒，除非真的不关心参数值——使用 eq() 或具体的值
```

**期望输出**

- 完整的测试类文件
- 每个业务方法有对应的 `@Nested` 测试分组
- 每个测试方法有清晰的 Given-When-Then 结构
- Mock 的返回值是完整对象而非只有 ID 的空壳
- 异常测试覆盖了所有 `throw new BusinessException` 分支
- 使用了 `verify` 确认关键依赖调用确实发生了

**验收标准**

- 所有测试通过（不只是"不报错"，断言必须真正验证了结果）
- 覆盖率报告：分支覆盖率 90%+，行覆盖率 85%+
- Mock 没有被滥用：只 Mock 了外部依赖，没有 Mock 被测试类自身的方法
- 删除被测试类的某行逻辑后，至少有一个测试失败（测试的有效性验证）

**常见风险**

| 风险 | 说明 |
|------|------|
| 无断言的测试 | AI 生成的测试方法里只有 Mock 的设置和被测方法调用，没有 `assertXXX`，或者断言了一个永远为 true 的条件 |
| 过度 Mock | Mock 了同一个 Service 的其他方法（`doReturn(x).when(orderService).someMethod()`），这样的测试绑定了实现细节 |
| Mock 返回值残缺 | `Product product = new Product(); product.setId(1L);` 其他字段全为 null，被测方法访问 product.getName() 时 NPE |
| 只测 happy path | 5个测试，4个测正常流程的变体，只有1个测异常。但实际80%的生产问题发生在异常路径 |
| 使用 ArgumentMatchers.any() 验证 | `verify(mapper).insert(any())` 不验证传入了什么，即使传了错误的对象测试也会通过 |

**人工检查点**

1. **断言有效性**：每个 `assertXXX` 都有实际意义。比如 `assertNotNull(result)` 不算有效断言，`assertThat(result.getStatus()).isEqualTo(OrderStatus.CANCELLED)` 才算
2. **异常测试的异常类型**：确认 `assertThrows` 捕获的是正确的异常类型和错误码，不是只验证"抛了异常"
3. **Mock 数据的字段完整度**：被测试方法访问了 Product 的 `getName()`、`getPrice()`、`getStock()`，Mock 的 Product 对象这三个字段都必须设置
4. **测试独立性**：每个测试方法可以独立运行（不依赖其他测试的副作用），用 `@BeforeEach` 重置 Mock 状态

---

### 场景9：Mock 数据生成

**适用场景**

为新模块的第一个单元测试搭建测试数据、为演示/联调生成有真实感的样例数据、为压力测试准备多样化的数据集合。

**输入材料**

- Entity 类的完整字段定义
- 业务约束（哪些字段有唯一性、哪些有枚举值、哪些有格式要求）
- 外键关联关系

**提示词（完整可复制）**

```
请为以下 Entity 生成 10 条 Mock 测试数据。要求数据有真实感，能覆盖各种业务场景，不要全部是 test1, test2 这种无意义值。

## Entity 定义
```java
@Data
public class User {
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String realName;
    private Gender gender; // enum: MALE, FEMALE, UNKNOWN
    private UserStatus status; // enum: ACTIVE, INACTIVE, SUSPENDED
    private LocalDateTime createdAt;
}

@Data
public class Order {
    private Long id;
    private String orderNo; // 格式: ORD + yyyyMMdd + 6位随机数
    private Long userId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private OrderStatus status; // PENDING, PAID, SHIPPED, COMPLETED, CANCELLED
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

## 数据要求
1. 针对 User:
   - id: 自增 1-10
   - username: 像真实用户名（如 zhang_san, li_hua_wang），不要 test1, test2
   - email: 使用常见域名（qq.com, 163.com, gmail.com, company.com）
   - phone: 1开头的11位合法手机号（不要 13800138000 这种假的）
   - realName: 像中文真实姓名
   - gender: 三种各至少2条
   - status: ACTIVE 为主（6-7条），INACTIVE 2条，SUSPENDED 1条

2. 针对 Order:
   - id: 自增 1-10
   - orderNo: 按格式生成，每天不同
   - userId: 1-10 之间，有些用户有多条订单
   - productName: 像真实商品（如 "iPhone 15 Pro Max 256GB 暗夜紫"）
   - quantity: 大部分 1-3，有一条 100（批量采购场景）
   - unitPrice 和 totalPrice: 逻辑一致（totalPrice = unitPrice * quantity）
   - status: PENDING 3条, PAID 2条, SHIPPED 2条, COMPLETED 2条, CANCELLED 1条
   - createdAt: 最近30天内，分布在不同日期

3. 额外要求:
   - 要有边界值：用户名最短（2字）和最长（50字），quantity=1 和 quantity=999
   - 要有异常数据场景：CANCELLED 状态的订单、SUSPENDED 状态的用户
   - 时间有不同时段：上午、下午、凌晨

## 输出格式
生成一个 Java 方法，返回 List/数组，可以直接 copy 到测试类的 @BeforeEach 中：
```java
private List<User> buildMockUsers() {
    return Arrays.asList(
        // 10条数据
    );
}
```
```

**期望输出**

- 可直接编译运行的 Java 代码块
- 数据字段完整，不会出现必填字段为 null
- 数据之间有合理的关联（userId 和 Order 中的 userId 匹配）
- 覆盖了正常值、边界值、异常值
- 价格的数学关系正确（totalPrice = unitPrice * quantity）

**验收标准**

- 粘贴到测试类后编译通过
- 数据种类覆盖了枚举的所有值
- 边界值存在（最短/最长字符串、最小/最大数量）
- 异常状态的数据存在（可以用于异常流程测试）

**常见风险**

| 风险 | 说明 |
|------|------|
| 无意义数据 | 全部是 "test1", "test2", "a", "b"——这样的数据在测试失败时无法判断是什么场景 |
| 数学错误 | `unitPrice = 10.00`, `quantity = 3`, `totalPrice = 10.00`——AI 忘了乘以数量 |
| 唯一约束冲突 | username 和 orderNo 有唯一约束，AI 生成了重复值 |
| 枚举值不匹配 | 生成的枚举值和 Entity 定义的枚举常亮名不一致（大写/小写/拼写） |

**人工检查点**

1. **外键关联的合理性**：Order.userId 在 User.id 集合中存在（可以用 SET 验证）
2. **唯一字段不重复**：username、orderNo、email 等字段
3. **价格字段的数学一致性**：每条 totalPrice = unitPrice * quantity（注意 BigDecimal 的精度）

---

### 场景10：接口测试用例生成

**适用场景**

编写 Controller 层的集成测试、验证接口的参数校验和异常处理是否正确工作、回归测试的核心用例。

**输入材料**

- Controller 类的完整代码
- 项目测试配置（Spring Boot Test 的基类、MockMvc 的使用方式）
- 认证方式（JWT Token、Session 等）

**提示词（完整可复制）**

```
请为以下 Controller 生成完整的接口测试用例。

## 测试框架
- Spring Boot Test + MockMvc（@SpringBootTest 或 @WebMvcTest）
- 使用 @Autowired MockMvc
- JUnit 5 + AssertJ
- 如果有鉴权，使用 @WithMockUser 或自定义的测试注解

## 项目约定
- API 路径前缀: /api/v1
- 鉴权方式: JWT，Header 中携带 Authorization: Bearer <token>
- 统一返回体: Result<T>，code=0 表示成功
- 分页返回结构: { "code": 0, "data": { "records": [...], "total": 100, "page": 1, "size": 20 } }

## Controller 代码
```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    // ... (粘贴完整代码)
}
```

## 测试用例覆盖要求
为每个接口至少覆盖以下场景:
1. 正常请求 — 200 响应，返回数据正确
2. 参数校验 — 缺少必填字段返回 400，错误信息包含字段名
3. 参数校验 — 格式错误（email格式不对、手机号位数不对）
4. 业务异常 — 资源不存在返回 404
5. 业务异常 — 重复创建返回 409
6. 鉴权 — 不带 Token 返回 401
7. 鉴权 — 带过期/伪造 Token 返回 401
8. 分页 — 默认分页参数（不传 page/size）
9. 分页 — 超大分页（size=10000，确认有上限限制）

## 生成要求
1. 使用 @Nested 给每个 API 接口分组
2. MockMvc 请求使用 .contentType(MediaType.APPLICATION_JSON) 和 .content(jsonString)
3. 使用 jsonPath() 精确断言返回字段值（不光校验 HTTP status）
4. 提供辅助方法将请求对象序列化为 JSON（可以复用 ObjectMapper）
5. 如果 Controller 依赖 Service，使用 @MockBean Mock Service 的行为
6. 每个测试方法有清晰的中文 @DisplayName
```

**期望输出**

- 完整的测试类
- 每个 API 接口一个 @Nested 分组
- 每组至少 3-5 个测试方法
- 使用了 `jsonPath("$.code")` 或 `jsonPath("$.data.xxx")` 精确断言
- MockMvc 的请求构造完整（Headers、ContentType、Body）

**验收标准**

- `mvn test` 或 `./gradlew test` 全部通过
- 删除 Controller 的 `@Valid` 注解后，参数校验相关的测试应该失败（验证测试有效性）
- 断言不只检查 HTTP status，检查了返回体的具体字段值
- 鉴权测试覆盖了 401 场景

**常见风险**

| 风险 | 说明 |
|------|------|
| 只测了 HTTP status | `andExpect(status().isOk())` 但没有 `jsonPath` 校验返回内容——接口返回了错误数据测试也通过 |
| MockMvc 不加载 Security Filter | `@WebMvcTest` 默认不加载 Spring Security 的 Filter Chain，鉴权测试可能因为配置问题不生效 |
| MockBean 没 reset | 多个测试方法共享 `@MockBean`，前一个测试的 Mock 状态影响后续测试 |
| 测试数据库污染 | 使用了 `@SpringBootTest` 但没加 `@Transactional`，测试数据残留在数据库 |

**人工检查点**

1. **鉴权测试是否真的生效**：写一个不带 token 的测试，确认返回 401 而非 200（说明 Security 配置没加载）
2. **jsonPath 语法正确**：`$.data.records[0].username` 这种多层嵌套路径，JSON 结构稍有变化就会导致测试挂掉——如果频繁因字段变化而失败，考虑适当降低断言精度
3. **测试间的数据隔离**：如果使用真实数据库（非 H2），确认加了 `@Transactional` 且每条测试数据不影响其他测试

---

### 场景11：Postman Collection 生成

**适用场景**

前后端联调前准备接口文档、第三方对接时提供 API 调用示例、QA 团队需要完整的接口测试集合。

**输入材料**

- Controller 类的完整代码（包含注解和注释）
- 项目 Base URL（开发/测试/生产环境）
- 鉴权方式（Token 获取方式、环境变量设置方式）

**提示词（完整可复制）**

```
请根据以下 Spring Boot Controller 代码生成一个 Postman Collection v2.1 JSON 文件。

## Postman Collection 要求

### 基本信息
- Collection 名称: 项目名称 + API
- 描述: 简要说明 API 的用途

### 环境变量
需要在 Collection 中预定义以下变量（用户导入后手动设置）:
- `base_url`: API 基础地址（如 http://localhost:8080）
- `auth_token`: JWT Token（登录后获取）

### 每个接口的 Folder 分组
按 Controller 的 @RequestMapping 前缀分组

### 每个 Request 需要包括:
1. 请求方法 (GET/POST/PUT/DELETE)
2. URL: 使用 {{base_url}} + 路径
3. Headers:
   - Content-Type: application/json
   - Authorization: Bearer {{auth_token}}（需要认证的接口）
4. Query Params（如果有 @RequestParam）
5. Path Variables（如果有 @PathVariable）
6. Request Body（如果有 @RequestBody），填写一个合理的示例 JSON
7. Pre-request Script（如果需要动态生成数据，如当前时间戳）
8. Tests 脚本（每个 request 至少包含）:
   - 校验 HTTP status code
   - 校验返回体 code 字段为 0 或指定的错误码
   - 对于创建接口，将返回的 ID 存到环境变量供后续使用

### 生成要求
- 输出完整有效的 JSON（可以直接导入 Postman）
- Request Body 中的示例数据要真实、合理
- 按业务操作顺序排列 folder 中的 request（先创建、再查询、再更新、最后删除）
- 错误场景的 request 也包含进去（如查询不存在的 ID、传错误的参数）
- 不要生成空的 Request Body（{}），至少包含所有必填字段

## Controller 代码
```java
// (粘贴完整的 Controller 代码)
```

## 输出格式
直接输出完整的 Postman Collection JSON，放在 ```json 代码块中。
```

**期望输出**

- 一个完整的 Postman Collection JSON，符合 Postman Collection v2.1 格式规范
- 包含 info（name、description、schema）、item（folder 和 request）、variable（环境变量定义）
- 每个 request 有 Pre-request Script 和 Tests（Postman 的 JavaScript 脚本）
- Request Body 的示例数据字段完整、值合理

**验收标准**

- 导入 Postman 不报错
- 所有环境变量（`{{base_url}}`、`{{auth_token}}`）替换后 URL 有效
- Tests 脚本中的断言语法正确（Postman 的 pm.test / pm.response / pm.expect）
- 至少有一个 request 的 Tests 包含 `pm.environment.set("xxx_id", jsonData.data.id)` 用于链式调用

**常见风险**

| 风险 | 说明 |
|------|------|
| Schema 版本错误 | v2.0 和 v2.1 的 schema URL 不同，混用导致导入失败 |
| JSON 格式错误 | 多了一个逗号、少了引号，JSON 解析失败 |
| Postman 脚本语法错误 | Postman 的沙箱不是 Node.js，不支持 require/module，某些 ES6 语法可能不支持 |
| URL 路径硬编码 | 写了 `http://localhost:8080/api/users` 而不是 `{{base_url}}/api/users` |

**人工检查点**

1. **JSON 有效性**：复制到任意 JSON 校验工具验证
2. **Postman 导入实测**：导入后跑一个 GET 请求确认正常
3. **Tests 脚本语法**：发送一个请求后查看 Postman Console 确认 Tests 没有报 `SyntaxError`
4. **示例数据的合理性**：不要出现 "string"、"0"、"test" 这种占位值，应该像真实数据

---

## 11.4 人机分工表

AI 编码不是简单的"人写需求 → AI 出代码 → 人点确认"。不同编码环节的 AI 可用性差异巨大，以下是根据实际项目经验总结的分工表：

| 编码环节 | AI 能做 | AI 做不好 | 人必须做 |
|----------|---------|-----------|----------|
| **Controller** | 生成标准 CRUD 模板、参数校验注解、Swagger 文档注解 | 复杂的 API 编排（聚合多个 Service 的结果）、细粒度的权限控制 | 审查 RESTful 设计是否合理（资源命名、HTTP 方法选择）、确认权限校验的覆盖范围 |
| **Service** | 生成标准事务逻辑、状态机的 happy path、简单业务规则 | 复杂状态流转（多条件分支的状态变更）、分布式事务的补偿逻辑、缓存一致性策略 | 审查业务规则的正确性、验证状态机的完整性、评估事务边界是否合理 |
| **DAO/Mapper** | 生成标准 CRUD SQL、简单条件查询、基于索引的关联查询 | 复杂多表关联查询的优化、慢 SQL 的分析和改写、分库分表的路由逻辑 | 审查 SQL 性能（Explain）、确认索引使用是否正确、检查 SQL 注入防护 |
| **DTO/VO** | 根据 Entity 生成字段映射、补充校验注解、生成 Swagger Schema | 字段设计合理性（哪些字段应该分组为嵌套对象）、API 版本演进时的兼容性处理 | 审查字段定义是否符合前端需求、确认敏感字段是否在错误的 VO 中暴露 |
| **参数校验** | 补充 @NotNull/@NotBlank/@Size 等注解、生成正则表达式 | 跨字段校验（如开始时间必须早于结束时间）、业务规则校验（如库存数量 > 订单数量） | 验证正则是否正确、补充跨字段的自定义校验器 |
| **异常处理** | 生成 @ControllerAdvice 模板、定义标准错误码枚举 | 错误码粒度设计（太粗不好定位，太细维护困难）、第三方异常的转译策略 | 审查错误码划分的合理性、确认异常信息不泄露敏感数据 |
| **日志** | 在方法入口/出口和异常分支添加日志 | 日志级别选择（有时 AI 会把正常流程打 ERROR）、确定哪些信息应该记入审计日志 | 审查日志内容是否有敏感信息泄露、确认关键业务流程有完整的日志链路 |
| **单元测试** | 生成 Happy Path 测试、Mock 设置、基础异常测试 | 复杂的并发场景测试、外部系统依赖的超时和重试测试、测试数据的真实性设计 | 审查断言是否有效、确认边界值覆盖、验证 Mock 没有被滥用 |
| **集成测试** | 生成 MockMvc 请求构造代码、基本断言 | 测试数据准备和清理的可靠性、多个接口的链式调用场景 | 确认鉴权测试生效、验证测试之间的数据隔离 |
| **配置文件** | 生成 application.yml 模板、多环境配置结构 | 生产环境配置的安全值设定（数据库连接池大小、超时时间等需要根据实际压测确定） | 审查配置值是否合理、确认密钥和密码不进入版本控制 |
| **SQL 脚本** | 生成建表 DDL、基础索引 | 复杂索引策略（联合索引的字段顺序）、大表 DDL 的在线变更方案 | 审查索引设计、确认字段类型选择合理（用 varchar(20) 还是 text） |

---

## 11.5 编码质量速查表

以下是 AI 生成 Java 代码时最容易出现的质量问题。每个问题都有具体的错误示例和正确做法。

### 1. 事务边界错误

**症状**：远程调用（RPC、HTTP、MQ 发送）放在 `@Transactional` 方法内，导致事务时间过长、数据库连接耗尽。

```java
// 错误：RPC 调用在事务内
@Transactional
public void createOrder(OrderRequest request) {
    orderMapper.insert(order);
    paymentClient.pay(request); // RPC 调用可能耗时 3-5 秒
    inventoryClient.deductStock(request); // 又一个 RPC
}

// 正确：事务只包裹数据库操作，RPC 放在事务后
public void createOrder(OrderRequest request) {
    Order order = doCreateInTransaction(request);
    try {
        paymentClient.pay(request);
        inventoryClient.deductStock(request);
    } catch (Exception e) {
        compensateOrder(order.getId()); // 补偿逻辑
        throw e;
    }
}

@Transactional
private Order doCreateInTransaction(OrderRequest request) {
    // 纯数据库操作
    orderMapper.insert(order);
    return order;
}
```

### 2. 缺失空值检查

**症状**：数据库查询使用了可能返回 null 的方法，直接调用结果的方法导致 NPE。

```java
// 错误：selectById 可能返回 null
public OrderVO getOrder(Long orderId) {
    Order order = orderMapper.selectById(orderId);
    return OrderConverter.toVO(order); // NPE!
}

// 正确：先判空再处理
public OrderVO getOrder(Long orderId) {
    Order order = orderMapper.selectById(orderId);
    if (order == null) {
        throw new BusinessException(ErrorCode.ORDER_NOT_FOUND);
    }
    return OrderConverter.toVO(order);
}
```

### 3. 动态 SQL 注入

**症状**：排序字段、分组字段使用 `${}` 拼接用户输入。

```xml
<!-- 错误：sortField 来自前端参数，直接用 ${} -->
<select id="listOrders">
    SELECT * FROM t_order
    WHERE user_id = #{userId}
    ORDER BY ${sortField} ${sortOrder}
</select>

<!-- 正确：白名单校验后再拼接 -->
<!-- 在 Java 代码中先校验 -->
private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("created_at", "total_price", "status");
private static final Set<String> ALLOWED_SORT_ORDERS = Set.of("ASC", "DESC");

public IPage<Order> listOrders(Long userId, String sortField, String sortOrder) {
    if (sortField == null || !ALLOWED_SORT_FIELDS.contains(sortField)) {
        throw new BusinessException(ErrorCode.INVALID_SORT_FIELD);
    }
    if (sortOrder == null || !ALLOWED_SORT_ORDERS.contains(sortOrder.toUpperCase())) {
        throw new BusinessException(ErrorCode.INVALID_SORT_ORDER);
    }
    return mapper.listOrders(userId, sortField, sortOrder.toUpperCase());
}
```

### 4. 列表查询缺少分页

**症状**：`SELECT * FROM table WHERE condition` 没有 LIMIT，数据量增长后接口超时。

```java
// 错误：查询没有分页
@GetMapping("/orders")
public Result<List<OrderVO>> listOrders(Long userId) {
    List<Order> orders = orderMapper.selectByUserId(userId); // 可能返回几十万条
    return Result.success(orders);
}

// 正确：强制分页
@GetMapping("/orders")
public Result<PageVO<OrderVO>> listOrders(
        Long userId,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "20") int size) {
    if (size > 100) { size = 100; } // 硬限制
    Page<Order> orderPage = orderMapper.selectByUserId(
        new Page<>(page, size), userId);
    return Result.success(PageVO.of(orderPage));
}
```

### 5. 单例 Bean 的线程安全问题

**症状**：Spring 的 `@Service`、`@Component` 默认是单例，AI 生成的代码可能在其中使用可变成员变量。

```java
// 错误：单例 Bean 中使用了可变字段
@Service
public class ReportService {
    private List<String> currentReportData; // 多线程共享，数据串了！

    public ReportVO generate(List<Long> orderIds) {
        this.currentReportData = new ArrayList<>();
        // ... 填充数据
        return buildReport(currentReportData);
    }
}

// 正确：使用方法局部变量
@Service
public class ReportService {
    public ReportVO generate(List<Long> orderIds) {
        List<String> reportData = new ArrayList<>(); // 方法内局部变量，线程安全
        // ... 填充数据
        return buildReport(reportData);
    }
}
```

### 6. 资源泄露（未关闭流）

**症状**：文件读取、网络连接使用了 Stream 但未正确关闭。

```java
// 错误：Stream 未关闭
public String readFileContent(String path) throws IOException {
    return Files.lines(Path.of(path))
        .collect(Collectors.joining("\n"));
    // Files.lines 返回的 Stream 持有文件句柄，没有关闭！
}

// 正确：使用 try-with-resources
public String readFileContent(String path) throws IOException {
    try (Stream<String> lines = Files.lines(Path.of(path))) {
        return lines.collect(Collectors.joining("\n"));
    }
}
```

### 7. 异常被吞没

**症状**：catch 块中只打日志不重新抛出，或者用 `e.printStackTrace()` 处理。

```java
// 错误：捕获异常后什么都不做
try {
    paymentService.refund(order);
} catch (Exception e) {
    // 什么都没做，调用方以为退款成功了
}

// 错误：只打日志不决定下一步
try {
    paymentService.refund(order);
} catch (Exception e) {
    log.error("退款失败", e);
    // 然后呢？调用方拿到的是成功还是失败？
}

// 正确：明确异常处理策略
try {
    paymentService.refund(order);
} catch (RefundException e) {
    log.error("退款失败, orderId={}, reason={}", order.getId(), e.getMessage());
    order.setStatus(OrderStatus.REFUND_PENDING); // 标记为需人工处理
    order.setRefundFailureReason(e.getMessage());
    orderMapper.updateById(order);
    // 不抛异常，让主流程继续，但留下了可追溯的记录
}
```

---

## 11.6 测试质量速查表

AI 生成的测试代码同样有自己的"症状群"，下面是最常见的 5 类问题。

### 1. 测试永远通过（无有效断言）

**症状**：测试方法有 Mock 设置和方法调用，但没有 `assertXXX`，或者断言值恒为 true。

```java
// 错误：没有断言
@Test
void should_createOrder() {
    when(orderMapper.insert(any())).thenReturn(1);
    when(productMapper.selectById(1L)).thenReturn(mockProduct);
    
    orderService.createOrder(1L, request);
    
    // 没有 assert！这个方法只要不抛异常就算通过。
}

// 正确：有效断言
@Test
void should_createOrder_successfully() {
    when(orderMapper.insert(any())).thenReturn(1);
    when(productMapper.selectById(1L)).thenReturn(mockProduct);
    
    OrderCreateVO result = orderService.createOrder(1L, request);
    
    assertThat(result).isNotNull();
    assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.PENDING);
    assertThat(result.getTotalPrice()).isEqualByComparingTo(
        new BigDecimal("299.97")); // unitPrice 99.99 * quantity 3
    verify(orderMapper).insert(argThat(order ->
        order.getStatus() == OrderStatus.PENDING));
}
```

### 2. 缺少边界值测试

**症状**：所有测试都使用"常规值"（quantity=5, page=1），没有测试最小值、最大值、零值、负值。

```java
// 缺失的边界值测试应该补充：

@Test
void should_createOrder_withMinimumQuantity() {
    request.setQuantity(1);
    OrderCreateVO result = orderService.createOrder(1L, request);
    assertThat(result).isNotNull();
}

@Test
void should_rejectOrder_whenQuantityExceedsMax() {
    request.setQuantity(1000); // 超过上限 999
    assertThrows(BusinessException.class,
        () -> orderService.createOrder(1L, request));
}

@Test
void should_rejectOrder_whenQuantityIsZero() {
    request.setQuantity(0);
    assertThrows(BusinessException.class,
        () -> orderService.createOrder(1L, request));
}

@Test
void should_rejectOrder_whenQuantityIsNegative() {
    request.setQuantity(-1);
    assertThrows(BusinessException.class,
        () -> orderService.createOrder(1L, request));
}
```

### 3. 过度 Mock（Mock 了被测对象自己）

**症状**：使用 `@SpyBean` 或 `doReturn().when(service).method()` Mock 了正在被测试的类。

```java
// 错误：Mock 了被测对象自己的方法
@SpyBean
private OrderService orderService;

@Test
void should_createOrder() {
    // Mock 了被测类的私有方法——测试绑定了实现细节
    doReturn("ORD20260701000001").when(orderService).generateOrderNo();
    
    OrderCreateVO result = orderService.createOrder(1L, request);
    // ...
}
// 以后 generateOrderNo() 改成别的实现，测试会挂掉，但你只是换了个内部实现

// 正确：只 Mock 外部依赖
@Mock
private OrderMapper orderMapper;
@Mock
private PaymentService paymentService;
@InjectMocks
private OrderService orderService;

@Test
void should_createOrder() {
    when(orderMapper.insert(any())).thenReturn(1);
    // 不 Mock orderService 自己的方法，信任其内部实现
}
```

### 4. 缺少异常场景测试

**症状**：所有测试都是 Happy Path，没有测试依赖失败、数据库异常、网络超时等异常场景。

```java
// 缺失的异常测试应该补充：

@Test
void should_throwException_when_productNotFound() {
    when(productMapper.selectById(999L)).thenReturn(null);
    
    BusinessException ex = assertThrows(BusinessException.class,
        () -> orderService.createOrder(1L, request));
    assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.PRODUCT_NOT_FOUND);
}

@Test
void should_throwException_when_stockInsufficient() {
    Product product = new Product();
    product.setStock(2); // 库存只有2
    when(productMapper.selectById(1L)).thenReturn(product);
    request.setQuantity(5); // 需要5
    
    BusinessException ex = assertThrows(BusinessException.class,
        () -> orderService.createOrder(1L, request));
    assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.INSUFFICIENT_STOCK);
}

@Test
void should_throwException_when_mapperInsertFails() {
    when(productMapper.selectById(1L)).thenReturn(mockProduct);
    when(orderMapper.insert(any()))
        .thenThrow(new DataAccessException("DB error") {});
    
    assertThrows(DataAccessException.class,
        () -> orderService.createOrder(1L, request));
}
```

### 5. 测试耦合于实现细节

**症状**：测试代码使用了过于精确的 `verify` 或依赖内部调用顺序，重构（不改行为）后测试失败。

```java
// 错误：测试绑定了内部调用顺序
@Test
void should_createOrder() {
    orderService.createOrder(1L, request);
    
    InOrder inOrder = inOrder(productMapper, inventoryService, orderMapper);
    inOrder.verify(productMapper).selectById(1L);
    inOrder.verify(inventoryService).deductStock(1L, 3);
    inOrder.verify(orderMapper).insert(any());
    // 以后如果先扣库存再查商品，这个测试就挂了，但行为没变
}

// 正确：验证行为和结果，不绑顺序
@Test
void should_createOrder_andDeductStock() {
    OrderCreateVO result = orderService.createOrder(1L, request);
    
    assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.PENDING);
    verify(inventoryService).deductStock(1L, 3);
    verify(orderMapper).insert(any());
    // 不关心调用顺序
}
```

---

## 11.7 本章要点

1. **给 AI 看输入和输出，不要描述实现步骤**。AI 擅长推导实现，人擅长定义需求和审查结果。角色不要互换。

2. **每个场景都有固定的人工检查点**。不是"AI 生成完就完事"，而是对照检查点逐项核实。这些检查点是过往项目踩坑后沉淀的，忽略其中任何一个都会在调试阶段付出代价。

3. **事务、并发、安全**是 AI 最不靠谱的三个方面。这三类问题编译不会报错，测试不刻意覆盖也不会暴露，但生产环境一定会炸。对这三类代码，必须人工逐行审查。

4. **编码质量速查表中的 7 个问题**不是 Java 编程的基础知识——有经验的程序员也会犯。把速查表当成 Code Review Checklist，审查 AI 代码时逐项打勾。

5. **测试的有效性比覆盖率更重要**。100% 覆盖率的无断言测试不如 60% 覆盖率的有效测试。人工审查测试时，先看断言，再看覆盖。

> **下一章**：第12章《AI 辅助代码审查与重构》——如何用 AI 做 Code Review，以及如何让 AI 安全地进行代码重构。
