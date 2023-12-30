package top.codingshen.chatgpt.data.domain.openai.service.rule;

import top.codingshen.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import top.codingshen.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;

import java.util.concurrent.ExecutionException;

/**
 * @ClassName ILogicFilter
 * @Description 规则过滤接口
 * @Author alex_shen
 * @Date 2023/12/28 - 23:41
 */
public interface ILogicFilter {
    RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess) throws Exception;
}
