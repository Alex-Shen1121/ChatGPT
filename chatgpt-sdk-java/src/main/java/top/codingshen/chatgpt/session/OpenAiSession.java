package top.codingshen.chatgpt.session;

import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import top.codingshen.chatgpt.domain.chat.ChatCompletionRequest;
import top.codingshen.chatgpt.domain.chat.ChatCompletionResponse;
import top.codingshen.chatgpt.domain.images.ImageRequest;
import top.codingshen.chatgpt.domain.images.ImageResponse;

/**
 * @ClassName OpenAiSession
 * @Description OpenAi 会话接口
 * @Author alex_shen
 * @Date 2023/11/17 - 15:26
 */
public interface OpenAiSession {

    /**
     * 问答模型 GPT-3.5/4.0
     *
     * @param chatCompletionRequest 请求信息
     * @return 返回结果
     */
    ChatCompletionResponse completions(ChatCompletionRequest chatCompletionRequest);

    /**
     * 问答模型 GPT-3.5/4.0 & 流式反馈
     *
     * @param chatCompletionRequest 请求信息
     * @param eventSourceListener   实现监听；通过监听的 onEvent 方法接收数据
     * @return 返回结果
     */
    EventSource completions(ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException;

    /**
     * 问答模型 GPT-3.5/4.0 & 流式反馈 & 自定义 host/Api
     * @param apiHostByUser 自定义 Host
     * @param apiKeyByUser  自定义 ApiKey
     * @param chatCompletionRequest 请求信息
     * @param eventSourceListener   实现监听；通过监听的 onEvent 方法接收数据
     * @return 返回结果
     */
    EventSource completions(String apiHostByUser, String apiKeyByUser, ChatCompletionRequest chatCompletionRequest, EventSourceListener eventSourceListener) throws JsonProcessingException;

    /**
     * 生成图片
     *
     * @param imageRequest 图片描述
     * @return 应答结果
     */
    ImageResponse genImages(ImageRequest imageRequest);
}
