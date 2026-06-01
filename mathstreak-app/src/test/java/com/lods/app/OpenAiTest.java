package com.lods.app;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class OpenAiTest {

    private ChatModel chatModel;

    private ChatClient chatClient;

    @Resource
    private PgVectorStore vectorStore;



    @BeforeEach
    public void init(@Value("${spring.ai.openai.api-key}") String apiKey) {

        chatModel = OpenAiChatModel.builder()
                .options(OpenAiChatOptions.builder()
                        .model("deepseek-v4-pro")
                            .apiKey(apiKey)
                            .baseUrl("https://api.deepseek.com/")
//                        .toolCallbacks(new SyncMcpToolCallbackProvider(stdioMcpClient(), sseMcpClient01(), sseMcpClient02()).getToolCallbacks())
                        .build())
                .build();

        chatClient = ChatClient.builder(chatModel)
                .defaultSystem("""
                        	 你是一个能够解决和解析各个阶段、各种类型数学题目的解题讲解员，只能且唯一能够做的事是专注于题目和解题本身，除了和解题过程相关不能有其他无效输出。
                        
                        	 你将会接收到一份题目包含各个类型，内含题目本身和正确答案，你需要根据题目的条件和逻辑完整的从题目已知详细地推断到题目答案就好像你就是资深讲解员
                        
                        	 请全程使用中文作答，你的输出应该让人易懂，涉及到数学公式时必须使用LaTeX公式，解题的关键部分必须详细讲解，在输出最后附上---故本题答案为：
                        
                        	 {documents}
                        """)
//                .defaultToolCallbacks(new SyncMcpToolCallbackProvider(stdioMcpClient(), sseMcpClient01(), sseMcpClient02()).getToolCallbacks())
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(
                                MessageWindowChatMemory.builder()
                                        .maxMessages(100)
                                        .build()
                        ).build()
//                        new RagAnswerAdvisor(vectorStore, SearchRequest.builder()
//                                .topK(5)
//                                .filterExpression("knowledge == 'article-prompt-words'")
//                                .build()),
//                        SimpleLoggerAdvisor.builder().build())
                )
                .build();
    }

    @Test
    public void test() {
        String userInput = "再次验证过程";
        System.out.println("\n>>> QUESTION: " + userInput);
        System.out.println("\n>>> ASSISTANT: " + chatClient
                .prompt(userInput)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, "test-conversation"))
//                .system(s -> s.param("current_date", LocalDate.now().toString()))
                .call().content());
    }

//    @Test
//    public void upload() {
//        // textResource、articlePromptWordsResource
//        TikaDocumentReader reader = new TikaDocumentReader(articlePromptWordsResource);
//
//        List<Document> documents = reader.get();
//        List<Document> documentSplitterList = tokenTextSplitter.apply(documents);
//
//        documentSplitterList.forEach(doc -> doc.getMetadata().put("knowledge", "article-prompt-words"));
//
//        pgVectorStore.accept(documentSplitterList);
//
//        log.info("上传完成");
//    }
}
