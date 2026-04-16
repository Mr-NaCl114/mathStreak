package com.lods.infrastructure.dao;

import com.lods.domain.model.entity.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChoiceQuestionDao {

    Question getQuestions(int id);

    Question getTotal();
}
