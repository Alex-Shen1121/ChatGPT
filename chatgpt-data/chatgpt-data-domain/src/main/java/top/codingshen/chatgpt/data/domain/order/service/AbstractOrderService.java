package top.codingshen.chatgpt.data.domain.order.service;

import lombok.extern.slf4j.Slf4j;
import top.codingshen.chatgpt.data.domain.order.model.entity.*;
import top.codingshen.chatgpt.data.domain.order.model.valobj.PayStatusVO;
import top.codingshen.chatgpt.data.domain.order.repository.IOrderRepository;
import top.codingshen.chatgpt.data.types.common.Constants;
import top.codingshen.chatgpt.data.types.exception.ChatGPTException;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @ClassName AbstractOrderService
 * @Description description
 * @Author alex_shen
 * @Date 2024/1/3 - 21:44
 */
@Slf4j
public abstract class AbstractOrderService implements IOrderService {

    @Resource
    protected IOrderRepository orderRepository;

    /**
     * 用户下单，通过购物车信息，返回下单后的支付单
     *
     * @param shopCartEntity 购物车实体
     * @return 支付单实体
     */
    @Override
    public PayOrderEntity createOrder(ShopCartEntity shopCartEntity) {
        // 0. 基础信息
        String openid = shopCartEntity.getOpenid();
        Integer productId = shopCartEntity.getProductId();

        // 1. 查询有效的为支付订单, 如果存在直接返回微信支付 Native CodeUrl
        UnpaidOrderEntity unpaidOrderEntity = orderRepository.queryUnpaidOrder(shopCartEntity);
        // 存在订单 + 状态:等待支付 + 已生成微信支付 URL
        if (unpaidOrderEntity != null && PayStatusVO.WAIT.equals(unpaidOrderEntity.getPayStatus()) && null != unpaidOrderEntity.getPayUrl()) {
            log.info("创建订单-存在, 已生成微信支付, 返回 openid: {}, orderId: {}, payUrl: {}", openid,unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getPayUrl());
            return PayOrderEntity.builder()
                    .openid(openid)
                    .orderId(unpaidOrderEntity.getOrderId())
                    .payUrl(unpaidOrderEntity.getPayUrl())
                    .payStatus(unpaidOrderEntity.getPayStatus())
                    .build();
        }
        // 存在订单 + 未生成微信支付 URL
        else if (null != unpaidOrderEntity && null == unpaidOrderEntity.getPayUrl()){
            log.info("创建订单-存在，未生成微信支付，返回 openid: {} orderId: {}", openid, unpaidOrderEntity.getOrderId());
            // 完成之前未完成的订单
            PayOrderEntity payOrderEntity = this.doPrepayOrder(openid, unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getProductName(), unpaidOrderEntity.getTotalAmount());
            log.info("创建订单-完成，生成支付单。openid: {} orderId: {} payUrl: {}", openid, payOrderEntity.getOrderId(), payOrderEntity.getPayUrl());
            return payOrderEntity;
        }

        // 2. 商品查询
        ProductEntity productEntity = orderRepository.queryProduct(productId);
        if (!productEntity.isAvailable()) {
            throw new ChatGPTException(Constants.ResponseCode.ORDER_PRODUCT_ERR.getCode(), Constants.ResponseCode.ORDER_PRODUCT_ERR.getInfo());
        }

        // 3. 保存订单
        OrderEntity orderEntity = this.doSaveOrder(openid, productEntity);

        // 4. 创建支付
        PayOrderEntity payOrderEntity = this.doPrepayOrder(openid, orderEntity.getOrderId(), productEntity.getProductName(), orderEntity.getTotalAmount());
        log.info("创建订单-完成，生成支付单。openid: {} orderId: {} payUrl: {}", openid, orderEntity.getOrderId(), payOrderEntity.getPayUrl());

        return payOrderEntity;
    }

    protected abstract OrderEntity doSaveOrder(String openid, ProductEntity productEntity);

    protected abstract PayOrderEntity doPrepayOrder (String openid, String orderId, String productName, BigDecimal amountTotal);
}
