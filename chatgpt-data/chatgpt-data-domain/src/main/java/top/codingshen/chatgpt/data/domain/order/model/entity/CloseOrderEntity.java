package top.codingshen.chatgpt.data.domain.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.codingshen.chatgpt.data.types.common.Constants;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName CloseOrderEntity
 * @Description description
 * @Author alex_shen
 * @Date 2024/1/14 - 21:14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CloseOrderEntity {

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

}
