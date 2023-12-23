package top.codingshen.chatgpt.data.trigger.http.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.codingshen.chatgpt.data.types.enums.ChatGPTModel;

import java.util.List;

/**
 * @ClassName ChatGPTRequestDTO
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/24 - 01:16
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatGPTRequestDTO {

    /** 默认模型 */
    private String model = ChatGPTModel.GPT_3_5_TURBO.getCode();

    /** 问题描述 */
    private List<MessageEntity> messages;
}
