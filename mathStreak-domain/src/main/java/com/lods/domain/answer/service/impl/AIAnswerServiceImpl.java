package com.lods.domain.answer.service.impl;

import com.lods.domain.answer.adapter.repository.IAIAnswerRepository;
import com.lods.domain.answer.model.entity.AIAnswerGetQuestionEntity;
import com.lods.domain.answer.model.entity.AIAnswerGetQuestionReqEntity;
import com.lods.domain.answer.model.entity.AIAnswerInsertEntity;
import com.lods.domain.answer.model.entity.AIAnswerMsgEntity;
import com.lods.domain.answer.service.IAIAnswerService;
import com.lods.domain.question.model.valobj.QuestionVO;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private ExecutorService aiExecutor;

    @PostConstruct
    public void init() {
        aiExecutor = Executors.newFixedThreadPool(Math.min(25, Runtime.getRuntime().availableProcessors() * 3));
        chatMemoryDelegate = MessageWindowChatMemory.builder()
                .maxMessages(100)
                .build();
        chatClient = chatClientBuilder
                .defaultSystem("""
                         	 你是一个能够解决和解析各个阶段、各种类型数学或逻辑题目的解题讲解员，只能且唯一能够做的事是专注于题目和解题本身，除了和解题过程相关不能有其他无效输出。
                        
                         	 你将会接收到一份题目包含各个类型，内含题目本身和正确答案，你需要根据题目的条件和逻辑完整的从题目已知详细地推断到题目答案就好像你就是资深讲解员
                        
                         	 请全程使用中文作答，你的输出应该让人易懂，涉及到数学公式时必须使用LaTeX公式，使用规范和层次清晰的Markdown
                        
                         	 你回答的内容的详细程度必须根据题目难度的阶段做出调整，解题的关键部分必须详细讲解，回答在输出最后附上---故本题答案为：
                        
                         	 如果遇到多次推导和分析都无法完成的题目则直接回答---抱歉，暂时无法解答
                        
                         	 {documents}
                        """)
//                .defaultAdvisors(
//                        MessageChatMemoryAdvisor.builder(
//                                new TrackedChatMemory(chatMemoryDelegate)
//                        ).build()
//                )
                .build();
    }

    @PreDestroy
    public void destroy() {
        if (aiExecutor != null) {
            aiExecutor.shutdown();
        }
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
                        .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, UUID.randomUUID().toString()))
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
            List<QuestionVO> questions = aiAnswerRepository.getChoiceQuestions(offset, PAGE_SIZE);
            // 每页题目并行调用AI，每个线程使用独立的conversationId避免ChatMemory污染
            List<CompletableFuture<AIAnswerInsertEntity>> futures = new ArrayList<>(questions.size());
            for (QuestionVO q : questions) {
                futures.add(CompletableFuture.supplyAsync(() -> {
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
                            .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, UUID.randomUUID().toString()))
                            .call().content();
                    return AIAnswerInsertEntity.builder()
                            .questionId(q.getQuestionId())
                            .aiAnswer(aiResult)
                            .build();
                }, aiExecutor));
            }
            // 在主线程收集结果，写入writeBuffer，保持原有刷写逻辑
            for (CompletableFuture<AIAnswerInsertEntity> future : futures) {
                AIAnswerInsertEntity entity = future.join();
                writeBuffer.add(entity);
                log.info("生成AI解析 type=选择题 questionId={}，当前writeBuffer大小：{}", entity.getQuestionId(), writeBuffer.size());
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
            List<QuestionVO> questions = aiAnswerRepository.getGapQuestions(offset, PAGE_SIZE);
            // 每页题目并行调用AI，每个线程使用独立的conversationId避免ChatMemory污染
            List<CompletableFuture<AIAnswerInsertEntity>> futures = new ArrayList<>(questions.size());
            for (QuestionVO q : questions) {
                futures.add(CompletableFuture.supplyAsync(() -> {
                    StringBuilder sb = new StringBuilder();
                    sb.append(q.getDescription()).append("\n\n");
                    sb.append("正确答案：").append(q.getAnswer());

                    String aiResult = chatClient.prompt(sb.toString())
//                            .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, UUID.randomUUID().toString()))
                            .call().content();
                    return AIAnswerInsertEntity.builder()
                            .questionId(q.getQuestionId())
                            .aiAnswer(aiResult)
                            .build();
                }, aiExecutor));
            }
            // 在主线程收集结果，写入writeBuffer，保持原有刷写逻辑
            for (CompletableFuture<AIAnswerInsertEntity> future : futures) {
                AIAnswerInsertEntity entity = future.join();
                writeBuffer.add(entity);
                log.info("生成AI解析 type=填空题 questionId={}，当前writeBuffer大小：{}", entity.getQuestionId(), writeBuffer.size());
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
    public AIAnswerMsgEntity generate(AIAnswerGetQuestionReqEntity aiAnswerGetQuestionReqEntity) {

        // 延迟5~10秒，使用 CompletableFuture.delayedExecutor 不阻塞其他线程
        long delaySeconds = 2 + (long) (Math.random() * 6);
        log.info("获取已生成回答：{}，等待：{}", aiAnswerGetQuestionReqEntity, delaySeconds);
        return CompletableFuture.supplyAsync(() ->
                        AIAnswerMsgEntity.builder()
                                .msg(aiAnswerRepository.getAnswerByQuestionId(aiAnswerGetQuestionReqEntity))
                                .build(),
                CompletableFuture.delayedExecutor(delaySeconds, java.util.concurrent.TimeUnit.SECONDS)
        ).join();
    }

    // ===================== Conversation 超时清理 =====================

    /** conversation 最后访问时间记录 */
    private final Map<String, Long> conversationLastAccess = new ConcurrentHashMap<>();

    /** 超时阈值：30分钟未访问则清理（单位：毫秒） */
    private static final long CONVERSATION_TIMEOUT_MS = 10 * 60 * 1000L;

    /**
     * 每10分钟清理一次超过30分钟未访问的 conversation
     */
    // 每10分钟执行一次
    @Scheduled(cron = "0 */30 * * * ?")
    public void cleanStaleConversations() {
        long now = System.currentTimeMillis();
        int cleaned = 0;
        var iterator = conversationLastAccess.entrySet().iterator();
        while (iterator.hasNext()) {
            var entry = iterator.next();
            if (now - entry.getValue() > CONVERSATION_TIMEOUT_MS) {
                chatMemoryDelegate.clear(entry.getKey());
                iterator.remove();
                cleaned++;
            }
        }
        if (cleaned > 0) {
            log.info("清理过期conversation {}个，剩余{}个", cleaned, conversationLastAccess.size());
        } else {
            log.info("没有过期conversation");
        }
    }

    /** 持有底层 MessageWindowChatMemory 引用，用于清理 */
    private MessageWindowChatMemory chatMemoryDelegate;

    /**
     * 包装 ChatMemory，自动跟踪每个 conversation 的最后访问时间
     */
    private class TrackedChatMemory implements ChatMemory {

        private final MessageWindowChatMemory delegate;

        TrackedChatMemory(MessageWindowChatMemory delegate) {
            this.delegate = delegate;
        }

        @Override
        public void add(String conversationId, List<Message> messages) {
            conversationLastAccess.put(conversationId, System.currentTimeMillis());
            delegate.add(conversationId, messages);
        }

        @Override
        public List<Message> get(String conversationId) {
            conversationLastAccess.put(conversationId, System.currentTimeMillis());
            return delegate.get(conversationId);
        }

        @Override
        public void clear(String conversationId) {
            conversationLastAccess.remove(conversationId);
            delegate.clear(conversationId);
        }
    }
}
