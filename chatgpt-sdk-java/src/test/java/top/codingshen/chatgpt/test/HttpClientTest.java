package top.codingshen.chatgpt.test;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.json.JSON;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
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
import java.util.concurrent.TimeUnit;

/**
 * @ClassName HttpClientTest
 * @Description description
 * @Author alex_shen
 * @Date 2023/11/16 - 17:46
 */
@Slf4j
public class HttpClientTest {
    public static void main(String[] args) {
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
                            .addQueryParameter("token", "gjjug")
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
                .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(7890)))
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
                .content("写一个java冒泡排序")
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
}
