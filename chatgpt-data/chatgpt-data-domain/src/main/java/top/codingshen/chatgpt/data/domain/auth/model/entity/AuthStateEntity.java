package top.codingshen.chatgpt.data.domain.auth.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName AuthStateEntity
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/26 - 01:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthStateEntity {
    private String code;
    private String info;
    private String openId;
    private String token;
}
