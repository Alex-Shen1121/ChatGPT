package top.codingshen.chatgpt.data.domain.openai.service.rule.impl;

import cn.hutool.extra.tokenizer.Word;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.codingshen.chatgpt.data.domain.openai.annotation.LogicStrategy;
import top.codingshen.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import top.codingshen.chatgpt.data.domain.openai.model.entity.MessageEntity;
import top.codingshen.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import top.codingshen.chatgpt.data.domain.openai.model.valobj.LogicCheckTypeVO;
import top.codingshen.chatgpt.data.domain.openai.service.rule.ILogicFilter;
import top.codingshen.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFactory;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName SensitiveWordFilter
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/29 - 01:39
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.SENSITIVE_WORD)
public class SensitiveWordFilter implements ILogicFilter {
    @Resource
    private SensitiveWordBs words;

    @Value("${app.config.white-list}")
    private String whiteListStr;

    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess) throws Exception {
        // 白名单用户不做敏感词处理
        if (chatProcess.isWhiteList(whiteListStr)) {
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS)
                    .data(chatProcess)
                    .build();
        }

        ChatProcessAggregate newChatProcessAggregate = new ChatProcessAggregate();
        newChatProcessAggregate.setOpenid(chatProcess.getOpenid());
        newChatProcessAggregate.setModel(chatProcess.getModel());

        // 对原 message 进行脱敏操作
        List<MessageEntity> newMessages = chatProcess.getMessages()
                .stream()
                .map(message -> {
                    String content = message.getContent();
                    // 词条脱敏
                    String replace = words.replace(content);
                    return MessageEntity.builder()
                            .role(message.getRole())
                            .name(message.getName())
                            .content(replace)
                            .build();
                })
                .collect(Collectors.toList());

        chatProcess.setMessages(newMessages);
        newChatProcessAggregate.setMessages(newMessages);

        return RuleLogicEntity.<ChatProcessAggregate>builder()
                .type(LogicCheckTypeVO.SUCCESS)
                .data(newChatProcessAggregate)
                .build();
    }

}
