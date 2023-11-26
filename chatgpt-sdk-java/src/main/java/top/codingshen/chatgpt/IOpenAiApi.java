package top.codingshen.chatgpt;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import top.codingshen.chatgpt.domain.chat.ChatCompletionRequest;
import top.codingshen.chatgpt.domain.chat.ChatCompletionResponse;

/**
 * @ClassName IOpenAiApi
 * @Description description
 * @Author alex_shen
 * @Date 2023/11/16 - 23:56
 */
public interface IOpenAiApi {
    /**
     * chatgpt-3.5 chat 模型
     * @param chatCompletionRequest 请求信息
     * @return 返回结果
     */
    @POST("v1/chat/completions")
    Single<ChatCompletionResponse> completions(@Body ChatCompletionRequest chatCompletionRequest);
}
