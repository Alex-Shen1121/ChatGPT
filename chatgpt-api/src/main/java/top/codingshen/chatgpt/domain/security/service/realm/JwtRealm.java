package top.codingshen.chatgpt.domain.security.service.realm;

import top.codingshen.chatgpt.domain.security.model.vo.JwtToken;
import top.codingshen.chatgpt.domain.security.service.JwtUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName JwtRealm
 * @Description description
 * @Author alex_shen
 * @Date 2023/11/11 - 18:54
 */
public class JwtRealm extends AuthorizingRealm {
    private Logger logger = LoggerFactory.getLogger(JwtRealm.class);

    private static JwtUtil jwtUtil = new JwtUtil();

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 自定义授权方法
     * @param principals the primary identifying principals of the AuthorizationInfo that should be retrieved.
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // 暂时不需要实现
        return null;
    }

    /**
     * 自定义登录认证方法
     *
     * @param token the authentication token containing the user's principal and credentials.
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String jwt = (String) token.getPrincipal();
        if (jwt == null) {
            throw new NullPointerException("jwtToken 不允许为空");
        }
        // 判断
        if (!jwtUtil.isVerify(jwt)) {
            throw new UnknownAccountException();
        }
        // 可以获取username信息，并做一些处理
        String username = (String) jwtUtil.decode(jwt).get("username");
        logger.info("鉴权用户 username：{}", username);
        return new SimpleAuthenticationInfo(jwt, jwt, "JwtRealm");
    }
}
