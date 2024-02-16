package top.codingshen.chatgpt.interceptor;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import top.codingshen.chatgpt.common.Constants;

import java.io.IOException;

public class OpenAiInterceptor implements Interceptor {

    /** OpenAi apiKey 需要在官网申请 */
    private String apiKeyBySystem;
    /** 访问授权接口的认证 Token */
    private String authToken;

    public OpenAiInterceptor(String apiKeyBySystem) {
        this.apiKeyBySystem = apiKeyBySystem;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        // 1. 获取原始 Request
        Request original = chain.request();

        // 2. 读取 apiKey；优先使用自己传递的 apiKey
        String apiKeyByUser = original.header("apiKey") == null ? "NULL": original.header("apiKey") ;
        String apiKey = Constants.NULL.equals(apiKeyByUser) ? apiKeyBySystem : apiKeyByUser;

        // 3. 构建 Request
        Request request = original.newBuilder()
                .url(original.url())
                .header(Header.AUTHORIZATION.getValue(), "Bearer " + apiKey)
                .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                .method(original.method(), original.body())
                .build();

        // 4. 返回执行结果
        return chain.proceed(request);
    }
}
