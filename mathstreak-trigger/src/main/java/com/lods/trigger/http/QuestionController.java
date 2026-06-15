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
import com.lods.types.common.constants.Constants;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    @GetMapping("current-question")
    public Response<Object> currentQuestion() throws Exception{

//        lodsWebSocketHandler.sendMessage(IStatusService.getCurrentStatus());

        QuestionDataResEntity question = IQuestionService.getQuestion();

        return Response.builder()
                .code(Constants.ResponseCode.SUCCESS.getCode())
                .info(Constants.ResponseCode.SUCCESS.getMsg())
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
    @PostMapping("submit")
    public Response<Object> submit(@RequestBody SubmitDTO submitDTO) throws IOException {

        QuestionSubmitEntity submit = QuestionSubmitEntity.builder()
                .type(submitDTO.getType())
                .answerContent(submitDTO.getAnswerContent())
                .questionId(submitDTO.getQuestionId())
                .build();

        QuestionCorrectEntity res = IQuestionService.submit(submit);

        lodsWebSocketHandler.sendMessage(IStatusService.getCurrentStatus());

        return Response.builder()
                .code(Constants.ResponseCode.SUCCESS.getCode())
                .info(Constants.ResponseCode.SUCCESS.getMsg())
                .data(CheckRes.builder()
                        .isCorrect(res.getIsCorrect())
                        .correctLatexAnswer(res.getCorrectLatexAnswer())
                        .build())
                .build();
    }
}
