# 第六章：Vibe Coding 企业边界手册

## 6.1 Vibe Coding 是什么

Vibe Coding 是指开发者用自然语言向 AI 描述需求，由 AI 直接生成完整代码，开发者通过运行、观察、反馈来迭代调整，全程不深入理解每一行实现细节的开发方式。它把编程从"精确书写指令"变成了"对话式引导"，本质是**用直觉和反馈驱动代码生成，而非用规格和控制驱动代码构建**。

## 6.2 适合场景 vs 不适合场景

| 场景类型 | 适合 Vibe Coding? | 原因 | 替代方案 |
|----------|-------------------|------|----------|
| 原型验证 | ✅ 适合 | 速度优先，核心目标是快速看到效果验证想法，代码质量不是约束条件。Vibe Coding 可以在几十分钟内产出可交互原型 | — |
| Demo 演示 | ✅ 适合 | 演示代码只需在特定 happy path 下运行正确，不涉及边界条件和异常处理。Vibe Coding 天然匹配这种"看起来能用"的需求 | — |
| 内部小工具 | ✅ 适合（有限） | 用户是开发者自己或团队内部，出问题影响可控。但需要加安全规则约束——见 6.4 节 | 若涉及数据库写操作，改用 Spec-Driven |
| 一次性脚本 | ✅ 适合 | 用完即弃，不需要长期维护。数据处理脚本、格式转换工具、临时迁移脚本都是典型场景 | — |
| 核心业务模块 | ❌ 不适合 | 核心模块的正确性直接影响业务，且需要长期维护和多人协作。AI 生成的代码缺少系统性的架构考量和完整的异常处理链路 | Spec-Driven：先写规格文档，AI 辅助实现，强制 Code Review |
| 金融交易系统 | ❌ 不适合 | 涉及资金安全，对正确性、一致性、可审计性有极致要求。任何一笔错误都不可接受，Vibe Coding 的"差不多对"在这里就是"完全不能用" | Spec-Driven + 形式化验证 + 独立测试团队 |
| 需合规审计 | ❌ 不适合 | 监管机构要求完整的开发过程记录、代码溯源、安全审查。Vibe Coding 的过程不可追溯，生成的代码难以按传统审计标准解释 | Spec-Driven：需求文档→设计文档→代码→测试，全链路可追溯 |
| 多人协作项目 | ⚠️ 谨慎 | Vibe Coding 产出的代码风格不一致、缺少注释、结构随意，其他人接手时理解成本极高。适合单人阶段，一旦需要协作就应转向规范开发 | 从 Vibe Coding 过渡到 Spec-Driven（见 6.5 节"工程化过渡"） |
| 安全敏感系统 | ❌ 不适合 | 认证、授权、加密逻辑一旦出错就是安全漏洞。AI 可能生成看似正确但实际不安全的代码，比如用了已废弃的加密算法或错误的 token 验证方式 | Spec-Driven + 安全专家审查 + 渗透测试 |
| 性能关键路径 | ⚠️ 谨慎 | AI 通常生成功能正确的代码，但不会主动优化算法复杂度、减少内存分配、利用缓存。用于性能关键路径时，需要额外的人工优化轮次 | Vibe Coding 生成初版 + 人工性能调优 |

## 6.3 Vibe Coding vs Spec-Driven Development 对比表

| 维度 | Vibe Coding | Spec-Driven Development |
|------|-------------|------------------------|
| **核心目标** | 快速产出可运行代码 | 产出正确、可维护、可审计的软件 |
| **开发速度（初始）** | 极快，几十分钟出原型 | 较慢，需要先写规格文档 |
| **开发速度（长尾）** | 减速明显，修改越多越难改 | 匀速，架构承载扩展 |
| **代码质量** | 不稳定，取决于 AI 当时的状态和 prompt 质量 | 稳定可控，有统一规范和 Review 把关 |
| **可维护性** | 低——代码结构随意，缺少文档和注释 | 高——有架构设计，代码按规范组织 |
| **可测试性** | 低——通常不包含测试，改动后无法快速验证回归 | 高——通常有单元测试和集成测试覆盖 |
| **可审计性** | 几乎为零——无法追溯"为什么要这样写" | 完整——需求→设计→实现→测试全链路 |
| **适合团队规模** | 1-2 人 | 2 人以上，规模越大 Spec-Driven 优势越明显 |
| **学习成本** | 低——会描述需求即可开始 | 中高——需要掌握规格编写、架构设计、代码审查 |
| **对开发者的要求** | 能判断输出是否正确，能精准写 prompt | 能系统性地设计和拆解问题 |
| **对 AI 的依赖程度** | 极高——离开 AI 几乎无法继续 | 中等——AI 是辅助工具，开发者是决策者 |
| **风险** | 安全漏洞、隐蔽 bug、技术债务快速累积 | 进度风险（如果规格阶段耗时过长） |
| **适用阶段** | 探索期、验证期 | 交付期、维护期 |
| **代码所有权** | 模糊——代码是谁写的？AI 还是开发者？ | 清晰——开发者设计和审查每一行 |
| **知识沉淀** | 弱——知识在 AI 的黑盒里，不在团队中 | 强——知识在文档、设计决策、代码注释中 |

## 6.4 安全使用 Vibe Coding 的 7 条规则

### 规则 1：不碰生产数据库

Vibe Coding 生成的代码**永远不能**直接连接生产数据库。即使是"只读查询"，也可能因为 AI 生成了低效的查询语句拖垮数据库。

**示例**：做一个内部报表工具，让 AI 生成查询脚本。连接串必须指向**只读副本**或**脱敏后的测试库**，且必须有人工 Review 所有 SQL 语句。

```sql
-- ❌ AI 可能生成的危险代码
SELECT * FROM orders o
JOIN users u ON o.user_id = u.id  -- 全表 JOIN，百万级数据直接 OOM
WHERE o.status = 'pending';

-- ✅ 人工审核后限制范围
SELECT o.id, o.amount, o.created_at FROM orders o
WHERE o.status = 'pending' AND o.created_at > DATE_SUB(NOW(), INTERVAL 7 DAY)
LIMIT 1000;
```

### 规则 2：不生成认证/授权逻辑

认证（Authentication）和授权（Authorization）是企业系统安全的最后一道门。AI 可能在 token 验证、session 管理、权限检查中引入微妙但致命的安全漏洞。

**示例**：需要加登录功能时，不要问 AI "帮我写一个 JWT 登录"。应该使用团队已验证过的认证库或服务，AI 只负责集成调用。

```
# ❌ 危险 prompt
"帮我实现用户登录，用 JWT 验证，支持 refresh token"

# ✅ 安全 prompt
"在现有的 auth_service.verify(token) 基础上，给这个路由加一个登录检查装饰器，调用已有的认证服务"
```

### 规则 3：生成后必须安全扫描

AI 生成的代码必须经过**自动化安全扫描**才能进入任何非本地环境。最低限度检查：SQL 注入、XSS、敏感信息硬编码、依赖漏洞。

**示例**：CI 流程中增加强制步骤——任何标记为 `ai-generated` 的代码合并前，必须通过 `bandit`（Python）、`brakeman`（Ruby）、`npm audit`（Node）或 SonarQube 扫描，不通过则自动阻断合并。

```bash
# Python 项目示例：CI 中加入
bandit -r src/ -f json -o bandit-report.json
# 任何 severity >= medium 的结果阻断 CI

# 同时检查是否硬编码了密钥
gitleaks detect --source . --report-path gitleaks-report.json
```

### 规则 4：限制 AI 修改范围

不要让 AI 在"整个项目"范围内自由发挥。每次对话明确限制修改的文件或模块范围，防止 AI 为解决问题而修改无关代码。

**示例**：

```
# ❌ 范围太大，AI 可能改任何文件
"项目启动报错，帮我修一下"

# ✅ 明确边界
"src/api/order_handler.py 里的 create_order 函数报 KeyError，只改这个函数，不要动其他文件"
```

实用技巧：使用 `.cursorrules` 或 `CLAUDE.md` 在项目配置中明确禁止 AI 修改的目录（如 `migrations/`、`config/`、`infrastructure/`）。

### 规则 5：关键逻辑必须人工审核

定义"关键逻辑"的明确标准，落入标准的代码不接受仅 AI 生成。关键逻辑包括但不限于：

- 涉及资金计算的代码
- 数据删除或修改操作
- 权限检查逻辑
- 外部 API 调用（含计费的）
- 并发或事务处理

**示例**：AI 生成了一段退款逻辑，即便运行测试通过了，也必须有人逐行审查退款金额计算是否正确、幂等性是否保证、日志是否完整。

### 规则 6：必须有回滚方案

任何 Vibe Coding 产物的部署，必须有**一键回滚**的能力。这不是针对 AI 代码的特殊要求，但 AI 代码的不可预测性让回滚变得更加重要。

**示例**：

```yaml
# Kubernetes deployment 示例
# 部署前保留上一个正常运行的 replicaset
# 观察 5 分钟无异常后认为部署成功
# 出现异常时：
kubectl rollout undo deployment/internal-tool-api
```

对于非 K8s 环境：部署前务必保留上一版本的二进制/JAR/容器镜像，回滚路径要在部署文档中明确写出并实际验证过。

### 规则 7：明确标注 "AI 生成，未经完整测试"

所有 AI 生成的代码文件，在文件头部或对应模块的注释中明确标注来源。这不是免责声明，而是**工程诚实**——让每个接手的人知道这段代码的"出身"，从而投入相应的审查力度。

**示例**：

```python
"""
模块: internal_metrics_report.py
用途: 生成内部运营周报
注意: 本模块由 AI 辅助生成（2026-07-01），未经完整测试覆盖。
      已知局限: 大数据量（>10万行）下内存可能溢出，高负载下未验证。
      如将此模块转为正式服务，请先完成:
      1. 补充单元测试覆盖核心计算逻辑
      2. 大数据量压力测试
      3. 异常日志接入告警系统
"""
```

## 6.5 完整案例：用 AI 快速做一个内部接口测试小工具

### 场景描述

团队开发了一个 REST API，QA 需要频繁调用接口、修改参数、查看返回值来手动测试。Postman 配置太繁琐，curl 太原始。需要一个简单的命令行工具，能发送请求、查看结果、保存测试用例。

### 初始提示词（完整可复制）

```
你是一个 Python 开发者。帮我做一个命令行工具 `apitest`，功能如下：

1. 读取 YAML 格式的测试用例文件，一个文件可以包含多个测试用例
2. 每个测试用例包含：名称、请求方法、URL、请求头、请求体、期望状态码
3. 依次执行每个用例，打印请求和响应摘要
4. 比较实际状态码和期望状态码，标记通过/失败
5. 最后打印汇总统计

YAML 格式参考：
```yaml
tests:
  - name: 获取用户列表
    method: GET
    url: http://localhost:8080/api/users
    headers:
      Authorization: Bearer xxx
    expect_status: 200

  - name: 创建用户
    method: POST
    url: http://localhost:8080/api/users
    headers:
      Content-Type: application/json
    body:
      name: test_user
      email: test@example.com
    expect_status: 201
```

要求：
- 使用 Python 标准库 + requests + pyyaml
- 输出带颜色的终端结果（绿色通过，红色失败）
- 支持命令行参数指定 YAML 文件路径
- 单文件实现
```

### 第一版生成结果

AI 生成一个约 200 行的 `apitest.py`，核心结构：

```python
import sys
import yaml
import requests
from pathlib import Path

# ANSI 颜色
GREEN = '\033[92m'
RED = '\033[91m'
YELLOW = '\033[93m'
RESET = '\033[0m'
BOLD = '\033[1m'

def load_test_file(filepath: str) -> dict:
    """加载 YAML 测试文件"""
    with open(filepath, 'r', encoding='utf-8') as f:
        return yaml.safe_load(f)

def run_test(test: dict, index: int) -> dict:
    """执行单个测试用例，返回结果字典"""
    method = test['method'].upper()
    url = test['url']
    headers = test.get('headers', {})
    body = test.get('body')
    expect_status = test.get('expect_status', 200)

    try:
        response = requests.request(method, url, headers=headers,
                                     json=body, timeout=10)
        actual_status = response.status_code
        passed = actual_status == expect_status

        result = {
            'name': test['name'],
            'method': method,
            'url': url,
            'expect_status': expect_status,
            'actual_status': actual_status,
            'passed': passed,
            'response_body': response.text[:200],
            'elapsed_ms': round(response.elapsed.total_seconds() * 1000)
        }
    except requests.exceptions.RequestException as e:
        result = {
            'name': test['name'],
            'method': method,
            'url': url,
            'error': str(e),
            'passed': False
        }

    return result

def print_result(result: dict, index: int):
    """打印单个测试用例的执行结果"""
    status_icon = f"{GREEN}✓ PASS{RESET}" if result.get('passed') else f"{RED}✗ FAIL{RESET}"
    name = result.get('name', 'Unknown')
    method = result.get('method', '?')
    url = result.get('url', '?')

    print(f"\n{BOLD}[{index}] {name}{RESET}")
    print(f"    {method} {url}")
    if 'error' in result:
        print(f"    {RED}Error: {result['error']}{RESET}")
    else:
        print(f"    Status: {result['actual_status']} (expected {result['expect_status']}) {status_icon}")
        print(f"    Time: {result['elapsed_ms']}ms")
        print(f"    Body: {result['response_body'][:200]}")

def main():
    if len(sys.argv) < 2:
        print(f"{YELLOW}Usage: python apitest.py <test_file.yaml>{RESET}")
        sys.exit(1)

    filepath = sys.argv[1]
    if not Path(filepath).exists():
        print(f"{RED}File not found: {filepath}{RESET}")
        sys.exit(1)

    data = load_test_file(filepath)
    tests = data.get('tests', [])

    if not tests:
        print(f"{YELLOW}No tests found in file{RESET}")
        sys.exit(0)

    results = []
    for i, test in enumerate(tests, 1):
        result = run_test(test, i)
        results.append(result)
        print_result(result, i)

    # Summary
    passed = sum(1 for r in results if r.get('passed'))
    failed = len(results) - passed
    print(f"\n{BOLD}── Summary ──{RESET}")
    print(f"Total: {len(results)} | "
          f"{GREEN}Passed: {passed}{RESET} | "
          f"{RED}Failed: {failed}{RESET}")

    sys.exit(0 if failed == 0 else 1)

if __name__ == '__main__':
    main()
```

### 运行验证

```bash
# 安装依赖
pip install requests pyyaml

# 准备测试文件 test_cases.yaml（内容如上）
# 启动本地测试 API
# 运行
python apitest.py test_cases.yaml
```

终端输出：

```
[1] 获取用户列表
    GET http://localhost:8080/api/users
    Status: 200 (expected 200) ✓ PASS
    Time: 45ms
    Body: {"users":[...]}

[2] 创建用户
    POST http://localhost:8080/api/users
    Status: 201 (expected 201) ✓ PASS
    Time: 123ms
    Body: {"id":42,"name":"test_user"}

── Summary ──
Total: 2 | Passed: 2 | Failed: 0
```

### 反馈修改（3 轮典型迭代）

**第 1 轮反馈：增加变量支持**

```
在 YAML 中增加变量功能。支持在文件顶部定义变量，测试用例中用 ${变量名} 引用。
变量在运行时替换。示例：

variables:
  base_url: http://localhost:8080
  token: eyJhbGciOi...

tests:
  - name: 获取用户
    url: ${base_url}/api/users
    headers:
      Authorization: Bearer ${token}
```

AI 修改：在 `load_test_file` 中增加变量解析逻辑，用正则 `\$\{(\w+)\}` 处理替换，递归替换嵌套结构中的值。

**第 2 轮反馈：增加响应断言**

```
除了检查状态码，还需要支持对响应体做断言。支持两种断言：
1. jsonpath: 从响应 JSON 中提取值并比较
2. contains: 检查响应体是否包含指定字符串

YAML 中这样写：
  - name: 创建用户并验证
    method: POST
    url: ${base_url}/api/users
    body:
      name: test_user
    expect_status: 201
    assert:
      - type: jsonpath
        path: $.name
        operator: eq
        value: test_user
      - type: contains
        value: "created"
```

AI 修改：引入 `jsonpath-ng` 库处理 JSONPath，增加 `check_assertions` 函数，在执行结果中新增断言通过/失败明细。

**第 3 轮反馈：增加测试报告导出**

```
增加 --report 选项，支持将测试结果导出为 HTML 报告。
HTML 报告包含：
- 测试概要（通过率、耗时）
- 每个用例的详细信息（可折叠）
- 失败用例高亮
使用纯 HTML+内联 CSS，无外部依赖
```

AI 修改：增加 `generate_html_report(results, output_path)` 函数，使用 Python 字符串模板生成带内联样式的 HTML 文件。

### 功能迭代路径

工具逐步演进的典型路径：

```
v0.1: 基础请求执行 + 状态码检查        (30 分钟)
v0.2: 变量支持                          (+15 分钟)
v0.3: 响应断言（jsonpath + contains）  (+20 分钟)
v0.4: HTML 报告导出                     (+15 分钟)
v0.5: 并发执行（-p 4 并行跑用例）      (+20 分钟)
v0.6: 环境切换（--env staging 切换变量）(+15 分钟)
```

总计约 2 小时，完成一个可以在团队内使用的接口测试工具。

### 收工判断：什么时候该停

满足以下**全部**条件时，Vibe Coding 阶段应该停止：

1. **功能跑通了**：核心场景能稳定执行，不再频繁报错
2. **满足当前需求**：工具已经在解决实际问题，不是在做"可能有用"的功能
3. **代码开始变乱**：每次新增功能需要改动多处，结构开始失控
4. **超过 500 行**：单文件超过 500 行，继续堆功能维护成本加速上升

此时继续 Vibe Coding 的边际收益已经为负。应该进入"工程化过渡"。

### 工程化过渡：从 Vibe Coding 产物到正式项目

停止 Vibe Coding 后，不要直接废弃重写——那会丢掉已积累的领域知识。按以下步骤过渡：

**Step 1：提取规格文档**
基于已有功能反写规格，而不是从头设计：

```markdown
# apitest 规格

## 核心功能
- 从 YAML 文件加载测试用例
- 按顺序或并发执行 HTTP 请求
- 对响应做状态码和内容断言
- 生成终端输出和 HTML 报告

## 关键设计决策
- 变量替换采用 ${var} 语法，运行时替换
- 断言插件化：jsonpath / contains / regex 可扩展

## 当前限制
- 不支持 WebSocket 和 gRPC
- 并发模型简单（thread pool），高并发不是设计目标
```

**Step 2：重构项目结构**
从一个 500 行的单文件拆分为模块：

```
apitest/
├── cli.py              # 命令行入口
├── loader.py           # YAML 加载和变量解析
├── runner.py           # 测试执行引擎
├── assertions.py       # 断言插件
├── reporter.py         # 终端输出 + HTML 报告
├── models.py           # 数据模型（TestCase, TestResult）
└── tests/              # 补充的测试
    ├── test_loader.py
    └── test_runner.py
```

**Step 3：补测试**
优先补：解析逻辑测试、断言逻辑测试、边界条件（空文件、格式错误、超时处理）。

**Step 4：加 CI**
```yaml
# .github/workflows/test.yml
# 每次 PR 自动运行 apitest 自身的测试
# 确保重构不破坏功能
```

**Step 5：写 CLAUDE.md**
让后续开发者（以及 AI）知道这个项目的规范和边界。

经过这 5 步，一个 Vibe Coding 产物就完成了"转正"，成为一个可以交给团队维护的正式项目。

## 6.6 风险清单

在使用任何 Vibe Coding 产物前，逐项检查：

- [ ] **SQL 注入风险**：所有数据库查询是否使用了参数化查询或 ORM？拼接 SQL 必须清除
- [ ] **XSS 风险**：输出到 HTML/前端的用户数据是否经过转义？
- [ ] **命令注入风险**：是否有 `os.system()`、`subprocess` 接收用户输入？
- [ ] **权限绕过风险**：是否有未检查权限就返回数据的 API 端点？
- [ ] **敏感信息泄露**：代码中是否硬编码了密钥、token、密码、数据库连接串？
- [ ] **敏感信息泄露（日志）**：日志是否打印了 token、密码、用户手机号等敏感字段？
- [ ] **依赖版本安全**：使用的第三方库是否有已知 CVE？`pip audit` / `npm audit` 是否通过？
- [ ] **异常处理完整性**：所有网络调用、文件操作、数据库访问是否有 try-catch？异常是否被静默吞掉？
- [ ] **日志记录完整性**：关键操作（数据修改、外部调用、错误）是否有日志？日志级别是否合理？
- [ ] **输入校验**：所有外部输入（API 参数、文件内容、环境变量）是否做了非空、类型、范围校验？
- [ ] **并发安全**：多线程/多进程场景下是否有竞态条件？共享状态是否正确同步？
- [ ] **资源泄漏**：文件句柄、数据库连接、HTTP 连接是否正确关闭？是否使用了 `with` 或 `try-finally`？
- [ ] **超时设置**：所有网络请求是否设置了合理超时？没有超时的请求可能永久阻塞
- [ ] **重试逻辑安全**：重试是否有上限和退避策略？无限制重试可能导致雪崩
- [ ] **错误信息安全**：面向用户的错误信息是否泄露了内部实现细节（栈追踪、文件路径、SQL 语句）？
- [ ] **数据脱敏**：测试环境中使用的数据是否经过脱敏处理？是否包含真实用户数据？
- [ ] **AI 生成标注**：文件头部是否有"AI 生成"的标识和注意事项？

## 6.7 企业 Vibe Coding 使用政策模板

以下是可供 Tech Lead 直接改编使用的政策模板：

---

# Vibe Coding 使用政策（试行）

**适用范围**：本部门所有使用 AI 辅助编码的项目
**版本**：v1.0
**生效日期**：____
**制定人**：____
**审批人**：____

## 1. 原则声明

公司鼓励使用 AI 工具提升开发效率，但必须确保代码质量、安全性和合规性不受影响。Vibe Coding（由 AI 主导生成代码、开发者主要通过对话和反馈驱动）是一种探索性工具，**不作为正式交付方法**。

## 2. 允许使用 Vibe Coding 的场景

以下场景可以使用 Vibe Coding，无需提前审批：

- 原型验证和概念证明
- Demo 和演示
- 团队内部使用的辅助小工具（不访问生产数据）
- 一次性数据处理脚本

## 3. 禁止使用 Vibe Coding 的场景

以下场景**严格禁止**使用 Vibe Coding 方式产出代码：

- 直接操作生产数据库的代码
- 认证、授权、加密相关模块
- 涉及资金计算的业务逻辑
- 需要符合监管合规要求的系统
- 任何面向外部客户的核心功能

## 4. 使用规范

### 4.1 标注要求

所有通过 Vibe Coding 方式生成的代码文件，必须在文件头部加入以下注释：

```
# ATTENTION: 本文件由 AI 辅助生成，未经完整测试覆盖。
# 创建时间: YYYY-MM-DD
# 用途: [简要说明]
# 已知局限: [列出]
```

### 4.2 安全扫描

AI 生成的代码在进入以下环境前，必须通过自动化安全扫描：

- 测试环境：建议扫描（推荐不阻断）
- 预发布环境：强制扫描，阻断高危结果
- 生产环境：强制扫描，阻断中危及以上结果

### 4.3 人工审查

通过安全扫描后，还必须有人工 Code Review。审查重点：

- 输入校验是否完整
- 异常处理是否合理
- 是否有硬编码的敏感信息
- 业务逻辑是否正确

## 5. 转正流程

当 Vibe Coding 产物需要在团队中长期使用时，必须完成以下"转正"步骤：

1. 补写功能规格文档
2. 拆分代码为合理模块结构
3. 补充单元测试（覆盖率不低于 60%）
4. 完成安全扫描和人工审查
5. 接入 CI/CD 流程

## 6. 违规处理

- 首次违规：口头提醒，限期整改
- 二次违规：书面警告，纳入绩效评估
- 造成安全事故的：按公司信息安全管理制度处理

## 7. 政策更新

本政策每季度审视一次，根据 AI 工具能力变化和团队实践经验调整。

---

## 6.8 核心结论

Vibe Coding 是一个**探索工具**，不是一个**交付方法**。

它最大的价值在于压缩"从想法到可运行代码"的时间，让开发者在几十分钟内验证一个想法的可行性。但它的价值曲线在代码超过 500 行、使用人数超过 2 人、运行环境超过本地机器之后，急剧下降。

企业对待 Vibe Coding 的正确姿势不是禁止，也不是放任。而是：

1. **画好边界**：明确哪里可以用，哪里不能碰
2. **建好护栏**：安全扫描、人工审查、回滚方案一个不能少
3. **铺好转正路**：好的 Vibe Coding 产物要有路径成长为正式项目
4. **保持诚实**：标注来源，不假装 AI 生成的代码有人工代码的质量

一句话：**用 Vibe Coding 做原型，用 Spec-Driven 做产品。前者负责"这个方向对吗"，后者负责"这个产品稳吗"。混淆两者的边界，就是技术债务失控的开始。**
