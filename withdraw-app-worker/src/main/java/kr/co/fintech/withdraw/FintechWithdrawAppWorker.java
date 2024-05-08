package kr.co.fintech.withdraw;

import kr.co.fintech.lib.queue.FairMessage;
import kr.co.fintech.lib.queue.FintechQueue;
import kr.co.fintech.withdraw.application.usecase.ExecuteWithdrawUseCase;
import kr.co.fintech.withdraw.queue.WithdrawConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@SpringBootApplication
public class FintechWithdrawAppWorker implements CommandLineRunner, ApplicationListener<ContextClosedEvent> {

    @Value("${withdraw.executorFixedPoolSize: 3}")
    private Integer executorFixedPoolSize;

    private final ExecuteWithdrawUseCase executeWithdrawUseCase;
    private final ThreadPoolTaskExecutor withdrawExecutor;
    private final FintechQueue<FairMessage<Long, Long>> queue;

    public static void main(String[] args) {
        SpringApplication.run(FintechWithdrawAppWorker.class, args);
    }

    @Override
    public void run(String... args) {
        for (int i = 0; i < executorFixedPoolSize; i++) {
            withdrawExecutor.submit(() -> new WithdrawConsumer(executeWithdrawUseCase, queue).exec());
        }
    }

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        log.info("On close event");
        withdrawExecutor.shutdown();
    }
}
