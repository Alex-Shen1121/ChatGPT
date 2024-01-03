package top.codingshen.chatgpt.data.domain.order.service;

import top.codingshen.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import top.codingshen.chatgpt.data.domain.order.model.entity.ShopCartEntity;

/**
 * @ClassName IOrderService
 * @Description 订单服务
 * @Author alex_shen
 * @Date 2024/1/3 - 17:59
 */
public interface IOrderService {

    /**
     * 用户下单，通过购物车信息，返回下单后的支付单
     * @param shopCartEntity 购物车实体
     * @return 支付单实体
     */
    PayOrderEntity createOrder(ShopCartEntity shopCartEntity);
}
