package top.codingshen.chatgpt.data.trigger.job.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName NotifyOrderEntity
 * @Description description
 * @Author alex_shen
 * @Date 2024/1/14 - 01:10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotifyOrderDTO {

    private final SimpleDateFormat weixinDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
    private final SimpleDateFormat alipayDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 交易成功与否
     */
    private boolean tradeStatusSuccess;

    /**
     * 订单 id
     */
    private String orderId;

    /**
     * 交易 id
     */
    private String transactionId;

    /**
     * 交易金额
     */
    private BigDecimal totalAmount;

    /**
     * 交易成功时间
     */
    private Date successTime;

}
