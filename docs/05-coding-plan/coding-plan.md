# NoteLM学习平台编码计划

## 1. 项目概述

### 1.1 项目目标
构建一个以笔记为虚拟概念载体的学习平台，支持多源内容整合、AI驱动问答、聊天机制、学习笔记、知识卡片、思维导图和测验生成功能的后端服务。

### 1.2 核心功能
- 知识空间管理
- 多源内容管理（网页、文本、PDF）
- AI驱动的问答、聊天、知识卡片、测验和思维导图生成
- 学习笔记管理
- 学习资料存储

### 1.3 技术架构
- 后端框架：Spring Boot 3.x + Java 17
- 数据库：PostgreSQL
- AI服务：阿里通义千问API
- API设计：RESTful API

## 2. 代码生成范围概览

### 2.1 需要生成的代码文件分类统计
- **实体/模型类**：约 9-10 个实体类
- **Repository接口**：约 9-10 个数据访问接口
- **Service接口及实现**：约 6-8 个服务层
- **Controller类**：约 8-10 个控制器
- **DTO/VO类**：约 15-20 个数据传输对象
- **配置类**：约 6-9 个配置类（包含AI配置）
- **工具类**：约 3-5 个实用工具
- **异常处理**：约 3-5 个异常处理类
- **安全相关**：约 3-4 个安全类
- **AI集成相关**：约 5-7 个AI服务类（包含阿里通义千问客户端）

## 3. 目录结构计划

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── notelm/
│   │   │           ├── NoteLMApplication.java
│   │   │           ├── config/                 # 配置类
│   │   │           │   ├── DatabaseConfig.java
│   │   │           │   ├── AIConfig.java       # 阿里通义千问配置
│   │   │           │   ├── SecurityConfig.java
│   │   │           │   ├── SwaggerConfig.java
│   │   │           │   └── RedisConfig.java
│   │   │           ├── controller/             # 控制器层
│   │   │           │   ├── LearningSpaceController.java
│   │   │           │   ├── KnowledgeSourceController.java
│   │   │           │   ├── AIQuestionController.java
│   │   │           │   ├── ChatController.java
│   │   │           │   ├── NoteController.java
│   │   │           │   ├── KnowledgeCardController.java
│   │   │           │   ├── QuizController.java
│   │   │           │   ├── MindMapController.java
│   │   │           │   └── StudyMaterialController.java
│   │   │           ├── service/                # 服务层接口与实现
│   │   │           │   ├── LearningSpaceService.java
│   │   │           │   ├── KnowledgeSourceService.java
│   │   │           │   ├── AIQuestionService.java
│   │   │           │   ├── ChatService.java
│   │   │           │   ├── NoteService.java
│   │   │           │   ├── KnowledgeCardService.java
│   │   │           │   ├── QuizService.java
│   │   │           │   ├── MindMapService.java
│   │   │           │   ├── StudyMaterialService.java
│   │   │           │   ├── impl/               # 服务实现类
│   │   │           │   │   ├── LearningSpaceServiceImpl.java
│   │   │           │   │   ├── KnowledgeSourceServiceImpl.java
│   │   │           │   │   ├── AIQuestionServiceImpl.java
│   │   │           │   │   ├── ChatServiceImpl.java
│   │   │           │   │   ├── NoteServiceImpl.java
│   │   │           │   │   ├── KnowledgeCardServiceImpl.java
│   │   │           │   │   ├── QuizServiceImpl.java
│   │   │           │   │   ├── MindMapServiceImpl.java
│   │   │           │   │   └── StudyMaterialServiceImpl.java
│   │   │           ├── repository/             # 数据访问层
│   │   │           │   ├── LearningSpaceRepository.java
│   │   │           │   ├── KnowledgeSourceRepository.java
│   │   │           │   ├── AIQuestionRepository.java
│   │   │           │   ├── ChatSessionRepository.java
│   │   │           │   ├── ChatMessageRepository.java
│   │   │           │   ├── NoteRepository.java
│   │   │           │   ├── KnowledgeCardRepository.java
│   │   │           │   ├── QuizRepository.java
│   │   │           │   ├── QuizQuestionRepository.java
│   │   │           │   ├── MindMapRepository.java
│   │   │           │   └── StudyMaterialRepository.java
│   │   │           ├── model/                  # 实体类
│   │   │           │   ├── LearningSpace.java
│   │   │           │   ├── KnowledgeSource.java
│   │   │           │   ├── AIQuestion.java
│   │   │           │   ├── ChatSession.java
│   │   │           │   ├── ChatMessage.java
│   │   │           │   ├── Note.java
│   │   │           │   ├── KnowledgeCard.java
│   │   │           │   ├── Quiz.java
│   │   │           │   ├── QuizQuestion.java
│   │   │           │   ├── MindMap.java
│   │   │           │   ├── StudyMaterial.java
│   │   │           │   └── User.java
│   │   │           ├── dto/                    # 数据传输对象
│   │   │           │   ├── request/            # 请求DTO
│   │   │           │   │   ├── CreateLearningSpaceRequest.java
│   │   │           │   │   ├── UpdateLearningSpaceRequest.java
│   │   │           │   │   ├── AddWebpageSourceRequest.java
│   │   │           │   │   ├── AddTextSourceRequest.java
│   │   │           │   │   ├── CreateAIQuestionRequest.java
│   │   │           │   │   ├── CreateChatSessionRequest.java
│   │   │           │   │   ├── SendMessageRequest.java
│   │   │           │   │   ├── CreateNoteRequest.java
│   │   │           │   │   ├── UpdateNoteRequest.java
│   │   │           │   │   ├── GenerateKnowledgeCardRequest.java
│   │   │           │   │   ├── GenerateQuizRequest.java
│   │   │           │   │   ├── GenerateMindMapRequest.java
│   │   │           │   │   └── CreateStudyMaterialRequest.java
│   │   │           │   └── response/           # 响应DTO
│   │   │           │       ├── LearningSpaceResponse.java
│   │   │           │       ├── KnowledgeSourceResponse.java
│   │   │           │       ├── AIQuestionResponse.java
│   │   │           │       ├── ChatSessionResponse.java
│   │   │           │       ├── ChatMessageResponse.java
│   │   │           │       ├── NoteResponse.java
│   │   │           │       ├── KnowledgeCardResponse.java
│   │   │           │       ├── QuizResponse.java
│   │   │           │       ├── QuizQuestionResponse.java
│   │   │           │       ├── MindMapResponse.java
│   │   │           │       ├── StudyMaterialResponse.java
│   │   │           │       └── ApiResponse.java
│   │   │           ├── exception/              # 异常处理
│   │   │           │   ├── GlobalExceptionHandler.java
│   │   │           │   ├── BusinessException.java
│   │   │           │   ├── ResourceNotFoundException.java
│   │   │           │   └── ValidationException.java
│   │   │           ├── security/               # 安全相关
│   │   │           │   ├── JwtAuthenticationFilter.java
│   │   │           │   ├── JwtTokenProvider.java
│   │   │           │   └── SecurityConstants.java
│   │   │           ├── util/                   # 工具类
│   │   │           │   ├── MarkdownUtil.java
│   │   │           │   ├── ValidationUtil.java
│   │   │           │   ├── FileUtil.java
│   │   │           │   └── PromptBuilderUtil.java
│   │   │           ├── ai/                     # AI服务集成（阿里通义千问）
│   │   │           │   ├── AIProperties.java      # 阿里通义千问配置属性
│   │   │           │   ├── AIClient.java          # 阿里通义千问API客户端
│   │   │           │   ├── ChatServiceAdapter.java # 聊天服务适配器
│   │   │           │   ├── ContentExtractor.java  # 内容提取器
│   │   │           │   ├── PromptBuilder.java     # 提示词构造器
│   │   │           │   └── QwenService.java       # 阿里通义千问服务实现
│   │   │           └── event/                  # 事件处理
│   │   │               └── ChatEvent.java
│   │   ├── resources/
│   │   │   ├── application.yml               # 主配置文件（包含阿里通义千问配置）
│   │   │   ├── data/
│   │   │   │   └── sql/                      # SQL脚本
│   │   │   │       ├── schema.sql            # 数据库表结构
│   │   │   │       └── init-data.sql         # 初始化数据
│   │   │   ├── static/                       # 静态资源
│   │   │   └── templates/                    # 模板文件
│   │   └── test/                             # 测试代码
│   │       ├── java/
│   │       └── resources/
│   └── pom.xml                               # 项目依赖
```

## 4. 生成进度规划

### 4.1 第一阶段：基础架构和模型层 (第1-3天)
- 项目初始化和依赖配置
- 实体类定义
- Repository接口定义
- 基础配置类定义
- 项目启动类

### 4.2 第二阶段：AI服务集成 (第4-6天)
- 阿里通义千问配置类
- AI客户端实现
- 提示词构建工具
- AI服务适配器

### 4.3 第三阶段：服务层实现 (第7-12天)
- 服务接口定义
- 服务实现类
- 业务逻辑实现

### 4.4 第四阶段：控制器层 (第13-16天)
- 控制器类实现
- API接口映射
- 请求响应处理

### 4.5 第五阶段：高级功能和优化 (第17-20天)
- 安全配置
- 异常处理
- 性能优化
- 测试用例补全

## 5. 代码文件规划

### 5.1 Model/Entity 层 (10个文件)
- **LearningSpace.java** (src/main/java/com/notelm/model/LearningSpace.java)
- **KnowledgeSource.java** (src/main/java/com/notelm/model/KnowledgeSource.java)
- **AIQuestion.java** (src/main/java/com/notelm/model/AIQuestion.java)
- **ChatSession.java** (src/main/java/com/notelm/model/ChatSession.java)
- **ChatMessage.java** (src/main/java/com/notelm/model/ChatMessage.java)
- **Note.java** (src/main/java/com/notelm/model/Note.java)
- **KnowledgeCard.java** (src/main/java/com/notelm/model/KnowledgeCard.java)
- **Quiz.java** (src/main/java/com/notelm/model/Quiz.java)
- **QuizQuestion.java** (src/main/java/com/notelm/model/QuizQuestion.java)
- **MindMap.java** (src/main/java/com/notelm/model/MindMap.java)

### 5.2 Repository 层 (10个文件)
- **LearningSpaceRepository.java** (src/main/java/com/notelm/repository/LearningSpaceRepository.java)
- **KnowledgeSourceRepository.java** (src/main/java/com/notelm/repository/KnowledgeSourceRepository.java)
- **AIQuestionRepository.java** (src/main/java/com/notelm/repository/AIQuestionRepository.java)
- **ChatSessionRepository.java** (src/main/java/com/notelm/repository/ChatSessionRepository.java)
- **ChatMessageRepository.java** (src/main/java/com/notelm/repository/ChatMessageRepository.java)
- **NoteRepository.java** (src/main/java/com/notelm/repository/NoteRepository.java)
- **KnowledgeCardRepository.java** (src/main/java/com/notelm/repository/KnowledgeCardRepository.java)
- **QuizRepository.java** (src/main/java/com/notelm/repository/QuizRepository.java)
- **QuizQuestionRepository.java** (src/main/java/com/notelm/repository/QuizQuestionRepository.java)
- **MindMapRepository.java** (src/main/java/com/notelm/repository/MindMapRepository.java)

### 5.3 Service 层 (9个接口 + 9个实现)
- **LearningSpaceService.java** (src/main/java/com/notelm/service/LearningSpaceService.java)
- **LearningSpaceServiceImpl.java** (src/main/java/com/notelm/service/impl/LearningSpaceServiceImpl.java)
- **KnowledgeSourceService.java** (src/main/java/com/notelm/service/KnowledgeSourceService.java)
- **KnowledgeSourceServiceImpl.java** (src/main/java/com/notelm/service/impl/KnowledgeSourceServiceImpl.java)
- **AIQuestionService.java** (src/main/java/com/notelm/service/AIQuestionService.java)
- **AIQuestionServiceImpl.java** (src/main/java/com/notelm/service/impl/AIQuestionServiceImpl.java)
- **ChatService.java** (src/main/java/com/notelm/service/ChatService.java)
- **ChatServiceImpl.java** (src/main/java/com/notelm/service/impl/ChatServiceImpl.java)
- **NoteService.java** (src/main/java/com/notelm/service/NoteService.java)
- **NoteServiceImpl.java** (src/main/java/com/notelm/service/impl/NoteServiceImpl.java)
- **KnowledgeCardService.java** (src/main/java/com/notelm/service/KnowledgeCardService.java)
- **KnowledgeCardServiceImpl.java** (src/main/java/com/notelm/service/impl/KnowledgeCardServiceImpl.java)
- **QuizService.java** (src/main/java/com/notelm/service/QuizService.java)
- **QuizServiceImpl.java** (src/main/java/com/notelm/service/impl/QuizServiceImpl.java)
- **MindMapService.java** (src/main/java/com/notelm/service/MindMapService.java)
- **MindMapServiceImpl.java** (src/main/java/com/notelm/service/impl/MindMapServiceImpl.java)

### 5.4 AI集成层 (6个文件) - 重点更新
- **AIProperties.java** (src/main/java/com/notelm/ai/AIProperties.java)
  - 配置类，使用@ConfigurationProperties绑定application.yml中的阿里通义千问配置
  - 包含apiUrl, modelName, apiKey, timeout等属性

- **AIClient.java** (src/main/java/com/notelm/ai/AIClient.java)
  - 阿里通义千问API客户端
  - 使用RestTemplate或WebClient实现API调用
  - 实现认证和错误处理

- **QwenService.java** (src/main/java/com/notelm/ai/QwenService.java)
  - 阿里通义千问服务实现类
  - 实现问答、聊天、内容生成等功能

- **ChatServiceAdapter.java** (src/main/java/com/notelm/ai/ChatServiceAdapter.java)
  - 聊天服务适配器
  - 适配不同AI提供商的接口

- **PromptBuilder.java** (src/main/java/com/notelm/ai/PromptBuilder.java)
  - 提示词构造器
  - 根据不同场景构建合适的提示词

- **ContentExtractor.java** (src/main/java/com/notelm/ai/ContentExtractor.java)
  - 内容提取器
  - 从AI响应中提取有用信息

### 5.5 Controller 层 (8个文件)
- **LearningSpaceController.java** (src/main/java/com/notelm/controller/LearningSpaceController.java)
- **KnowledgeSourceController.java** (src/main/java/com/notelm/controller/KnowledgeSourceController.java)
- **AIQuestionController.java** (src/main/java/com/notelm/controller/AIQuestionController.java)
- **ChatController.java** (src/main/java/com/notelm/controller/ChatController.java)
- **NoteController.java** (src/main/java/com/notelm/controller/NoteController.java)
- **KnowledgeCardController.java** (src/main/java/com/notelm/controller/KnowledgeCardController.java)
- **QuizController.java** (src/main/java/com/notelm/controller/QuizController.java)
- **MindMapController.java** (src/main/java/com/notelm/controller/MindMapController.java)

### 5.6 Config 层 (包含AI配置)
- **AIConfig.java** (src/main/java/com/notelm/config/AIConfig.java)
  - 阿里通义千问相关配置
  - 配置RestTemplate或其他HTTP客户端

### 5.7 DTO 层 (约30个文件)
- **请求DTO** (src/main/java/com/notelm/dto/request/)
- **响应DTO** (src/main/java/com/notelm/dto/response/)

## 6. 类依赖关系图

### 6.1 主要依赖关系
```
Controller层 (对外API接口)
    ↓
Service层 (业务逻辑处理)
    ↓
AI服务层 (阿里通义千问API调用)
    ↓
Repository层 (数据访问)
    ↓
Model层 (实体定义)
```

### 6.2 AI服务模块依赖关系
- **QwenService** → **AIClient** (调用阿里API)
- **AIClient** → **AIProperties** (获取配置信息)
- **ChatService** → **QwenService** (聊天功能使用AI服务)
- **AIQuestionService** → **QwenService** (问答功能使用AI服务)
- **KnowledgeCardService** → **QwenService** (卡片生成使用AI服务)
- **QuizService** → **QwenService** (测验生成使用AI服务)
- **MindMapService** → **QwenService** (导图生成使用AI服务)

## 7. 代码拆分策略

### 7.1 按业务功能拆分
- **学习空间模块**: 学习空间管理相关代码
- **知识来源模块**: 网页、文本、PDF处理相关代码
- **AI模块**: 阿里通义千问集成相关代码
- **聊天模块**: 聊天功能相关代码
- **笔记模块**: 学习笔记相关代码
- **知识卡片模块**: 卡片生成管理相关代码
- **测验模块**: 测验生成管理相关代码
- **思维导图模块**: 导图生成管理相关代码

### 7.2 按技术层拆分
- **数据层**: Model + Repository
- **业务层**: Service + ServiceImpl
- **AI层**: AI相关服务（阿里通义千问）
- **接口层**: Controller
- **传输层**: DTO
- **配置层**: Config
- **安全层**: Security
- **工具层**: Util

## 8. 包结构设计

### 8.1 标准包结构
- `com.notelm.config`: 配置类
- `com.notelm.controller`: 控制器类
- `com.notelm.service`: 服务接口
- `com.notelm.service.impl`: 服务实现类
- `com.notelm.repository`: 数据访问接口
- `com.notelm.model`: 实体类
- `com.notelm.dto`: 数据传输对象
- `com.notelm.dto.request`: 请求DTO
- `com.notelm.dto.response`: 响应DTO
- `com.notelm.exception`: 异常处理
- `com.notelm.security`: 安全相关
- `com.notelm.util`: 工具类
- `com.notelm.ai`: AI服务集成（阿里通义千问）
- `com.notelm.event`: 事件处理

## 9. 接口依赖分析

### 9.1 服务层依赖
- LearningSpaceService ← KnowledgeSourceService (知识来源需要关联学习空间)
- KnowledgeSourceService ← AIQuestionService, ChatService (知识来源用于AI服务)
- AIQuestionService, ChatService, KnowledgeCardService, QuizService, MindMapService ← QwenService (AI功能使用阿里通义千问服务)
- ChatService ← KnowledgeSourceService (聊天需要知识来源)
- KnowledgeCardService ← KnowledgeSourceService (生成卡片需要知识来源)
- QuizService ← KnowledgeSourceService (生成测验需要知识来源)
- MindMapService ← KnowledgeSourceService (生成导图需要知识来源)

### 9.2 控制器层依赖
- LearningSpaceController → LearningSpaceService
- KnowledgeSourceController → KnowledgeSourceService
- AIQuestionController → AIQuestionService
- ChatController → ChatService
- NoteController → NoteService
- KnowledgeCardController → KnowledgeCardService
- QuizController → QuizService
- MindMapController → MindMapService

## 10. 业务逻辑概要

### 10.1 学习空间管理
- 创建、更新、删除、查询学习空间
- 验证学习空间名称唯一性
- 统计关联内容数量

### 10.2 知识来源管理
- 支持网页链接、文本内容、PDF文件的添加
- 网页内容提取与解析
- PDF内容解析
- 内容存储与管理

### 10.3 AI问答与聊天（阿里通义千问）
- 调用阿里通义千问API生成回答
- 基于知识来源构建提示词
- 保持对话上下文
- 引用信息来源

### 10.4 知识卡片生成
- 从知识来源提取知识点
- 通过阿里通义千问生成问答对
- 管理复习计划

### 10.5 测验生成功能
- 基于知识来源生成选择题
- 通过阿里通义千问生成题目
- 管理题目难度
- 评估测验结果

### 10.6 思维导图生成
- 分析知识结构
- 通过阿里通义千问提取概念层级
- 构建知识关系

### 10.7 学习笔记管理
- Markdown格式支持
- 笔记标签管理
- 学习空间关联

## 11. 阿里通义千问配置管理

### 11.1 配置文件 (application.yml)
```yaml
# AI服务配置 - 统一管理阿里通义千问配置
ai:
  provider: "aliyun"  # 服务提供商
  qwen:
    api-url: https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation
    model-name: "qwen-max"  # 或 qwen-plus, qwen-turbo
    api-key: ${ALIBABA_API_KEY:your-api-key-here}  # 支持环境变量和默认值
    timeout: 60s  # 请求超时时间
    max-tokens: 2000  # 最大返回token数
    temperature: 0.7  # 创造性参数
```

### 11.2 配置类实现
- **AIProperties.java**: 配置类，使用@ConfigurationProperties注解读取配置文件
- **AIClient.java**: 封装对阿里通义千问API的调用
- **可扩展性**: 设计支持未来切换到其他大模型提供商（如OpenAI、百度文心一言等）

### 11.3 配置管理优势
- 统一管理：所有AI配置信息集中在一个配置文件中
- 灵活性：通过配置文件轻松切换不同AI模型
- 安全性：API Key通过环境变量管理
- 可维护性：配置变更无需修改代码

## 12. 生成顺序建议

### 12.1 基础优先级
1. Model实体类 (基础数据结构)
2. Repository接口 (数据访问接口)
3. 配置类 (数据库、安全等配置)
4. AI配置类 (阿里通义千问配置)

### 12.2 AI服务优先级
1. AIProperties (配置属性类)
2. AIClient (API客户端)
3. QwenService (阿里通义千问服务)
4. PromptBuilder (提示词构建器)

### 12.3 业务优先级
1. LearningSpaceService (核心基础)
2. KnowledgeSourceService (依赖学习空间)
3. AI相关的服务 (依赖知识来源和阿里通义千问)
4. 其他业务服务

### 12.4 接口优先级
1. LearningSpaceController (基础功能)
2. KnowledgeSourceController (核心功能)
3. AI问答控制器 (核心功能)
4. 其他功能控制器

## 13. 潜在问题预警

### 13.1 AI服务配置问题
- 确保阿里通义千问API密钥安全存储
- 验证API端点和模型名称正确性
- 实现API调用限流和错误处理

### 13.2 文件位置问题
- 确保所有代码文件按包结构正确放置
- AI相关类正确放置在ai包下

### 13.3 依赖关系问题
- 确保AI服务依赖关系正确
- 避免循环依赖

### 13.4 配置管理问题
- 确保配置属性正确绑定
- 实现配置验证机制

## 14. 完成后的验证标准

### 14.1 代码结构验证
- 所有文件按目录结构正确放置
- 包结构符合设计
- AI相关类正确组织

### 14.2 配置管理验证
- 阿里通义千问配置正确加载
- API密钥安全处理
- 配置项正确绑定

### 14.3 功能验证
- 所有AI功能正确调用阿里通义千问服务
- 配置变更可动态生效
- 支持未来切换其他AI模型

### 14.4 安全验证
- API密钥安全存储和使用
- 防止API密钥泄露
- 适当的错误处理