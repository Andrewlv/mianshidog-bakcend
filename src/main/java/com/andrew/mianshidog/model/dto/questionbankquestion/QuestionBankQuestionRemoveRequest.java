package com.andrew.mianshidog.model.dto.questionbankquestion;

import lombok.Data;

import java.io.Serializable;

/**
 * 移除题目题库关系请求
 */
@Data
public class QuestionBankQuestionRemoveRequest implements Serializable {

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 题库id
     */
    private Long questionBankId;

    private static final long serialVersionUID = 1L;
}
