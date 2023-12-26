package top.codingshen.chatgpt.data.domain.auth.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @ClassName AuthTypeVO
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/26 - 01:37
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum AuthTypeVO {

    A0000("0000", "验证成功"), A0001("0001", "验证码不存在"), A0002("0002", "验证码无效");

    private String code;
    private String info;
}
