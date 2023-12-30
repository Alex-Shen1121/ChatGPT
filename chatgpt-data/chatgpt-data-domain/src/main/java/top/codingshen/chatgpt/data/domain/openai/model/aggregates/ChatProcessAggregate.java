package top.codingshen.chatgpt.data.domain.openai.model.aggregates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.codingshen.chatgpt.data.domain.openai.model.entity.MessageEntity;
import top.codingshen.chatgpt.data.types.common.Constants;
import top.codingshen.chatgpt.data.types.enums.ChatGPTModel;

import java.util.List;

/**
 * @ClassName ChatProcessAggregate
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/24 - 17:52
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatProcessAggregate {
    /**
     * 用户 ID
     */
    private String openid;
    /**
     * 默认模型
     */
    private String model = ChatGPTModel.GPT_3_5_TURBO.getCode();
    /**
     * 问题描述
     */
    private List<MessageEntity> messages;

    public boolean isWhiteList(String whiteListStr) {
        String[] whiteList = whiteListStr.split(Constants.SPLIT);
        for (String whiteOpenid : whiteList) {
            if (whiteOpenid.equals(openid)) return true;
        }
        return false;
    }
}
