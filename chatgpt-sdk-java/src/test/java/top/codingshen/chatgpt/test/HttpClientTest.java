package top.codingshen.chatgpt.test;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.json.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.junit.Test;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import top.codingshen.chatgpt.IOpenAiApi;
import top.codingshen.chatgpt.common.Constants;
import top.codingshen.chatgpt.domain.chat.ChatCompletionRequest;
import top.codingshen.chatgpt.domain.chat.ChatCompletionResponse;
import top.codingshen.chatgpt.domain.chat.Message;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName HttpClientTest
 * @Description description
 * @Author alex_shen
 * @Date 2023/11/16 - 17:46
 */
@Slf4j
public class HttpClientTest {
    @Test
    public void test_client() {
        // 设置 http Log 日志
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 构建 http 请求 client
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                // 日志拦截器
                .addInterceptor(httpLoggingInterceptor)
                // 请求拦截器
                .addInterceptor(chain -> {
                    // 原始请求
                    Request original = chain.request();

                    // 从请求中获取 token 参数,并将其添加到请求路径中
                    HttpUrl url = original.url().newBuilder()
                            .addQueryParameter("token", "")
                            .build();
                    // 设置 http 请求
                    Request request = original.newBuilder()
                            .url(url)
                            // header 中添加 chatgpt3.5 Api
                            .header(Header.AUTHORIZATION.getValue(), "Bearer " + "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")
                            .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                })
                //.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(7890)))
                .connectTimeout(450, TimeUnit.SECONDS)
                .writeTimeout(450, TimeUnit.SECONDS)
                .readTimeout(450, TimeUnit.SECONDS)
                .build();

        IOpenAiApi openAiApi = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(IOpenAiApi.class);

        Message message = Message.builder()
                .role(Constants.Role.USER)
                .content("Hello")
                .build();

        ChatCompletionRequest chatCompletion = ChatCompletionRequest
                .builder()
                .messages(Collections.singletonList(message))
                .model(ChatCompletionRequest.Model.GPT_3_5_TURBO.getCode())
                .build();

        Single<ChatCompletionResponse> chatCompletionResponseSingle = openAiApi.completions(chatCompletion);
        ChatCompletionResponse chatCompletionResponse = chatCompletionResponseSingle.blockingGet();
        chatCompletionResponse.getChoices().forEach(e -> {
            System.out.println(e.getMessage());
        });
    }

    @Test
    public void test_client_stream() throws JsonProcessingException, InterruptedException {
        // 设置 http Log 日志
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 构建 http 请求 client
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                // 日志拦截器
                .addInterceptor(httpLoggingInterceptor)
                // 请求拦截器
                .addInterceptor(chain -> {
                    // 原始请求
                    Request original = chain.request();

                    // 从请求中获取 token 参数,并将其添加到请求路径中
                    HttpUrl url = original.url().newBuilder()
                            .addQueryParameter("token", "")
                            .build();
                    // 设置 http 请求
                    Request request = original.newBuilder()
                            .url(url)
                            // header 中添加 chatgpt3.5 Api
                            .header(Header.AUTHORIZATION.getValue(), "Bearer " + "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")
                            .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                })
                //.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(7890)))
                .connectTimeout(450, TimeUnit.SECONDS)
                .writeTimeout(450, TimeUnit.SECONDS)
                .readTimeout(450, TimeUnit.SECONDS)
                .build();

        Message message = Message.builder().role(Constants.Role.USER).content("写一个 java 冒泡排序").build();
        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .messages(Collections.singletonList(message))
                .model(ChatCompletionRequest.Model.GPT_3_5_TURBO.getCode())
                .stream(true)
                .build();

        EventSource.Factory factory = EventSources.createFactory(okHttpClient);
        String requestBody = new ObjectMapper().writeValueAsString(chatCompletionRequest);

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), requestBody))
                .build();

        EventSource eventSource = factory.newEventSource(request, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                log.info("测试结果: {}", data);
            }
        });

        new CountDownLatch(1).await();
    }
}
