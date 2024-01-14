package top.codingshen.chatgpt.data.trigger.job;

import com.alipay.api.AlipayClient;
import com.google.common.eventbus.EventBus;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.codingshen.chatgpt.data.domain.order.model.entity.NotifyOrderEntity;
import top.codingshen.chatgpt.data.domain.order.model.valobj.PayTypeVO;
import top.codingshen.chatgpt.data.domain.order.service.IOrderService;
import top.codingshen.chatgpt.data.domain.order.service.channel.PayMethodGroupService;
import top.codingshen.chatgpt.data.domain.order.service.channel.impl.AlipaySandboxService;
import top.codingshen.chatgpt.data.domain.order.service.channel.impl.WeixinNativePayService;
import top.codingshen.chatgpt.data.types.common.Constants;
import top.codingshen.chatgpt.data.types.enums.channel.PayMethodChannel;

import javax.annotation.Resource;
import javax.xml.ws.handler.LogicalHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName NoPayNotifyOrderJob
 * @Description 检测未接收到或未正确处理的支付回调通知
 * @Author alex_shen
 * @Date 2024/1/4 - 02:50
 */
@Slf4j
@Component()
public class NoPayNotifyOrderJob {

    private final Map<PayMethodChannel, PayMethodGroupService> payMethodGroup = new HashMap<>();

    public NoPayNotifyOrderJob(WeixinNativePayService weixinNativePayService, AlipaySandboxService alipaySandboxService) {
        payMethodGroup.put(PayMethodChannel.WEIXIN_NATIVE_PAY, weixinNativePayService);
        payMethodGroup.put(PayMethodChannel.ALIPAY_SANDBOX, alipaySandboxService);
    }

    @Resource
    private IOrderService orderService;
    @Resource
    private EventBus eventBus;

    @Value("${wxpay.config.mchid}")
    private String mchid;

    /**
     * 检查 已创建订单, 但未支付
     */
    @Timed(value = "no_pay_notify_order_job", description = "定时任务, 订单支付状态更新")
    @Scheduled(cron = "0 0/1 * * * ?")
    public void exec() {
        try {
            List<String> orderIds = orderService.queryNoPayNotifyOrder();
            if (orderIds.isEmpty()) {
                log.info("定时任务，订单支付状态更新，暂无未更新订单 orderId is null");
                return;
            }

            // 已创建订单, 但未支付
            for (String orderId : orderIds) {
                // 查询订单支付方式
                Integer payType = orderService.queryPayMethodByOrderId(orderId);
                PayMethodChannel payMethod = PayMethodChannel.getChannel(PayTypeVO.get(payType).getDesc());

                NotifyOrderEntity notifyOrderEntity = payMethodGroup.get(payMethod).checkNoPayNotifyOrder(orderId);

                // 更新订单
                if (Constants.ResponseCode.SUCCESS.equals(notifyOrderEntity.getTradeStatus())) {
                    boolean isSuccess = orderService.changeOrderPaySuccess(notifyOrderEntity.getOrderId(), notifyOrderEntity.getTransactionId(), notifyOrderEntity.getTotalAmount(), notifyOrderEntity.getSuccessTime());

                    if (isSuccess) {
                        log.info("订单: {} 检测到成功支付, 更新订单状态", orderId);
                        // 发布消息
                        eventBus.post(orderId);
                    }
                } else {
                    log.info("订单: {} 尚未检测到支付成功状态.", orderId);
                }
            }
        } catch (Exception e) {
            log.error("定时任务，订单支付状态更新失败", e);
        }
    }

}
