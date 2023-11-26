package top.codingshen.chatgpt.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * @ClassName Configuration
 * @Description description
 * @Author alex_shen
 * @Date 2023/11/17 - 15:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class Configuration {
    @NotNull
    private String apiKey;

    private String apiHost;

    @NotNull
    private String authToken;
}
