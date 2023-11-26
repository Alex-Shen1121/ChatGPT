package top.codingshen.chatgpt.domain.security.service;

import top.codingshen.chatgpt.domain.security.service.realm.JwtRealm;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SubjectFactory;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.SubjectContext;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSubjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ShiroConfig
 * @Description shiro 配置类
 * @Author alex_shen
 * @Date 2023/11/12 - 17:08
 */
@Configuration
public class ShiroConfig {

    @Bean
    public SubjectFactory subjectFactory() {
        class JwtDefaultSubjectFactory extends DefaultWebSubjectFactory {
            @Override
            public Subject createSubject(SubjectContext context) {
                context.setSessionCreationEnabled(false);
                return super.createSubject(context);
            }
        }
        return new JwtDefaultSubjectFactory();
    }

    @Bean
    public Realm realm() {
        return new JwtRealm();
    }

    @Bean
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();

        // 设置 realm
        securityManager.setRealm(realm());

        // 关闭 shiroDAO 功能
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();

        // 不需要将 Shiro Session 中的东西存到任何地方（包括 Http Session 中）
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);

        // 禁止Subject的getSession方法
        securityManager.setSubjectFactory(subjectFactory());

        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean() {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager());
        shiroFilter.setLoginUrl("/unauthenticated");
        shiroFilter.setUnauthorizedUrl("/unauthorized");

        // 添加 jwt 过滤器
        Map<String, Filter> filterMap = new HashMap<>();
        // 设置过滤器【anon\logout可以不设置】
        filterMap.put("anon", new AnonymousFilter());
        filterMap.put("jwt", new JwtFilter());
        filterMap.put("logout", new LogoutFilter());
        shiroFilter.setFilters(filterMap);

        // 拦截器，指定方法走哪个拦截器 【login->anon】【logout->logout】【verify->jwt】
        Map<String, String> filterRuleMap = new HashMap<>();
        filterRuleMap.put("/login", "anon");
        filterRuleMap.put("/logout", "logout");
        filterRuleMap.put("/verify", "jwt");
        shiroFilter.setFilterChainDefinitionMap(filterRuleMap);

        return shiroFilter;
    }
}
