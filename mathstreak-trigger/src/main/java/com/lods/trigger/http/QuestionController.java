package com.lods.trigger.http;

import com.lods.api.IQuestionController;
import com.lods.api.dto.SubmitDTO;
import com.lods.api.response.CheckRes;
import com.lods.api.response.QuestionRes;
import com.lods.api.response.Response;
import com.lods.domain.question.model.entity.QuestionCorrectEntity;
import com.lods.domain.question.model.entity.QuestionDataResEntity;
import com.lods.domain.question.model.entity.QuestionSubmitEntity;
import com.lods.domain.question.service.IQuestionService;
import com.lods.domain.status.service.IStatusService;
import com.lods.trigger.listener.LodsWebSocketHandler;
import com.lods.types.common.enums.ResponseCode;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/game/")
public class QuestionController implements IQuestionController {

    @Resource
    private IQuestionService IQuestionService;
    @Resource
    private IStatusService IStatusService;
    @Resource
    private LodsWebSocketHandler lodsWebSocketHandler;

    @Override
    @RequestMapping(value = "current_question", method = RequestMethod.GET)
    public Response<Object> currentQuestion() throws Exception {

        if (IStatusService.getRemainingCount() == 0) {
            return Response.builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(ResponseCode.REMAIN_COUNT_ZERO)
                    .build();
        }

        QuestionDataResEntity question = IQuestionService.getQuestion();

        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(QuestionRes.builder()
                        .type(question.getType())
                        .questionId(question.getQuestionId())
                        .description(question.getDescription())
                        .optA(question.getOptA())
                        .optB(question.getOptB())
                        .optC(question.getOptC())
                        .optD(question.getOptD())
                        .difficultyLevel(question.getDifficultyLevel())
                        .build())
                .build();
    }

    @Override
    @RequestMapping(value = "submit", method = RequestMethod.POST)
    public Response<Object> submit(@RequestBody SubmitDTO submitDTO) throws Exception {

        QuestionSubmitEntity submit = QuestionSubmitEntity.builder()
                .type(submitDTO.getType())
                .answerContent(submitDTO.getAnswerContent())
                .questionId(submitDTO.getQuestionId())
                .build();

        QuestionCorrectEntity res = IQuestionService.submit(submit);

        lodsWebSocketHandler.sendMessage(IStatusService.getCurrentStatus());

        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(CheckRes.builder()
                        .isCorrect(res.getIsCorrect())
                        .correctLatexAnswer(res.getCorrectLatexAnswer())
                        .sign(res.getSign())
                        .build())
                .build();
    }

    @Override
    @RequestMapping(value = "reset_count", method = RequestMethod.POST)
    public Response<Object> resetRemainCount() throws Exception {

        IStatusService.resetRemainCount();

        lodsWebSocketHandler.sendMessage(IStatusService.getCurrentStatus());

        return Response.builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(null)
                .build();
    }


}
