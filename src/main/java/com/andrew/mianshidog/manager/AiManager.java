package com.andrew.mianshidog.manager;

import cn.hutool.core.collection.CollUtil;
import com.andrew.mianshidog.common.ErrorCode;
import com.andrew.mianshidog.exception.BusinessException;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionChoice;
import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class AiManager {

    @Resource
    private ArkService aiService;

    private final String DEFAULT_MODEL = "deepseek-v3-241226";

    public String doChat(String userPrompt) {
        return this.doChat("", userPrompt, DEFAULT_MODEL);
    }

    public String doChat(String systemPrompt, String userPrompt) {
        return this.doChat(systemPrompt, userPrompt, DEFAULT_MODEL);
    }

    /**
     * 调用 AI 接口
     *
     * @param systemPrompt
     * @param userPrompt
     * @param model
     * @return
     */
    public String doChat(String systemPrompt, String userPrompt, String model) {
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = ChatMessage.builder().role(ChatMessageRole.SYSTEM).content(systemPrompt).build();
        final ChatMessage userMessage = ChatMessage.builder().role(ChatMessageRole.USER).content(userPrompt).build();
        messages.add(systemMessage);
        messages.add(userMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(model)
                .messages(messages)
                .build();

        List<ChatCompletionChoice> choices = aiService.createChatCompletion(chatCompletionRequest).getChoices();
        if (CollUtil.isNotEmpty(choices)) {
            return (String) choices.get(0).getMessage().getContent();
        }
        throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI 调用失败，没有返回结果");
        // shutdown service after all requests is finished
//        aiService.shutdownExecutor();
    }
}
