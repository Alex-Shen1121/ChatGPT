package top.codingshen.chatgpt.domain.images;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @ClassName ImageRequest
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/21 - 17:55
 */
@Data
@Builder
@Slf4j

@JsonInclude(JsonInclude.Include.NON_NULL)

public class ImageRequest implements Serializable {
    /**
     * 模型
     */

    private String model = Model.DALL_E_2.code;

    /**
     * 问题描述
     */
    @NonNull
    private String prompt;

    /**
     * 为每个提示生成的完成次数
     */
    @Builder.Default
    private Integer n = 1;

    /**
     * 图片大小
     */
    @Builder.Default
    private String size = ImageEnum.Size.size_256.getCode();

    /**
     * 图片格式化方式；URL、B64_JSON
     */

    @JsonProperty("response_format")
    @Builder.Default
    private String responseFormat = ImageEnum.ResponseFormat.URL.getCode();

    @Setter
    private String user;

    @Getter
    @AllArgsConstructor
    public enum Model {
        DALL_E_2("dall-e-2"),
        DALL_E_3("dall-e-3"),
        ;
        private final String code;
    }

}
