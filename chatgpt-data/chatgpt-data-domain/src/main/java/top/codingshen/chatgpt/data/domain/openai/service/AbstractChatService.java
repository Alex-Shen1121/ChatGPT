package top.codingshen.chatgpt.data.domain.openai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import top.codingshen.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import top.codingshen.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import top.codingshen.chatgpt.data.domain.openai.model.valobj.LogicCheckTypeVO;
import top.codingshen.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFactory;
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
public abstract class AbstractChatService implements IChatService {
    @Resource
    protected OpenAiSession openAiSession;

    /**
     * chat 对话接口
     *
     * @param chatProcessAggregate
     * @return
     */
    @Override
    public ResponseBodyEmitter completions(ResponseBodyEmitter emitter, ChatProcessAggregate chatProcess) {
        try {
            // 1. 请求应答
            emitter.onCompletion(() -> {
                log.info("流式问答请求完成，使用模型：{}", chatProcess.getModel());
            });

            emitter.onError(throwable -> log.error("流式问答请求异常，使用模型：{}", chatProcess.getModel(), throwable));

            // 2. 规则过滤
            RuleLogicEntity<ChatProcessAggregate> ruleLogicEntity = this.doCheckLogic(chatProcess, DefaultLogicFactory.LogicModel.ACCESS_LIMIT.getCode(), DefaultLogicFactory.LogicModel.SENSITIVE_WORD.getCode());

            // 过滤规则不通过
            if (!LogicCheckTypeVO.SUCCESS.equals(ruleLogicEntity.getType())) {
                emitter.send(ruleLogicEntity.getInfo());
                emitter.complete();
                return emitter;
            }

            // 3. 应答处理
            this.doMessageResponse(chatProcess, emitter);
        } catch (Exception e) {
            throw new ChatGPTException(Constants.ResponseCode.UN_ERROR.getCode(), Constants.ResponseCode.UN_ERROR.getInfo());
        }

        // 3. 返回结果
        return emitter;
    }

    protected abstract RuleLogicEntity<ChatProcessAggregate> doCheckLogic(ChatProcessAggregate chatProcess, String... logics) throws Exception;

    protected abstract void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter responseBodyEmitter) throws JsonProcessingException;

}
