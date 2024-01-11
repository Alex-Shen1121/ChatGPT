package top.codingshen.chatgpt.data.domain.weixin.repository;

/**
 * @ClassName IWeiXinRepository
 * @Description 微信仓储服务
 * @Author alex_shen
 * @Date 2024/1/12 - 00:31
 */
public interface IWeiXinRepository {

    /**
     * 生成验证码
     * @param openId 个人的openId
     * @return
     */
    String genCode(String openId);
}
