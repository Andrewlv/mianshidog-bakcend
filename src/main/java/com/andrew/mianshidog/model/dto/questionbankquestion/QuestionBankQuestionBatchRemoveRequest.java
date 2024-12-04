package com.andrew.mianshidog.model.dto.questionbankquestion;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class QuestionBankQuestionBatchRemoveRequest implements Serializable {

    /**
     * 题库 id
     */
    private Long questionBankId;

    /**
     * 题目 id 列表
     */
    private List<Long> questionId;

    private static final long serialVersionUID = 1L;
}