package top.codingshen.chatgpt.data.trigger.http.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @ClassName SaleProductDTO
 * @Description 商品对象 DTO
 * @Author alex_shen
 * @Date 2024/1/4 - 03:04
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleProductDTO {
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
