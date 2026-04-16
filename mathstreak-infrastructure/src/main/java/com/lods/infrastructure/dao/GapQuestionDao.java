package com.lods.infrastructure.dao;

import com.lods.domain.model.entity.Question;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GapQuestionDao {
    Question getQuestions(int ramId);

    Question getTotal();
}
