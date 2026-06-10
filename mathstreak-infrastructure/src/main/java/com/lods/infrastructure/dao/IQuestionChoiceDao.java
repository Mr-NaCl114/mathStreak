package com.lods.infrastructure.dao;

import com.lods.domain.answer.model.entity.AIAnswerInsertEntity;
import com.lods.infrastructure.dao.po.Question;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IQuestionChoiceDao {

    Question getCurrentQuestion(int random);

    Question getTotal();

    Question queryQuestionById(int id);

    Question[] getRangeQuestion(@Param("offset") int offset, @Param("pageSize") int pageSize);

    void batchUpdateAIAnswer(List<AIAnswerInsertEntity> list);

    String getAnswerByQuestionId(Integer questionId);
}
