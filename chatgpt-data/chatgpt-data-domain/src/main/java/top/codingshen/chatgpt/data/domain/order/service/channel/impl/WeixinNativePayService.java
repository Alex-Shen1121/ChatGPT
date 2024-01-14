package top.codingshen.chatgpt.data.domain.order.service.channel.impl;

import com.alipay.api.AlipayApiException;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.codingshen.chatgpt.data.domain.order.model.entity.CloseOrderEntity;
import top.codingshen.chatgpt.data.domain.order.model.entity.NotifyOrderEntity;
import top.codingshen.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import top.codingshen.chatgpt.data.domain.order.model.valobj.PayStatusVO;
import top.codingshen.chatgpt.data.domain.order.repository.IOrderRepository;
import top.codingshen.chatgpt.data.domain.order.service.channel.PayMethodGroupService;
import top.codingshen.chatgpt.data.types.common.Constants;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @ClassName WeixinNativePayService
 * @Description 微信原生支付
 * @Author alex_shen
 * @Date 2024/1/13 - 14:37
 */
@Slf4j
@Service
public class WeixinNativePayService implements PayMethodGroupService {

    private final SimpleDateFormat weixinDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    @Value("${wxpay.config.appid}")
    private String appid;
    @Value("${wxpay.config.mchid}")
    private String mchid;
    @Value("${wxpay.config.notify-url}")
    private String notifyUrl;
    @Autowired(required = false)
    private NativePayService payService;
    @Resource
    IOrderRepository orderRepository;

    @Override
    public PayOrderEntity doPrepayOrder(String userId, String orderId, String productName, BigDecimal amountTotal) {
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(amountTotal.multiply(new BigDecimal(100)).intValue());

        request.setAmount(amount);
        request.setAppid(appid);
        request.setMchid(mchid);
        request.setDescription(productName);
        request.setNotifyUrl(notifyUrl);
        request.setOutTradeNo(orderId);

        // 创建 微信支付单
        String payUrl = "";
        if (null != payService) {
            PrepayResponse prepay = payService.prepay(request);
            payUrl = prepay.getCodeUrl();
        } else {
            payUrl = "因未配置微信支付渠道，所以暂时不能生成支付URL";
        }

        PayOrderEntity payOrderEntity = PayOrderEntity.builder()
                .openid(userId)
                .orderId(orderId)
                .payUrl(payUrl)
                .payStatus(PayStatusVO.WAIT)
                .build();

        // 更新订单支付信息
        orderRepository.updateOrderPayInfo(payOrderEntity);
        return payOrderEntity;
    }

    @Override
    public NotifyOrderEntity checkNoPayNotifyOrder(String orderId) throws Exception {
        if (null == payService) {
            log.info("定时任务，订单支付状态更新。应用未配置微信支付渠道，任务不执行。");
            return NotifyOrderEntity.builder()
                    .tradeStatus(Constants.ResponseCode.UNABLE_CONFIG)
                    .orderId(orderId)
                    .build();
        }

        log.info("正在尝试查询订单: {} 支付状态", orderId);

        // 查询结果
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setMchid(mchid);
        request.setOutTradeNo(orderId);
        Transaction transaction = payService.queryOrderByOutTradeNo(request);


        // 支付单号
        String transactionId = transaction.getTransactionId();
        Integer total = transaction.getAmount().getTotal();
        BigDecimal totalAmount = new BigDecimal(total);
        String successTime = transaction.getSuccessTime();

        if (Transaction.TradeStateEnum.SUCCESS.equals(transaction.getTradeState())) {
            return NotifyOrderEntity.builder()
                    .tradeStatus(Constants.ResponseCode.SUCCESS)
                    .orderId(orderId)
                    .transactionId(transactionId)
                    .totalAmount(totalAmount)
                    .successTime(weixinDateFormat.parse(successTime))
                    .build();
        } else {
            return NotifyOrderEntity.builder()
                    .tradeStatus(Constants.ResponseCode.UN_ERROR)
                    .orderId(orderId)
                    .build();
        }
    }

    @Override
    public CloseOrderEntity changeOrderClose(String orderId) throws Exception {
        if (null == payService) {
            log.info("定时任务，订单支付状态更新。应用未配置微信支付渠道，任务不执行。");
            return CloseOrderEntity.builder()
                    .tradeStatus(Constants.ResponseCode.UNABLE_CONFIG)
                    .orderId(orderId)
                    .build();
        }

        log.info("正在尝试关闭订单: {}", orderId);

        //微信关单；暂时不需要主动关闭
        CloseOrderRequest request = new CloseOrderRequest();
        request.setMchid(mchid);
        request.setOutTradeNo(orderId);
        // todo: 微信关单没有回执
        payService.closeOrder(request);

        return CloseOrderEntity.builder()
                .tradeStatus(Constants.ResponseCode.SUCCESS)
                .orderId(orderId)
                .build();
    }
}
