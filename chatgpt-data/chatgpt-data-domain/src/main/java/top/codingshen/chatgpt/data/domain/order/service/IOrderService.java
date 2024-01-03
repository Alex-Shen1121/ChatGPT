package top.codingshen.chatgpt.data.domain.order.service;

import top.codingshen.chatgpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import top.codingshen.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import top.codingshen.chatgpt.data.domain.order.model.entity.ShopCartEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    /**
     * 变更；订单支付成功
     * @param orderId 订单 id
     * @param transactionId
     * @param totalAmount
     * @param payTime
     * @return
     */
    boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal totalAmount, Date payTime);

    /**
     * 查询订单信息
     *
     * @param orderId 订单ID
     * @return 查询结果
     */
    CreateOrderAggregate queryOrder(String orderId);

    /**
     * 订单商品发货
     *
     * @param orderId 订单ID
     */
    void deliverGoods(String orderId);

    /**
     * 查询待补货订单
     */
    List<String> queryReplenishmentOrder();

    /**
     * 查询有效期内，未接收到支付回调的订单
     */
    List<String> queryNoPayNotifyOrder();

    /**
     * 查询超时15分钟，未支付订单
     */
    List<String> queryTimeoutCloseOrderList();

    /**
     * 变更；订单支付关闭
     */
    boolean changeOrderClose(String orderId);

}
