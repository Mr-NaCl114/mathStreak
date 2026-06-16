package com.lods.domain.answer.adapter.repository;

import com.lods.domain.answer.model.entity.AIAnswerGetQuestionReqEntity;
import com.lods.domain.answer.model.entity.AIAnswerInsertEntity;
import com.lods.domain.question.model.valobj.QuestionVO;

import java.util.List;

public interface IAIAnswerRepository {

    QuestionVO getQuestionById(AIAnswerGetQuestionReqEntity aiAnswerGetQuestionReqEntity);

    Integer countChoiceQuestions();

    List<QuestionVO> getChoiceQuestions(int offset, int pageSize);

    Integer countGapQuestions();

    List<QuestionVO> getGapQuestions(int offset, int pageSize);

    void choiceBatchUpdateAIAnswer(List<AIAnswerInsertEntity> list);

    void gapBatchUpdateAIAnswer(List<AIAnswerInsertEntity> list);

    String getAnswerByQuestionId(AIAnswerGetQuestionReqEntity aiAnswerGetQuestionReqEntity);

    boolean getAnswerSign(String sign);
}
