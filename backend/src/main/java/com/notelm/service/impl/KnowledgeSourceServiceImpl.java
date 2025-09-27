package com.notelm.service.impl;

import com.notelm.dto.request.AddTextSourceRequest;
import com.notelm.dto.request.AddWebpageSourceRequest;
import com.notelm.dto.response.KnowledgeSourceResponse;
import com.notelm.exception.BusinessException;
import com.notelm.exception.ResourceNotFoundException;
import com.notelm.model.KnowledgeSource;
import com.notelm.model.LearningSpace;
import com.notelm.repository.KnowledgeSourceRepository;
import com.notelm.repository.LearningSpaceRepository;
import com.notelm.service.KnowledgeSourceService;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

@Service
@Transactional
public class KnowledgeSourceServiceImpl implements KnowledgeSourceService {

    @Autowired
    private KnowledgeSourceRepository knowledgeSourceRepository;

    @Autowired
    private LearningSpaceRepository learningSpaceRepository;

    @Override
    public KnowledgeSourceResponse addWebpageSource(Long learningSpaceId, AddWebpageSourceRequest request) {
        // 验证学习空间是否存在
        LearningSpace learningSpace = learningSpaceRepository.findById(learningSpaceId)
            .orElseThrow(() -> new ResourceNotFoundException("学习空间不存在"));

        // 提取网页内容（这里简化处理，实际实现需要使用HTTP客户端获取网页内容）
        String content = extractWebpageContent(request.getUrl());

        KnowledgeSource source = new KnowledgeSource();
        source.setLearningSpaceId(learningSpaceId);
        source.setSourceType(KnowledgeSource.SourceType.WEBPAGE);
        source.setTitle(request.getTitle() != null ? request.getTitle() : extractTitleFromUrl(request.getUrl()));
        source.setContent(content);
        source.setUrl(request.getUrl());
        source.setStatus(KnowledgeSource.Status.PROCESSED);

        KnowledgeSource saved = knowledgeSourceRepository.save(source);
        return convertToResponse(saved);
    }

    @Override
    public KnowledgeSourceResponse addTextSource(Long learningSpaceId, AddTextSourceRequest request) {
        // 验证学习空间是否存在
        LearningSpace learningSpace = learningSpaceRepository.findById(learningSpaceId)
            .orElseThrow(() -> new ResourceNotFoundException("学习空间不存在"));

        KnowledgeSource source = new KnowledgeSource();
        source.setLearningSpaceId(learningSpaceId);
        source.setSourceType(KnowledgeSource.SourceType.TEXT);
        source.setTitle(request.getTitle());
        source.setContent(request.getContent());
        source.setStatus(KnowledgeSource.Status.PROCESSED);

        KnowledgeSource saved = knowledgeSourceRepository.save(source);
        return convertToResponse(saved);
    }

    @Override
    public KnowledgeSourceResponse uploadPdfFile(Long learningSpaceId, MultipartFile file, String title) {
        // 验证学习空间是否存在
        LearningSpace learningSpace = learningSpaceRepository.findById(learningSpaceId)
            .orElseThrow(() -> new ResourceNotFoundException("学习空间不存在"));

        // 验证文件类型
        if (!file.getContentType().equals("application/pdf")) {
            throw new BusinessException("上传的文件必须是PDF格式");
        }

        String content;
        Long fileSize = file.getSize();
        Integer pageCount = 0;

        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {
            
            PDFTextStripper pdfStripper = new PDFTextStripper();
            content = pdfStripper.getText(document);
            
            pageCount = document.getNumberOfPages();
        } catch (IOException e) {
            throw new BusinessException("PDF文件处理失败: " + e.getMessage());
        }

        KnowledgeSource source = new KnowledgeSource();
        source.setLearningSpaceId(learningSpaceId);
        source.setSourceType(KnowledgeSource.SourceType.PDF);
        source.setTitle(title != null ? title : file.getOriginalFilename());
        source.setContent(content);
        source.setFilePath("uploads/" + file.getOriginalFilename()); // 实际实现中需要保存文件到指定位置
        source.setFileSize(fileSize);
        source.setPageCount(pageCount);
        source.setStatus(KnowledgeSource.Status.PROCESSED);

        KnowledgeSource saved = knowledgeSourceRepository.save(source);
        return convertToResponse(saved);
    }

    @Override
    public Page<KnowledgeSourceResponse> getKnowledgeSources(Long learningSpaceId, Pageable pageable) {
        return knowledgeSourceRepository.findByLearningSpaceId(learningSpaceId, pageable)
            .map(this::convertToResponse);
    }

    @Override
    public KnowledgeSourceResponse getKnowledgeSourceById(Long id) {
        KnowledgeSource source = knowledgeSourceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("知识来源不存在"));
        return convertToResponse(source);
    }

    @Override
    public void deleteKnowledgeSource(Long id) {
        KnowledgeSource source = knowledgeSourceRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("知识来源不存在"));
        knowledgeSourceRepository.deleteById(id);
    }

    @Override
    public Page<KnowledgeSourceResponse> searchKnowledgeSources(Long learningSpaceId, String search, Pageable pageable) {
        if (search == null || search.trim().isEmpty()) {
            return getKnowledgeSources(learningSpaceId, pageable);
        }
        return knowledgeSourceRepository.findByLearningSpaceIdAndSearch(learningSpaceId, search, pageable)
            .map(this::convertToResponse);
    }

    private String extractWebpageContent(String url) {
        // 实际实现中，这里需要使用HTTP客户端获取网页内容
        // 现在返回一个模拟内容
        return "模拟从网页获取的内容: " + url;
    }

    private String extractTitleFromUrl(String url) {
        // 从URL提取标题的简单实现
        String cleanUrl = url.replaceAll("https?://", "").replaceAll("www.", "");
        String[] parts = cleanUrl.split("/");
        if (parts.length > 0) {
            String lastPart = parts[parts.length - 1];
            if (lastPart.isEmpty() && parts.length > 1) {
                lastPart = parts[parts.length - 2];
            }
            return lastPart.replaceAll("-", " ").replaceAll("_", " ");
        }
        return url;
    }

    private KnowledgeSourceResponse convertToResponse(KnowledgeSource source) {
        KnowledgeSourceResponse response = new KnowledgeSourceResponse();
        response.setId(source.getId());
        response.setLearningSpaceId(source.getLearningSpaceId());
        response.setSourceType(source.getSourceType().name());
        response.setTitle(source.getTitle());
        response.setContent(source.getContent());
        response.setUrl(source.getUrl());
        response.setFilePath(source.getFilePath());
        response.setFileSize(source.getFileSize());
        response.setPageCount(source.getPageCount());
        response.setStatus(source.getStatus().name());
        response.setCreatedAt(source.getCreatedAt());
        response.setUpdatedAt(source.getUpdatedAt());
        return response;
    }
}