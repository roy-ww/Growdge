package com.notelm.util;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.Arrays;
import java.util.List;

public class MarkdownUtil {
    
    private static final List<Extension> EXTENSIONS = Arrays.asList(TablesExtension.create());
    private static final Parser PARSER = Parser.builder().extensions(EXTENSIONS).build();
    private static final HtmlRenderer RENDERER = HtmlRenderer.builder().extensions(EXTENSIONS).build();
    
    /**
     * 将Markdown文本转换为HTML
     * 
     * @param markdown Markdown文本
     * @return 转换后的HTML
     */
    public static String markdownToHtml(String markdown) {
        if (markdown == null || markdown.isEmpty()) {
            return "";
        }
        
        Node document = PARSER.parse(markdown);
        return RENDERER.render(document);
    }
    
    /**
     * 清理Markdown内容，移除潜在的恶意脚本
     * 
     * @param markdown 原始Markdown内容
     * @return 清理后的Markdown内容
     */
    public static String sanitizeMarkdown(String markdown) {
        if (markdown == null) {
            return null;
        }
        
        // 防止XSS攻击，移除潜在的恶意脚本
        return markdown.replaceAll("(?i)<script[^>]*>[\\s\\S]*?</script>", "")
                      .replaceAll("(?i)javascript:", "")
                      .replaceAll("(?i)vbscript:", "");
    }
}