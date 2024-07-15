package com.xiangkai.community.util;

import org.apache.commons.lang3.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SensitiveFilter.class);

    private static final String REPLACEMENT = "***";

    private final TrieNode root = new TrieNode();

    public String filter(String text) {
        StringBuilder builder = new StringBuilder();
        TrieNode tmpNode = root;
        int begin = 0;
        int position = 0;
        int length = text.length();
        while (position < length) {
            char c = text.charAt(position);
            if (isSymbol(c)) {
                if (tmpNode == root) {
                    builder.append(c);
                    begin++;
                }
                position++;
                continue;
            }

            TrieNode child = tmpNode.getChild(c);
            if (child == null) {
                // 以begin开头的词不是敏感词
                builder.append(text.charAt(begin));
                position = ++begin;
                tmpNode = root;
            } else {
                if (child.isKeyWordEnd()) {
                    // 敏感词被屏蔽了，已经被***替代了，begin就不要只加1，
                    // 而是position + 1
                    builder.append(REPLACEMENT);
                    begin = ++position;
                    tmpNode = root;
                } else {
                    tmpNode = child;
                    position++;
                }
            }
        }
        builder.append(text.substring(begin));
        return builder.toString();
    }

    // 判断是否为符号
    private boolean isSymbol(Character c) {
        // 0x2E80~0x9FFF 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    // 初始化敏感词库，构建前缀树
    @PostConstruct
    public void init() {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
        if (inputStream != null) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader reader = new BufferedReader(inputStreamReader)) {

                TrieNode tmpNode = root;
                String line;
                while ((line = reader.readLine()) != null) {
                    for (int i = 0; i < line.length(); i++) {
                        char c = line.charAt(i);
                        TrieNode node = tmpNode.getChild(c);

                        // 等于空，说明c还没加到tmpNode的孩子节点，
                        // new一个节点，加入tmpNode的孩子里面
                        if (node == null) {
                            node = new TrieNode();
                            // end只能出现在孩子节点
                            if (i == line.length() - 1) {
                                node.setKeyWordEnd(true);
                            }
                            tmpNode.addChild(c, node);
                        }
                        tmpNode = node;
                    }
                    tmpNode = root;
                }
                LOGGER.info("敏感词库构建完成！");
            } catch (IOException e) {
                LOGGER.error("加载敏感词文件错误：" + e);
            }
        }
    }

    private static class TrieNode {

        private boolean isKeyWordEnd = false;

        private final Map<Character, TrieNode> children = new HashMap<>();

        public boolean isKeyWordEnd() {
            return isKeyWordEnd;
        }

        public void setKeyWordEnd(boolean keyWordEnd) {
            isKeyWordEnd = keyWordEnd;
        }

        public void addChild(Character c, TrieNode node) {
            children.put(c, node);
        }

        public TrieNode getChild(Character c) {
            return children.get(c);
        }
    }
}
