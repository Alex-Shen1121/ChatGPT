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
@ConfigurationProperties(prefix = "chatglm.sdk.config", ignoreInvalidFields = true)
public class ChatGLMSdkConfigProperties {
    /**
     * 状态；open = 开启、close 关闭
     */
    private boolean enable;
    /**
     * 转发地址
     */
    private String apiHost;
    /**
     * 可以申请 sk-***
     */
    private String apiSecretKey;
}
