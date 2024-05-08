package kr.co.fintech.lib.queue;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class BlockingFairMessageBrokerEventLogTest {

    @DisplayName("메시지 생성자 1개와 메시지 소비자 1개가 각 쓰레드에서 처리")
    @RepeatedTest(10)
    void oneProducerOneConsumer() throws InterruptedException {
        BlockingEventLoggingWrapper<FairMessage<Integer, Integer>> messageBroker = new BlockingEventLoggingWrapper<>(new FairQueue<>());

        Thread thread = new Thread(() -> {
            for (int message = 0; message < 300; message++) {
                for (int fairKey = 0; fairKey < 3; fairKey++) {
                    messageBroker.offer(FairMessage.of(fairKey, message));
                }
            }
        });
        thread.start();

        CountDownLatch countDownLatch = new CountDownLatch(900);
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 3; i++) {
            executorService.execute(() -> {
                while (countDownLatch.getCount() != 0) {

                    FairMessage<Integer, Integer> message = messageBroker.poll();
                    if (message == null) {
                        continue;
                    }

                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        executorService.shutdown();

        if (!executorService.awaitTermination(1, TimeUnit.MINUTES)) {
            throw new RuntimeException();
        }

        messageBroker.getEventLogs().forEach(eventLog -> {
            log.info("{}", eventLog);
        });
        testByEventLog(messageBroker.getEventLogs(), new FairQueue<>());
    }

    @DisplayName("생산자, 소비자 모두 여러개의 멀티쓰레드로 처리")
    @RepeatedTest(10)
    void multipleProducersAndConsumers() throws InterruptedException {
        final int producerThreadCount = 4;
        final int consumerThreadCount = 4;
        final int messageCountPerProducer = 500;
        CountDownLatch countDownLatch = new CountDownLatch(producerThreadCount * messageCountPerProducer);

        BlockingEventLoggingWrapper<FairMessage<Integer, Integer>> messageBroker = new BlockingEventLoggingWrapper<>(new FairQueue<>());

        ExecutorService producerExecutor = Executors.newFixedThreadPool(producerThreadCount);
        ExecutorService consumerExecutor = Executors.newFixedThreadPool(consumerThreadCount);

        long start = System.currentTimeMillis();

        for (int i = 0; i < producerThreadCount; i++) {
            producerExecutor.submit(() -> {
                List<FairMessage<Integer, Integer>> messages = new ArrayList<>();
                for (int j = 0; j < messageCountPerProducer; j++) {
                    messages.add(getRandomMessage());
                }

                messageBroker.offer(messages);
            });
        }

        for (int i = 0; i < consumerThreadCount; i++) {
            consumerExecutor.submit(() -> {
                while (countDownLatch.getCount() > 0) {
                    if (messageBroker.poll() != null) {
                        countDownLatch.countDown();
                        continue;
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        countDownLatch.await();

        log.info("{}ms", System.currentTimeMillis() - start);

        producerExecutor.shutdown();
        consumerExecutor.shutdown();

        if (!producerExecutor.awaitTermination(1, TimeUnit.MINUTES) || !consumerExecutor.awaitTermination(1, TimeUnit.MINUTES)) {
            throw new RuntimeException();
        }

        testByEventLog(messageBroker.getEventLogs(), new FairQueue<>());
    }

    private FairMessage<Integer, Integer> getRandomMessage() {
        return FairMessage.of(ThreadLocalRandom.current().nextInt(1, 101), ThreadLocalRandom.current().nextInt(1, 101));
    }

    private <Message> void testByEventLog(List<EventLog<Message>> eventLogs, FintechQueue<Message> messageBroker) {
        for (EventLog<Message> eventLog : eventLogs) {
            EventLog.EventType eventType = eventLog.getEventType();

            switch (eventType) {
                case OFFER -> messageBroker.offer(eventLog.getMessage());
                case POLL -> assertThat(messageBroker.poll()).isEqualTo(eventLog.getMessage());
                default -> throw new IllegalStateException();
            }
        }
    }
}
