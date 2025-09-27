package com.notelm.util;

import com.notelm.model.KnowledgeSource;

import java.util.List;
import java.util.stream.Collectors;

public class PromptBuilderUtil {
    
    /**
     * 构建基于知识来源的问答提示词
     * 
     * @param knowledgeSources 知识来源列表
     * @param question 用户问题
     * @return 构建的提示词
     */
    public static String buildQAFromKnowledgeSources(List<KnowledgeSource> knowledgeSources, String question) {
        StringBuilder context = new StringBuilder();
        
        for (KnowledgeSource source : knowledgeSources) {
            context.append("来源: ").append(source.getTitle()).append("\n");
            context.append(source.getContent()).append("\n\n");
        }
        
        return String.format(
            "根据以下上下文信息回答问题：\n\n%s\n\n问题：%s\n\n请基于上述信息提供准确、简洁的回答，如果信息中没有相关内容，请明确说明。",
            context.toString(),
            question
        );
    }
    
    /**
     * 构建聊天对话的提示词
     * 
     * @param knowledgeSources 知识来源列表
     * @param conversationHistory 对话历史
     * @param userMessage 用户消息
     * @return 构建的提示词
     */
    public static String buildChatPrompt(List<KnowledgeSource> knowledgeSources, String conversationHistory, String userMessage) {
        StringBuilder context = new StringBuilder();
        
        for (KnowledgeSource source : knowledgeSources) {
            context.append("来源: ").append(source.getTitle()).append("\n");
            context.append(source.getContent()).append("\n\n");
        }
        
        return String.format(
            "你是NoteLM学习平台的AI助手，专门帮助用户学习。以下是相关的知识来源：\n\n%s\n\n这是之前的对话历史：\n%s\n\n用户的问题或消息：%s\n\n请基于相关知识来源提供准确、有帮助的回答，并尽量引用相关来源。",
            context.toString(),
            conversationHistory != null ? conversationHistory : "无历史对话",
            userMessage
        );
    }
    
    /**
     * 构建生成知识卡片的提示词
     * 
     * @param knowledgeSources 知识来源列表
     * @param concept 要生成卡片的概念
     * @return 构建的提示词
     */
    public static String buildKnowledgeCardPrompt(List<KnowledgeSource> knowledgeSources, String concept) {
        StringBuilder content = new StringBuilder();
        
        for (KnowledgeSource source : knowledgeSources) {
            content.append(source.getContent()).append("\n");
        }
        
        return String.format(
            "基于以下内容，为概念'%s'生成一个问答对，用于制作知识卡片：\n\n%s\n\n请提供问题和答案，要求问题清晰明确，答案简洁准确。",
            concept,
            content.toString()
        );
    }
    
    /**
     * 构建生成测验题目的提示词
     * 
     * @param knowledgeSources 知识来源列表
     * @param topic 主题
     * @return 构建的提示词
     */
    public static String buildQuizQuestionPrompt(List<KnowledgeSource> knowledgeSources, String topic) {
        StringBuilder content = new StringBuilder();
        
        // 过滤出非空内容的知识来源
        List<String> validContents = knowledgeSources.stream()
            .filter(source -> source.getContent() != null && !source.getContent().trim().isEmpty())
            .map(KnowledgeSource::getContent)
            .collect(Collectors.toList());
        
        for (String contentStr : validContents) {
            content.append(contentStr).append("\n");
        }
        
        return String.format(
            "基于以下内容，为'%s'这个主题生成一道选择题，包含题干、四个选项（A、B、C、D）和正确答案：\n\n%s\n\n请按照以下格式输出：题干\nA. 选项A\nB. 选项B\nC. 选项C\nD. 选项D\n正确答案：X\n解释：...",
            topic,
            content.toString()
        );
    }
}