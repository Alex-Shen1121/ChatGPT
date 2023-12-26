package top.codingshen.chatgpt.data.domain.auth.service;

import top.codingshen.chatgpt.data.domain.auth.model.entity.AuthStateEntity;

/**
 * @ClassName IAuthService
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/26 - 01:32
 */
public interface IAuthService {

    /**
     * 登录验证
     * @param code 验证码
     * @return Token
     */
    AuthStateEntity doLogin(String code);

    boolean checkToken(String token);

}
