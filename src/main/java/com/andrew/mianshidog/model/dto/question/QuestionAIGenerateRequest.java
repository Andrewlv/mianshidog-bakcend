package com.andrew.mianshidog.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * AI 生成题目请求
 */
@Data
public class QuestionAIGenerateRequest implements Serializable {

    private String questionType;

    private int number = 10;

    private static final long serialVersionUID = 1L;
}
