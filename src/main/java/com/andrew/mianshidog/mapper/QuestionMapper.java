package com.andrew.mianshidog.mapper;

import com.andrew.mianshidog.model.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
* @author Zhuanz
* @description 针对表【question(题目)】的数据库操作Mapper
* @createDate 2024-11-26 14:34:43
* @Entity com.andrew.mianshidog.model.entity.Question
*/
public interface QuestionMapper extends BaseMapper<Question> {

    @Select("select * from question where updateTime >= #{minUpdateTime}")
    List<Question> listQuestionWithDelete(Date minUpdateTime);
}




