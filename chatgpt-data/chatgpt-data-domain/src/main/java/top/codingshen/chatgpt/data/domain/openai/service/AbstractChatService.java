package top.codingshen.chatgpt.data.domain.openai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import top.codingshen.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import top.codingshen.chatgpt.data.types.common.Constants;
import top.codingshen.chatgpt.data.types.exception.ChatGPTException;
import top.codingshen.chatgpt.session.OpenAiSession;

import javax.annotation.Resource;

/**
 * @ClassName AbstractChatService
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/24 - 17:55
 */
@Slf4j
public abstract class AbstractChatService implements IChatService{
    @Resource
    protected OpenAiSession openAiSession;

    /**
     * chat 对话接口
     *
     * @param chatProcessAggregate
     * @return
     */
    @Override
    public ResponseBodyEmitter completions(ChatProcessAggregate chatProcess) {
        // 1. 校验权限
        if (!"cuhk".equals(chatProcess.getToken())) {
            throw new ChatGPTException(Constants.ResponseCode.TOKEN_ERROR.getCode(), Constants.ResponseCode.TOKEN_ERROR.getInfo());
        }

        // 2. 请求应答
        ResponseBodyEmitter emitter = new ResponseBodyEmitter(3 * 60 * 1000L);
        emitter.onCompletion(() -> {
            log.info("流式问答请求完成，使用模型：{}", chatProcess.getModel());
        });

        emitter.onError(throwable -> log.error("流式问答请求异常，使用模型：{}", chatProcess.getModel(), throwable));

        // 3. 应答处理
        try {
            this.doMessageResponse(chatProcess, emitter);
        } catch (Exception e) {
            throw new ChatGPTException(Constants.ResponseCode.UN_ERROR.getCode(), Constants.ResponseCode.UN_ERROR.getInfo());
        }

        // 4. 返回结果
        return emitter;
    }


    protected abstract void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter responseBodyEmitter) throws JsonProcessingException;

}
