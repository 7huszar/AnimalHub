package at.ac.tuwien.sepr.groupphase.backend.config;

import at.ac.tuwien.sepr.groupphase.backend.config.properties.ThreadPoolProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@RequiredArgsConstructor
public class AsyncConfig {

    private final ThreadPoolProperties pool;

    @Bean
    public Executor getExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(pool.getCorePoolSize()); // Set the core number of threads
        executor.setMaxPoolSize(pool.getMaxPoolSize()); // Set the maximum number of threads
        executor.setQueueCapacity(pool.getQueueCapacity()); // Set the capacity of the queue
        executor.setThreadNamePrefix("EmailVerification-");
        executor.initialize();
        return executor;
    }
}
