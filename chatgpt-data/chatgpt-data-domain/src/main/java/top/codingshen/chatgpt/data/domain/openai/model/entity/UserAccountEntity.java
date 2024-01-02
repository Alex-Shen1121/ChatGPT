package top.codingshen.chatgpt.data.domain.openai.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.codingshen.chatgpt.data.domain.openai.model.valobj.UserAccountStatusVO;
import top.codingshen.chatgpt.data.types.common.Constants;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName UserAccountEntity
 * @Description 用户账号实体对象
 * @Author alex_shen
 * @Date 2024/1/2 - 23:53
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccountEntity {
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
    private List<String> allowModelTypeList;
    /**
     * 账户状态；0-可用、1-冻结
     */
    private UserAccountStatusVO status;

    public void genModelTypes(String modelTypes) {
        String[] vals = modelTypes.split(Constants.SPLIT);
        this.allowModelTypeList = Arrays.asList(vals);
    }
}
