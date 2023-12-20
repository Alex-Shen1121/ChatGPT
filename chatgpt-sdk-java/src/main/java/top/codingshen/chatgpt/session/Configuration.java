package top.codingshen.chatgpt.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.NotNull;
import top.codingshen.chatgpt.IOpenAiApi;

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

    private IOpenAiApi openAiApi;

    private OkHttpClient okHttpClient;

    @NotNull
    private String apiKey;

    private String apiHost;

    @NotNull
    private String authToken;

    public EventSource.Factory createRequestFactory() {
        return EventSources.createFactory(okHttpClient);
    }
}
