package top.codingshen.chatgpt.data.domain.openai.service.rule.impl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.codingshen.chatgpt.data.domain.openai.annotation.LogicStrategy;
import top.codingshen.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import top.codingshen.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import top.codingshen.chatgpt.data.domain.openai.model.entity.UserAccountEntity;
import top.codingshen.chatgpt.data.domain.openai.repository.IOpenAiRepository;
import top.codingshen.chatgpt.data.domain.openai.model.valobj.LogicCheckTypeVO;
import top.codingshen.chatgpt.data.domain.openai.service.rule.ILogicFilter;
import top.codingshen.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFactory;

import javax.annotation.Resource;

/**
 * @author Fuzhengwei bugstack.cn @小傅哥
 * @description 用户额度扣减规则过滤
 * @create 2023-10-03 16:48
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.USER_QUOTA)
public class UserQuotaFilter implements ILogicFilter<UserAccountEntity> {

    @Resource
    private IOpenAiRepository openAiRepository;

    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess, UserAccountEntity data) throws Exception {
        if (data.getSurplusQuota() > 0) {
            // 扣减账户额度；因为是个人账户数据，无资源竞争，所以直接使用数据库也可以。但为了效率，也可以优化为 Redis 扣减。
            int updateCount = openAiRepository.subAccountQuota(data.getOpenid());
            if (0 != updateCount) {
                return RuleLogicEntity.<ChatProcessAggregate>builder()
                        .type(LogicCheckTypeVO.SUCCESS).data(chatProcess).build();
            }

            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .info("个人账户，总额度【" + data.getTotalQuota() + "】次，已耗尽！")
                    .type(LogicCheckTypeVO.REFUSE).data(chatProcess).build();
        }

        return RuleLogicEntity.<ChatProcessAggregate>builder()
                .info("个人账户，总额度【" + data.getTotalQuota() + "】次，已耗尽！")
                .type(LogicCheckTypeVO.REFUSE).data(chatProcess).build();
    }

}
