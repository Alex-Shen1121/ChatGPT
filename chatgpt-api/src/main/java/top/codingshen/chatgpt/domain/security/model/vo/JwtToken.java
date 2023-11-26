package top.codingshen.chatgpt.domain.security.model.vo;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @ClassName JwtToken
 * @Description description
 * @Author alex_shen
 * @Date 2023/11/12 - 17:42
 */
public class JwtToken implements AuthenticationToken {
    private String jwt;

    public JwtToken(String jwt) {
        this.jwt = jwt;
    }

    /**
     * 等同于账户
     */
    @Override
    public Object getPrincipal() {
        return jwt;
    }

    /**
     * 等同于密码
     */
    @Override
    public Object getCredentials() {
        return jwt;
    }
}
