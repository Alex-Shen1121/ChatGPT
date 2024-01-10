package top.codingshen.chatgpt.data.config;

import cn.bugstack.chatglm.session.OpenAiSession;
import cn.bugstack.chatglm.session.OpenAiSessionFactory;
import cn.bugstack.chatglm.session.defaults.DefaultOpenAiSessionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ChatGLMSdkConfig
 * @Description ChatGLMSDKConfig 工厂配置开启
 * @Author alex_shen
 * @Date 2024/1/10 - 16:27
 */
@Configuration
@EnableConfigurationProperties(value = ChatGLMSdkConfigProperties.class)
public class ChatGLMSdkConfig {

    @Bean(name = "chatGLMOpenAiSession")
    @ConditionalOnProperty(value = "chatglm.sdk.config.enable", havingValue = "true", matchIfMissing = false)
    public OpenAiSession openAiSession(ChatGLMSdkConfigProperties properties) {
        // 1. 配置文件
        cn.bugstack.chatglm.session.Configuration configuration = new cn.bugstack.chatglm.session.Configuration();
        configuration.setApiHost(properties.getApiHost());
        configuration.setApiSecretKey(properties.getApiSecretKey());

        // 2. 会话工厂
        OpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);

        // 3. 开启会话
        return factory.openSession();
    }
}
