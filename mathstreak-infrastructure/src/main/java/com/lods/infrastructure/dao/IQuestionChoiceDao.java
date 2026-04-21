package com.lods.infrastructure.dao;

import com.lods.infrastructure.dao.po.Question;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface IQuestionChoiceDao {

    Question getCurrentQuestion(int random);

    Question getTotal();

    Question queryQuestionById(int id);
}
