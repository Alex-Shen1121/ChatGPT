package top.codingshen.chatgpt.data.domain.openai.service;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import top.codingshen.chatgpt.common.Constants;
import top.codingshen.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import top.codingshen.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import top.codingshen.chatgpt.data.domain.openai.model.entity.UserAccountEntity;
import top.codingshen.chatgpt.data.domain.openai.model.valobj.LogicCheckTypeVO;
import top.codingshen.chatgpt.data.domain.openai.service.channel.impl.ChatGLMService;
import top.codingshen.chatgpt.data.domain.openai.service.channel.impl.ChatGPTService;
import top.codingshen.chatgpt.data.domain.openai.service.rule.ILogicFilter;
import top.codingshen.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFactory;
import top.codingshen.chatgpt.domain.chat.ChatChoice;
import top.codingshen.chatgpt.domain.chat.ChatCompletionRequest;
import top.codingshen.chatgpt.domain.chat.ChatCompletionResponse;
import top.codingshen.chatgpt.domain.chat.Message;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName ChatService
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/24 - 17:47
 */
@Service
public class ChatService extends AbstractChatService {
    @Resource
    private DefaultLogicFactory logicFactory;

    public ChatService(ChatGPTService chatGPTService, ChatGLMService chatGLMService) {
        super(chatGPTService, chatGLMService);
    }

    @Override
    protected RuleLogicEntity<ChatProcessAggregate> doCheckLogic(ChatProcessAggregate chatProcess, UserAccountEntity data, String... logics) throws Exception {
        Map<String, ILogicFilter<UserAccountEntity>> logicFilterMap = logicFactory.openLogicFilter();
        RuleLogicEntity<ChatProcessAggregate> entity = null;

        // 规则过滤
        for (String code : logics) {
            if (DefaultLogicFactory.LogicModel.NULL.getCode().equals(code)) continue;
            entity = logicFilterMap.get(code).filter(chatProcess, data);
            if (!LogicCheckTypeVO.SUCCESS.equals(entity.getType())) return entity;
        }

        return entity != null ? entity : RuleLogicEntity.<ChatProcessAggregate>builder().type(LogicCheckTypeVO.SUCCESS).data(chatProcess).build();
    }
}
