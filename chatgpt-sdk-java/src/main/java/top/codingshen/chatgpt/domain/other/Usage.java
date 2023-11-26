package top.codingshen.chatgpt.domain.other;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName Usage
 * @Description description
 * @Author alex_shen
 * @Date 2023/11/17 - 15:16
 */
@Data
public class Usage {

    /** 完成令牌 */
    @JsonProperty("completion_tokens")
    private long completionTokens;

    /** 提示令牌 */
    @JsonProperty("prompt_tokens")
    private long promptTokens;

    /** 总量令牌 */
    @JsonProperty("total_tokens")
    private long totalTokens;
}
