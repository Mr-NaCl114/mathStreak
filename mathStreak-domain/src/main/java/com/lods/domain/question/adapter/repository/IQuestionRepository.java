package com.lods.domain.question.adapter.repository;

import com.lods.domain.question.model.valobj.QuestionVO;

public interface IQuestionRepository {

    QuestionVO getRandomQuestion();

    QuestionVO getQuestionChoiceAnswerById(int Id);

    QuestionVO getQuestionGapAnswerById(int Id);

    void updateStreakCountByIsCorrect(boolean isCorrect);

    String createAnswerSign();
}
