package top.codingshen.chatgpt.data.domain.order.repository;

import top.codingshen.chatgpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import top.codingshen.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import top.codingshen.chatgpt.data.domain.order.model.entity.ProductEntity;
import top.codingshen.chatgpt.data.domain.order.model.entity.ShopCartEntity;
import top.codingshen.chatgpt.data.domain.order.model.entity.UnpaidOrderEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName IOrderRepository
 * @Description description
 * @Author alex_shen
 * @Date 2024/1/3 - 23:04
 */
public interface IOrderRepository {
    /**
     * 查询未支付订单
     * @param shopCartEntity
     * @return
     */
    UnpaidOrderEntity queryUnpaidOrder(ShopCartEntity shopCartEntity);

    /**
     * 查询产品
     * @param productId 产品 Id
     * @return
     */
    ProductEntity queryProduct(Integer productId);

    /**
     * 更新订单
     * @param payOrderEntity
     */
    void updateOrderPayInfo(PayOrderEntity payOrderEntity);

    /**
     * 保存订单
     * @param aggregate
     */
    void saveOrder(CreateOrderAggregate aggregate);

    /**
     * 变更；订单支付成功
     * @param orderId
     * @param transactionId
     * @param totalAmount
     * @param payTime
     * @return
     */
    boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal totalAmount, Date payTime);

    CreateOrderAggregate queryOrder(String orderId);

    void deliverGoods(String orderId);

    List<String> queryReplenishmentOrder();
}
