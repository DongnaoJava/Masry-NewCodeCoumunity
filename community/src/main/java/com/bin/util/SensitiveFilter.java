package com.bin.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class SensitiveFilter {
    //替换符
    private static final String REPLACEMENT = "***";
    //根节点
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init() {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("sensitiveWords.txt");
        if (inputStream == null)
            log.error("敏感词文件没有获取到！");
        else {
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream)) {
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String sensitiveWord;
                while ((sensitiveWord = bufferedReader.readLine()) != null) {
                    this.addSensitiveWord(sensitiveWord);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("敏感词文件初始化失败！" + e.getMessage());
                }
            }
        }
    }

    private void addSensitiveWord(String sensitiveWord) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < sensitiveWord.length(); i++) {
            char c = sensitiveWord.charAt(i);
            TrieNode subNode = tempNode.getSonTireNode(c);

            if (subNode == null) {
                //初始化子节点
                subNode = new TrieNode();
                tempNode.addSonTireNode(c, subNode);
            }
            //进入下一轮
            tempNode = subNode;

            //添加关键keyword
            if (i == sensitiveWord.length() - 1)
                tempNode.setKeywords(true);
        }
    }

    /**
     * 过滤敏感词
     *
     * @param text
     * @return
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        // 指针1
        TrieNode tempNode = rootNode;
        // 指针2
        int begin = 0;
        // 指针3
        int position = 0;
        // 结果
        StringBuilder sb = new StringBuilder();

        while (position < text.length()) {
            char c = text.charAt(position);

            // 跳过符号
            if (isSymbol(c)) {
                // 若指针1处于根节点,将此符号计入结果,让指针2向下走一步
                if (tempNode == rootNode) {
                    sb.append(c);
                    begin++;
                }
                // 无论符号在开头或中间,指针3都向下走一步
                position++;
                continue;
            }

            // 检查下级节点
            tempNode = tempNode.getSonTireNode(c);
            if (tempNode == null) {
                // 以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                // 进入下一个位置
                position = ++begin;
                // 重新指向根节点
                tempNode = rootNode;
            } else if (tempNode.isKeywords()) {
                // 发现敏感词,将begin~position字符串替换掉
                sb.append(REPLACEMENT);
                // 进入下一个位置
                begin = ++position;
                // 重新指向根节点
                tempNode = rootNode;
            } else {
                // 检查下一个字符
                position++;
            }
        }

        // 将最后一批字符计入结果
        sb.append(text.substring(begin));

        return sb.toString();
    }

    // 判断是否为符号
    private boolean isSymbol(Character c) {
        // 0x2E80~0x9FFF 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    private static class TrieNode {
        //关键词结束标志
        private boolean isKeywords = false;
        //子节点，其中key是子节点的字符，value是子节点的下级节点
        private Map<Character, TrieNode> sonTireNode = new HashMap<>();

        public boolean isKeywords() {
            return isKeywords;
        }

        public void setKeywords(boolean keywords) {
            isKeywords = keywords;
        }

        //添加子节点
        public void addSonTireNode(Character character, TrieNode tireNode) {
            sonTireNode.put(character, tireNode);
        }

        //获得子节点
        public TrieNode getSonTireNode(Character character) {
            return sonTireNode.get(character);
        }
    }
}
