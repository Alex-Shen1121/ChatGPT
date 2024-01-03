package top.codingshen.chatgpt.data.trigger.job;

import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.CloseOrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.codingshen.chatgpt.data.domain.order.service.IOrderService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName TimeoutCloseOrderJob
 * @Description 超时关单任务
 * @Author alex_shen
 * @Date 2024/1/4 - 02:48
 */
@Slf4j
@Component()
public class TimeoutCloseOrderJob {

    @Resource
    private IOrderService orderService;

    @Resource
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
                boolean status = orderService.changeOrderClose(orderId);

                //微信关单；暂时不需要主动关闭
                CloseOrderRequest request = new CloseOrderRequest();
                request.setMchid(mchid);
                request.setOutTradeNo(orderId);
                payService.closeOrder(request);

                log.info("定时任务，超时30分钟订单关闭 orderId: {} status：{}", orderId, status);
            }
        } catch (Exception e) {
            log.error("定时任务，超时15分钟订单关闭失败", e);
        }
    }

}