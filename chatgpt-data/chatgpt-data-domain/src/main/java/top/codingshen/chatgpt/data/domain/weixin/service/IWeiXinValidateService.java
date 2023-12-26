package top.codingshen.chatgpt.data.domain.weixin.service;

/**
 * @ClassName IWeiXinValidateService
 * @Description 验签接口
 * @Author alex_shen
 * @Date 2023/12/26 - 21:34
 */
public interface IWeiXinValidateService {

    boolean checkSign(String signature, String timestamp, String nonce);

}
