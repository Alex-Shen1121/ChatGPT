package top.codingshen.chatgpt.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import top.codingshen.chatgpt.common.Constants;
import top.codingshen.chatgpt.domain.chat.ChatCompletionRequest;
import top.codingshen.chatgpt.domain.chat.ChatCompletionResponse;
import top.codingshen.chatgpt.domain.chat.Message;
import top.codingshen.chatgpt.session.Configuration;
import top.codingshen.chatgpt.session.OpenAiSession;
import top.codingshen.chatgpt.session.OpenAiSessionFactory;
import top.codingshen.chatgpt.session.defaults.DefaultOpenAiSessionFactory;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @ClassName ClientTest
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/21 - 20:45
 */
public class ClientTest {
    public static void main(String[] args) throws JsonProcessingException {
        // 1. 配置文件；
        // 1.1 官网原始 apiHost https://api.openai.com/ - 官网的Key可直接使用
        // 1.2 三方公司 apiHost https://pro-share-aws-api.zcyai.com/ - 需要找我获得 Key
        Configuration configuration = new Configuration();
        configuration.setApiHost("https://api.openai.com/");
        configuration.setApiKey("sk-xxx");
        //configuration.setAuthToken("xxx");

        // 2. 会话工厂
        OpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);
        OpenAiSession openAiSession = factory.openSession();

        System.out.println("我是 OpenAI ChatGPT，请输入你的问题：");

        ChatCompletionRequest chatCompletion = ChatCompletionRequest
                .builder()
                //.stream(true)
                .messages(new ArrayList<>())
                .model(ChatCompletionRequest.Model.GPT_3_5_TURBO.getCode())
                .maxTokens(1024)
                .build();

        // 3. 等待输入
        //Scanner scanner = new Scanner(System.in);
        //while (scanner.hasNextLine()) {
        //    String text = scanner.nextLine();
        //
        //    chatCompletion.getMessages().add(Message.builder().role(Constants.Role.USER).content(text).build());
        //    EventSource eventSource = openAiSession.completions(chatCompletion, new EventSourceListener() {
        //        @Override
        //        public void onEvent(EventSource eventSource, String id, String type, String data) {
        //            System.out.println(data);
        //        }
        //
        //        @Override
        //        public void onFailure(EventSource eventSource, Throwable t, Response response) {
        //            System.out.println(response.code());
        //        }
        //
        //        @Override
        //        public void onClosed(EventSource eventSource) {
        //            System.out.println("请输入你的问题：");
        //        }
        //    });
        //}
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String text = scanner.nextLine();
            chatCompletion.getMessages().add(Message.builder().role(Constants.Role.USER).content(text).build());
            ChatCompletionResponse chatCompletionResponse = openAiSession.completions(chatCompletion);
            chatCompletion.getMessages().add(Message.builder().role(Constants.Role.USER).content(chatCompletionResponse.getChoices().get(0).getMessage().getContent()).build());
            // 输出结果
            System.out.println(chatCompletionResponse.getChoices().get(0).getMessage().getContent());
            System.out.println("请输入你的问题：");
        }
    }
}
