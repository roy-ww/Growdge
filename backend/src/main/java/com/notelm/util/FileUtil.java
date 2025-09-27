package com.notelm.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
    
    /**
     * 验证上传的文件是否为PDF格式
     * 
     * @param file 上传的文件
     * @return 如果是PDF格式返回true，否则返回false
     */
    public static boolean isPdfFile(MultipartFile file) {
        // 检查文件扩展名
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return false;
        }
        
        if (!fileName.toLowerCase().endsWith(".pdf")) {
            return false;
        }
        
        // 检查内容类型
        String contentType = file.getContentType();
        if (contentType == null) {
            return false;
        }
        
        return contentType.equals("application/pdf");
    }
    
    /**
     * 验证上传的文件是否为文本格式
     * 
     * @param file 上传的文件
     * @return 如果是文本格式返回true，否则返回false
     */
    public static boolean isTextFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return false;
        }
        
        return fileName.toLowerCase().endsWith(".txt") || 
               fileName.toLowerCase().endsWith(".md") ||
               fileName.toLowerCase().endsWith(".text");
    }
    
    /**
     * 检查文件大小是否超过限制
     * 
     * @param file 上传的文件
     * @param maxSize 最大大小（字节）
     * @return 如果文件大小未超过限制返回true，否则返回false
     */
    public static boolean isFileSizeWithinLimit(MultipartFile file, long maxSize) {
        return file.getSize() <= maxSize;
    }
    
    /**
     * 获取文件扩展名
     * 
     * @param fileName 文件名
     * @return 扩展名（不包含点号），如果没有扩展名则返回空字符串
     */
    public static String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            return fileName.substring(lastDotIndex + 1);
        }
        
        return "";
    }
}