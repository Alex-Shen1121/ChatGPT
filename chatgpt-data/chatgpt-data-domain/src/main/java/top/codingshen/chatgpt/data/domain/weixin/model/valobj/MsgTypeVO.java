package top.codingshen.chatgpt.data.domain.weixin.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ClassName MsgTypeVO
 * @Description 微信公众号消息类型值对象，用于描述对象属性的值，为值对象
 * @Author alex_shen
 * @Date 2023/12/26 - 22:22
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum MsgTypeVO {

    EVENT("event","事件消息"),
    TEXT("text","文本消息");

    private String code;
    private String desc;

}

