
package top.codingshen.chatgpt.data.domain.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.codingshen.chatgpt.data.types.common.Constants;

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
public class NotifyOrderEntity {

    /**
     * 交易成功与否
     */
    private Constants.ResponseCode tradeStatus;

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
