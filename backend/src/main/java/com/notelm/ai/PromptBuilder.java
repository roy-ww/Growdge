package com.notelm.ai;

import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {

    public String buildQAFromKnowledgeSources(String context, String question) {
        return String.format(
            "根据以下上下文信息回答问题：\n\n%s\n\n问题：%s\n\n请基于上述信息提供准确、简洁的回答，如果信息中没有相关内容，请明确说明。",
            context,
            question
        );
    }

    public String buildChatPrompt(String context, String conversationHistory, String userMessage) {
        return String.format(
            "你是NoteLM学习平台的AI助手，专门帮助用户学习。以下是相关的知识来源：\n\n%s\n\n这是之前的对话历史：\n%s\n\n用户的问题或消息：%s\n\n请基于相关知识来源提供准确、有帮助的回答，并尽量引用相关来源。",
            context,
            conversationHistory != null ? conversationHistory : "无历史对话",
            userMessage
        );
    }

    public String buildKnowledgeCardPrompt(String content, String concept) {
        return String.format(
            "基于以下内容，为概念'%s'生成一个问答对，用于制作知识卡片：\n\n%s\n\n请提供问题和答案，要求问题清晰明确，答案简洁准确。",
            concept,
            content
        );
    }

    public String buildQuizQuestionPrompt(String content, String topic) {
        return String.format(
            "基于以下内容，为'%s'这个主题生成一道选择题，包含题干、四个选项（A、B、C、D）和正确答案：\n\n%s\n\n请按照以下格式输出：题干\nA. 选项A\nB. 选项B\nC. 选项C\nD. 选项D\n正确答案：X\n解释：...",
            topic,
            content
        );
    }

    public String buildMindMapPrompt(String content, String topic) {
        return String.format(
            "基于以下内容，为'%s'这个主题生成思维导图的结构，以JSON格式输出：\n\n%s\n\nJSON应包含nodes（节点数组）和edges（关系数组），每个节点包含id、label等属性，每个关系包含from、to、label等属性。",
            topic,
            content
        );
    }
}