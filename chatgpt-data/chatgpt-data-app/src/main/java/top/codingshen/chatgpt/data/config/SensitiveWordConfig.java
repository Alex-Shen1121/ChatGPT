package top.codingshen.chatgpt.data.config;

import com.github.houbb.sensitive.word.api.IWordContext;
import com.github.houbb.sensitive.word.api.IWordReplace;
import com.github.houbb.sensitive.word.api.IWordResult;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.core.SensitiveWords;
import com.github.houbb.sensitive.word.utils.InnerWordCharUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName SensitiveWordConfig
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/29 - 03:43
 */
@Slf4j
@Configuration
public class SensitiveWordConfig {
    @Bean
    public SensitiveWordBs sensitiveWordBs() {
        return SensitiveWordBs.newInstance()
                .wordReplace((stringBuilder, chars, iWordResult, iWordContext) -> {
                    String sensitiveWord = InnerWordCharUtils.getString(chars, iWordResult);
                    log.info("检测到敏感词: {}", sensitiveWord);
                    // 替换操作，你可以指定的替换为*或者其他
                    int wordLength = iWordResult.endIndex() - iWordResult.startIndex();
                    for (int i = 0; i < wordLength; i++) {
                        stringBuilder.append("*");
                    }
                })
                .ignoreCase(true)
                .ignoreWidth(true)
                .ignoreNumStyle(true)
                .ignoreChineseStyle(true)
                .ignoreEnglishStyle(true)
                .ignoreRepeat(false)
                .enableNumCheck(true)
                .enableEmailCheck(true)
                .enableUrlCheck(true)
                .enableWordCheck(true)
                .numCheckLen(1024)
                .init();
    }
}
