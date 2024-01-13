package top.codingshen.chatgpt.data.domain.order.service.channel.impl;

import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import top.codingshen.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import top.codingshen.chatgpt.data.domain.order.model.valobj.PayStatusVO;
import top.codingshen.chatgpt.data.domain.order.repository.IOrderRepository;
import top.codingshen.chatgpt.data.domain.order.service.channel.PayMethodGroupService;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @ClassName WeixinNativePayService
 * @Description 微信原生支付
 * @Author alex_shen
 * @Date 2024/1/13 - 14:37
 */
@Slf4j
@Service
public class WeixinNativePayService implements PayMethodGroupService {
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
}
