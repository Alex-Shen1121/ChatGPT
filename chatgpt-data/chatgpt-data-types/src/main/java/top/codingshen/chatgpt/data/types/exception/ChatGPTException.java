package top.codingshen.chatgpt.data.types.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName ChatGPTException
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/24 - 02:15
 */
@Data
public class ChatGPTException extends RuntimeException {
    // 异常码
    private String code;
    // 异常信息
    private String message;

    public ChatGPTException(String code) {
        this.code = code;

    }

    public ChatGPTException(String code, Throwable cause) {
        this.code = code;
        super.initCause(cause);
    }

    public ChatGPTException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ChatGPTException(String code, String message, Throwable cause) {
        this.code = code;
        this.message = message;
        super.initCause(cause);
    }
}
