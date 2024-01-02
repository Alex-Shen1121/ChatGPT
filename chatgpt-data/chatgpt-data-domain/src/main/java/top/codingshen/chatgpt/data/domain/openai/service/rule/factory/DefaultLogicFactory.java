package top.codingshen.chatgpt.data.domain.openai.service.rule.factory;

import lombok.Getter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;
import top.codingshen.chatgpt.data.domain.openai.annotation.LogicStrategy;
import top.codingshen.chatgpt.data.domain.openai.model.entity.UserAccountEntity;
import top.codingshen.chatgpt.data.domain.openai.service.rule.ILogicFilter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName DefaultLogicFactory
 * @Description 规则工厂
 * @Author alex_shen
 * @Date 2023/12/28 - 23:51
 */
@Service
public class DefaultLogicFactory {
    public Map<String, ILogicFilter<UserAccountEntity>> logicFilterMap = new ConcurrentHashMap<>();

    public DefaultLogicFactory(List<ILogicFilter<UserAccountEntity>> logicFilters) {
        logicFilters.forEach(logic -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logic.getClass(), LogicStrategy.class);
            if (strategy != null) {
                logicFilterMap.put(strategy.logicMode().getCode(), logic);
            }
        });
    }

    public Map<String, ILogicFilter<UserAccountEntity>> openLogicFilter() {
        return logicFilterMap;
    }

    /**
     * 规则逻辑枚举
     */
    @Getter
    public enum LogicModel {

        NULL("NULL", "放行不用过滤"),
        ACCESS_LIMIT("ACCESS_LIMIT", "访问次数过滤"),
        SENSITIVE_WORD("SENSITIVE_WORD", "敏感词过滤"),
        ACCOUNT_STATUS("ACCOUNT_STATUS", "账户状态过滤"),
        USER_QUOTA("USER_QUOTA", "用户额度过滤"),
        MODEL_TYPE("MODEL_TYPE", "可用模型过滤"),
        ;

        private String code;
        private String info;

        LogicModel(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
