package top.codingshen.chatgpt.data.domain.openai.service.rule.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.codingshen.chatgpt.data.domain.openai.annotation.LogicStrategy;
import top.codingshen.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import top.codingshen.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import top.codingshen.chatgpt.data.domain.openai.model.entity.UserAccountEntity;
import top.codingshen.chatgpt.data.domain.openai.model.valobj.LogicCheckTypeVO;
import top.codingshen.chatgpt.data.domain.openai.model.valobj.UserAccountStatusVO;
import top.codingshen.chatgpt.data.domain.openai.service.rule.ILogicFilter;
import top.codingshen.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFactory;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 账户校验
 * @create 2023-10-03 17:44
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.ACCOUNT_STATUS)
public class AccountStatusFilter implements ILogicFilter<UserAccountEntity> {

    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess, UserAccountEntity data) throws Exception {
        // 账户可用，直接放行
        if (UserAccountStatusVO.AVAILABLE.equals(data.getStatus())) {
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS).data(chatProcess).build();
        }

        return RuleLogicEntity.<ChatProcessAggregate>builder()
                .info("您的账户已冻结，暂时不可使用。如果有疑问，可以联系管理员解冻账户。")
                .type(LogicCheckTypeVO.REFUSE).data(chatProcess).build();
    }

}
