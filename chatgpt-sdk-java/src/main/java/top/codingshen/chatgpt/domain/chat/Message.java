package top.codingshen.chatgpt.domain.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.codingshen.chatgpt.common.Constants;

/**
 * @ClassName Message
 * @Description description
 * @Author alex_shen
 * @Date 2023/11/17 - 14:18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {
    /**
     * The contents of the user message.
     */
    private String content;

    /**
     * the role of the messages author
     */
    private String role;

    /**
     * An optional name for the participant. Provides the model information to differentiate between participants of the same role.
     */
    private String name;

    private Message(Builder builder) {
        this.role = builder.role;
        this.content = builder.content;
        this.name = builder.name;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * 建造者模式
     */
    public static final class Builder {

        private String content;
        private String role;
        private String name;

        public Builder() {
        }

        public Builder role(Constants.Role role) {
            this.role = role.getCode();
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }


}
