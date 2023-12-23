package top.codingshen.chatgpt.data.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.*;

/**
 * @ClassName ThreadPoolConfig
 * @Description description
 * @Author alex_shen
 * @Date 2023/12/23 - 22:18
 */
@Configuration
@EnableConfigurationProperties(ThreadPoolConfigProperties.class)
@Slf4j
@EnableAsync
public class ThreadPoolConfig {

    @Bean
    @ConditionalOnMissingBean(ThreadPoolExecutor.class)
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties properties) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        // 实例化策略
        RejectedExecutionHandler handler;
        switch (properties.getPolicy()) {
            case "AbortPolicy":
                handler = new ThreadPoolExecutor.AbortPolicy();
                break;
            case "DiscardPolicy":
                handler = new ThreadPoolExecutor.DiscardPolicy();
                break;
            case "DiscardOldestPolicy":
                handler = new ThreadPoolExecutor.DiscardOldestPolicy();
                break;
            case "CallerRunsPolicy":
                handler = new ThreadPoolExecutor.CallerRunsPolicy();
                break;
            default:
                handler = new ThreadPoolExecutor.AbortPolicy();
                break;
        }
        // 创建线程池
        // 创建线程池
        return new ThreadPoolExecutor(
                properties.getCorePoolSize(),         // 核心线程池大小
                properties.getMaxPoolSize(),          // 最大线程池大小
                properties.getKeepAliveTime(),        // 非核心线程的闲置存活时间
                TimeUnit.SECONDS,                     // 时间单位（这里是秒）
                new LinkedBlockingQueue<>(properties.getBlockQueueSize()), // 任务队列
                Executors.defaultThreadFactory(),     // 线程工厂
                handler);                             // 拒绝策略
    }
}
