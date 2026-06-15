package com.lods.infrastructure.dao;

import com.lods.domain.answer.model.entity.AIAnswerInsertEntity;
import com.lods.infrastructure.dao.po.Question;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IQuestionGapDao {

    Question getCurrentQuestion(int random);

    Question getTotal();

    Question queryQuestionById(int id);

    List<Question> getRangeQuestion(int offset, int pageSize);

    void batchUpdateAIAnswer(List<AIAnswerInsertEntity> list);

    String getAnswerByQuestionId(Integer questionId);
}
