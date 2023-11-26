package top.codingshen.chatgpt.domain.chat;

import lombok.Data;
import top.codingshen.chatgpt.domain.other.Usage;

import java.util.List;

/**
 * @ClassName ChatCompletionResponse
 * @Description description
 * @Author alex_shen
 * @Date 2023/11/17 - 00:28
 */
@Data
public class ChatCompletionResponse {
    /** ID */
    private String id;

    /** 对话 */
    private List<ChatChoice> choices;

    /** 模型 */
    private String model;

    /** 创建 */
    private long created;

    /** 对象 */
    private String object;

    /** 耗材 */
    private Usage usage;
}
