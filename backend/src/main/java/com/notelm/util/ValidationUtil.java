package com.notelm.util;

import java.util.regex.Pattern;

public class ValidationUtil {
    
    private static final Pattern URL_PATTERN = Pattern.compile(
        \"^https?://[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9](\\.[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9])*\" +
        \"(/[a-zA-Z0-9_~!$&'()*+,;=:@%-]*)*$\"
    );
    
    /**
     * 验证URL是否有效
     * 
     * @param url 待验证的URL
     * @return 如果URL有效返回true，否则返回false
     */
    public static boolean isValidUrl(String url) {
        if (url == null) {
            return false;
        }
        return URL_PATTERN.matcher(url).matches();
    }
    
    /**
     * 验证字符串长度是否在指定范围内
     * 
     * @param str 待验证的字符串
     * @param minLength 最小长度
     * @param maxLength 最大长度
     * @return 如果长度在范围内返回true，否则返回false
     */
    public static boolean isLengthInRange(String str, int minLength, int maxLength) {
        if (str == null) {
            return minLength <= 0;
        }
        return str.length() >= minLength && str.length() <= maxLength;
    }
}