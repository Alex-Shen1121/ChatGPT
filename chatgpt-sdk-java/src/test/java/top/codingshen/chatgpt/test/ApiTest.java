package top.codingshen.chatgpt.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import top.codingshen.chatgpt.common.Constants;
import top.codingshen.chatgpt.domain.chat.ChatChoice;
import top.codingshen.chatgpt.domain.chat.ChatCompletionRequest;
import top.codingshen.chatgpt.domain.chat.ChatCompletionResponse;
import top.codingshen.chatgpt.domain.chat.Message;
import top.codingshen.chatgpt.session.Configuration;
import top.codingshen.chatgpt.session.OpenAiSession;
import top.codingshen.chatgpt.session.OpenAiSessionFactory;
import top.codingshen.chatgpt.session.defaults.DefaultOpenAiSessionFactory;

import java.io.PushbackInputStream;
import java.util.Collections;
import java.util.List;

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
        configuration.setApiHost("https://api.openai.com/");
        configuration.setApiKey("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");
        configuration.setAuthToken("xxx");
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

        //List<ChatChoice> choices = chatCompletionResponse.getChoices();
        //System.out.println(choices);

        // 3. 解析结果
        chatCompletionResponse.getChoices().forEach(e -> {
            log.info("测试结果：{}", e.getMessage());
        });
    }

}
