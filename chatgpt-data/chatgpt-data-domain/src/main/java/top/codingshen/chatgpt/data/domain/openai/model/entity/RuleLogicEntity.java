package top.codingshen.chatgpt.data.domain.openai.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.codingshen.chatgpt.data.domain.openai.model.valobj.LogicCheckTypeVO;

/**
 * @ClassName RuleLogicEntity
 * @Description 规则校验结果实体
 * @Author alex_shen
 * @Date 2023/12/28 - 23:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RuleLogicEntity<T> {

    private LogicCheckTypeVO type;
    private String info;
    private T data;

}
