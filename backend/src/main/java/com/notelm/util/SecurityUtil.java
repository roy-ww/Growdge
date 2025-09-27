package com.notelm.util;

import com.notelm.security.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class SecurityUtil {
    
    /**
     * 从安全上下文中获取当前用户的ID
     * @return 当前用户的ID，如果未认证则返回null
     */
    public static Long getCurrentUserId() {
        // 首先尝试从SecurityContext获取
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            try {
                return Long.valueOf(userDetails.getUsername());
            } catch (NumberFormatException e) {
                // 如果用户名不是数字，尝试从token中提取
                return getUserIdFromToken();
            }
        }
        
        // 如果SecurityContext中没有，尝试从请求头中提取
        return getUserIdFromToken();
    }
    
    /**
     * 从请求头的JWT令牌中提取用户ID
     * @return 用户ID，如果无法提取则返回null
     */
    private static Long getUserIdFromToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String token = getTokenFromRequest(request);
            
            if (token != null) {
                JwtTokenProvider tokenProvider = SpringContextUtil.getBean(JwtTokenProvider.class);
                if (tokenProvider != null) {
                    return tokenProvider.getUserIdFromToken(token);
                }
            }
        }
        
        return null;
    }
    
    /**
     * 从请求头中获取JWT令牌
     * @param request HTTP请求
     * @return JWT令牌，如果不存在返回null
     */
    private static String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    /**
     * 检查用户是否已认证
     * @return 如果用户已认证返回true，否则返回false
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && 
               !"anonymousUser".equals(authentication.getPrincipal());
    }
}