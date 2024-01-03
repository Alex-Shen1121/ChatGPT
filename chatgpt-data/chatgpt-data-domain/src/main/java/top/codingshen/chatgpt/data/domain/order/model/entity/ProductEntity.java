package top.codingshen.chatgpt.data.domain.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.codingshen.chatgpt.data.types.enums.OpenAIProductEnableModel;

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
    /**
     * 商品状态；0无效、1有效
     */
    private OpenAIProductEnableModel enable;

    /**
     * 是否有效；true = 有效，false = 无效
     */
    public boolean isAvailable() {
        return OpenAIProductEnableModel.OPEN.equals(enable);
    }
}
