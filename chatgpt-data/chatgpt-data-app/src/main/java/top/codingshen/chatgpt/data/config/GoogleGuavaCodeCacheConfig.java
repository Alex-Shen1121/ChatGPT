package top.codingshen.chatgpt.data.config;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.codingshen.chatgpt.data.trigger.mq.OrderPaySuccessListener;

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


    @Bean(name = "visitCache")
    public Cache<String, Integer> visitCache() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(12, TimeUnit.HOURS)
                .build();
    }

    @Bean
    public EventBus eventBusListener(OrderPaySuccessListener listener){
        EventBus eventBus = new EventBus();
        eventBus.register(listener);
        return eventBus;
    }
}
