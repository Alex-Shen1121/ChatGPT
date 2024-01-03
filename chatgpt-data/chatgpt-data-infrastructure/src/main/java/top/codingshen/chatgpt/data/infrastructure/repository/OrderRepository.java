package top.codingshen.chatgpt.data.infrastructure.repository;

import org.springframework.stereotype.Repository;
import top.codingshen.chatgpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import top.codingshen.chatgpt.data.domain.order.model.entity.*;
import top.codingshen.chatgpt.data.domain.order.model.valobj.PayStatusVO;
import top.codingshen.chatgpt.data.domain.order.repository.IOrderRepository;
import top.codingshen.chatgpt.data.infrastructure.dao.IOpenAiOrderDao;
import top.codingshen.chatgpt.data.infrastructure.dao.IOpenAiProductDao;
import top.codingshen.chatgpt.data.infrastructure.po.OpenAiOrderPO;
import top.codingshen.chatgpt.data.infrastructure.po.OpenAiProductPO;
import top.codingshen.chatgpt.data.types.enums.OpenAIProductEnableModel;

import javax.annotation.Resource;

/**
 * @ClassName OrderRepository
 * @Description 订单仓储服务
 * @Author alex_shen
 * @Date 2024/1/3 - 23:04
 */
@Repository
public class OrderRepository implements IOrderRepository {

    @Resource
    private IOpenAiOrderDao openAiOrderDao;

    @Resource
    private IOpenAiProductDao openAiProductDao;

    @Override
    public UnpaidOrderEntity queryUnpaidOrder(ShopCartEntity shopCartEntity) {
        // 根据 openid+productid 查询未支付订单
        OpenAiOrderPO openAiOrderPOReq = new OpenAiOrderPO();
        openAiOrderPOReq.setOpenid(shopCartEntity.getOpenid());
        openAiOrderPOReq.setProductId(shopCartEntity.getProductId());

        OpenAiOrderPO openAiOrderPORes = openAiOrderDao.queryUnpaidOrder(openAiOrderPOReq);

        if (openAiOrderPORes == null) {
            return null;
        }

        return UnpaidOrderEntity.builder().openid(openAiOrderPORes.getOpenid()).orderId(openAiOrderPORes.getOrderId()).totalAmount(openAiOrderPORes.getTotalAmount()).productName(openAiOrderPORes.getProductName()).payUrl(openAiOrderPORes.getPayUrl()).payStatus(PayStatusVO.get(openAiOrderPORes.getPayStatus())).build();
    }

    @Override
    public ProductEntity queryProduct(Integer productId) {
        OpenAiProductPO openAiProductPO = openAiProductDao.queryProductByProductId(productId);
        ProductEntity productEntity = new ProductEntity();
        productEntity.setProductId(openAiProductPO.getProductId());
        productEntity.setProductName(openAiProductPO.getProductName());
        productEntity.setProductDesc(openAiProductPO.getProductDesc());
        productEntity.setQuota(openAiProductPO.getQuota());
        productEntity.setPrice(openAiProductPO.getPrice());
        productEntity.setEnable(OpenAIProductEnableModel.get(openAiProductPO.getIsEnabled()));
        return productEntity;
    }

    @Override
    public void updateOrderPayInfo(PayOrderEntity payOrderEntity) {
        OpenAiOrderPO openAiOrderPO = new OpenAiOrderPO();
        openAiOrderPO.setOpenid(payOrderEntity.getOpenid());
        openAiOrderPO.setOrderId(payOrderEntity.getOrderId());
        openAiOrderPO.setPayUrl(payOrderEntity.getPayUrl());
        openAiOrderPO.setPayStatus(payOrderEntity.getPayStatus().getCode());
        openAiOrderDao.updateOrderPayInfo(openAiOrderPO);
    }

    @Override
    public void saveOrder(CreateOrderAggregate aggregate) {
        String openid = aggregate.getOpenid();
        ProductEntity product = aggregate.getProduct();
        OrderEntity order = aggregate.getOrder();

        OpenAiOrderPO openAIOrderPO = new OpenAiOrderPO();
        openAIOrderPO.setOpenid(openid);
        openAIOrderPO.setProductId(product.getProductId());
        openAIOrderPO.setProductName(product.getProductName());
        openAIOrderPO.setProductQuota(product.getQuota());
        openAIOrderPO.setOrderId(order.getOrderId());
        openAIOrderPO.setOrderTime(order.getOrderTime());
        openAIOrderPO.setOrderStatus(order.getOrderStatus().getCode());
        openAIOrderPO.setTotalAmount(order.getTotalAmount());
        openAIOrderPO.setPayType(order.getPayTypeVO().getCode());
        openAIOrderPO.setPayStatus(PayStatusVO.WAIT.getCode());

        openAiOrderDao.insert(openAIOrderPO);
    }
}
