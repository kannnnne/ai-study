# Glossary: AI 时代程序员术语表

> 面向 Java 后端开发者的 AI 术语参考手册。
> 每个术语用后端开发的类比解释，让你秒懂，不再被新名词劝退。

---

## 目录

1. [Agent（智能体/代理）](#agent智能体代理)
2. [Agentic Coding（智能体编程）](#agentic-coding智能体编程)
3. [Attention（注意力机制）](#attention注意力机制)
4. [Chain of Thought（思维链）](#chain-of-thought思维链)
5. [CLAUDE.md / AGENTS.md（AI 上下文文件）](#claudemd--agentsmdai-上下文文件)
6. [Codex / Claude Code（AI 编程工具）](#codex--claude-codeai-编程工具)
7. [Context Engineering（上下文工程）](#context-engineering上下文工程)
8. [Context Window（上下文窗口）](#context-window上下文窗口)
9. [Cursor / Windsurf（AI IDE）](#cursor--windsurfai-ide)
10. [Embedding（嵌入/向量化）](#embedding嵌入向量化)
11. [Fine-tuning（微调）](#fine-tuning微调)
12. [Function Calling（函数调用）](#function-calling函数调用)
13. [Guardrails（护栏/安全边界）](#guardrails护栏安全边界)
14. [Hallucination（幻觉）](#hallucination幻觉)
15. [Human-in-the-loop（人机协作/人环中）](#human-in-the-loop人机协作人环中)
16. [Inference（推理）](#inference推理)
17. [KV Cache（键值缓存）](#kv-cache键值缓存)
18. [LLM（大语言模型）](#llm大语言模型)
19. [MCP（模型上下文协议）](#mcp模型上下文协议)
20. [Memory（记忆机制）](#memory记忆机制)
21. [Planning（规划能力）](#planning规划能力)
22. [Prompt Engineering（提示词工程）](#prompt-engineering提示词工程)
23. [RAG（检索增强生成）](#rag检索增强生成)
24. [ReAct（推理与行动模式）](#react推理与行动模式)
25. [Reflection（反思能力）](#reflection反思能力)
26. [Spec Coding（规格编程）](#spec-coding规格编程)
27. [Spec-Driven Development（规格驱动开发）](#spec-driven-development规格驱动开发)
28. [System Prompt（系统提示词）](#system-prompt系统提示词)
29. [Temperature（温度参数）](#temperature温度参数)
30. [Token（令牌/词元）](#token令牌词元)
31. [Tool Calling（工具调用）](#tool-calling工具调用)
32. [Top-P（核采样）](#top-p核采样)
33. [Transformer（Transformer 架构）](#transformertransformer-架构)
34. [Vector Database（向量数据库）](#vector-database向量数据库)
35. [Vibe Coding（氛围编程）](#vibe-coding氛围编程)
36. [Workflow（工作流）](#workflow工作流)
37. [Zero-shot / Few-shot（零样本/少样本学习）](#zero-shot--few-shot零样本少样本学习)
38. [AI Governance（AI 治理）](#ai-governanceai-治理)

---

## 正文

### Agent（智能体/代理）

**定义**：能自主感知环境、做出决策、执行动作以完成目标的 AI 系统。

**程序员视角**：Agent 就像一个带状态机的后台服务。它有一个主循环（思考 → 行动 → 观察 → 思考），类似 `while (!goalReached) { plan(); execute(); observe(); }`。Agent 可以调用工具（Tool Calling），就像你的 Service 层调用各种 DAO 和外部 API 一样。不同之处在于：传统服务的执行路径是确定性的（if-else + 循环），Agent 的执行路径由 LLM 根据上下文动态决定。

**为什么重要**：2025 年以后，AI 编程工具从"补全代码"进化到"自主完成任务"。Agent 是这一切的基础。理解 Agent 的工作原理，你才能理解为什么 Claude Code 可以自己读文件、写代码、跑测试、修 bug —— 而不是仅仅给你一段代码建议。

**相关术语**：Agentic Coding, Tool Calling, Planning, ReAct

---

### Agentic Coding（智能体编程）

**定义**：使用 AI Agent 自主完成编程任务的开发模式。Agent 不只生成代码，还会读项目文件、理解上下文、执行命令、验证结果，形成闭环。

**程序员视角**：把 Agentic Coding 想象成你雇了一个永远不会累的初级程序员。你描述需求（就像写 JIRA ticket），它自己读代码、写代码、跑单测、修编译错误，最后把 diff 交给你 review。区别是它不会抱怨需求不清楚，也不会偷偷摸鱼。你们的交互从"你一行行写代码"变成"你审核它写的代码"。

**为什么重要**：这是程序员角色的分水岭。Agentic Coding 出现后，编码能力本身的价值在下降，而需求拆解、架构设计、代码审核、质量把控的能力在上升。就像从"自己搬砖"变成"带施工队"，你的核心能力从执行力转向判断力和掌控力。

**相关术语**：Agent, Vibe Coding, Spec-Driven Development, Workflow

---

### Attention（注意力机制）

**定义**：Transformer 架构的核心算法组件，让模型在处理一个词时能"关注"到输入序列中的其他相关词，从而理解上下文关系。

**程序员视角**：想象你有一个巨大的 Map<String, Double>，Key 是输入文本中的每个词，Value 是当前词应该"关注"它的权重。Attention 做的事情就是动态计算这些权重。如果用 Java 表达：对于句子中的每个词，它给所有其他词打分（相似度计算），然后加权求和，这样"银行"在"我去银行存钱"和"我坐在河边的银行长椅上"中的含义就不同了——因为 Attention 给了"存钱"和"河"不同的权重。

**为什么重要**：Attention 是 LLM 能理解长文本的核心原因。没有它，GPT 系列只能处理固定长度的上下文且无法理解远距离依赖。理解 Attention 也帮助你理解为什么 LLM 的推理成本随输入长度呈平方级增长（每个词都要和所有其他词计算注意力分）。

**相关术语**：Transformer, KV Cache, Context Window

---

### Chain of Thought（思维链）

**定义**：一种提示词技术，要求模型在给出最终答案前，先输出逐步推理过程，从而显著提升复杂问题的正确率。

**程序员视角**：类似于让你写代码时不直接 return 结果，而是先写清楚思路注释，再推导出实现。比如计算 `(a + b) * c`，CoT 会让模型输出："先算 a+b=x，再算 x*c=y，所以答案是 y"。这本质上是在推理阶段给模型更多的"草稿纸"空间，让中间状态显式化，避免在隐式推理中引入错误。就像 debug 时打印中间变量能帮你定位问题一样。

**为什么重要**：CoT 是让 LLM 从"模式匹配器"变成"推理机"的关键技术。在企业 IT 场景中，让模型生成复杂 SQL、分析 bug 根因、设计架构方案时，CoT 能显著降低出错概率。最新的推理模型（如 o1、Claude Opus）已经将 CoT 内置到模型内部推理过程中。

**相关术语**：Prompt Engineering, ReAct, Inference

---

### CLAUDE.md / AGENTS.md（AI 上下文文件）

**定义**：放在项目根目录的 Markdown 文件，为 AI 编程工具提供项目级上下文信息：架构约定、代码规范、常用命令、注意事项等。

**程序员视角**：把它理解为 AI 的"项目 onboarding 文档"。新人入职第一天你会给他看什么文档？CLAUDE.md 就是给 AI 看的那个。它告诉 AI：这个项目做什么、用什么技术栈、目录结构怎么组织的、命名规范是什么、测试怎么跑。写得不清楚，AI 就乱写。写得精准，AI 的输出质量直线上升。这本质上就是 Context Engineering 在编码场景中的落地。

**为什么重要**：这是你作为开发者能直接控制 AI 输出质量的最有效手段。一个精心编写的 CLAUDE.md 能让 AI 的代码生成准确率提升 30%-50%。企业级项目中，CLAUDE.md 应该像 `.gitignore` 一样标配。它也是团队 AI 协作规范的载体——你们对 AI 的使用约定、prompt 模板、架构红线都可以写在这里。

**相关术语**：Context Engineering, System Prompt, Prompt Engineering

---

### Codex / Claude Code（AI 编程工具）

**定义**：OpenAI Codex 和 Anthropic Claude Code 是当前最主流的终端级 AI 编程 Agent。它们直接在命令行中运行，能读文件、编辑代码、执行命令、操作 Git。

**程序员视角**：这是你的 AI 副驾驶，不是 IDE 插件，而是一个能在终端里独立工作的 Agent。你可以理解为：把 Postman 换成 curl，把 IDE 换成 vim，Codex/Claude Code 就是命令行里的 AI 程序员。它们通过 System Prompt + CLAUDE.md 理解项目，通过 Tool Calling 读写文件、执行 shell，通过 Agent 循环完成复杂任务。你跟它的交互方式从"生成代码片段"变成了"下命令 + 审核 PR"。

**为什么重要**：这两款工具代表了 AI 编程的当前天花板。它们不是代码补全，而是能端到端完成开发任务。理解它们的能力边界和工作原理，你才能判断哪些任务该交给它、哪些该自己写、如何设计协作流程。在企业环境中，它们正在改变 code review 的方式——越来越多的 diff 由 AI 生成，Reviewer 的角色在变化。

**相关术语**：Agent, Agentic Coding, Tool Calling, Cursor / Windsurf

---

### Context Engineering（上下文工程）

**定义**：系统性地设计和管理喂给 LLM 的上下文信息（System Prompt、历史消息、检索到的文档、工具输出等），以最大化输出质量和任务成功率。

**程序员视角**：可以这样类比：LLM 是无状态的 function，输入（上下文）决定输出。Context Engineering 就是为这个 function 精心准备参数。你的 CLAUDE.md 是静态参数，历史对话是运行时状态，RAG 检索结果是按需加载的数据，System Prompt 是函数签名约定。就像你不会给一个 function 传一堆 null 和无意义的参数一样，你也不该给 LLM 塞满无关上下文。上下文窗口再大，塞多了也会被"噪声淹没"。

**为什么重要**：Prompt Engineering 教你怎么"问"，Context Engineering 教你怎么"准备"。在企业 IT 落地中，Context Engineering 比 Prompt Engineering 更重要——因为企业场景的上下文复杂得多：项目架构、业务规则、数据库 schema、已有代码风格……这些东西需要系统性地管理和注入，而不是靠每次手写 prompt。

**相关术语**：Prompt Engineering, CLAUDE.md, Context Window, RAG

---

### Context Window（上下文窗口）

**定义**：LLM 一次能"看到"的最大文本量，通常以 Token 数衡量。超出窗口的内容会被截断或丢失。

**程序员视角**：Context Window 就是 LLM 的"工作内存"大小，类似 JVM 的堆内存上限。早期模型只有 4K token（约 3000 字），现在的模型能做到 200K（约 15 万字），Claude 甚至支持 500K。但大窗口不等于你能随意灌数据——存在"迷失中间"效应：窗口两端的内容模型记得最清楚，中间部分容易被忽略。类似浏览一个超长 List，你只会记住前几个和后几个元素。

**为什么重要**：在企业 IT 应用中，Context Window 直接决定了你能喂给模型多少上下文。代码库太大？需要 RAG。历史对话太长？需要摘要压缩。理解了 Context Window 的机制和限制，你才能正确设计 AI 应用的架构，而不是盲目依赖"把代码全塞进去"。

**相关术语**：Token, RAG, Context Engineering, Attention

---

### Cursor / Windsurf（AI IDE）

**定义**：内置了深度 AI 能力的现代化代码编辑器。Cursor 基于 VS Code 深度定制，Windsurf 由 Codeium 团队打造。它们能理解整个代码库，支持行内补全、对话式编辑、多文件重构。

**程序员视角**：如果说 Claude Code 是命令行里的 AI 程序员，Cursor/Windsurf 就是带 GUI 的 AI 程序员。它们像增强版 IntelliJ IDEA——不仅能智能补全，还能理解你整个项目的上下文（自动索引代码库做 RAG），在聊天窗口里直接帮你改代码，甚至跨多个文件自动应用修改。你不需要切换到终端，一切在 IDE 里闭环。

**为什么重要**：对于习惯 IDE 的 Java 开发者，Cursor/Windsurf 是 AI 编程的最低门槛入口。它们保留了传统 IDE 的体验，叠加了 AI 能力。但也有限制：受 IDE 架构约束，它们的 Agent 自主能力不如终端级工具。选什么工具取决于你的协作模式——喜欢 chat + 手动 review 改动用 IDE，习惯命令行 + Agent 自主执行用 Claude Code。

**相关术语**：Agent, Codex / Claude Code, Agentic Coding

---

### Embedding（嵌入/向量化）

**定义**：将文本、图片等非结构化数据转换为一组固定长度的浮点数向量，语义相近的内容在向量空间中距离也近。

**程序员视角**：Embedding 就是把一个对象"序列化"成一个 1024 维的 `float[]`，而且这个序列化有个神奇特性：含义相近的文本，序列化后的数组在数学上也很近（余弦相似度高）。就像 Java 里 `equals()` 和 `hashCode()` 的关系——语义上等价的东西，hash 值也应该相近。只不过 `hashCode()` 返回一个 int，Embedding 返回 768 到 3072 个 float。

**为什么重要**：Embedding 是 RAG 和语义搜索的基础。没有它，你只能用关键词（like '%keyword%'）搜文档；有了它，你可以用"那个处理用户登录的模块"找到 `AuthService.java`。在企业知识库、代码搜索、智能客服等场景中，Embedding 让"意会"变成了数学计算。

**相关术语**：Vector Database, RAG, Token

---

### Fine-tuning（微调）

**定义**：在预训练模型的基础上，用特定领域的数据继续训练，使模型在特定任务上表现更好。

**程序员视角**：Fine-tuning 就像基于 Spring Framework 做二次开发——你不需要从零写一个 DI 容器，而是在 Spring 的基础上加自己的 Bean 配置和业务逻辑。预训练模型（base model）就是 Spring Framework，已经学会了语言的基本能力；Fine-tuning 就是你的项目代码，让它掌握你们公司的业务术语、编码规范、特定格式输出。成本远低于从头训练（类比：配置 Spring Boot 项目 vs 自己写 Web 服务器）。

**为什么重要**：通用模型不懂你公司的内部术语（"请帮我查一下 GR-2024-003 的审批流状态"）。Fine-tuning 可以让模型学会这些。对于企业 IT 来说，Fine-tuning 的场景包括：代码生成适配公司框架、日志分析适配内部格式、客服机器人了解产品细节。全量 Fine-tuning 贵，但 LoRA 等轻量方案成本可控。

**相关术语**：LLM, RAG, Embedding

---

### Function Calling（函数调用）

**定义**：LLM 根据用户意图，自动选择并调用预定义的函数/API，用结构化参数完成实际操作。

**程序员视角**：Function Calling 本质上是 LLM 输出 JSON 而不是自然语言。你定义一个函数的签名（名称、参数、描述），模型判断当前对话是否需要调用它，如果需要，就输出一个结构化的 JSON 调用请求。你的程序接到这个请求，执行真正的函数，把结果返回给模型。这就像 Spring 的依赖注入：你定义了一个接口，LLM 在运行时"注入"了对这个接口的调用意图，你的代码负责执行。或者更直接地比作：LLM 是一个能看懂自然语言的 HTTP 客户端，它自动帮你构造请求参数。

**为什么重要**：这是 LLM 从"聊天机器人"变成"能干活的服务"的关键一步。没有 Function Calling，LLM 只能说"建议你查数据库"；有了它，LLM 可以直接调用 `queryDatabase(sql)` 然后基于查询结果回答你。在企业场景中，Function Calling 让 LLM 能对接 CRM、ERP、工单系统，真正进入业务流程。

**相关术语**：Tool Calling, MCP, Agent

---

### Guardrails（护栏/安全边界）

**定义**：部署在 LLM 应用周围的规则和检查机制，防止模型输出不安全、不合规或有害的内容。

**程序员视角**：Guardrails 就是给 LLM 加了一组 `@Valid` 注解和一个全局 `Filter`。输入校验（检查用户 prompt 是否包含注入攻击）、输出校验（检查模型回复是否包含敏感信息）、行为校验（检查 Agent 是否做了不该做的事）。类比 Spring Security 的 Filter Chain：每个 Guardrail 是一个 Filter，输入经过一系列 Filter 检查后才到达 LLM，输出也要经过一系列 Filter 检查后才返回给用户。

**为什么重要**：企业 IT 应用 AI 时，安全合规是第一道坎。银行不能用会泄露客户信息的模型，医院不能用会在诊断中胡说八道的模型。Guardrails 让企业可以在不可控的模型外面包一层可控的安全壳。NVIDIA 的 NeMo Guardrails 是目前主流方案之一。

**相关术语**：AI Governance, Hallucination, Human-in-the-loop

---

### Hallucination（幻觉）

**定义**：LLM 生成的内容看起来合理、自信，但与事实不符或完全编造。这是 LLM 的固有缺陷，而非偶发 bug。

**程序员视角**：Hallucination 就像你的 Code Review 同事，明明没看过代码，但回答得非常自信流畅。或者像一个实习生，你问他"这个接口的 QPS 是多少？"，他没查监控就直接给了个数字，听起来还挺合理。本质上，LLM 是概率模型，它在预测"最可能的下一个词"，而不是在"检索事实"。当训练数据中缺乏相关信息时，它不会说"不知道"，而是基于已有知识"补全"一个听起来合理的答案。

**为什么重要**：在企业场景中，Hallucination 是最危险的 trap。它可能在生成 SQL 时编造不存在的表名，在分析日志时虚构错误原因，在写文档时伪造 API 参数。防御策略：关键事实需要 RAG 检索验证、输出需要 Guardrails 检查、敏感操作需要 Human-in-the-loop 确认。

**相关术语**：RAG, Guardrails, Human-in-the-loop, Inference

---

### Human-in-the-loop（人机协作/人环中）

**定义**：在 AI 系统的自动执行流程中，在关键节点插入人工审核或决策环节，确保最终控制权在人手中。

**程序员视角**：这和 Git 的 Pull Request 流程一模一样。AI Agent 可以自由地在 feature 分支上写代码、跑测试，但合并到 main 必须经过人工 Code Review。更广泛的类比：就像审批流里的"需要上级审批"节点。AI 可以填好报销单、附上发票、计算好金额，但最后点"提交"的是人。在产品设计中问的是：哪些环节必须让人确认？哪些环节 AI 可以自己过？

**为什么重要**：这是企业 IT 采纳 AI 的心理安全阀。完全自主的 Agent 在企业环境里很难落地（谁背锅？），Human-in-the-loop 提供了一种渐进式采纳路径：先用 AI 提效（生成初稿、推荐方案），保留人工决策（审核、批准、发布）。随着信任建立，逐步扩大 AI 的自主范围。

**相关术语**：Guardrails, AI Governance, Workflow, Agent

---

### Inference（推理）

**定义**：模型加载后，接收输入并生成输出的计算过程。与 Training（训练）相对——一个学，一个用。

**程序员视角**：Inference 就是"调用 API"，Training 就是"编译 + 训练"。用 Spring Boot 类比：Training 是把应用打成 jar 包的过程（耗时、耗资源、做一次），Inference 是用户请求打到运行中的服务上的过程（每次请求都执行、要求低延迟）。成本结构也不同：Training 是一次性的大额投入（GPU 跑几周），Inference 是持续的小额支出（按 token 计费，类似云函数的按调用次数计费）。

**为什么重要**：理解 Training vs Inference 的经济账，你才能正确评估 AI 项目的成本。大多数企业不需要 Training，只需要选好模型 + 做好 Inference 优化（缓存、批处理、提示词压缩）。Inference 成本占 AI 项目总成本的 80% 以上，优化 Inference 比折腾 Training 更有 ROI。

**相关术语**：LLM, KV Cache, Token

---

### KV Cache（键值缓存）

**定义**：Transformer 在自回归生成时，缓存已计算的 Key 和 Value 矩阵，避免每一步都重新计算整个序列的 Attention，从而大幅加速 Inference。

**程序员视角**：KV Cache 就是 LLM 推理的"备忘录缓存"。生成第 100 个 token 时，前 99 个 token 的 K/V 矩阵不需要重新算，直接用缓存。类比：你的 API 服务收到一个 pageSize=100 的分页请求，你不会把全表扫 100 遍，而是把结果缓存起来。KV Cache 让 LLM 推理的复杂度从 O(n²) 降到 O(n)，其中 n 是生成 token 数。

**为什么重要**：KV Cache 的大小直接影响 LLM 服务能吞吐多少并发请求。在自建 LLM 服务（如 vLLM）时，KV Cache 的显存管理是核心优化点——就像 JVM 的堆内存调优。另外，你常常听到的"生成到一半报显存不足"，大概率是 KV Cache 爆了。

**相关术语**：Attention, Transformer, Inference, Context Window

---

### LLM（大语言模型）

**定义**：基于海量文本数据训练的、具有数十亿以上参数的神经网络模型，能理解和生成自然语言。

**程序员视角**：LLM 就是一个超大尺寸的"自动补全引擎"——你给它一段文本开头，它预测最可能的下一个词，不断循环直到生成完整回答。如果用 Java 类比，LLM 是一个被极度压缩的"世界知识数据库"：把互联网上的所有文本压缩进几百 GB 的参数文件里，推理时就从这个压缩数据库中"解压"出相关信息来回答。它不是真正的数据库查询，而是概率性回忆，所以有时会"记错"（Hallucination）。

**为什么重要**：LLM 是整个 AI 编程、AI 应用生态的底座。ChatGPT 是 LLM 的应用形态，Claude 是 LLM 的应用形态。对 Java 程序员来说，理解 LLM 不需要懂神经网络数学，但需要理解它的能力边界（能做什么、不能做什么、容易在什么场景出错），这样才能正确地把 LLM 集成到企业系统中。

**相关术语**：Transformer, Inference, Token, Hallucination

---

### MCP（模型上下文协议）

**定义**：Anthropic 发布的开放协议，定义了 AI 模型如何与外部工具、数据源、服务进行标准化交互，类似 AI 世界的"USB 协议"。

**程序员视角**：MCP 之于 AI Agent，就像 JDBC 之于 Java 应用。JDBC 定义了一套标准接口让 Java 应用连接任何数据库（MySQL、PostgreSQL、Oracle），MCP 定义了一套标准协议让 AI 模型连接任何外部资源（文件系统、数据库、API、浏览器）。以前每个 AI 工具都要自己写适配器连接各种外部服务（类似每个应用自己写数据库驱动），MCP 之后，一次实现、处处复用——你写一个 MCP Server，所有支持 MCP 的 AI 工具都能用。

**为什么重要**：MCP 正在成为 AI Agent 生态的基础设施标准。在企业 IT 中，这意味着你可以用统一的方式让 AI 访问内部的工单系统、CMDB、审批流——不需要为每个 AI 工具单独开发集成。这也是 AI 编程工具能直接操作你的 JIRA、读取你的 Confluence 的技术基础。

**相关术语**：Tool Calling, Function Calling, Agent

---

### Memory（记忆机制）

**定义**：AI Agent 用来保存和检索历史信息的机制，使其能在多次交互中保持对上下文、用户偏好、任务进度的记忆。

**程序员视角**：Memory 就是 Agent 的"数据库层"。分为几种：短期记忆（当前对话，类似 session 内的局部变量）、长期记忆（跨会话持久化，类似写入 MySQL）、工作记忆（当前任务所需的上下文，类似线程栈）。Agent 通过 Tool Calling 写入 Memory（"记录用户偏好：只生成 Java 17+ 语法"），通过检索读取 Memory（"回忆之前的讨论，我们用了什么架构？"）。实现上可以是纯文本存储、向量化存储或结构化存储。

**为什么重要**：没有 Memory 的 Agent 像金鱼——每次对话都从头开始。在企业协作场景中（Agent 参与持续迭代的项目开发），Memory 让 Agent 能记住"上次为什么选了 Redis 而不是 Kafka""用户是 Java 后端团队，不要推荐 Python 方案"。这也是 Claude Code 的 MEMORY.md 和 checkpoint 机制在解决的问题。

**相关术语**：Context Engineering, RAG, Vector Database, Workflow

---

### Planning（规划能力）

**定义**：AI Agent 在执行复杂任务前，先分析目标、拆解子任务、制定执行计划，然后再逐步执行的能力。

**程序员视角**：Planning 就是 Agent 的"需求拆解 + 技术方案设计"阶段。你让 Agent "给用户模块加一个手机号登录功能"，它不会直接开始写代码，而是先规划：需要改哪些文件、数据库加什么字段、接口参数怎么定义、单测覆盖哪些 case。这类似你在写代码前先画流程图、写技术方案。Agent 通常通过 ReAct 或 Chain of Thought 实现 Planning：先输出计划，再逐步执行。

**为什么重要**：Planning 是衡量 Agent 能力的关键指标。简单 Agent 只能处理单步任务（"重命名这个变量"），有 Planning 能力的 Agent 能处理端到端需求（"给系统加一个双因子认证"）。对于 Java 后端开发者，你需要评估 Agent 的 Planning 输出质量——它的拆解是否合理、是否考虑了事务边界、是否遗漏了异常处理。

**相关术语**：Agent, ReAct, Chain of Thought, Reflection

---

### Prompt Engineering（提示词工程）

**定义**：设计和优化输入给 LLM 的指令，以引导模型生成期望输出的实践。

**程序员视角**：Prompt Engineering 就是为 LLM 这个"函数"编写最佳参数。你定义接口契约（System Prompt）、传入参数（User Prompt）、期望返回值（Response）。好的 prompt 包含：角色设定（"你是一个资深 Java 架构师"）、任务描述（"评审以下代码的安全问题"）、输出格式（"以表格形式列出"）、正反示例（few-shot）。核心原则是：明确、结构化、给上下文。未来可能不再需要"prompt 工程师"这个专门岗位，但每个开发者都需要掌握基本的 prompt 技巧。

**为什么重要**：同样的模型，prompt 的好坏能让输出质量有 50% 以上的差距。在企业场景中，好的 prompt 模板是可以复用的团队资产——就像团队积累的 Code Review checklist 和代码模板一样。但要注意：Prompt Engineering 正在从"手写精调"转向"上下文工程 + 自动优化"。

**相关术语**：Context Engineering, System Prompt, Chain of Thought, Few-shot

---

### RAG（检索增强生成）

**定义**：在 LLM 生成回答前，先从外部知识库中检索相关信息，将检索结果与用户问题一起喂给模型，提升回答的事实准确性。

**程序员视角**：RAG 就是给 LLM 加了一个"搜索引擎"。用户提问 → 把问题转成 Embedding → 去 Vector Database 中找相似内容 → 把检索结果拼到 prompt 里 → LLM 基于"问题 + 检索结果"生成回答。这和你在写代码时遇到不确定的 API，先 Google 一下再继续写是同一个逻辑。Architecture 上，RAG 包含三个核心组件：Embedding 服务（把文本转向量）、Vector Database（存向量 + 原文）、Retriever（检索 + 排序）。

**为什么重要**：RAG 是企业 IT 使用 LLM 最核心的模式。它解决了三个关键问题：事实准确（模型基于你给的文档回答，不是靠记忆）、信息时效（知识库更新 = 模型"知识"更新）、数据安全（企业内部文档不需要拿去 Fine-tuning，存在自己的向量库里）。典型应用：企业知识库问答、代码库搜索、合规文档审核。

**相关术语**：Embedding, Vector Database, Context Window, Hallucination

---

### ReAct（推理与行动模式）

**定义**：Reasoning + Acting 的缩写，一种 Agent 范式，让模型交替进行推理（思考下一步做什么）和行动（执行工具调用），在思考和行动的循环中完成任务。

**程序员视角**：ReAct 就是 Agent 版的"分析 → 执行 → 观察 → 调整"循环。类比调试一个复杂 bug：你不是一次性想好所有步骤再执行，而是"先打日志看看 → 哦，这里参数是 null → 那往前追溯调用链 → 啊，是上游没传 → 修一下"。ReAct 让 Agent 也这样工作：思考（Reasoning）→ 执行工具（Acting）→ 看到结果（Observation）→ 再思考 → 再执行……

**为什么重要**：纯推理（只思考不行动）解决不了需要外部信息的问题；纯行动（只执行不思考）会盲目乱撞。ReAct 将两者结合，是当前 Agent 实现的主流范式。理解 ReAct 帮你判断 Agent 为什么"卡住了"——可能是在错误的循环里打转，需要给出更高层级的引导。

**相关术语**：Agent, Planning, Tool Calling, Chain of Thought

---

### Reflection（反思能力）

**定义**：AI Agent 对自己的输出或决策进行自我评估、发现错误、修正改进的能力。

**程序员视角**：Reflection 就是 Agent 的"Code Review"环节。Agent 写完代码后，它自己先审查一遍："这段代码有没有 NPE 风险？""异常处理是否完善？""是否遵循了项目的命名规范？"这不依赖外部反馈，是 Agent 内部的自我迭代。就像你写完代码后自己先过一遍，发现有坑就先改了，不用等别人指出。Claude Code 的验证流程（写完代码 → 跑测试 → 发现失败 → 自己修）就是 Reflection 的体现。

**为什么重要**：Reflection 是 Agent 输出质量的关键保障。没有 Reflection 的 Agent "写什么就是什么"，有 Reflection 的 Agent 能自我修正，大幅减少人工 Review 的负担。但 Reflection 也有上限——Agent 无法发现它根本不知道是问题的 bug（需要领域知识才能判断的错误）。

**相关术语**：Agent, Planning, Chain of Thought

---

### Spec Coding（规格编程）

**定义**：一种编码模式，用形式化的规格（规范文档）描述软件行为，然后由 AI 根据规格生成实现代码。

**程序员视角**：Spec Coding 就是"你写接口文档 + AI 写实现"。你用自然语言或结构化格式定义清楚：输入什么、输出什么、边界条件是什么、异常情况怎么处理，然后 AI 负责填充具体的代码。这跟你写好 interface 让实习生写实现类很相似——interface 是规格，实习生是 AI。区别在于 AI 不需要睡觉，也能同时写测试。

**为什么重要**：Spec Coding 把开发的重心从"写代码"转移到"写规格"。在企业中，这意味着团队可以更关注"设计清楚"而非"实现准确"。但挑战也在这里：写清楚规格本身就需要很强的抽象和表达能力，而且规格和实现之间的缺口（规格没覆盖到的边缘情况）仍是 Bug 的来源。

**相关术语**：Spec-Driven Development, Agentic Coding, Prompt Engineering

---

### Spec-Driven Development（规格驱动开发）

**定义**：一种以规格文档为核心的开发方法论，先用结构化规格锁定需求和行为，再通过 AI Agent 或人工实现编码，规格是贯穿需求-设计-实现-测试的唯一真相来源。

**程序员视角**：SDD 就是"先写好完整的技术规格文档，再让 AI 去实现"。以 Java 项目的视角：你先写好所有的 interface、DTO、异常定义、业务规则伪代码，形成了完整的"合约层"。然后 AI Agent 拿着这份合约生成实现类、单元测试、集成测试。如果实现跑不过规格定义的测试，就继续迭代。这和传统 TDD 的区别在于：TDD 是"先写测试用例"（微观规格），SDD 是"先写系统设计"（宏观规格），然后测试用例可以由 AI 根据规格自动生成。

**为什么重要**：SDD 被认为是 AI 时代最适配的开发方法论。因为 AI 擅长"根据明确指令实现"，而不擅长"从模糊需求推理"。当你给 AI 一份清晰的规格，它能高效地生成可用代码；当你给 AI 一句话需求，它大概率产出一堆需要大改的东西。对 Java 后端团队的启示：花更多时间写好规格文档，AI 会让你的实现效率翻倍。

**相关术语**：Spec Coding, Agentic Coding, Vibe Coding, System Prompt

---

### System Prompt（系统提示词）

**定义**：在对话开始时设置给 LLM 的基础指令，定义模型的角色、行为边界、输出格式等全局规则，通常用户不可见。

**程序员视角**：System Prompt 就是 LLM 的"框架配置 + Bean 定义"。它告诉模型：你是谁（role = "Java 后端专家"）、你的能力边界（capabilities = ["写代码", "review", "解释"]）、你的行为约束（constraints = ["只用 Java 17+ 语法", "不用 lombok"]）。类比 Spring Boot 的 `application.yml`——它不是业务逻辑，但它决定了整个应用的运行方式。CLAUDE.md 本质上也是 System Prompt 在项目级的一个特化形式。

**为什么重要**：System Prompt 是 Context Engineering 的核心组件。在 AI 编程工具的语境下，System Prompt 定义了 Agent 的"人设"和行为约束。设计好的 System Prompt 能让 Agent 在项目框架内自由发挥但不越界，类似给团队定的编码规范。

**相关术语**：Context Engineering, Prompt Engineering, CLAUDE.md

---

### Temperature（温度参数）

**定义**：控制 LLM 输出随机性的参数，范围 0-2。值越低输出越确定、越保守（适合代码生成）；值越高输出越多样、越有创造性（适合头脑风暴）。

**程序员视角**：Temperature 就像一个"创造力滑块"。设为 0 时，模型每次对相同输入输出几乎完全一致（类似确定性哈希函数）；设为 1 时，模型会在多个"合理"的选项中随机选（类似加了随机盐）；设为 2 时，模型开始放飞自我（类似 random.nextInt() 替换了你的逻辑分支）。写代码时用 Temperature=0，因为代码需要确定性；想创意时用 0.7-0.9，因为你需要多样性。

**为什么重要**：很多开发者忽略了这个参数，然后抱怨"模型每次给的代码不一样"。在企业 IT 应用中，代码生成、SQL 生成、数据提取等确定性场景必须设为低 Temperature；文案撰写、方案探索等创造性场景可适当提高。不同 AI 工具默认值不同，需要根据场景显式设置。

**相关术语**：Top-P, Inference, LLM

---

### Token（令牌/词元）

**定义**：LLM 处理文本的最小单位。一个 Token 约等于英文的一个子词（"playing" 可能被切为 "play" + "ing"），中文一个 Token 约等于 1-2 个汉字。

**程序员视角**：Token 就是 LLM 的"基本计费单位 + 上下文长度单位"，类似云服务的"API 调用次数"或"带宽字节数"。你调用 API 时，Input Token 和 Output Token 分开计费，Output 通常更贵（约 3-5 倍）。用 Java 类比：Token 类似于 JVM 字节码指令——不是源代码，也不是机器码，是介于之间的中间表示。LLM 不直接处理原始文本，而是先 Tokenize（分词），对 Token 序列建模，最后 Detokenize 还原成文本。

**为什么重要**：Token 是理解 LLM 成本和限制的基础。提示词太长 → Token 超 Context Window → 被截断。Output 太啰嗦 → Token 消耗大 → 费用高 + 响应慢。写 prompt 时要有 Token 意识：精简=input 省钱，要求简洁 output=output 省钱。1000 个 Token 中文约等于 1500-2000 个汉字。

**相关术语**：Context Window, Inference, LLM

---

### Tool Calling（工具调用）

**定义**：LLM 在对话过程中调用外部工具/API 的能力。与 Function Calling 实质相同，Tool Calling 更侧重 Agent 场景中的工具使用。

**程序员视角**：Tool Calling 给 LLM 装上了"手"——它不再只是动嘴（输出文本），而是可以动手（执行操作）。你定义一系列工具：readFile(path)、writeFile(path, content)、runCommand(cmd)、searchCode(pattern)，Agent 根据任务需要自动选择和调用这些工具。这就像微服务架构中的服务编排：Agent 是编排层，Tools 是被编排的原子服务。Claude Code 中的 Bash、Read、Write、Edit 等操作本质上都是 Tool Calling。

**为什么重要**：Tool Calling 让 LLM 从"顾问"升级为"执行者"。在企业场景中，这意味着 LLM 不再只是"建议你查数据库"而是"直接帮你查了并分析完返回结果"。理解 Tool Calling 帮你设计更好的 Agent 工具集：工具粒度、工具描述、错误处理、幂等性——这些熟悉的后端概念在 AI Agent 场景中同样重要。

**相关术语**：Function Calling, MCP, Agent, ReAct

---

### Top-P（核采样）

**定义**：另一种控制 LLM 输出随机性的参数。模型从概率最高的 Token 中按累积概率阈值 P 采样，P=0.1 表示只从累积概率达到 10% 的最小 Token 集合中选。

**程序员视角**：如果说 Temperature 是"全局调低所有选项的概率差异"，Top-P 就是"直接砍掉概率太低的选项，只保留高概率池子"。类比：Temperature=0 是"永远选第一个"，Top-P=0.1 是"从排名前 K 个候选里挑一个"，K 不固定，取决于概率分布。实际使用中，一般只调 Temperature 和 Top-P 其中一个就够了。写代码时两者都设低值。

**为什么重要**：Temperature 和 Top-P 是控制 LLM 行为的两个核心参数。理解它们的区别有助于在确定性和创造性之间找到精确平衡。企业应用中，生成代码、SQL、结构化数据建议 Temperature=0且Top-P=0.3 以下；生成文档、邮件可以略高。

**相关术语**：Temperature, Inference, LLM

---

### Transformer（Transformer 架构）

**定义**：2017 年 Google 提出的神经网络架构，完全基于 Attention 机制，是 GPT、Claude、Gemini 等所有现代 LLM 的底层架构。

**程序员视角**：Transformer 之于 LLM，就像 Spring Framework 之于 Java Web 应用——它是底层架构范式，几乎所有现代 LLM 都基于它。虽然你不需要读 Transformer 论文来用好 LLM，但理解几个关键概念有用：Attention（关注相关上下文）、Encoder-Decoder（输入编码 → 输出解码，但 GPT 只用 Decoder）、Positional Encoding（给没有顺序概念的 Attention 加上位置信息）。现在最关键的认知是：Transformer 的自回归生成决定了 LLM 只能"从左到右"生成文本，不能回溯修改已输出的内容（所以会有 Hallucination）。

**为什么重要**：理解 Transformer 帮你理解 LLM 的根本能力与局限——为什么它能理解长文本、为什么它有时胡编乱造、为什么改一个 bug 后可能引入新 bug（自回归输出无法"整体重写"）。你不用实现 Transformer，但理解了它，就不会被人忽悠"AI 马上有意识了"。

**相关术语**：Attention, LLM, KV Cache, Inference

---

### Vector Database（向量数据库）

**定义**：专门存储和检索向量数据的数据库，核心能力是高效进行近似最近邻搜索，找到语义最相似的内容。

**程序员视角**：Vector Database 就是"做 `ORDER BY cosine_similarity(query_vector, doc_vector) DESC LIMIT 10` 的数据库"，只不过用了 ANN（近似最近邻）算法让这个查询在十亿级向量中也能毫秒级返回。它不等于传统数据库的替代品——通常和关系型数据库配合使用：MySQL 存结构化数据（用户信息、订单），Vector DB 存语义索引（文档向量、图片向量）。主流选项：Milvus（Java 友好）、Pinecone（SaaS）、pgvector（PostgreSQL 插件，支持混合查询）。

**为什么重要**：Vector Database 是 RAG 基础设施的核心。没有它，企业知识库、代码搜索、智能问答都无从谈起。对 Java 后端来说，最常见的架构是：MySQL + pgvector 或 Elasticsearch + 向量插件，避免引入新的基础设施复杂度。

**相关术语**：Embedding, RAG, Context Engineering

---

### Vibe Coding（氛围编程）

**定义**：一种非正式的 AI 编程方式，开发者用自然语言向 AI 描述需求，快速生成原型或功能，不过分关注代码细节，强调速度和体验。

**程序员视角**：Vibe Coding 就是"先让 AI 写出能跑的东西，再决定要不要认真重构"。类似你在 IDE 里先用 `psvm` 生成一个 main 方法，然后开始写实现——只不过 Vibe Coding 中整个实现都是 AI 写的。你描述："给我一个 Spring Boot controller，接收文件上传，存 MinIO，返回 URL"，AI 直接给你完整可运行的代码。你不需要检查每一行，只要能跑通就行。这也是为什么有人批评它产出"屎山"——快速原型常常缺少异常处理、并发安全等生产级考量。

**为什么重要**：Vibe Coding 是 AI 编程的"快速探索模式"，适合原型验证、内部工具、个人项目。在正式的企业项目中，Vibe Coding 可以用于快速验证方案可行性，但最终代码需要经过 Spec-Driven 的规范化。理解 Vibe Coding 的定位，才能避免"AI 写的代码直接上生产"的灾难。

**相关术语**：Agentic Coding, Spec-Driven Development, Spec Coding

---

### Workflow（工作流）

**定义**：将 AI Agent 的一次性行为和人工审核节点编排成可重复、可追溯的业务流程。

**程序员视角**：Workflow 就是 AI Agent 的"流程引擎"。用 Java 生态类比就是 Activiti/Camunda/Flowable——你定义流程节点（Agent 生成 → 人工审核 → Agent 修改 → 自动测试 → 自动部署），定义节点间的流转条件（测试通过 → 继续；失败 → 打回修改），然后 Workflow 引擎按定义执行。单次 Agent 交互是"一个操作"，Workflow 把多次操作串联成"一个流程"。

**为什么重要**：企业 IT 中，AI 的价值不在于"一次性的神奇回答"，而在于"可重复、可追踪、可审计的批量处理"。Workflow 是 AI 从玩具进入生产的关键。比如：代码审查 Workflow（AI 初评 → 人工确认 → 自动归档）、客服工单 Workflow（AI 分类 → AI 初答 → 人工审核 → 自动回复）。

**相关术语**：Agent, Human-in-the-loop, AI Governance

---

### Zero-shot / Few-shot（零样本/少样本学习）

**定义**：Zero-shot：不提供任何示例，直接让模型完成任务。Few-shot：在 prompt 中提供少量（通常 2-5 个）示例，帮助模型理解任务格式。

**程序员视角**：Zero-shot 就是"给模型一个函数签名，让它直接写实现"——你告诉它做什么，不给例子。Few-shot 就是"给模型一个函数签名 + 几个单测用例"——通过例子明确期望的输入输出映射。类比给新同事派活：Zero-shot 是"写一个把 CSV 转 JSON 的脚本"；Few-shot 是"写一个把 CSV 转 JSON 的脚本，像这样的输入输出对应……"。Few-shot 通常效果更好，但消耗更多 token。

**为什么重要**：在实际使用 AI 编程工具时，Few-shot 是最实用的 prompt 技巧。当你需要 AI 生成特定格式的代码时（公司的 Logger 写法、异常处理模式、测试结构），给 1-2 个现有代码文件中的示例，比用自然语言描述 100 遍格式要求都有效。这也是为什么 CLAUDE.md 中放代码示例很有效——它们在充当隐式的 Few-shot。

**相关术语**：Prompt Engineering, System Prompt, Context Engineering

---

### AI Governance（AI 治理）

**定义**：企业层面管理和规范 AI 使用的框架，包括策略制定、风险管理、合规审查、效果评估，确保 AI 使用符合企业价值观和法律法规。

**程序员视角**：AI Governance 就是给 AI 使用建立"公司级 coding standard + 审批流程"。哪些场景可以用 AI？用什么模型？（内部自建还是调用外部 API？）代码能不能发到外部模型？（数据安全）AI 生成的代码要不要标注？（IP 溯源）谁来审批 AI 应用的部署上线？这和你们公司已有的信息安全管理制度是同一种思路，只是适配到了 AI 场景。

**为什么重要**：企业 IT 引入 AI 的最大阻力不是技术，而是治理。没有明确的 AI Governance，团队要么不敢用 AI（保守），要么乱用 AI（风险）。Java 后端开发者通常离企业合规最近（处理敏感数据、数据库访问、API 权限），是推动和参与 AI Governance 的关键角色。你需要理解：AI Governance 不是限制创新，而是让创新合法合规。

**相关术语**：Guardrails, Human-in-the-loop, Workflow

---

## 快速索引

| 遇到什么问题 | 看哪几个术语 |
|---|---|
| 想理解 LLM 怎么运作 | Token, Transformer, Attention, Inference |
| 想用好 AI 编程工具 | Agent, Agentic Coding, Context Engineering, CLAUDE.md, Tool Calling |
| 想提升 AI 输出质量 | Prompt Engineering, Context Engineering, Chain of Thought, Few-shot, System Prompt |
| 想把控 AI 输出行为 | Temperature, Top-P, Guardrails |
| 想在企业落地 AI | RAG, Vector Database, Embedding, Human-in-the-loop, AI Governance, Workflow |
| 想选用开发方法论 | Spec-Driven Development, Spec Coding, Vibe Coding |
| 想了解推理优化 | KV Cache, Inference, Token |
| 想理解 AI 出错原因 | Hallucination, Context Window, Reflection |
| 想选 AI IDE | Cursor / Windsurf, Codex / Claude Code |

---

> **最后更新**：2026-07-01
> **维护说明**：AI 领域术语迭代极快，本文档将随教程系列持续更新。如有术语缺失或解释不当，欢迎提交反馈。
