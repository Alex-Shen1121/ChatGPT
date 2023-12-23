package top.codingshen.chatgpt.data.trigger.http.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName MessageEntity
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/24 - 01:57
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity {
    private String content;
    private String role;
    private String name;
}
