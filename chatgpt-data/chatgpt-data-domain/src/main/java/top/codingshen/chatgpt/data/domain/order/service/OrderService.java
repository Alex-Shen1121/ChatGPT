package top.codingshen.chatgpt.data.domain.order.service;


import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.codingshen.chatgpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import top.codingshen.chatgpt.data.domain.order.model.entity.OrderEntity;
import top.codingshen.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import top.codingshen.chatgpt.data.domain.order.model.entity.ProductEntity;
import top.codingshen.chatgpt.data.domain.order.model.valobj.OrderStatusVO;
import top.codingshen.chatgpt.data.domain.order.model.valobj.PayStatusVO;
import top.codingshen.chatgpt.data.domain.order.model.valobj.PayTypeVO;
import top.codingshen.chatgpt.data.domain.order.service.channel.impl.AlipaySandboxService;
import top.codingshen.chatgpt.data.domain.order.service.channel.impl.WeixinNativePayService;
import top.codingshen.chatgpt.data.types.enums.channel.PayMethodChannel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @ClassName OrderService
 * @Description description
 * @Author alex_shen
 * @Date 2024/1/3 - 21:44
 */
@Service
public class OrderService extends AbstractOrderService {
    public OrderService(WeixinNativePayService weixinNativePayService, AlipaySandboxService alipaySandboxService) {
        super(weixinNativePayService, alipaySandboxService);
    }

    @Override
    public OrderEntity doSaveOrder(String openid, ProductEntity productEntity, PayMethodChannel payMethod) {
        OrderEntity orderEntity = new OrderEntity();
        // 数据库有幂等拦截，如果有重复的订单ID会报错主键冲突。如果是公司里一般会有专门的雪花算法UUID服务
        orderEntity.setOrderId(RandomStringUtils.randomNumeric(12));
        orderEntity.setOrderTime(new Date());
        orderEntity.setOrderStatus(OrderStatusVO.CREATE);
        orderEntity.setTotalAmount(productEntity.getPrice());
        orderEntity.setPayTypeVO(PayTypeVO.get(payMethod.getCode()));

        // 聚合信息
        CreateOrderAggregate aggregate = CreateOrderAggregate.builder()
                .openid(openid)
                .product(productEntity)
                .order(orderEntity)
                .build();

        // 保存订单；订单和支付，是2个操作。
        // 一个是数据库操作，一个是HTTP操作。所以不能一个事务处理，只能先保存订单再操作创建支付单，如果失败则需要任务补偿
        orderRepository.saveOrder(aggregate);
        return orderEntity;
    }

    /**
     * 变更；订单支付成功
     *
     * @param orderId       订单 id
     * @param transactionId 交易单号
     * @param totalAmount   订单金额
     * @param payTime       支付时间
     * @return
     */
    @Override
    public boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal totalAmount, Date payTime) {
        return orderRepository.changeOrderPaySuccess(orderId, transactionId, totalAmount, payTime);
    }

    /**
     * 查询订单信息
     *
     * @param orderId 订单ID
     * @return 查询结果
     */
    @Override
    public CreateOrderAggregate queryOrder(String orderId) {
        return orderRepository.queryOrder(orderId);
    }

    /**
     * 订单商品发货
     *
     * @param orderId 订单ID
     */
    @Override
    public void deliverGoods(String orderId) {
        orderRepository.deliverGoods(orderId);
    }

    @Override
    public List<String> queryReplenishmentOrder() {
        return orderRepository.queryReplenishmentOrder();
    }

    @Override
    public List<String> queryNoPayNotifyOrder() {
        return orderRepository.queryNoPayNotifyOrder();
    }

    @Override
    public List<String> queryTimeoutCloseOrderList() {
        return orderRepository.queryTimeoutCloseOrderList();
    }

    @Override
    public boolean changeOrderClose(String orderId) {
        return orderRepository.changeOrderClose(orderId);
    }

    @Override
    public List<ProductEntity> queryProductList() {
        return orderRepository.queryProductList();
    }

    @Override
    public Integer queryPayMethodByOrderId(String orderId) {
        return orderRepository.queryPayMethodByOrderId(orderId);
    }
}
