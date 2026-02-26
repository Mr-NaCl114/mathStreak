package com.lods.dao;

import com.lods.domain.vo.ChoiceQuestionVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChoiceQuestionDao {

    ChoiceQuestionVO getQuestions(int ramId);

    ChoiceQuestionVO getTotal();
}
