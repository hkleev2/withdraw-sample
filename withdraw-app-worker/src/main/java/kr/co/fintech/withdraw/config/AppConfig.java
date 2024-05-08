package kr.co.fintech.withdraw.config;

import kr.co.fintech.bank.api.BankingApiClient;
import kr.co.fintech.lib.queue.FairMessage;
import kr.co.fintech.lib.queue.FairQueue;
import kr.co.fintech.lib.queue.FintechQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.time.Duration;

@Configuration
public class AppConfig {

    @Value("${withdraw.executorFixedPoolSize: 3}")
    private Integer executorFixedPoolSize;

    @Value("${withdraw.awaitTerminationPeriod: 10}")
    private Integer awaitTerminationPeriod;

    @Bean
    public FintechQueue<FairMessage<Long, Long>> queue() {
        return new FairQueue<>();
    }

    @Bean
    public ThreadPoolTaskExecutor withdrawExecutor() {
        return new ThreadPoolTaskExecutorBuilder()
                .threadNamePrefix("WithdrawExecutor-")
                .corePoolSize(executorFixedPoolSize)
                .maxPoolSize(executorFixedPoolSize)
                .awaitTermination(false)
                .awaitTerminationPeriod(Duration.ofMinutes(awaitTerminationPeriod))
                .build();
    }

    @Bean
    public BankingApiClient bankingApiClient() {
        return new BankingApiClient();
    }

}
