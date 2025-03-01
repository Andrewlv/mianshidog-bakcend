package com.andrew.mianshidog.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class AiManagerTest {

    @Resource
    private AiManager aiManager;

    String userPrompt = "你好";

    @Test
    void doChat() {
        String s = aiManager.doChat(userPrompt);
        System.out.println(s);
    }

    @Test
    void testDoChat() {
        String systemPrompt = "你叫什么名字";
        String s = aiManager.doChat(systemPrompt, userPrompt, "deepseek-v3-241226");
        System.out.println(s);
    }
}