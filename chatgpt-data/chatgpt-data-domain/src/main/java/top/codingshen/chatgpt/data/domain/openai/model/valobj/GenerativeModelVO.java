package top.codingshen.chatgpt.data.domain.openai.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 模型生成类型
 */
@Getter
@AllArgsConstructor
public enum GenerativeModelVO {

    TEXT("TEXT","文本"),
    IMAGES("IMAGES","图片"),
    ;

    private final String code;
    private final String info;

}
