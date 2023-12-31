package top.codingshen.chatgpt.session.defaults;

import cn.hutool.http.ContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import top.codingshen.chatgpt.IOpenAiApi;
import top.codingshen.chatgpt.common.Constants;
import top.codingshen.chatgpt.domain.chat.ChatCompletionRequest;
import top.codingshen.chatgpt.domain.chat.ChatCompletionResponse;
import top.codingshen.chatgpt.domain.images.ImageRequest;
import top.codingshen.chatgpt.domain.images.ImageResponse;
import top.codingshen.chatgpt.session.Configuration;
import top.codingshen.chatgpt.session.OpenAiSession;

/**
 * @ClassName DefaultOpenAiSession
 * @Description description
 * @Author alex_shen
 * @Date 2023/11/17 - 15:31
 */

public class DefaultOpenAiSession implements OpenAiSession {
    /**
     * 配置信息
     **/
    private final Configuration configuration;

    /**
     * OpenAi 接口
     **/
    private final IOpenAiApi openAiApi;

    /**
     * 工厂事件
     **/
    private final EventSource.Factory factory;

    public DefaultOpenAiSession(Configuration configuration) {
        this.configuration = configuration;

        this.openAiApi = configuration.getOpenAiApi();
        this.factory = configuration.createRequestFactory();
    }

    /**
     * 默认 GPT-3.5 问答模型
     *
     * @param chatCompletionRequest 请求信息
     * @return 返回结果
     */
    @Override
    public ChatCompletionResponse completions(ChatCompletionRequest chatCompletionRequest) {
        return this.openAiApi.completions(chatCompletionRequest).blockingGet();
    }

    /**
     * 默认 GPT-3.5 流式问答模型
     *
     * @param chatCompletionRequest
     * @param eventSourceListener
     * @return
     */
    @Override
    public EventSource completions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException {
        return completions(Constants.NULL, Constants.NULL, chatCompletionRequest, eventSourceListener);
    }

    /**
     * @param apiHostByUser 自定义 Host
     * @param apiKeyByUser  自定义 ApiKey
     * @param chatCompletionRequest 请求信息
     * @param eventSourceListener   实现监听；通过监听的 onEvent 方法接收数据
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public EventSource completions(String apiHostByUser, String apiKeyByUser, ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException {

        // 核心参数校验
        if (!chatCompletionRequest.isStream()) {
            throw new RuntimeException("illegal parameter stream is false!");
        }

        // 构建请求信息
        String apiHost = Constants.NULL.equals(apiHostByUser) ? configuration.getApiHost() : apiHostByUser;
        String apiKey = Constants.NULL.equals(apiKeyByUser) ? configuration.getApiKey() : apiKeyByUser;

        Request request = new Request.Builder()
                .url(apiHost.concat(openAiApi.v1_chat_completions))
                .addHeader("apiKey", apiKey)
                .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), new ObjectMapper().writeValueAsString(chatCompletionRequest)))
                .build();

        // 返回事件结果
        return factory.newEventSource(request, eventSourceListener);
    }

    /**
     * 生成图片
     *
     * @param imageRequest 图片描述
     * @return 应答结果
     */
    @Override
    public ImageResponse genImages(ImageRequest imageRequest) {
        return openAiApi.genImages(imageRequest).blockingGet();
    }
}
