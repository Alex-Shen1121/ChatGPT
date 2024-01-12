package top.codingshen.chatgpt.data.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.codingshen.chatgpt.session.OpenAiSession;
import top.codingshen.chatgpt.session.defaults.DefaultOpenAiSessionFactory;

/**
 * @ClassName ChatGPTSdkConfig
 * @Description ChatGPTSDKConfig 工厂配置开启
 * @Author alex_shen
 * @Date 2023/12/23 - 22:09
 */
@Configuration
@EnableConfigurationProperties(ChatGPTSdkConfigProperties.class)
public class ChatGPTSdkConfig {

    @Bean(name = "chatGPTOpenAiSession")
    @ConditionalOnProperty(value = "chatgpt.sdk.config.enable", havingValue = "true", matchIfMissing = false)
    public OpenAiSession openAiSession(ChatGPTSdkConfigProperties properties) {
        // 1. 配置文件
        top.codingshen.chatgpt.session.Configuration configuration = new top.codingshen.chatgpt.session.Configuration();

        configuration.setApiHost(properties.getApiHost());
        configuration.setApiKey(properties.getApiKey());
        //configuration.setAuthToken(properties.getAuthToken());

        // 2. 会话工厂
        DefaultOpenAiSessionFactory factory = new DefaultOpenAiSessionFactory(configuration);

        // 3. 开启会话
        return factory.openSession();
    }
}
