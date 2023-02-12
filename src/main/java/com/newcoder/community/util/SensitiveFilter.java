package com.newcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.plaf.metal.MetalIconFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuyang
 * @create 2023-02-11 10:33
 */


@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
    // 替换符：将敏感词替换
    private static final String REPLACEMENT = "***";
    //根节点
    private TrieNode rootNode = new TrieNode();


    //@PostConstruct该注解被用来修饰一个非静态的void（）方法。被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，
    //并且只会被服务器执行一次。PostConstruct在构造函数之后执行，init（）方法之前执行。以理解为在spring容器启动的时候执行，
    //可作为一些数据的常规化加载，比如数据字典之类的。

    //初始化前缀树
    @PostConstruct
    public void init(){

        // try()里的内容表示定义的流使用后可以被自动销毁，相当于在finally中关闭流
        try(
                //字节流
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                //字节流 --》  字符流 --》 缓冲流
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null){
                //读完了一行，添加到前缀树
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            logger.error("加载敏感词文件失败: " + e.getMessage());
        }
    }


    // 将一个敏感词添加到前缀树中
    private void addKeyword(String keyword) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            //获取该节点的子节点
            TrieNode subNode = tempNode.getSubNode(c);
            if(subNode == null){
                // 初始化子节点
                subNode = new TrieNode();
                tempNode.addSubNode(c,subNode);
            }

            // 指向子节点,进入下一轮循环
            tempNode = subNode;

            // 设置结束标识
            if( i == keyword.length() - 1){
                tempNode.setKeywordEnd(true);
            }
        }
    }


    /**
     * 过滤敏感词
     * @param text 待过滤的文本
     * @return 过滤后的文本
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }

        //指针一
        TrieNode tempNode =rootNode;
        //指针二
        int begin = 0;
        //指针三
        int position = 0;
        //结果
        StringBuilder sb = new StringBuilder();

        while (begin < text.length()){
            char c = text.charAt(position);

            // 跳过敏感词中间的特殊符号
            if(isSymbol(c)){
                // 若指针1处于根节点,表明不是敏感词中间的特殊符号，将此符号计入结果,让指针2向下走一步
                if(tempNode == rootNode){
                    begin++;
                    sb.append(c);
                }
                // 无论符号在开头或中间,指针3都向下走一步
                position++;
                continue;
            }

            //检查下级节点
            tempNode = tempNode.getSubNode(c);
            // 以begin开头的字符串不是敏感词
            if(tempNode == null){
                sb.append(text.charAt(begin));
                // 进入下一个位置
                position = ++begin;
                // 重新指向根节点
                tempNode = rootNode;
             // 发现敏感词结束标志,将begin~position字符串替换掉
            }else if(tempNode.isKeywordEnd()){
                sb.append(REPLACEMENT);
                // 进入下一个位置
                begin = ++position;
                // 重新指向根节点
                tempNode = rootNode;
            }else {
                // 检查下一个字符
                if(position < text.length() - 1){
                    position++;
                }else {
                    sb.append(text.charAt(begin));
                    position = ++begin;
                    tempNode = rootNode;
                }
            }
        }
        return sb.toString();
    }


    // 判断是否为特殊符号
    private boolean isSymbol(Character c){
        // 0x2E80~0x9FFF 是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }



    //定义前缀树节点
    private class TrieNode{

        // 敏感词结束标识
        private boolean isKeywordEnd = false;

        //get方法
        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        //set方法
        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        // 子节点(key是下级字符,value是下级节点)
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        // 添加子节点
        public void addSubNode(Character c,TrieNode node){
            subNodes.put(c,node);
        }

        // 获取子节点
        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }

    }




}






