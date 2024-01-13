package top.codingshen.chatgpt.data.domain.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.codingshen.chatgpt.data.types.enums.channel.PayMethodChannel;

/**
 * @ClassName ShopCartEntity
 * @Description description
 * @Author alex_shen
 * @Date 2024/1/3 - 18:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShopCartEntity {

    /**
     * 用户微信唯一ID
     */
    private String openid;

    /**
     * 商品ID
     */
    private Integer productId;

    /**
     * 支付方式
     */
    private String payMethod;

    public PayMethodChannel getChannel() {
        return PayMethodChannel.getChannel(this.payMethod);
    }

}
