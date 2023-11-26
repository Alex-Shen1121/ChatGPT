package top.codingshen.chatgpt.session;

import top.codingshen.chatgpt.domain.chat.ChatCompletionRequest;
import top.codingshen.chatgpt.domain.chat.ChatCompletionResponse;

/**
 * @ClassName OpenAiSession
 * @Description OpenAi 会话接口
 * @Author alex_shen
 * @Date 2023/11/17 - 15:26
 */
public interface OpenAiSession {

    /**
     * 默认 GPT-3.5 问答模型
     *
     * @param chatCompletionRequest 请求信息
     * @return 返回结果
     */
    ChatCompletionResponse completions(ChatCompletionRequest chatCompletionRequest);

}
