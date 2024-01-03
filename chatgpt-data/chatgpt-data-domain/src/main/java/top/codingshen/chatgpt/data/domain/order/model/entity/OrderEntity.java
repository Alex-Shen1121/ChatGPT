package top.codingshen.chatgpt.data.domain.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.codingshen.chatgpt.data.domain.order.model.valobj.OrderStatusVO;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName OrderEntity
 * @Description description
 * @Author alex_shen
 * @Date 2024/1/3 - 18:10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderEntity {

    /** 订单编号 */
    private String orderId;
    /** 下单时间 */
    private Date orderTime;
    /** 订单状态；0-创建完成、1-等待发货、2-发货完成、3-系统关单 */
    private OrderStatusVO orderStatus;
    /** 订单金额 */
    private BigDecimal totalAmount;
}
