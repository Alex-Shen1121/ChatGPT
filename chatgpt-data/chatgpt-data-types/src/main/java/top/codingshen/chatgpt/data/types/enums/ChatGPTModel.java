package top.codingshen.chatgpt.data.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName ChatGPTModel
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/24 - 01:38
 */
@Getter
@AllArgsConstructor
public enum ChatGPTModel {
    GPT_3_5_TURBO("gpt-3.5-turbo"),
    ;
    private final String code;
}
