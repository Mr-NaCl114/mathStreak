package com.lods.dao;

import com.lods.domain.po.Question;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GapQuestionDao {
    Question getQuestions(int ramId);

    Question getTotal();
}
