package top.codingshen.chatgpt.data.domain.openai.annotation;

import top.codingshen.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName LogicStrategy
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/28 - 23:46
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogicStrategy {
    DefaultLogicFactory.LogicModel logicMode();
}
