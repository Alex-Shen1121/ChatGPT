package top.codingshen.chatgpt;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import top.codingshen.chatgpt.domain.chat.ChatCompletionRequest;
import top.codingshen.chatgpt.domain.chat.ChatCompletionResponse;
import top.codingshen.chatgpt.domain.images.ImageRequest;
import top.codingshen.chatgpt.domain.images.ImageResponse;

/**
 * @ClassName IOpenAiApi
 * @Description description
 * @Author alex_shen
 * @Date 2023/11/16 - 23:56
 */
public interface IOpenAiApi {

    String v1_chat_completions = "v1/chat/completions";
    /**
     * chatgpt-3.5 chat 模型
     * @param chatCompletionRequest 请求信息
     * @return 返回结果
     */
    @POST(v1_chat_completions)
    Single<ChatCompletionResponse> completions(@Body ChatCompletionRequest chatCompletionRequest);

    /**
     * 生成图片
     * @param imageRequest 图片对象
     * @return 应答结果
     */
    String v1_images_generations = "v1/images/generations";
    @POST(v1_images_generations)
    Single<ImageResponse> genImages(@Body ImageRequest imageRequest);
}
