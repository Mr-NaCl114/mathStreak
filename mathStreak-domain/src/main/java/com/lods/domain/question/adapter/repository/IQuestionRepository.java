package com.lods.domain.question.adapter.repository;

import com.lods.domain.question.model.valobj.QuestionVO;

public interface IQuestionRepository {

    QuestionVO getRandomQuestion();

    QuestionVO getQuestionChoiceAnswerById(int Id);

    QuestionVO getQuestionGapAnswerById(int Id);

    String createAnswerSign();
}
