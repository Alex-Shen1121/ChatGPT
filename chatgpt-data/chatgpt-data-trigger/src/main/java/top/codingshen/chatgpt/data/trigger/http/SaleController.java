package top.codingshen.chatgpt.data.trigger.http;

import com.google.common.eventbus.EventBus;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.service.partnerpayments.nativepay.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.codingshen.chatgpt.data.domain.auth.service.IAuthService;
import top.codingshen.chatgpt.data.domain.order.service.IOrderService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;

/**
 * @ClassName SaleController
 * @Description 售卖服务
 * @Author alex_shen
 * @Date 2024/1/4 - 02:13
 */
@Slf4j
@RestController
@CrossOrigin("${app.config.cross-origin}")
@RequestMapping("/api/${app.config.api-version}/sale/")
public class SaleController {
    @Resource
    private NotificationParser notificationParser;
    @Resource
    private IOrderService orderService;
    @Resource
    private IAuthService authService;
    @Resource
    private EventBus eventBus;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    /**
     * 支付回调
     * 开发地址：http:/localhost:8099/api/v1/mall/pay_notify
     * 测试地址：http://apix.natapp1.cc/api/v1/mall/pay_notify
     * 线上地址：https://你的域名/api/v1/mall/pay_notify
     */
    @PostMapping("pay_notify")
    public void payNotify(@RequestBody String requestBody, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // 随机串
            String nonceStr = request.getHeader("Wechatpay-Nonce");
            // 微信传递过来的签名
            String signature = request.getHeader("Wechatpay-Signature");
            // 证书序列号（微信平台）
            String serialNo = request.getHeader("Wechatpay-Serial");
            // 时间戳
            String timestamp = request.getHeader("Wechatpay-Timestamp");

            // 构造 RequestParam
            com.wechat.pay.java.core.notification.RequestParam requestParam = new com.wechat.pay.java.core.notification.RequestParam.Builder()
                    .serialNumber(serialNo)
                    .nonce(nonceStr)
                    .signature(signature)
                    .timestamp(timestamp)
                    .body(requestBody)
                    .build();

            // 以支付通知回调为例，验签、解密并转换成 Transaction
            Transaction transaction = notificationParser.parse(requestParam, Transaction.class);

            Transaction.TradeStateEnum tradeState = transaction.getTradeState();
            if (Transaction.TradeStateEnum.SUCCESS.equals(tradeState)) {
                // 支付单号
                String orderId = transaction.getOutTradeNo();
                String transactionId = transaction.getTransactionId();
                Integer total = transaction.getAmount().getTotal();
                String successTime = transaction.getSuccessTime();
                log.info("支付成功 orderId:{} total:{} successTime: {}", orderId, total, successTime);
                // 更新订单
                boolean isSuccess = orderService.changeOrderPaySuccess(orderId, transactionId, new BigDecimal(total).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP), dateFormat.parse(successTime));
                if (isSuccess) {
                    // 发布消息
                    eventBus.post(orderId);
                }
                response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
            } else {
                response.getWriter().write("<xml><return_code><![CDATA[FAIL]]></return_code></xml>");
            }
        } catch (Exception e) {
            log.error("支付失败", e);
            response.getWriter().write("<xml><return_code><![CDATA[FAIL]]></return_code></xml>");
        }
    }

}
