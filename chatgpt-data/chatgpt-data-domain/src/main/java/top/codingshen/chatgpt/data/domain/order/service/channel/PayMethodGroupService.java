package top.codingshen.chatgpt.data.domain.order.service.channel;

import top.codingshen.chatgpt.data.domain.order.model.entity.PayOrderEntity;

import java.math.BigDecimal;

/**
 * @ClassName PayMethodGroupService
 * @Description 支付方式服务组
 * @Author alex_shen
 * @Date 2024/1/13 - 14:35
 */
public interface PayMethodGroupService {
    PayOrderEntity doPrepayOrder(String openid, String orderId, String productName, BigDecimal amountTotal);

}
