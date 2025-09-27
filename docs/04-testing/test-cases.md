# NoteLM学习平台测试用例设计

## 1. 概述

### 1.1 测试目标
验证NoteLM学习平台后端服务的功能正确性、性能、安全性和稳定性，确保系统能够满足用户需求和技术方案的要求。

### 1.2 测试范围
- 学习空间管理功能
- 知识来源管理功能
- AI问答功能（使用阿里通义千问）
- 聊天机制功能（使用阿里通义千问）
- 学习笔记功能
- 知识卡片生成功能（使用阿里通义千问）
- 测验生成功能（使用阿里通义千问）
- 思维导图生成功能（使用阿里通义千问）
- 学习资料管理功能
- 用户认证和权限控制
- 数据库操作
- API接口测试
- 阿里通义千问集成测试

### 1.3 测试策略
- 单元测试：覆盖核心业务逻辑和算法
- 集成测试：验证服务间通信和数据流
- 端到端测试：验证完整业务流程
- 性能测试：验证系统在负载下的表现
- 安全测试：验证安全防护措施
- AI服务测试：验证阿里通义千问集成

## 2. 学习空间管理测试用例

### 2.1 创建学习空间

#### TC_LS_001: 成功创建学习空间
- **测试目的**: 验证用户可以成功创建学习空间
- **前置条件**: 用户已登录
- **测试步骤**:
  1. 调用POST /api/v1/learning-spaces接口
  2. 提供有效的学习空间信息（名称、描述、状态）
- **输入数据**: {"name": "Java学习空间", "description": "Java相关知识学习", "status": "ACTIVE"}
- **预期结果**: 返回200状态码，包含创建的学习空间信息和ID
- **验证点**:
  - 响应状态码为200
  - 返回数据包含正确的学习空间信息
  - 数据库中存在对应的学习空间记录

#### TC_LS_002: 创建名称为空的学习空间
- **测试目的**: 验证系统对无效输入的处理
- **前置条件**: 用户已登录
- **测试步骤**:
  1. 调用POST /api/v1/learning-spaces接口
  2. 提供名称为空的学习空间信息
- **输入数据**: {"name": "", "description": "Java相关知识学习", "status": "ACTIVE"}
- **预期结果**: 返回400状态码，包含错误信息
- **验证点**:
  - 响应状态码为400
  - 返回错误信息提示名称不能为空

#### TC_LS_003: 创建名称过长的学习空间
- **测试目的**: 验证系统对超长输入的处理
- **前置条件**: 用户已登录
- **测试步骤**:
  1. 调用POST /api/v1/learning-spaces接口
  2. 提供名称超过100字符的学习空间信息
- **输入数据**: {"name": "一个特别长的名称...[超过100字符]", "description": "Java相关知识学习", "status": "ACTIVE"}
- **预期结果**: 返回400状态码，包含错误信息
- **验证点**:
  - 响应状态码为400
  - 返回错误信息提示名称长度限制

### 2.2 获取学习空间列表

#### TC_LS_004: 获取学习空间列表
- **测试目的**: 验证用户可以获取自己的学习空间列表
- **前置条件**: 用户已登录，存在学习空间数据
- **测试步骤**:
  1. 调用GET /api/v1/learning-spaces接口
  2. 传递分页参数
- **输入数据**: pageSize=10, page=1, sortBy=updateTime
- **预期结果**: 返回200状态码，包含学习空间列表和分页信息
- **验证点**:
  - 响应状态码为200
  - 返回数据包含正确的学习空间列表
  - 包含分页信息(total, page, pageSize)
  - 数据按指定排序规则排序

#### TC_LS_005: 分页获取学习空间列表
- **测试目的**: 验证分页功能的正确性
- **前置条件**: 用户已登录，存在多个学习空间
- **测试步骤**:
  1. 调用GET /api/v1/learning-spaces接口，设置pageSize=5
  2. 调用第二次接口，获取第二页数据
- **输入 data**: pageSize=5, page=1和2
- **预期 results**: 返回对应页的数据
- **验证 points**:
  - 两页数据不重复
  - 每页数据数量符合pageSize设置

### 2.3 更新学习空间

#### TC_LS_006: 更新学习空间信息
- **测试 purpose**: 验证用户可以更新学习空间信息
- **前置 conditions**: 用户已登录，存在学习空间数据
- **测试 steps**:
  1. 调用PUT /api/v1/learning-spaces/{id}接口
- **输入 data**: {"name": "更新后的学习空间名称", "description": "更新后的描述"}
- **预期 results**: 返回200状态码，包含更新后的信息
- **验证 points**:
  - 响应状态码为200
  - 返回数据包含更新后的信息
  - 数据库中对应记录已更新

### 2.4 删除学习空间

#### TC_LS_007: 删除学习空间
- **测试 purpose**: 验证用户可以删除学习空间
- **前置 conditions**: 用户已登录，存在学习空间数据
- **测试 steps**:
  1. 调用DELETE /api/v1/learning-spaces/{id}接口
- **输入 data**: 学习空间ID
- **预期 results**: 返回200状态码
- **验证 points**:
  - 响应状态码为200
  - 数据库中对应学习空间状态更新为已删除或记录被移除

## 3. 知识来源管理测试用例

### 3.1 上传网页链接

#### TC_KS_001: 成功添加网页链接作为知识来源
- **Test purpose**: 验证用户可以成功添加网页链接
- **Pre-conditions**: 用户已登录，存在学习空间
- **Test steps**:
  1. 调用POST /api/v1/learning-spaces/{spaceId}/sources/webpage接口
  2. 提供有效的URL
- **Input data**: {"url": "https://example.com/article", "title": "文章标题"}
- **Expected results**: 返回200状态码，包含创建的知识来源信息
- **Verification points**:
  - 响应状态码为200
  - 返回数据包含正确的知识来源信息
  - 系统成功提取网页内容并存储

#### TC_KS_002: 添加无效URL
- **Test purpose**: 验证系统对无效URL的处理
- **Pre-conditions**: 用户已登录，存在学习空间
- **Test steps**:
  1. 调用POST /api/v1/learning-spaces/{spaceId}/sources/webpage接口
  2. 提供无效的URL
- **Input data**: {"url": "invalid-url", "title": "文章标题"}
- **Expected results**: 返回400状态码，包含错误信息
- **Verification points**:
  - 响应状态码为400
  - 返回错误信息提示URL格式无效

### 3.2 上传文本内容

#### TC_KS_003: 成功上传文本内容
- **Test purpose**: 验证用户可以成功上传文本内容
- **Pre-conditions**: 用户已登录，存在学习空间
- **Test steps**:
  1. 调用POST /api/v1/learning-spaces/{spaceId}/sources/text接口
  2. 提供有效的文本内容
- **Input data**: {"title": "文本标题", "content": "这里是文本内容..."}
- **Expected results**: 返回200状态码，包含创建的知识来源信息
- **Verification points**:
  - 响应状态码为200
  - 返回数据包含正确的知识来源信息
  - 文本内容正确存储到数据库

### 3.3 上传PDF文件

#### TC_KS_004: 成功上传PDF文件
- **Test purpose**: 验证用户可以成功上传PDF文件
- **Pre-conditions**: 用户已登录，存在学习空间
- **Test steps**:
  1. 调用POST /api/v1/learning-spaces/{spaceId}/sources/pdf接口
  2. 以multipart/form-data格式上传有效的PDF文件
- **Input data**: file字段上传PDF，title字段提供标题
- **Expected results**: 返回200状态码，包含创建的知识来源信息
- **Verification points**:
  - 响应状态码为200
  - 返回数据包含正确的知识来源信息
  - PDF内容正确解析并存储

#### TC_KS_005: 上传非PDF文件
- **Test purpose**: 验证系统对非PDF文件的处理
- **Pre-conditions**: 用户已登录，存在学习空间
- **Test steps**:
  1. 调用POST /api/v1/learning-spaces/{spaceId}/sources/pdf接口
  2. 上传非PDF文件
- **Input data**: file字段上传非PDF文件
- **Expected results**: 返回400状态码，包含错误信息
- **Verification points**:
  - 响应状态码为400
  - 返回错误信息提示文件格式不正确

### 3.4 获取知识来源列表

#### TC_KS_006: 获取知识来源列表
- **Test purpose**: 验证用户可以获取指定学习空间的知识来源列表
- **Pre-conditions**: 用户已登录，存在知识来源数据
- **Test steps**:
  1. 调用GET /api/v1/learning-spaces/{spaceId}/sources接口
  2. 传递分页参数
- **Input data**: spaceId, type=WEBPAGE, search="关键词", pageSize=10, page=1
- **Expected results**: 返回200状态码，包含知识来源列表和分页信息
- **Verification points**:
  - 响应状态码为200
  - 返回数据包含筛选后的知识来源列表
  - 支持按类型和搜索关键词过滤

## 4. AI问答功能测试用例（阿里通义千问）

### 4.1 提出问题

#### TC_AI_001: 基于知识来源向阿里通义千问提问
- **Test purpose**: 验证用户可以基于知识来源向阿里通义千问提问并获得答案
- **Pre-conditions**: 用户已登录，存在学习空间和知识来源
- **Test steps**:
  1. 调用POST /api/v1/learning-spaces/{spaceId}/ai-questions接口
  2. 提供问题内容
- **Input data**: {"question": "Java中的多态性是什么？"}
- **Expected results**: 返回200状态码，包含阿里通义千问生成的答案
- **Verification points**:
  - 响应状态码为200
  - 返回数据包含问题ID、问题内容、AI答案
  - 答案基于学习空间内的知识来源生成
  - 记录使用的知识来源ID
  - 正确调用阿里通义千问API

#### TC_AI_002: 提出与知识来源无关的问题
- **Test purpose**: 验证阿里通义千问对超出知识范围问题的处理
- **Pre-conditions**: 用户已登录，存在学习空间和知识来源
- **Test steps**:
  1. 调用POST /api/v1/learning-spaces/{spaceId}/ai-questions接口
  2. 提供与现有知识来源无关的问题
- **Input data**: {"question": "与学习内容完全无关的问题"}
- **Expected results**: 返回200状态码，阿里通义千问给出适当回应
- **Verification points**:
  - 响应状态码为200
  - AI回答表明无法从现有来源找到相关信息
  - 正确调用阿里通义千问API

#### TC_AI_003: 阿里通义千问API配置测试
- **Test purpose**: 验证阿里通义千问API配置正确加载
- **Pre-conditions**: 系统配置了阿里通义千问相关参数
- **Test steps**:
  1. 检查配置文件中的阿里通义千问配置
  2. 尝试调用AI功能
- **Input data**: 有效的提问
- **Expected results**: 配置正确加载，API调用成功
- **Verification points**:
  - 配置参数（API URL, 模型名称, API Key）正确加载
  - API调用成功，返回合理响应
  - 支持通过配置文件切换不同模型

## 5. 聊天机制测试用例（阿里通义千问）

### 5.1 创建聊天会话

#### TC_CHAT_001: 创建聊天会话
- **Test purpose**: 验证用户可以在学习空间中创建聊天会话
- **Pre-conditions**: 用户已登录，存在学习空间
- **Test steps**:
  1. 调用POST /api/v1/learning-spaces/{spaceId}/chat-sessions接口
  2. 提供会话标题
- **Input data**: {"title": "关于Java并发的讨论"}
- **Expected results**: 返回200状态码，包含创建的聊天会话信息
- **Verification points**:
  - 响应状态码为200
  - 返回数据包含聊天会话ID、标题、状态和时间戳

### 5.2 发送聊天消息

#### TC_CHAT_002: 发送聊天消息并接收阿里通义千问回复
- **Test purpose**: 验证用户可以与阿里通义千问进行聊天，AI基于知识来源回复
- **Pre-conditions**: 用户已登录，存在学习空间和聊天会话
- **Test steps**:
  1. 调用POST /api/v1/chat-sessions/{sessionId}/messages接口
  2. 发送聊天内容
- **Input data**: {"content": "请解释一下Java中的线程安全概念"}
- **Expected results**: 返回200状态码，包含用户消息和阿里通义千问回复
- **Verification points**:
  - 响应状态码为200
  - 返回数据包含用户消息和AI回复
  - AI回复基于学习空间内的知识来源
  - AI回复中包含引用的知识来源信息
  - 正确调用阿里通义千问API

#### TC_CHAT_003: 连续聊天保持上下文（仅限当前会话）
- **Test purpose**: 验证阿里通义千问在当前会话内能保持上下文
- **Pre-conditions**: 用户已登录，存在学习空间和聊天会话，已有聊天记录
- **Test steps**:
  1. 连续调用POST /api/v1/chat-sessions/{sessionId}/messages接口
  2. 发送多个相关联的问题
- **Input data**: 连续发送相关问题（如先问概念，再问具体实现）
- **Expected results**: 阿里通义千问能理解当前会话内的上下文并给出连贯回答
- **Verification points**:
  - AI回复显示对当前会话内之前对话的理解
  - 回复内容与当前会话上下文相关
  - 不涉及跨会话的内容检索
  - 正确维护对话历史

#### TC_CHAT_004: 获取聊天消息历史
- **Test purpose**: 验证用户可以获取聊天会话的历史消息
- **Pre-conditions**: 用户已登录，存在聊天会话和消息记录
- **Test steps**:
  1. 调用GET /api/v1/chat-sessions/{sessionId}/messages接口
  2. 传递分页参数
- **Input data**: sessionId, pageSize=20, page=1
- **Expected results**: 返回200状态码，包含聊天消息列表
- **Verification points**:
  - 响应状态码为200
  - 返回数据包含消息列表和分页信息
  - 消息按时间顺序正确排列
  - 仅返回当前会话内的消息（不涉及聊天内容检索功能）

## 6. 学习笔记功能测试用例

### 6.1 创建学习笔记

#### TC_NOTE_001: 创建Markdown格式学习笔记
- **Test purpose**: 验证用户可以创建Markdown格式的学习笔记
- **Pre-conditions**: 用户已登录，存在学习空间
- **Test steps**:
  1. 调用POST /api/v1/learning-notes接口
  2. 提供Markdown格式的笔记内容
- **Input data**: {
    "learningSpaceId": 1,
    "title": "Java并发学习总结",
    "content": "# Java并发\n\n## 线程安全\n\n线程安全是指...",
    "contentFormat": "MARKDOWN",
    "tags": ["Java", "并发", "总结"]
  }
- **Expected results**: 返回200状态码，包含创建的笔记信息
- **Verification points**:
  - 响应状态码为200
  - 返回数据包含正确的笔记信息
  - Markdown内容正确存储和格式标识

#### TC_NOTE_002: 创建带XSS内容的笔记（安全测试）
- **Test purpose**: 验证系统对恶意Markdown内容的防护
- **Pre-conditions**: 用户已登录，存在学习空间
- **Test steps**:
  1. 调用POST /api/v1/learning-notes接口
  2. 提供包含XSS攻击代码的笔记内容
- **Input data**: {
    "learningSpaceId": 1,
    "title": "测试XSS防护",
    "content": "# 标题\n\n<script>alert('XSS')</script>",
    "contentFormat": "MARKDOWN",
    "tags": ["安全", "测试"]
  }
- **Expected results**: 内容经过安全处理，XSS代码被移除或转义
- **Verification points**:
  - 响应状态码为200
  - 存储的内容中XSS代码被移除或转义
  - Markdown渲染时不会执行恶意脚本

### 6.2 更新学习笔记

#### TC_NOTE_003: 更新学习笔记内容
- **Test purpose**: 验证用户可以更新已有的学习笔记
- **Pre-conditions**: 用户已登录，存在学习笔记
- **Test steps**:
  1. 调用PUT /api/v1/learning-notes/{noteId}接口
  2. 提供更新后的笔记内容
- **Input data**: {
    "title": "更新后的标题",
    "content": "# 更新后的Markdown内容\n\n...",
    "tags": ["Java", "并发", "总结", "更新"]
}
- **Expected results**: 返回200状态码，包含更新后的笔记信息
- **Verification points**:
  - 响应状态码为200
  - 返回数据包含更新后的信息
  - 数据库中对应记录已更新

### 6.3 获取学习笔记列表

#### TC_NOTE_004: 获取学习笔记列表
- **Test purpose**: 验证用户可以获取学习笔记列表
- **Pre-conditions**: 用户已登录，存在学习笔记数据
- **Test steps**:
  1. 调用GET /api/v1/learning-notes接口
  2. 传递筛选和分页参数
- **Input data**: spaceId=1, pageSize=10, page=1
- **Expected results**: 返回200状态码，包含笔记列表和分页信息
- **Verification points**:
  - 响应状态码为200
  - 返回数据包含正确的笔记列表
  - 包含分页信息

## 7. 知识卡片功能测试用例（阿里通义千问生成）

### 7.1 生成知识卡片

#### TC_CARD_001: 基于知识来源通过阿里通义千问生成知识卡片
- **Test purpose**: 验证系统可以通过阿里通义千问基于知识来源自动生成知识卡片
- **Pre-conditions**: 用户已登录，存在学习空间和知识来源
- **Test steps**:
  1. 调用POST /api/v1/learning-spaces/{spaceId}/knowledge-cards/generate接口
  2. 提供生成参数
- **Input data**: {"cardCount": 10, "difficultyLevel": 3}
- **Expected results**: 返回200状态码，生成指定数量和难度的知识卡片
- **Verification points**:
  - 响应状态码为200
  - 生成的知识卡片基于学习空间内的知识来源
  - 通过阿里通义千问生成问答对
  - 卡片数量和难度符合请求参数

### 7.2 获取知识卡片列表

#### TC_CARD_002: 获取知识卡片列表
- **Test purpose**: 验证用户可以获取知识卡片列表
- **Pre-conditions**: 用户已登录，存在知识卡片数据
- **Test steps**:
  1. 调用GET /api/v1/learning-spaces/{spaceId}/knowledge-cards接口
  2. 传递筛选参数
- **Input data**: spaceId, reviewed=false, tag="Java", pageSize=10, page=1
- **Expected results**: 返回200状态码，包含筛选后的知识卡片列表
- **Verification points**:
  - 响应状态码为200
  - 返回数据符合筛选条件

### 7.3 标记卡片为已复习

#### TC_CARD_003: 标记知识卡片为已复习
- **Test purpose**: 验证用户可以标记知识卡片为已复习
- **Pre-conditions**: 用户已登录，存在知识卡片
- **Test steps**:
  1. 调用POST /api/v1/knowledge-cards/{cardId}/mark-reviewed接口
- **Input data**: cardId
- **Expected results**: 返回200状态码，卡片状态更新
- **Verification points**:
  - 响应状态码为200
  - 数据库中卡片的复习状态和时间戳已更新

## 8. 测验功能测试用例（阿里通义千问生成）

### 8.1 生成测验

#### TC_QUIZ_001: 基于知识来源通过阿里通义千问生成测验
- **Test purpose**: 验证系统可以通过阿里通义千问基于知识来源自动生成包含选择题的测验
- **Pre-conditions**: 用户已登录，存在学习空间和知识来源
- **Test steps**:
  1. 调用POST /api/v1/learning-spaces/{spaceId}/quizzes/generate接口
  2. 提供生成参数
- **Input data**: {"title": "Java基础测验", "questionCount": 10, "difficultyLevel": 3}
- **Expected results**: 返回200状态码，生成包含10个选择题的测验
- **Verification points**:
  - 响应状态码为200
  - 生成的题目基于学习空间内的知识来源
  - 通过阿里通义千问生成选择题
  - 生成题目数量符合请求参数（约10题）
  - 题目难度符合要求

### 8.2 提交测验答案

#### TC_QUIZ_002: 提交测验答案并获得结果
- **Test purpose**: 验证用户可以提交测验答案并获得评分结果
- **Pre-conditions**: 用户已登录，存在测验
- **Test steps**:
  1. 调用POST /api/v1/quizzes/{quizId}/submit接口
  2. 提交答案
- **Input data**: {
    "answers": [
      {"questionId": 1, "selectedOption": "A"},
      {"questionId": 2, "selectedOption": "C"}
    ]
  }
- **Expected results**: 返回200状态码，包含评分结果
- **Verification points**:
  - 响应状态码为200
  - 返回正确的评分结果
  - 显示正确答案和解释

## 9. 思维导图功能测试用例（阿里通义千问生成）

### 9.1 生成思维导图

#### TC_MAP_001: 基于知识来源通过阿里通义千问生成思维导图
- **Test purpose**: 验证系统可以通过阿里通义千问基于知识来源自动生成思维导图
- **Pre-conditions**: 用户已登录，存在学习空间和知识来源
- **Test steps**:
  1. 调用POST /api/v1/learning-spaces/{spaceId}/mind-maps/generate接口
  2. 提供导图信息
- **Input data**: {"title": "Java知识体系导图", "description": "Java核心概念的思维导图"}
- **Expected results**: 返回200状态码，生成思维导图结构
- **Verification points**:
  - 响应状态码为200
  - 生成的导图结构基于学习空间内的知识来源
  - 通过阿里通义千问分析知识结构
  - 导图包含节点和节点关系的JSON数据

## 10. 学习资料管理测试用例

### 10.1 保存学习资料

#### TC_MAT_001: 保存学习资料
- **Test purpose**: 验证用户可以保存不同类型的个人学习资料
- **Pre-conditions**: 用户已登录，存在学习空间
- **Test steps**:
  1. 调用POST /api/v1/learning-spaces/{spaceId}/study-materials接口
  2. 提供资料信息
- **Input data**: {
    "title": "学习资料标题",
    "content": "学习资料内容",
    "materialType": "CUSTOM",
    "tags": ["标签1", "标签2"]
  }
- **Expected results**: 返回200状态码，包含保存的资料信息
- **Verification points**:
  - 响应状态码为200
  - 返回数据包含正确的资料信息
  - 资料类型和标签正确存储

## 11. 阿里通义千问集成测试用例

### 11.1 配置管理测试

#### TC_AI_CFG_001: 验证阿里通义千问配置正确加载
- **Test purpose**: 验证AI配置参数从配置文件正确加载
- **Pre-conditions**: 应用程序启动
- **Test steps**:
  1. 检查应用启动日志
  2. 验证配置属性绑定
- **Input data**: 无
- **Expected results**: 配置参数正确加载
- **Verification points**:
  - API URL、模型名称、API Key等参数正确加载
  - 支持环境变量配置
  - 配置验证机制有效

#### TC_AI_CFG_002: 验证配置切换能力
- **Test purpose**: 验证通过配置文件可以切换阿里通义千问模型
- **Pre-conditions**: 存在不同模型的配置参数
- **Test steps**:
  1. 修改配置文件中的模型参数
  2. 重启应用或重载配置
  3. 调用AI功能
- **Input data**: 有效的AI请求
- **Expected results**: 使用新模型参数调用AI服务
- **Verification points**:
  - 配置变更生效
  - 使用正确的模型进行API调用
  - 服务正常响应

### 11.2 API调用测试

#### TC_AI_API_001: 验证阿里通义千问API调用
- **Test purpose**: 验证系统可以正确调用阿里通义千问API
- **Pre-conditions**: 配置有效的API密钥和端点
- **Test steps**:
  1. 发起AI功能请求
  2. 检查API调用过程
- **Input data**: 有效的用户请求
- **Expected results**: 成功获得AI响应
- **Verification points**:
  - 请求头包含正确认证信息
  - 请求体格式正确
  - API响应成功处理

#### TC_AI_API_002: 验证API错误处理
- **Test purpose**: 验证系统对阿里通义千问API错误的处理
- **Pre-conditions**: 配置无效或过期的API密钥
- **Test steps**:
  1. 发起AI功能请求
  2. 检查错误处理机制
- **Input data**: 有效的用户请求
- **Expected results**: 返回适当的错误信息，不导致系统崩溃
- **Verification points**:
  - 返回合适的错误状态码
  - 错误信息对用户友好
  - 系统其他功能正常运行

## 12. 性能测试用例

### 12.1 高并发测试

#### TC_PERF_001: 高并发访问学习空间列表
- **Test purpose**: 验证系统在高并发访问下的性能表现
- **Pre-conditions**: 系统正常运行，存在大量学习空间数据
- **Test steps**:
  1. 使用压力测试工具模拟100个并发用户访问学习空间列表
  2. 持续测试30秒
- **Input data**: 并发请求GET /api/v1/learning-spaces?pageSize=10
- **Expected results**: 系统稳定运行，响应时间在可接受范围内
- **Verification points**:
  - 95%的请求响应时间<2秒
  - 错误率<1%
  - 系统资源使用率在合理范围内

### 12.2 AI服务性能测试

#### TC_PERF_002: 阿里通义千问API调用性能
- **Test purpose**: 验证AI服务调用的性能表现
- **Pre-conditions**: 系统配置有效的阿里通义千问API
- **Test steps**:
  1. 模拟多个用户同时发起AI问答请求
  2. 监控响应时间
- **Input data**: 并发的AI问答请求
- **Expected results**: AI响应时间在可接受范围内
- **Verification points**:
  - 95%的AI请求响应时间<10秒
  - API调用成功率>95%
  - 服务无崩溃或异常

## 13. 安全测试用例

### 13.1 认证测试

#### TC_SEC_001: 未认证用户访问受保护资源
- **Test purpose**: 验证未认证用户无法访问受保护的API
- **Pre-conditions**: 系统正常运行
- **Test steps**:
  1. 以未认证用户身份调用受保护的API
  2. 例如: GET /api/v1/learning-spaces
- **Input data**: 不提供认证token
- **Expected results**: 返回401状态码
- **Verification points**:
  - 响应状态码为401
  - 返回认证错误信息

#### TC_SEC_002: 越权访问测试
- **Test purpose**: 验证用户无法访问他人的资源
- **Pre-conditions**: 存在多个用户及相应资源
- **Test steps**:
  1. 用户A尝试访问用户B的学习空间
  2. 例如: GET /api/v1/learning-spaces/{用户B的空间ID}
- **Input data**: 用户A的token，访问用户B的资源
- **Expected results**: 返回403状态码
- **Verification points**:
  - 响应状态码为403
  - 返回权限不足错误信息

### 13.2 阿里通义千问API安全

#### TC_SEC_003: API密钥安全测试
- **Test purpose**: 验证阿里通义千问API密钥的安全处理
- **Pre-conditions**: 系统配置了API密钥
- **Test steps**:
  1. 检查日志中是否包含API密钥
  2. 检查API请求头中的认证信息
- **Input data**: 无
- **Expected results**: API密钥不在日志中泄露
- **Verification points**:
  - 日志中不包含明文API密钥
  - 认证信息妥善处理
  - API密钥通过安全方式传输

### 13.3 输入验证测试

#### TC_SEC_004: SQL注入测试
- **Test purpose**: 验证系统对SQL注入攻击的防护
- **Pre-conditions**: 系统正常运行
- **Test steps**:
  1. 在参数中输入SQL注入代码
  2. 例如: GET /api/v1/learning-spaces?search=' OR '1'='1
- **Input data**: 各种SQL注入尝试
- **Expected results**: 系统正确处理输入，不执行恶意SQL
- **Verification points**:
  - 系统不执行恶意SQL语句
  - 返回正常的错误或空结果

#### TC_SEC_005: XSS防护测试
- **Test purpose**: 验证系统对XSS攻击的防护
- **Pre-conditions**: 系统正常运行
- **Test steps**:
  1. 在可输入内容的地方输入XSS代码
  2. 例如在笔记内容中输入<script>alert('XSS')</script>
- **Input data**: 各种XSS攻击代码
- **Expected results**: XSS代码被过滤或转义
- **Verification points**:
  - 恶意代码被过滤或转义
  - 不会在输出时执行恶意脚本

## 14. 异常测试用例

### 14.1 AI服务不可用

#### TC_EXC_001: 阿里通义千问API调用失败处理
- **Test purpose**: 验证当阿里通义千问API不可用时系统的处理方式
- **Pre-conditions**: 临时断开AI服务连接或API密钥无效
- **Test steps**:
  1. 调用AI问答或聊天相关接口
  2. 例如: POST /api/v1/learning-spaces/{spaceId}/ai-questions
- **Input data**: {"question": "测试问题"}
- **Expected results**: 返回适当的错误信息，不导致系统崩溃
- **Verification points**:
  - 返回503或类似错误状态
  - 给出友好的错误提示
  - 不影响其他功能正常运行

### 14.2 文件处理异常

#### TC_EXC_002: 上传损坏的PDF文件
- **Test purpose**: 验证系统对损坏文件的处理
- **Pre-conditions**: 用户已登录，存在学习空间
- **Test steps**:
  1. 上传一个损坏的PDF文件
- **Input data**: 损坏的PDF文件
- **Expected results**: 返回适当的错误信息，不导致系统崩溃
- **Verification points**:
  - 返回错误状态码
  - 给出文件损坏的提示
  - 保持系统稳定性