package top.codingshen.chatgpt.session.defaults;

import io.reactivex.Single;
import top.codingshen.chatgpt.IOpenAiApi;
import top.codingshen.chatgpt.domain.chat.ChatCompletionRequest;
import top.codingshen.chatgpt.domain.chat.ChatCompletionResponse;
import top.codingshen.chatgpt.session.OpenAiSession;

/**
 * @ClassName DefaultOpenAiSession
 * @Description description
 * @Author alex_shen
 * @Date 2023/11/17 - 15:31
 */

public class DefaultOpenAiSession implements OpenAiSession {
    private IOpenAiApi openAiApi;

    public DefaultOpenAiSession(IOpenAiApi openAiApi) {
        this.openAiApi = openAiApi;
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
}
