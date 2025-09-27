-- 数据库表结构定义

-- 学习空间表
CREATE TABLE IF NOT EXISTS learning_spaces (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    user_id VARCHAR(36),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 知识来源表
CREATE TABLE IF NOT EXISTS knowledge_sources (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content_type VARCHAR(50) NOT NULL, -- 'webpage', 'text', 'pdf'
    content TEXT,
    source_url TEXT,
    learning_space_id VARCHAR(36),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (learning_space_id) REFERENCES learning_spaces(id)
);

-- AI问题表
CREATE TABLE IF NOT EXISTS ai_questions (
    id VARCHAR(36) PRIMARY KEY,
    question TEXT NOT NULL,
    answer TEXT,
    knowledge_source_id VARCHAR(36),
    user_id VARCHAR(36),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (knowledge_source_id) REFERENCES knowledge_sources(id)
);

-- 聊天会话表
CREATE TABLE IF NOT EXISTS chat_sessions (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255),
    user_id VARCHAR(36),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 聊天消息表
CREATE TABLE IF NOT EXISTS chat_messages (
    id VARCHAR(36) PRIMARY KEY,
    session_id VARCHAR(36),
    content TEXT NOT NULL,
    sender_type VARCHAR(20) NOT NULL, -- 'user', 'ai'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES chat_sessions(id)
);

-- 笔记表
CREATE TABLE IF NOT EXISTS notes (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    learning_space_id VARCHAR(36),
    user_id VARCHAR(36),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (learning_space_id) REFERENCES learning_spaces(id)
);

-- 知识卡片表
CREATE TABLE IF NOT EXISTS knowledge_cards (
    id VARCHAR(36) PRIMARY KEY,
    front_content TEXT NOT NULL,
    back_content TEXT NOT NULL,
    knowledge_source_id VARCHAR(36),
    difficulty INTEGER DEFAULT 1, -- 1-5 scale
    next_review_date TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (knowledge_source_id) REFERENCES knowledge_sources(id)
);

-- 测验表
CREATE TABLE IF NOT EXISTS quizzes (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    learning_space_id VARCHAR(36),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (learning_space_id) REFERENCES learning_spaces(id)
);

-- 测验题目表
CREATE TABLE IF NOT EXISTS quiz_questions (
    id VARCHAR(36) PRIMARY KEY,
    quiz_id VARCHAR(36),
    question_text TEXT NOT NULL,
    question_type VARCHAR(50) NOT NULL, -- 'multiple_choice', 'true_false', 'short_answer'
    correct_answer TEXT NOT NULL,
    options JSON, -- 存储选择题选项
    difficulty INTEGER DEFAULT 1, -- 1-5 scale
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

-- 思维导图表
CREATE TABLE IF NOT EXISTS mind_maps (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content JSON, -- 存储思维导图的JSON结构
    learning_space_id VARCHAR(36),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (learning_space_id) REFERENCES learning_spaces(id)
);

-- 学习资料表
CREATE TABLE IF NOT EXISTS study_materials (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content_type VARCHAR(50) NOT NULL, -- 'pdf', 'image', 'document', 'link'
    file_path VARCHAR(500),
    file_url TEXT,
    learning_space_id VARCHAR(36),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (learning_space_id) REFERENCES learning_spaces(id)
);

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引以提高查询性能
CREATE INDEX IF NOT EXISTS idx_learning_spaces_user_id ON learning_spaces(user_id);
CREATE INDEX IF NOT EXISTS idx_knowledge_sources_learning_space_id ON knowledge_sources(learning_space_id);
CREATE INDEX IF NOT EXISTS idx_ai_questions_knowledge_source_id ON ai_questions(knowledge_source_id);
CREATE INDEX IF NOT EXISTS idx_ai_questions_user_id ON ai_questions(user_id);
CREATE INDEX IF NOT EXISTS idx_chat_sessions_user_id ON chat_sessions(user_id);
CREATE INDEX IF NOT EXISTS idx_chat_messages_session_id ON chat_messages(session_id);
CREATE INDEX IF NOT EXISTS idx_notes_learning_space_id ON notes(learning_space_id);
CREATE INDEX IF NOT EXISTS idx_notes_user_id ON notes(user_id);
CREATE INDEX IF NOT EXISTS idx_knowledge_cards_knowledge_source_id ON knowledge_cards(knowledge_source_id);
CREATE INDEX IF NOT EXISTS idx_quizzes_learning_space_id ON quizzes(learning_space_id);
CREATE INDEX IF NOT EXISTS idx_quiz_questions_quiz_id ON quiz_questions(quiz_id);
CREATE INDEX IF NOT EXISTS idx_mind_maps_learning_space_id ON mind_maps(learning_space_id);
CREATE INDEX IF NOT EXISTS idx_study_materials_learning_space_id ON study_materials(learning_space_id);