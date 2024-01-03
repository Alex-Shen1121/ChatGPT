package top.codingshen.chatgpt.data.infrastructure.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @ClassName UserAccountPO
 * @Description 用户账户持久化对象
 * @Author alex_shen
 * @Date 2024/1/2 - 23:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccountPO {

    /**
     * 自增ID
     */
    private Long id;
    /**
     * 用户ID；这里用的是微信ID作为唯一ID，你也可以给用户创建唯一ID，之后绑定微信ID
     */
    private String openid;
    /**
     * 总量额度
     */
    private Integer totalQuota;
    /**
     * 剩余额度
     */
    private Integer surplusQuota;
    /**
     * 可用模型；gpt-3.5-turbo,gpt-3.5-turbo-16k,gpt-4,gpt-4-32k
     */
    private String modelTypes;
    /**
     * 账户状态；0-可用、1-冻结
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}
