package top.codingshen.chatgpt.data.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName GoogleGuavaCodeCacheConfig
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/26 - 21:15
 */
@Configuration
public class GoogleGuavaCodeCacheConfig {
    @Bean(name = "codeCache")
    public Cache<String, String> codeCache() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(3, TimeUnit.MINUTES)
                .build();
    }
}
