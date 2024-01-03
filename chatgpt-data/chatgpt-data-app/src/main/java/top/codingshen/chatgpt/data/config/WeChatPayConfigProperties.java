package top.codingshen.chatgpt.data.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName WeChatPayConfigProperties
 * @Description 微信支付配置
 * @Author alex_shen
 * @Date 2024/1/3 - 18:20
 */
@Data
@ConfigurationProperties(prefix = "wxpay.config", ignoreInvalidFields = true)
public class WeChatPayConfigProperties {

    /** 状态；open = 开启、close 关闭 */
    private boolean enable;
    /**
     * 申请支付主体的 appid
     */
    private String appid;
    /**
     * 商户号
     */
    private String mchid;
    /**
     * 回调地址
     */
    private String notifyUrl;
    /**
     * 商户API私钥路径
     */
    private String privateKeyPath;
    /**
     * 商户证书序列号：openssl x509 -in apiclient_cert.pem -noout -serial
     */
    private String merchantSerialNumber;
    /**
     * 商户APIV3密钥
     */
    private String apiV3Key;
}
