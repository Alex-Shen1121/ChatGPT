package top.codingshen.chatgpt.data.domain.order.model.aggregates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.codingshen.chatgpt.data.domain.order.model.entity.OrderEntity;
import top.codingshen.chatgpt.data.domain.order.model.entity.ProductEntity;

import javax.annotation.security.DenyAll;

/**
 * @ClassName CreateOrderAggregate
 * @Description 下单聚合对象
 * @Author alex_shen
 * @Date 2024/1/3 - 18:09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderAggregate {
    /** 用户ID；微信用户唯一标识 */
    private String openid;
    /** 商品 */
    private ProductEntity product;
    /** 订单 */
    private OrderEntity order;
}
