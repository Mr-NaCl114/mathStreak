package com.lods.trigger.http;

import com.lods.api.dto.AIAnswerReqInfo;
import com.lods.api.response.AIAnswerMsgRes;
import com.lods.api.response.Response;
import com.lods.domain.answer.model.entity.AIAnswerMsgEntity;
import com.lods.domain.answer.model.entity.AIAnswerReqEntity;
import com.lods.domain.answer.service.IAIAnswerService;
import com.lods.types.common.constants.Constants;
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

    @RequestMapping(value = "new_generate", method = RequestMethod.POST)
    public Response<AIAnswerMsgRes> newGenerate(@RequestBody AIAnswerReqInfo info) throws Exception {

        AIAnswerMsgEntity entity = IAIAnswerService.newGenerate(AIAnswerReqEntity.builder()
                .type(info.getType())
                .questionId(info.getQuestionId())
                .build());

        return Response.<AIAnswerMsgRes>builder()
                .code(Constants.ResponseCode.SUCCESS.getCode())
                .info(Constants.ResponseCode.SUCCESS.getMsg())
                .data(AIAnswerMsgRes.builder()
                        .msg(entity.getMsg())
                        .build())
                .build();
    }
}
