package top.codingshen.chatgpt.data.domain.auth.repository;

/**
 * @ClassName IAuthRepository
 * @Description 认证仓储服务
 * @Author alex_shen
 * @Date 2024/1/12 - 01:25
 */
public interface IAuthRepository {

    String getCodeUserOpenId(String code);

    void removeCodeByOpenId(String code, String openId);
}
