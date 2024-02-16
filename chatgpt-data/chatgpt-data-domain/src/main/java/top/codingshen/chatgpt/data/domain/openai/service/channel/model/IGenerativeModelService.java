package top.codingshen.chatgpt.data.domain.openai.service.channel.model;

import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import top.codingshen.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;

import java.io.IOException;

/**
 * @ClassName IGenerativeModelService
 * @Description 模型生成文字/图片接口服务
 * @Author alex_shen
 * @Date 2024/2/17 - 01:04
 */
public interface IGenerativeModelService {
    void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter responseBodyEmitter) throws IOException;
}
