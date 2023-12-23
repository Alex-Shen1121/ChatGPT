package top.codingshen.chatgpt.domain.validate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.codingshen.chatgpt.application.IWeiXinValidateService;
import top.codingshen.chatgpt.infrastructure.util.sdk.SignatureUtil;

/**
 * @ClassName WeiXinValidateImpl
 * @Description description
 * @Author alex_shen
 * @Date 2023/11/26 - 22:18
 */
@Service
public class WeiXinValidateImpl implements IWeiXinValidateService{
    @Value("${wx.config.token}")
    private String token;

    @Override
    public boolean checkSign(String signature, String timestamp, String nonce) {
        return SignatureUtil.check(token, signature, timestamp, nonce);
    }
}
