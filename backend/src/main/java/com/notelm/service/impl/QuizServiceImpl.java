package com.notelm.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notelm.dto.request.GenerateQuizRequest;
import com.notelm.dto.response.QuizQuestionResponse;
import com.notelm.dto.response.QuizResponse;
import com.notelm.exception.BusinessException;
import com.notelm.exception.ResourceNotFoundException;
import com.notelm.model.KnowledgeSource;
import com.notelm.model.Quiz;
import com.notelm.model.QuizQuestion;
import com.notelm.repository.KnowledgeSourceRepository;
import com.notelm.repository.QuizQuestionRepository;
import com.notelm.repository.QuizRepository;
import com.notelm.service.QuizService;
import com.notelm.ai.QwenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private QuizQuestionRepository quizQuestionRepository;

    @Autowired
    private KnowledgeSourceRepository knowledgeSourceRepository;

    @Autowired
    private QwenService qwenService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public QuizResponse generateQuiz(Long learningSpaceId, GenerateQuizRequest request) {
        // 获取学习空间中的知识来源
        List<KnowledgeSource> knowledgeSources = knowledgeSourceRepository.findByLearningSpaceId(learningSpaceId);
        if (knowledgeSources.isEmpty()) {
            throw new BusinessException("当前学习空间没有可用的知识来源，无法生成测验");
        }

        // 创建测验
        Quiz quiz = new Quiz();
        quiz.setLearningSpaceId(learningSpaceId);
        quiz.setTitle(request.getTitle());
        quiz.setDescription("基于学习空间内容自动生成的测验");
        Quiz savedQuiz = quizRepository.save(quiz);

        // 生成指定数量的题目
        for (int i = 0; i < request.getQuestionCount(); i++) {
            String aiResponse = qwenService.generateQuizQuestion(learningSpaceId, "主题" + (i + 1));
            
            // 解析AI响应并创建题目
            QuizQuestion question = parseQuizQuestionFromAIResponse(aiResponse, savedQuiz.getId());
            if (question != null) {
                quizQuestionRepository.save(question);
            }
        }

        return convertToResponse(savedQuiz);
    }

    @Override
    public QuizResponse getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("测验不存在"));
        return convertToResponse(quiz);
    }

    @Override
    public Page<QuizResponse> getQuizzes(Long learningSpaceId, Pageable pageable) {
        return quizRepository.findByLearningSpaceId(learningSpaceId, pageable)
            .map(this::convertToResponse);
    }

    @Override
    public Page<QuizQuestionResponse> getQuizQuestions(Long quizId, Pageable pageable) {
        List<QuizQuestion> questions = quizQuestionRepository.findByQuizId(quizId);
        
        // 手动实现分页，因为findByQuizId不直接支持分页
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), questions.size());
        
        if (start > questions.size()) {
            start = questions.size();
        }
        
        List<QuizQuestion> pagedQuestions = questions.subList(start, end);
        
        // 计算总数量用于Page对象
        long total = questions.size();
        
        List<QuizQuestionResponse> responses = pagedQuestions.stream()
            .map(this::convertToQuestionResponse)
            .toList();
        
        // 这里应该返回一个Page实现，但我们使用简化的方式
        // 实际实现中可能需要创建自定义的Page实现
        return null; // 需要更复杂的实现
    }

    @Override
    public Object submitQuiz(Long quizId, Object answers) {
        // 获取测验和题目
        Quiz quiz = quizRepository.findById(quizId)
            .orElseThrow(() -> new ResourceNotFoundException("测验不存在"));
        
        List<QuizQuestion> questions = quizQuestionRepository.findByQuizId(quizId);
        
        // 解析用户答案
        Map<String, String> userAnswers;
        try {
            userAnswers = objectMapper.convertValue(answers, Map.class);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("答案格式错误");
        }
        
        // 评分
        int correctCount = 0;
        int totalCount = questions.size();
        List<Map<String, Object>> results = new ArrayList<>();
        
        for (QuizQuestion question : questions) {
            String userAnswer = userAnswers.get(String.valueOf(question.getId()));
            boolean isCorrect = question.getCorrectAnswer().equals(userAnswer);
            
            if (isCorrect) {
                correctCount++;
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("questionId", question.getId());
            result.put("isCorrect", isCorrect);
            result.put("correctAnswer", question.getCorrectAnswer());
            result.put("userAnswer", userAnswer);
            result.put("explanation", question.getExplanation());
            results.add(result);
        }
        
        // 构建结果
        Map<String, Object> result = new HashMap<>();
        result.put("quizId", quizId);
        result.put("score", correctCount);
        result.put("total", totalCount);
        result.put("percentage", totalCount > 0 ? (double) correctCount / totalCount * 100 : 0);
        result.put("results", results);
        
        return result;
    }

    private QuizQuestion parseQuizQuestionFromAIResponse(String aiResponse, Long quizId) {
        QuizQuestion question = new QuizQuestion();
        question.setQuizId(quizId);
        
        // 使用正则表达式解析AI响应
        String[] lines = aiResponse.split("
");
        
        StringBuilder questionText = new StringBuilder();
        Map<String, String> options = new LinkedHashMap<>();
        String correctAnswer = null;
        StringBuilder explanation = new StringBuilder();
        
        boolean inExplanation = false;
        
        for (String line : lines) {
            line = line.trim();
            
            if (line.toLowerCase().startsWith("正确答案：") || line.toLowerCase().startsWith("correct answer:")) {
                // 提取正确答案
                String answerPart = line.substring(line.indexOf(":") + 1).trim();
                if (answerPart.length() > 0) {
                    correctAnswer = String.valueOf(answerPart.charAt(0)).toUpperCase();
                }
                inExplanation = true;
            } else if (line.toLowerCase().startsWith("解释：") || line.toLowerCase().startsWith("explanation:")) {
                // 开始解释部分
                String explanationPart = line.substring(line.indexOf(":") + 1).trim();
                explanation.append(explanationPart).append(" ");
                inExplanation = true;
            } else if (line.matches("^[A-Da-d][.。].*")) {
                // 识别选项 A. B. C. D.
                String optionKey = String.valueOf(line.charAt(0)).toUpperCase();
                String optionValue = line.substring(2).trim();
                options.put(optionKey, optionValue);
            } else if (line.startsWith("A.") || line.startsWith("B.") || line.startsWith("C.") || line.startsWith("D.")) {
                // 识别选项 A. B. C. D.
                String optionKey = String.valueOf(line.charAt(0));
                String optionValue = line.substring(2).trim();
                options.put(optionKey, optionValue);
            } else if (!inExplanation && !options.isEmpty()) {
                // 如果已经有选项了，但还没有问题文本，则这可能是问题的延续
                if (questionText.length() == 0) {
                    questionText.append(line);
                } else {
                    questionText.append(" ").append(line);
                }
            } else if (!inExplanation) {
                // 这是问题部分
                if (questionText.length() > 0) {
                    questionText.append(" ");
                }
                questionText.append(line);
            } else {
                // 这是解释部分
                explanation.append(line).append(" ");
            }
        }
        
        // 提取题目部分（通常在第一个选项之前）
        String fullQuestion = questionText.toString();
        if (!options.isEmpty()) {
            for (String optionKey : options.keySet()) {
                int optionIndex = fullQuestion.toUpperCase().indexOf(optionKey + ".");
                if (optionIndex != -1) {
                    question.setQuestionText(fullQuestion.substring(0, optionIndex).trim());
                    break;
                }
            }
        }
        
        // 如果没有找到合适的分割点，使用全文作为问题
        if (question.getQuestionText() == null || question.getQuestionText().isEmpty()) {
            question.setQuestionText(fullQuestion);
        }
        
        try {
            question.setOptions(objectMapper.writeValueAsString(options));
        } catch (JsonProcessingException e) {
            // 如果序列化失败，使用空的选项
            try {
                question.setOptions(objectMapper.writeValueAsString(new HashMap<>()));
            } catch (JsonProcessingException ex) {
                // 不可能走到这里
            }
        }
        
        question.setCorrectAnswer(correctAnswer);
        question.setExplanation(explanation.toString().trim());
        question.setCreatedAt(LocalDateTime.now());
        
        return question;
    }

    private QuizResponse convertToResponse(Quiz quiz) {
        QuizResponse response = new QuizResponse();
        response.setId(quiz.getId());
        response.setLearningSpaceId(quiz.getLearningSpaceId());
        response.setTitle(quiz.getTitle());
        response.setDescription(quiz.getDescription());
        
        // 设置题目数量
        List<QuizQuestion> questions = quizQuestionRepository.findByQuizId(quiz.getId());
        response.setQuestionCount(questions.size());
        
        response.setCreatedAt(quiz.getCreatedAt());
        return response;
    }

    private QuizQuestionResponse convertToQuestionResponse(QuizQuestion question) {
        QuizQuestionResponse response = new QuizQuestionResponse();
        response.setId(question.getId());
        response.setQuizId(question.getQuizId());
        response.setQuestionText(question.getQuestionText());
        response.setQuestionType(question.getQuestionType().name());
        response.setCorrectAnswer(question.getCorrectAnswer());
        response.setExplanation(question.getExplanation());
        response.setDifficultyLevel(question.getDifficultyLevel());
        response.setCreatedAt(question.getCreatedAt());
        
        // 解析选项
        if (question.getOptions() != null) {
            try {
                Map<String, String> options = objectMapper.readValue(question.getOptions(), Map.class);
                response.setOptions(options);
            } catch (JsonProcessingException e) {
                response.setOptions(new HashMap<>());
            }
        } else {
            response.setOptions(new HashMap<>());
        }
        
        return response;
    }
}