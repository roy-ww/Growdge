package com.notelm.ai;

import com.notelm.model.KnowledgeSource;
import com.notelm.repository.KnowledgeSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QwenService {

    @Autowired
    private AIClient aiClient;

    @Autowired
    private KnowledgeSourceRepository knowledgeSourceRepository;

    public String generateAnswer(Long learningSpaceId, String question) {
        // 获取相关知识来源
        List<KnowledgeSource> knowledgeSources = knowledgeSourceRepository.findByLearningSpaceId(learningSpaceId);
        
        // 构建上下文
        StringBuilder context = new StringBuilder();
        for (KnowledgeSource source : knowledgeSources) {
            context.append("来源: ").append(source.getTitle()).append("\n");
            context.append(source.getContent()).append("\n\n");
        }

        // 构建提示词
        String prompt = String.format(
            "根据以下上下文信息回答问题：\n\n%s\n\n问题：%s\n\n请基于上述信息提供准确、简洁的回答，如果信息中没有相关内容，请明确说明。",
            context.toString(),
            question
        );

        // 调用AI服务
        return aiClient.callQwenAPI(prompt);
    }

    public String generateChatReply(Long learningSpaceId, String userMessage, String conversationHistory) {
        // 获取相关知识来源
        List<KnowledgeSource> knowledgeSources = knowledgeSourceRepository.findByLearningSpaceId(learningSpaceId);
        
        // 构建上下文
        StringBuilder context = new StringBuilder();
        for (KnowledgeSource source : knowledgeSources) {
            context.append("来源: ").append(source.getTitle()).append("\n");
            context.append(source.getContent()).append("\n\n");
        }

        // 构建提示词
        String prompt = String.format(
            "你是NoteLM学习平台的AI助手，专门帮助用户学习。以下是相关的知识来源：\n\n%s\n\n这是之前的对话历史：\n%s\n\n用户的问题或消息：%s\n\n请基于相关知识来源提供准确、有帮助的回答，并尽量引用相关来源。",
            context.toString(),
            conversationHistory != null ? conversationHistory : "无历史对话",
            userMessage
        );

        // 调用AI服务
        return aiClient.callQwenAPI(prompt);
    }

    public String generateKnowledgeCardQuestion(Long learningSpaceId, String concept) {
        // 获取相关知识来源
        List<KnowledgeSource> knowledgeSources = knowledgeSourceRepository.findByLearningSpaceId(learningSpaceId);
        
        // 构建上下文
        StringBuilder content = new StringBuilder();
        for (KnowledgeSource source : knowledgeSources) {
            content.append(source.getContent()).append("\n");
        }

        // 构建提示词
        String prompt = String.format(
            "基于以下内容，为概念'%s'生成一个问答对，用于制作知识卡片：\n\n%s\n\n请提供问题和答案，要求问题清晰明确，答案简洁准确。",
            concept,
            content.toString()
        );

        // 调用AI服务
        return aiClient.callQwenAPI(prompt);
    }

    public String generateQuizQuestion(Long learningSpaceId, String topic) {
        // 获取相关知识来源
        List<KnowledgeSource> knowledgeSources = knowledgeSourceRepository.findByLearningSpaceId(learningSpaceId);
        
        // 构建上下文
        StringBuilder content = new StringBuilder();
        for (KnowledgeSource source : knowledgeSources) {
            content.append(source.getContent()).append("\n");
        }

        // 构建提示词
        String prompt = String.format(
            "基于以下内容，为'%s'这个主题生成一道选择题，包含题干、四个选项（A、B、C、D）和正确答案：\n\n%s\n\n请按照以下格式输出：题干\nA. 选项A\nB. 选项B\nC. 选项C\nD. 选项D\n正确答案：X\n解释：...",
            topic,
            content.toString()
        );

        // 调用AI服务
        return aiClient.callQwenAPI(prompt);
    }

    public String generateMindMapContent(Long learningSpaceId, String topic) {
        // 获取相关知识来源
        List<KnowledgeSource> knowledgeSources = knowledgeSourceRepository.findByLearningSpaceId(learningSpaceId);
        
        // 构建上下文
        StringBuilder content = new StringBuilder();
        for (KnowledgeSource source : knowledgeSources) {
            content.append(source.getContent()).append("\n");
        }

        // 构建提示词
        String prompt = String.format(
            "基于以下内容，为'%s'这个主题生成思维导图的结构，以JSON格式输出：\n\n%s\n\nJSON应包含nodes（节点数组）和edges（关系数组），每个节点包含id、label等属性，每个关系包含from、to、label等属性。",
            topic,
            content.toString()
        );

        // 调用AI服务
        return aiClient.callQwenAPI(prompt);
    }
}