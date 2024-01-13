package top.codingshen.chatgpt.data.types.enums.channel;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName PayMethodChannel
 * @Description description
 * @Author alex_shen
 * @Date 2024/1/8 - 13:33
 */
@Getter
@AllArgsConstructor
public enum PayMethodChannel {
    ALIPAY_SANDBOX("AlipaySandbox"),
    WEIXIN_NATIVE_PAY("WeixinNativePay");

    private final String code;

    public static PayMethodChannel getChannel(String payMethod) {
        switch (payMethod) {
            case "AlipaySandbox":
                return ALIPAY_SANDBOX;
            case "WeixinNativePay":
                return WEIXIN_NATIVE_PAY;
            default:
                return null;
        }
    }
}
