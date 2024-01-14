package top.codingshen.chatgpt.data.domain.openai.repository;

import top.codingshen.chatgpt.data.domain.openai.model.entity.UserAccountEntity;

/**
 * @ClassName IOpenAiRepository
 * @Description description
 * @Author alex_shen
 * @Date 2024/1/3 - 01:06
 */
public interface IOpenAiRepository {
    UserAccountEntity queryUserAccount(String openid);

    int subAccountQuota(String openid);
}
