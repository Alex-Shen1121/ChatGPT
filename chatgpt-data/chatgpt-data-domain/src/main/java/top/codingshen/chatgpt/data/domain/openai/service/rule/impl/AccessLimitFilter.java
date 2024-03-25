package top.codingshen.chatgpt.data.domain.openai.service.rule.impl;

import com.google.common.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.codingshen.chatgpt.data.domain.openai.annotation.LogicStrategy;
import top.codingshen.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import top.codingshen.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import top.codingshen.chatgpt.data.domain.openai.model.entity.UserAccountEntity;
import top.codingshen.chatgpt.data.domain.openai.model.valobj.LogicCheckTypeVO;
import top.codingshen.chatgpt.data.domain.openai.service.rule.ILogicFilter;
import top.codingshen.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFactory;

import javax.annotation.Resource;

/**
 * @ClassName AccessLimitFilter
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/28 - 23:45
 */
@Slf4j
@Component
@LogicStrategy(logicMode = DefaultLogicFactory.LogicModel.ACCESS_LIMIT)
public class AccessLimitFilter implements ILogicFilter<UserAccountEntity> {
    @Value("${app.config.limit-count}")
    private Integer limitCount;
    @Value("${app.config.white-list}")
    private String whiteListStr;
    @Resource
    private Cache<String, Integer> visitCache;

    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate chatProcess, UserAccountEntity data) throws Exception {
        // 1. 白名单用户直接放行
        if (chatProcess.isWhiteList(whiteListStr)) {
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS)
                    .data(chatProcess)
                    .build();
        }

        // 2. 个人账户不为空，不做系统访问次数拦截(付费用户)
        if (data != null) {
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS)
                    .data(chatProcess)
                    .build();
        }

        // 3. 访问次数判断(临时用户)
        String openid = chatProcess.getOpenid();

        int visitCount = visitCache.get(openid, () -> 0);
        if (visitCount < limitCount) {
            log.info("临时用户: {} 今日已使用 {} 次, 剩余 {} 次.", openid, visitCount, limitCount - visitCount);
            visitCache.put(openid, visitCount + 1);
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicCheckTypeVO.SUCCESS)
                    .data(chatProcess)
                    .build();
        }

        return RuleLogicEntity.<ChatProcessAggregate>builder()
                .info("您今日的免费" + limitCount + "次，已耗尽！请购买使用次数或明日再来!")
                .type(LogicCheckTypeVO.REFUSE)
                .data(chatProcess)
                .build();
    }
}
