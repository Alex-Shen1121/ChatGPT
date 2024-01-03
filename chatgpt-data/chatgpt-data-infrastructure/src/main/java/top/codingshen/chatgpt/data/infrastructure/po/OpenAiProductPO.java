package top.codingshen.chatgpt.data.infrastructure.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName OpenAiProductPO
 * @Description description
 * @Author alex_shen
 * @Date 2024/1/3 - 18:15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OpenAiProductPO {

    /**
     * 自增ID
     */
    private Long id;
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
     * 商品排序
     */
    private Integer sort;
    /**
     * 是否有效；0无效、1有效
     */
    private Integer isEnabled;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}
