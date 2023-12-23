package top.codingshen.chatgpt.data.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.codingshen.chatgpt.common.Constants;
import top.codingshen.chatgpt.domain.chat.ChatChoice;
import top.codingshen.chatgpt.domain.chat.ChatCompletionRequest;
import top.codingshen.chatgpt.domain.chat.ChatCompletionResponse;
import top.codingshen.chatgpt.domain.chat.Message;
import top.codingshen.chatgpt.session.OpenAiSession;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

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
}
