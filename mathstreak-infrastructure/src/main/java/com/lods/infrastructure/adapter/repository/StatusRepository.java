package com.lods.infrastructure.adapter.repository;

import com.lods.domain.status.apadter.repository.IStatusRepository;
import com.lods.domain.status.model.entity.CurrentAnswerChangeEntity;
import com.lods.domain.status.model.entity.QuestionDescriptionEntity;
import com.lods.domain.status.model.valobj.FailInterruptVO;
import com.lods.domain.status.model.valobj.GameStatusVO;
import com.lods.infrastructure.dao.IQuestionChoiceDao;
import com.lods.infrastructure.dao.IQuestionGapDao;
import com.lods.infrastructure.dao.po.GameStatus;
import com.lods.infrastructure.dao.po.Question;
import com.lods.infrastructure.redis.StatusOpt;
import com.lods.infrastructure.redis.StreakCount;
import com.lods.types.common.constants.Constants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class StatusRepository implements IStatusRepository {

    @Resource
    private StatusOpt statusOpt;
    @Resource
    private StreakCount streakCount;
    @Resource
    private IQuestionChoiceDao questionChoiceDao;
    @Resource
    private IQuestionGapDao questionGapDao;

    @Override
    public GameStatusVO getCurrentStatus() {

        GameStatus status = statusOpt.getCurrentStatus();

        return GameStatusVO.builder()
                .totalStreak(status.getTotalStreak())
                .maxStreak(status.getMaxStreak())
                .life(status.getLife())
                .maxLife(status.getMaxLife())
                .accountTodayRemainingCount(status.getAccountTodayRemainingCount())
                .answeringCount(status.getAnsweringCount())
                .build();
    }

    @Override
    public void updateCurrentAnswer(CurrentAnswerChangeEntity currentAnswerChangeEntity) {

        statusOpt.CurrentAnswerCount(currentAnswerChangeEntity.getIsAdd());
    }

    @Override
    public void initCurrentAnswer() {

        statusOpt.initCurrentAnswer();
    }

    @Override
    public int getRemainingCount() {

        return statusOpt.getRemainingCount();
    }

    @Override
    public void resetRemainCount() {

        statusOpt.resetRemainCount();
    }

    @Override
    public Constants.FailResult updateStreakCountByIsCorrect(boolean isCorrect) {

        return streakCount.isCorrect(isCorrect);
    }

    @Override
    public void updateFailTimes(QuestionDescriptionEntity questionDescriptionEntity) {

        if (questionDescriptionEntity.getType().equals(Constants.TypeOfQuestion.CHOICE.getCode())) {
            questionChoiceDao.updateFailTimes(questionDescriptionEntity);
        } else if (questionDescriptionEntity.getType().equals(Constants.TypeOfQuestion.GAP.getCode())) {
            questionGapDao.updateFailTimes(questionDescriptionEntity);
        }
    }

    @Override
    public void updateInterruptTimes(QuestionDescriptionEntity questionDescriptionEntity) {

        if (questionDescriptionEntity.getType().equals(Constants.TypeOfQuestion.CHOICE.getCode())) {
            questionChoiceDao.updateInterruptTimes(questionDescriptionEntity);
        } else if (questionDescriptionEntity.getType().equals(Constants.TypeOfQuestion.GAP.getCode())) {
            questionGapDao.updateInterruptTimes(questionDescriptionEntity);
        }
    }

    @Override
    public List<FailInterruptVO> getFailInterrupt() {

        List<FailInterruptVO> resList = new ArrayList<>();
        List<Question> choiceList = questionChoiceDao.queryAllFailInterrupt();

        for (Question question : choiceList) {
            FailInterruptVO failInterruptVO = FailInterruptVO.builder()
                    .type(Constants.TypeOfQuestion.CHOICE.getCode())
                    .questionId(question.getQuestionId())
                    .failTimes(question.getFailTimes())
                    .interruptTimes(question.getInterruptTimes())
                    .build();
            resList.add(failInterruptVO);
        }

        List<Question> gapList = questionGapDao.queryAllFailInterrupt();
        for (Question question : gapList) {
            FailInterruptVO failInterruptVO = FailInterruptVO.builder()
                    .type(Constants.TypeOfQuestion.GAP.getCode())
                    .questionId(question.getQuestionId())
                    .failTimes(question.getFailTimes())
                    .interruptTimes(question.getInterruptTimes())
                    .build();
            resList.add(failInterruptVO);
        }

        return resList;
    }

    @Override
    public List<FailInterruptVO> getFailInterruptIncrement(QuestionDescriptionEntity questionDescriptionEntity) {

        Question failInterruptQuestion = new Question();
        if(questionDescriptionEntity.getType().equals(Constants.TypeOfQuestion.CHOICE.getCode())){
            failInterruptQuestion = questionChoiceDao.queryAssignFailInterrupt(questionDescriptionEntity.getQuestionId());
        }else if(questionDescriptionEntity.getType().equals(Constants.TypeOfQuestion.GAP.getCode())){
            failInterruptQuestion = questionGapDao.queryAssignFailInterrupt(questionDescriptionEntity.getQuestionId());
        }

        FailInterruptVO res = FailInterruptVO.builder()
                .type(questionDescriptionEntity.getType())
                .questionId(questionDescriptionEntity.getQuestionId())
                .failTimes(failInterruptQuestion.getFailTimes())
                .interruptTimes(failInterruptQuestion.getInterruptTimes())
                .build();

        List<FailInterruptVO> result = new ArrayList<>();
        result.add(res);
        return result;
    }

}

