package top.codingshen.chatgpt.data.domain.openai.service;

import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import top.codingshen.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;

/**
 * @ClassName IChatService
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/24 - 17:46
 */
public interface IChatService {
    /**
     * chat 对话接口
     * @param chatProcessAggregate
     * @return
     */
    ResponseBodyEmitter completions(ResponseBodyEmitter emitter, ChatProcessAggregate chatProcessAggregate);
}
