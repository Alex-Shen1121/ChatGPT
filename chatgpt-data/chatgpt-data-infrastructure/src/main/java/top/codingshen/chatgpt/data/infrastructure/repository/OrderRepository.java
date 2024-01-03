package top.codingshen.chatgpt.data.infrastructure.repository;

import org.springframework.stereotype.Repository;
import top.codingshen.chatgpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import top.codingshen.chatgpt.data.domain.order.model.entity.*;
import top.codingshen.chatgpt.data.domain.order.model.valobj.OrderStatusVO;
import top.codingshen.chatgpt.data.domain.order.model.valobj.PayStatusVO;
import top.codingshen.chatgpt.data.domain.order.repository.IOrderRepository;
import top.codingshen.chatgpt.data.infrastructure.dao.IOpenAiOrderDao;
import top.codingshen.chatgpt.data.infrastructure.dao.IOpenAiProductDao;
import top.codingshen.chatgpt.data.infrastructure.dao.IUserAccountDao;
import top.codingshen.chatgpt.data.infrastructure.po.OpenAiOrderPO;
import top.codingshen.chatgpt.data.infrastructure.po.OpenAiProductPO;
import top.codingshen.chatgpt.data.infrastructure.po.UserAccountPO;
import top.codingshen.chatgpt.data.types.enums.OpenAIProductEnableModel;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

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

    @Resource
    private IUserAccountDao userAccountDao;

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

    @Override
    public boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal totalAmount, Date payTime) {
        OpenAiOrderPO openAIOrderPO = new OpenAiOrderPO();
        openAIOrderPO.setOrderId(orderId);
        openAIOrderPO.setPayAmount(totalAmount);
        openAIOrderPO.setPayTime(payTime);
        openAIOrderPO.setTransactionId(transactionId);
        int count = openAiOrderDao.changeOrderPaySuccess(openAIOrderPO);
        return count == 1;
    }

    @Override
    public CreateOrderAggregate queryOrder(String orderId) {
        OpenAiOrderPO openAiOrderPO = openAiOrderDao.queryOrder(orderId);

        CreateOrderAggregate createOrderAggregate = new CreateOrderAggregate();

        ProductEntity product = new ProductEntity();
        product.setProductId(openAiOrderPO.getProductId());
        product.setProductName(openAiOrderPO.getProductName());

        OrderEntity order = new OrderEntity();
        order.setOrderId(openAiOrderPO.getOrderId());
        order.setOrderTime(openAiOrderPO.getOrderTime());
        order.setOrderStatus(OrderStatusVO.get(openAiOrderPO.getOrderStatus()));
        order.setTotalAmount(openAiOrderPO.getTotalAmount());

        createOrderAggregate.setOpenid(openAiOrderPO.getOpenid());
        createOrderAggregate.setProduct(product);
        createOrderAggregate.setOrder(order);

        return createOrderAggregate;
    }

    @Override
    public void deliverGoods(String orderId) {
        OpenAiOrderPO openAIOrderPO = openAiOrderDao.queryOrder(orderId);

        // 1. 变更发货状态
        int updateOrderStatusDeliverGoodsCount = openAiOrderDao.updateOrderStatusDeliverGoods(orderId);
        if (1 != updateOrderStatusDeliverGoodsCount)
            throw new RuntimeException("updateOrderStatusDeliverGoodsCount update count is not equal 1");

        // 2. 账户额度变更
        UserAccountPO userAccountPO = userAccountDao.queryUserAccount(openAIOrderPO.getOpenid());
        UserAccountPO userAccountPOReq = new UserAccountPO();
        userAccountPOReq.setOpenid(openAIOrderPO.getOpenid());
        userAccountPOReq.setTotalQuota(openAIOrderPO.getProductQuota());
        userAccountPOReq.setSurplusQuota(openAIOrderPO.getProductQuota());
        if (null != userAccountPO) {
            int addAccountQuotaCount = userAccountDao.addAccountQuota(userAccountPOReq);
            if (1 != addAccountQuotaCount)
                throw new RuntimeException("addAccountQuotaCount update count is not equal 1");
        } else {
            userAccountDao.insert(userAccountPOReq);
        }
    }
}
