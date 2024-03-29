package top.codingshen.chatgpt.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import com.alibaba.fastjson.JSON;
import org.junit.Before;
import org.junit.Test;
import top.codingshen.chatgpt.common.Constants;
import top.codingshen.chatgpt.domain.chat.ChatChoice;
import top.codingshen.chatgpt.domain.chat.ChatCompletionRequest;
import top.codingshen.chatgpt.domain.chat.ChatCompletionResponse;
import top.codingshen.chatgpt.domain.chat.Message;
import top.codingshen.chatgpt.domain.images.ImageEnum;
import top.codingshen.chatgpt.domain.images.ImageRequest;
import top.codingshen.chatgpt.domain.images.ImageResponse;
import top.codingshen.chatgpt.session.Configuration;
import top.codingshen.chatgpt.session.OpenAiSession;
import top.codingshen.chatgpt.session.OpenAiSessionFactory;
import top.codingshen.chatgpt.session.defaults.DefaultOpenAiSessionFactory;

import java.io.PushbackInputStream;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;


/**
 * @ClassName ApiTest
 * @Description description
 * @Author alex_shen
 * @Date 2023/11/16 - 16:19
 */
@Slf4j
public class ApiTest {

    private OpenAiSession openAiSession;

    @Before
    public void test_OpenAiSessionFactory() {
        // 1. 配置文件
        Configuration configuration = new Configuration();
        configuration.setApiHost("https://api.pro365.top/");
        configuration.setApiKey("sk-EBvUxcMSyMV9plr21dAc860bD6Ca46F88fA883C520751692");
        //configuration.setAuthToken("xxx");
        // 2. 会话工厂
        OpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);
        // 3. 开启会话
        this.openAiSession = factory.openSession();
    }

    /**
     * 此对话模型 3.5 接近于官网体验
     */
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

    @Test
    public void test_images() throws InterruptedException {
        ImageRequest request = ImageRequest.builder()
                .prompt("画个小猫")
                .model(ImageRequest.Model.DALL_E_3.getCode())
                .size(ImageEnum.Size.size_1024.getCode())
                .build();

        ImageResponse imageResponse = openAiSession.genImages(request);

        log.info("测试结果：{}", JSON.toJSONString(imageResponse.getData()));

        // 等待
        new CountDownLatch(1).await();
    }
}
