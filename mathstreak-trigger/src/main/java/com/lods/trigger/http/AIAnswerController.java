package com.lods.trigger.http;

import com.lods.api.dto.AIAnswerReqInfo;
import com.lods.api.response.AIAnswerMsgRes;
import com.lods.api.response.Response;
import com.lods.domain.answer.model.entity.AIAnswerGetQuestionReqEntity;
import com.lods.domain.answer.model.entity.AIAnswerMsgEntity;
import com.lods.domain.answer.service.IAIAnswerService;
import com.lods.types.common.enums.ResponseCode;
import com.lods.types.common.exception.AppException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/game/ai_answer")
public class AIAnswerController {

    @Resource
    private IAIAnswerService IAIAnswerService;

    @RequestMapping(value = "generate", method = RequestMethod.POST)
    public Response<AIAnswerMsgRes> Generate(@RequestBody AIAnswerReqInfo info) throws Exception {

        AIAnswerMsgEntity entity = IAIAnswerService.generate(AIAnswerGetQuestionReqEntity.builder()
                .type(info.getType())
                .questionId(info.getQuestionId())
                .build());

        return Response.<AIAnswerMsgRes>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(AIAnswerMsgRes.builder()
                        .msg(entity.getMsg())
                        .build())
                .build();
    }

    @RequestMapping(value = "/inside/new_generate", method = RequestMethod.POST)
    public Response<AIAnswerMsgRes> newGenerate() throws Exception {

        try{
            IAIAnswerService.newGenerateForAll();
        } catch (Exception e) {
            throw new AppException(e.getMessage(),e.getMessage());
        }

        return Response.<AIAnswerMsgRes>builder()
                .code(ResponseCode.SUCCESS.getCode())
                .info(ResponseCode.SUCCESS.getInfo())
                .data(null)
                .build();
    }
}
