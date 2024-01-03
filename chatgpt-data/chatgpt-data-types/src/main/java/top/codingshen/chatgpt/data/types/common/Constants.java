package top.codingshen.chatgpt.data.types.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ClassName Constants
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/24 - 17:57
 */
public class Constants {
    public final static String SPLIT = ",";

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum ResponseCode {
        SUCCESS("0000", "成功"),
        UN_ERROR("0001", "未知失败"),
        ILLEGAL_PARAMETER("0002", "非法参数"),
        TOKEN_ERROR("0003", "权限拦截"),
        ORDER_PRODUCT_ERR("OE001", "所购商品已下线，请重新选择下单商品");

        private String code;
        private String info;

    }
}
