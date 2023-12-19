package top.codingshen.chatgpt.domain.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatChoice implements Serializable {

    /** The index of the choice in the list of choices. **/
    private long index;

    /** stream = false 请求参数里返回的属性是 delta */
    @JsonProperty("message")
    private Message message;

    /** stream = true 请求参数里返回的属性是 delta */
    @JsonProperty("delta")
    private Message delta;

    @JsonProperty("finish_reason")
    private String finishReason;

}
