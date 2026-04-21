package com.lods.domain.question.adapter.repository;

import com.lods.domain.question.model.valobj.QuestionVO;

public interface IQuestionRepository {

    QuestionVO getRandomQuestion();

    QuestionVO getQuestionChoiceById(int Id);

    QuestionVO getQuestionGapById(int Id);

    void updateStreakCountByIsCorrect(boolean isCorrect);

}
