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
    /**
     * 文生图
     */
    DALL_E_2("dall-e-2"), DALL_E_3("dall-e-3"),
    ;
    private final String code;

    private static ChatGPTModel getByCode(String code) {
        switch (code) {
            case "gpt-3.5-turbo":
                return ChatGPTModel.GPT_3_5_TURBO;
            case "dall-e-2":
                return ChatGPTModel.DALL_E_2;
            case "dall-e-3":
                return ChatGPTModel.DALL_E_3;
            default:
                return ChatGPTModel.GPT_3_5_TURBO;
        }
    }
}
