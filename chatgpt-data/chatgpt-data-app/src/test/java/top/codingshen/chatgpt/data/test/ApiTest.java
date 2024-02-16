package top.codingshen.chatgpt.data.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import top.codingshen.chatgpt.common.Constants;
import top.codingshen.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import top.codingshen.chatgpt.data.domain.openai.model.entity.MessageEntity;
import top.codingshen.chatgpt.data.domain.openai.service.IChatService;
import top.codingshen.chatgpt.data.types.enums.ChatGPTModel;
import top.codingshen.chatgpt.domain.chat.ChatChoice;
import top.codingshen.chatgpt.domain.chat.ChatCompletionRequest;
import top.codingshen.chatgpt.domain.chat.ChatCompletionResponse;
import top.codingshen.chatgpt.domain.chat.Message;
import top.codingshen.chatgpt.session.OpenAiSession;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @ClassName ApiTest
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/23 - 23:01
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class ApiTest {
    @Resource
    private OpenAiSession openAiSession;

    @Test
    public void test_chat_completions() {
        // 1. 创建参数
        ChatCompletionRequest chatCompletion = ChatCompletionRequest
                .builder()
                .messages(Collections.singletonList(Message.builder().role(Constants.Role.USER).content("Hello").build()))
                .model(ChatCompletionRequest.Model.GPT_3_5_TURBO.getCode())
                .build();

        // 2. 发起请求
        ChatCompletionResponse chatCompletionResponse = openAiSession.completions(chatCompletion);

        List<ChatChoice> choices = chatCompletionResponse.getChoices();
        System.out.println(choices);

        // 3. 解析结果
        chatCompletionResponse.getChoices().forEach(e -> {
            log.info("测试结果：{}", e.getMessage());
        });
    }

    @Test
    public void test_chat_completions_stream() throws JsonProcessingException, InterruptedException {
        // 1. 创建参数
        ChatCompletionRequest chatCompletion = ChatCompletionRequest
                .builder()
                .messages(Collections.singletonList(Message.builder().role(Constants.Role.USER).content("Hello").build()))
                .model(ChatCompletionRequest.Model.GPT_3_5_TURBO.getCode())
                .stream(true)
                .build();

        // 2. 发起请求
        EventSource eventSource = openAiSession.completions(chatCompletion, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                log.info("测试结果: {}", data);
            }
        });

        // 等待
        new CountDownLatch(1).await();
    }


    @Resource
    private IChatService chatService;
    @Test
    public void test_completions() throws InterruptedException {
        ResponseBodyEmitter emitter = new ResponseBodyEmitter();

        ChatProcessAggregate chatProcessAggregate = new ChatProcessAggregate();
        chatProcessAggregate.setOpenid("scy");
        chatProcessAggregate.setModel(ChatGPTModel.DALL_E_3.getCode());
        chatProcessAggregate.setMessages(Collections.singletonList(MessageEntity.builder().role(Constants.Role.USER.getCode()).content("画一个小狗").build()));

        ResponseBodyEmitter completions = chatService.completions(emitter, chatProcessAggregate);

        // 等待
        new CountDownLatch(1).await();

    }
}
