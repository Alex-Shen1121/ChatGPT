package top.codingshen.chatgpt.data.domain.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.codingshen.chatgpt.data.domain.order.model.valobj.PayStatusVO;


/**
 * @ClassName PayOrderEntity
 * @Description 支付单实体
 * @Author alex_shen
 * @Date 2024/1/3 - 18:00
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayOrderEntity {
    // 用户 id
    private String openid;

    // 订单 id
    private String orderId;

    // 支付地址: 创建支付后,获得的 URL 地址
    private String payUrl;

    // 支付状态: 0-等待支付、1-支付完成、2-支付失败、3-放弃支付
    private PayStatusVO payStatus;

}
