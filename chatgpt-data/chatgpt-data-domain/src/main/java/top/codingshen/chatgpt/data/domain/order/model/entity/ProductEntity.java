package top.codingshen.chatgpt.data.domain.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName ProductEntity
 * @Description 商品实体对象
 * @Author alex_shen
 * @Date 2024/1/3 - 18:10
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductEntity {
    /**
     * 商品ID
     */
    private Integer productId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品描述
     */
    private String productDesc;
    /**
     * 额度次数
     */
    private Integer quota;
    /**
     * 商品价格
     */
    private BigDecimal price;

}
