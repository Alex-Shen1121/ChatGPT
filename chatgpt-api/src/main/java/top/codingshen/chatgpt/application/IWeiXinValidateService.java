package top.codingshen.chatgpt.application;

/**
 * @ClassName IWeiXinValidateService
 * @Description 微信验证接口
 * @Author alex_shen
 * @Date 2023/11/26 - 22:17
 */
public interface IWeiXinValidateService {

    boolean checkSign(String signature, String timestamp, String nonce);

}
