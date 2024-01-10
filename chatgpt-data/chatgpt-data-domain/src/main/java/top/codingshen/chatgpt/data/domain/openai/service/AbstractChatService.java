package top.codingshen.chatgpt.data.domain.openai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import top.codingshen.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import top.codingshen.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import top.codingshen.chatgpt.data.domain.openai.model.entity.UserAccountEntity;
import top.codingshen.chatgpt.data.domain.openai.model.repository.IOpenAiRepository;
import top.codingshen.chatgpt.data.domain.openai.model.valobj.LogicCheckTypeVO;
import top.codingshen.chatgpt.data.domain.openai.service.channel.OpenAiGroupService;
import top.codingshen.chatgpt.data.domain.openai.service.channel.impl.ChatGLMService;
import top.codingshen.chatgpt.data.domain.openai.service.channel.impl.ChatGPTService;
import top.codingshen.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFactory;
import top.codingshen.chatgpt.data.types.common.Constants;
import top.codingshen.chatgpt.data.types.enums.OpenAiChannel;
import top.codingshen.chatgpt.data.types.exception.ChatGPTException;
import top.codingshen.chatgpt.session.OpenAiSession;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AbstractChatService
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/24 - 17:55
 */
@Slf4j
public abstract class AbstractChatService implements IChatService {
    private final Map<OpenAiChannel, OpenAiGroupService> openAiGroup = new HashMap<>();

    public AbstractChatService(ChatGPTService chatGPTService, ChatGLMService chatGLMService) {
        openAiGroup.put(OpenAiChannel.ChatGPT, chatGPTService);
        openAiGroup.put(OpenAiChannel.ChatGLM, chatGLMService);
    }

    @Resource
    protected IOpenAiRepository openAIRepository;


    /**
     * chat 对话接口
     * @param emitter
     * @param chatProcess
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

            // 2. 查询账户
            UserAccountEntity userAccountEntity = openAIRepository.queryUserAccount(chatProcess.getOpenid());

            // 3. 规则过滤
            RuleLogicEntity<ChatProcessAggregate> ruleLogicEntity = this.doCheckLogic(chatProcess, userAccountEntity,
                    DefaultLogicFactory.LogicModel.ACCESS_LIMIT.getCode(),
                    DefaultLogicFactory.LogicModel.SENSITIVE_WORD.getCode(),
                    null == userAccountEntity? DefaultLogicFactory.LogicModel.NULL.getCode(): DefaultLogicFactory.LogicModel.ACCOUNT_STATUS.getCode(),
                    null == userAccountEntity? DefaultLogicFactory.LogicModel.NULL.getCode(): DefaultLogicFactory.LogicModel.MODEL_TYPE.getCode(),
                    null == userAccountEntity? DefaultLogicFactory.LogicModel.NULL.getCode(): DefaultLogicFactory.LogicModel.USER_QUOTA.getCode()
            );

            // 过滤规则不通过
            if (!LogicCheckTypeVO.SUCCESS.equals(ruleLogicEntity.getType())) {
                emitter.send(ruleLogicEntity.getInfo());
                emitter.complete();
                return emitter;
            }

            // 4. 应答处理 【ChatGPT、ChatGLM 策略模式】
            openAiGroup.get(chatProcess.getChannel()).doMessageResponse(ruleLogicEntity.getData(), emitter);

        } catch (Exception e) {
            throw new ChatGPTException(Constants.ResponseCode.UN_ERROR.getCode(), Constants.ResponseCode.UN_ERROR.getInfo());
        }

        // 5. 返回结果
        return emitter;
    }

    protected abstract RuleLogicEntity<ChatProcessAggregate> doCheckLogic(ChatProcessAggregate chatProcess, UserAccountEntity data, String... logics) throws Exception;
}
