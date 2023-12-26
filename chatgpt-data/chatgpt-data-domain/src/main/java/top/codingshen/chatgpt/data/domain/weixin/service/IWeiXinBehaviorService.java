package top.codingshen.chatgpt.data.domain.weixin.service;

import top.codingshen.chatgpt.data.domain.weixin.model.entity.UserBehaviorMessageEntity;

/**
 * @ClassName IWeiXinBehaviorService
 * @Description 受理用户行为接口
 * @Author alex_shen
 * @Date 2023/12/26 - 21:35
 */
public interface IWeiXinBehaviorService {

    String acceptUserBehavior(UserBehaviorMessageEntity userBehaviorMessageEntity);

}
