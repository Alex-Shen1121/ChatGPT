package top.codingshen.chatgpt.data.types.enums.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName OpenAiChannel
 * @Description OpenAi 渠道
 * @Author alex_shen
 * @Date 2024/1/10 - 15:39
 */
@Getter
@AllArgsConstructor
public enum OpenAiChannel {
    ChatGLM("ChatGLM"),
    ChatGPT("ChatGPT"),

    ;
    private final String code;

    public static OpenAiChannel getChannel(String model) {
        if (model.toLowerCase().contains("gpt"))
            return OpenAiChannel.ChatGPT;
        if (model.toLowerCase().contains("glm"))
            return OpenAiChannel.ChatGLM;
        return null;
    }
}
