package top.codingshen.chatgpt.data.trigger.job;

import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.CloseOrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.codingshen.chatgpt.data.domain.order.model.entity.CloseOrderEntity;
import top.codingshen.chatgpt.data.domain.order.model.entity.NotifyOrderEntity;
import top.codingshen.chatgpt.data.domain.order.model.valobj.PayTypeVO;
import top.codingshen.chatgpt.data.domain.order.service.IOrderService;
import top.codingshen.chatgpt.data.domain.order.service.channel.PayMethodGroupService;
import top.codingshen.chatgpt.data.domain.order.service.channel.impl.AlipaySandboxService;
import top.codingshen.chatgpt.data.domain.order.service.channel.impl.WeixinNativePayService;
import top.codingshen.chatgpt.data.types.common.Constants;
import top.codingshen.chatgpt.data.types.enums.channel.PayMethodChannel;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName TimeoutCloseOrderJob
 * @Description 超时关单任务
 * @Author alex_shen
 * @Date 2024/1/4 - 02:48
 */
@Slf4j
@Component()
public class TimeoutCloseOrderJob {

    private final Map<PayMethodChannel, PayMethodGroupService> payMethodGroup = new HashMap<>();

    public TimeoutCloseOrderJob(WeixinNativePayService weixinNativePayService, AlipaySandboxService alipaySandboxService) {
        payMethodGroup.put(PayMethodChannel.WEIXIN_NATIVE_PAY, weixinNativePayService);
        payMethodGroup.put(PayMethodChannel.ALIPAY_SANDBOX, alipaySandboxService);
    }

    @Resource
    private IOrderService orderService;

    @Autowired(required = false)
    private NativePayService payService;

    @Value("${wxpay.config.mchid}")
    private String mchid;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void exec() {
        try {
            List<String> orderIds = orderService.queryTimeoutCloseOrderList();

            if (orderIds.isEmpty()) {
                log.info("定时任务，超时30分钟订单关闭，暂无超时未支付订单 orderIds is null");
                return;
            }

            for (String orderId : orderIds) {
                // 查询订单支付方式
                Integer payType = orderService.queryPayMethodByOrderId(orderId);
                PayMethodChannel payMethod = PayMethodChannel.getChannel(PayTypeVO.get(payType).getDesc());

                CloseOrderEntity closeOrderEntity = payMethodGroup.get(payMethod).changeOrderClose(orderId);

                if (Constants.ResponseCode.SUCCESS.equals(closeOrderEntity.getTradeStatus())) {
                    boolean status = orderService.changeOrderClose(orderId);
                    log.info("定时任务，超时30分钟订单关闭 orderId: {} status：{}", orderId, status);
                } else {
                    log.info("订单: {} 支付关单失败", orderId);
                }
            }
        } catch (Exception e) {
            log.error("定时任务，超时30分钟订单关闭失败", e);
        }
    }

}

