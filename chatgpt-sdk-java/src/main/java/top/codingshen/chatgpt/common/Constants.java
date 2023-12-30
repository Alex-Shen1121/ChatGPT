package top.codingshen.chatgpt.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName Constants
 * @Description description
 * @Author alex_shen
 * @Date 2023/11/17 - 14:29
 */
public class Constants {
    public final static String NULL = "NULL";

    /**
     * 官网支持的请求角色类型；system、user、assistant
     * <a href="https://platform.openai.com/docs/guides/chat/introduction">官方网站</a>
     */
    @Getter
    @AllArgsConstructor
    public enum Role {

        SYSTEM("system"), USER("user"), ASSISTANT("assistant"),
        ;

        private String code;

    }
}
