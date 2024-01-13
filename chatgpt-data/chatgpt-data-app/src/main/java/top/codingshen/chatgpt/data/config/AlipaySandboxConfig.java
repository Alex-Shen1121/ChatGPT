package top.codingshen.chatgpt.data.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName AlipaySandboxConfig
 * @Description 支付宝沙箱配置
 * @Author alex_shen
 * @Date 2024/1/13 - 15:28
 */
@Configuration
@EnableConfigurationProperties(AliPaySandboxConfigProperties.class)
public class AlipaySandboxConfig {
    @Bean(name = "alipayClient")
    @ConditionalOnProperty(value = "alipay.sandbox.config.enable", havingValue = "true", matchIfMissing = false)
    public AlipayClient alipayClient (AliPaySandboxConfigProperties properties) {
        return new DefaultAlipayClient(properties.getGatewayUrl(),
                properties.getApp_id(),
                properties.getMerchant_private_key(),
                properties.getFormat(),
                properties.getCharset(),
                properties.getAlipay_public_key(),
                properties.getSign_type());
    }

}
