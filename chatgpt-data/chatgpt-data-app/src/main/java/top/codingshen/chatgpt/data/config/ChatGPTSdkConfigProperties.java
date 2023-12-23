package top.codingshen.chatgpt.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName ChatGPTSdkConfigProperties
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/23 - 21:56
 */
@Data
@ConfigurationProperties(prefix = "chatgpt.sdk.config", ignoreInvalidFields = true)
public class ChatGPTSdkConfigProperties {

    private String apiHost;

    private String apiKey;

    private String authToken;
}
