package top.codingshen.chatgpt.data.domain.weixin.service.validate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.codingshen.chatgpt.data.domain.weixin.service.IWeiXinValidateService;
import top.codingshen.chatgpt.data.types.sdk.weixin.SignatureUtil;

/**
 * @ClassName WeiXinValidateService
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/26 - 21:37
 */
@Service
public class WeiXinValidateService implements IWeiXinValidateService {

    @Value("${wx.config.token}")
    private String token;

    @Override
    public boolean checkSign(String signature, String timestamp, String nonce) {
        return SignatureUtil.check(token, signature, timestamp, nonce);
    }
}
