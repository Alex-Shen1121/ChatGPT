package top.codingshen.chatgpt.data.domain.order.service.channel.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.codingshen.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import top.codingshen.chatgpt.data.domain.order.model.valobj.PayStatusVO;
import top.codingshen.chatgpt.data.domain.order.repository.IOrderRepository;
import top.codingshen.chatgpt.data.domain.order.service.channel.PayMethodGroupService;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * @ClassName AlipaySandboxService
 * @Description description
 * @Author alex_shen
 * @Date 2024/1/13 - 16:20
 */
@Slf4j
@Service
public class AlipaySandboxService implements PayMethodGroupService {

    @Value("${alipay.sandbox.config.notify_url}")
    private String notifyUrl;
    @Value("${alipay.sandbox.config.return_url}")
    private String returnUrl;
    @Autowired(required = false)
    private AlipayClient alipayClient;
    @Resource
    IOrderRepository orderRepository;

    @Override
    public PayOrderEntity doPrepayOrder(String openid, String orderId, String productName, BigDecimal totalAmount) throws AlipayApiException {
        // 生成请求参数
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl(notifyUrl);
        request.setReturnUrl(returnUrl);

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderId);
        bizContent.put("subject", productName);
        bizContent.put("total_amount", totalAmount.toString());
        request.setBizContent(bizContent.toString());

        // 创建 支付宝沙盒支付单
        String payUrl = "";
        if (null != alipayClient) {
            AlipayTradePrecreateResponse response = alipayClient.execute(request);
            log.info("完成支付宝订单下单" + response.getBody());
            payUrl = response.getQrCode();
        } else {
            payUrl = "因未配置支付宝沙箱支付渠道，所以暂时不能生成支付URL";
        }

        PayOrderEntity payOrderEntity = PayOrderEntity.builder()
                .openid(openid)
                .orderId(orderId)
                .payUrl(payUrl)
                .payStatus(PayStatusVO.WAIT)
                .build();

        // 更新订单支付信息
        orderRepository.updateOrderPayInfo(payOrderEntity);
        return payOrderEntity;
    }
}
