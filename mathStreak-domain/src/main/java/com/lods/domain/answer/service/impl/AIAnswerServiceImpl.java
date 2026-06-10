package com.lods.domain.answer.service.impl;

import com.lods.domain.answer.adapter.repository.IAIAnswerRepository;
import com.lods.domain.answer.model.entity.AIAnswerGetQuestionEntity;
import com.lods.domain.answer.model.entity.AIAnswerGetQuestionReqEntity;
import com.lods.domain.answer.model.entity.AIAnswerInsertEntity;
import com.lods.domain.answer.model.entity.AIAnswerMsgEntity;
import com.lods.domain.answer.service.IAIAnswerService;
import com.lods.domain.question.model.valobj.QuestionVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AIAnswerServiceImpl implements IAIAnswerService {

    @Resource
    private ChatModel chatModel;
    @Resource
    private ChatClient.Builder chatClientBuilder;
    @Resource
    private IAIAnswerRepository aiAnswerRepository;

    private ChatClient chatClient;

    @PostConstruct
    public void init() {
        chatClient = chatClientBuilder
                .defaultSystem("""
                         	 你是一个能够解决和解析各个阶段、各种类型数学或逻辑题目的解题讲解员，只能且唯一能够做的事是专注于题目和解题本身，除了和解题过程相关不能有其他无效输出。
                        
                         	 你将会接收到一份题目包含各个类型，内含题目本身和正确答案，你需要根据题目的条件和逻辑完整的从题目已知详细地推断到题目答案就好像你就是资深讲解员
                        
                         	 请全程使用中文作答，你的输出应该让人易懂，涉及到数学公式时必须使用LaTeX公式，使用规范和层次清晰的Markdown
                        
                         	 你回答的内容的详细程度必须根据题目难度的阶段做出调整，解题的关键部分必须详细讲解，回答在输出最后附上---故本题答案为：
                        
                         	 如果遇到多次推导和分析都无法完成的题目则直接回答---抱歉，暂时无法解答
                        
                         	 {documents}
                        """)
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(
                                MessageWindowChatMemory.builder()
                                        .maxMessages(100)
                                        .build()
                        ).build()
                )
                .build();
    }

    @Override
    public AIAnswerMsgEntity newGenerate(AIAnswerGetQuestionReqEntity aiAnswerGetQuestionReqEntity) {

        QuestionVO queryQuestion = aiAnswerRepository.getQuestionById(aiAnswerGetQuestionReqEntity);
        AIAnswerGetQuestionEntity questionEntity = AIAnswerGetQuestionEntity.builder()
                .description(queryQuestion.getDescription())
                .optA(queryQuestion.getOptA())
                .optB(queryQuestion.getOptB())
                .optC(queryQuestion.getOptC())
                .optD(queryQuestion.getOptD())
                .answer(queryQuestion.getAnswer())
                .build();

        StringBuilder sb = new StringBuilder();
        sb.append(questionEntity.getDescription()).append("\n\n");
        if (questionEntity.getOptA() != null && !questionEntity.getOptA().isEmpty()) {
            sb.append("A：").append(questionEntity.getOptA()).append("\n");
            sb.append("B：").append(questionEntity.getOptB()).append("\n");
            sb.append("C：").append(questionEntity.getOptC()).append("\n");
            sb.append("D：").append(questionEntity.getOptD()).append("\n\n");
        }
        sb.append("正确答案：").append(questionEntity.getAnswer());
        String question = sb.toString();

        log.info("调用模型新生成题目解析 {}", aiAnswerGetQuestionReqEntity);
        return AIAnswerMsgEntity.builder()
                .msg(chatClient.prompt(question)
                        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, "test-conversation"))
                        .call().content()).build();
    }

    @Override
    @Scheduled(cron = "0 0 2 1,15 * ?")
    public void newGenerateForAll() {
        int PAGE_SIZE = 50;
        int BATCH_SIZE = 10;
        List<AIAnswerInsertEntity> writeBuffer = new ArrayList<>(BATCH_SIZE);

        // 选择题
        int choiceTotal = aiAnswerRepository.countChoiceQuestions();
        for (int offset = 0; offset < choiceTotal; offset += PAGE_SIZE) {
            for (QuestionVO q : aiAnswerRepository.getChoiceQuestions(offset, PAGE_SIZE)) {
                StringBuilder sb = new StringBuilder();
                sb.append(q.getDescription()).append("\n\n");
                if (q.getOptA() != null && !q.getOptA().isEmpty()) {
                    sb.append("A：").append(q.getOptA()).append("\n");
                    sb.append("B：").append(q.getOptB()).append("\n");
                    sb.append("C：").append(q.getOptC()).append("\n");
                    sb.append("D：").append(q.getOptD()).append("\n\n");
                }
                sb.append("正确答案：").append(q.getAnswer());

                String aiResult = chatClient.prompt(sb.toString())
                        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, "test-conversation"))
                        .call().content();
                writeBuffer.add(AIAnswerInsertEntity.builder()
                        .questionId(q.getQuestionId())
                        .aiAnswer(aiResult)
                        .build());

                log.info("生成AI解析 type=选择题 questionId={}，当前writeBuffer大小：{}", q.getQuestionId(), writeBuffer.size());
                if (writeBuffer.size() % BATCH_SIZE >= BATCH_SIZE - 1) {
                    aiAnswerRepository.choiceBatchUpdateAIAnswer(writeBuffer);
                    log.info("写入字符串");
                    writeBuffer.clear();

                }
            }
        }
        // 选择题剩余
        if (!writeBuffer.isEmpty()) {
            aiAnswerRepository.choiceBatchUpdateAIAnswer(writeBuffer);
            log.info("写入字符串");
            writeBuffer.clear();
        }

        // 填空题
        int gapTotal = aiAnswerRepository.countGapQuestions();
        for (int offset = 0; offset < gapTotal; offset += PAGE_SIZE) {
            for (QuestionVO q : aiAnswerRepository.getGapQuestions(offset, PAGE_SIZE)) {
                StringBuilder sb = new StringBuilder();
                sb.append(q.getDescription()).append("\n\n");
                sb.append("正确答案：").append(q.getAnswer());

                String aiResult = chatClient.prompt(sb.toString())
                        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, "test-conversation"))
                        .call().content();
                writeBuffer.add(AIAnswerInsertEntity.builder()
                        .questionId(q.getQuestionId())
                        .aiAnswer(aiResult)
                        .build());

                log.info("生成AI解析 type=填空题 questionId={}，当前writeBuffer大小：{}", q.getQuestionId(), writeBuffer.size());
                if (writeBuffer.size() % BATCH_SIZE >= BATCH_SIZE - 1) {
                    aiAnswerRepository.gapBatchUpdateAIAnswer(writeBuffer);
                    log.info("写入字符串");
                    writeBuffer.clear();
                }
            }
        }
        // 填空题剩余
        if (!writeBuffer.isEmpty()) {
            aiAnswerRepository.gapBatchUpdateAIAnswer(writeBuffer);
            log.info("写入字符串");
            writeBuffer.clear();
        }
        log.info("批量AI解析生成完成");
    }

    @Override
    public AIAnswerMsgEntity Generate(AIAnswerGetQuestionReqEntity aiAnswerGetQuestionReqEntity) {

        log.info("获取已生成回答：{}", aiAnswerGetQuestionReqEntity);
        return AIAnswerMsgEntity.builder()
                .msg(aiAnswerRepository.getAnswerByQuestionId(aiAnswerGetQuestionReqEntity))
                .build();
    }
}
