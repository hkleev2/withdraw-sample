package kr.co.fintech.lib.queue;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class BlockingFairMessageBrokerTest {

    @DisplayName("미리 준비된 메시지를 소비자 1개로 처리")
    @RepeatedTest(10)
    void handlePreparedMessagesByOneConsumer() {
        FintechQueue<FairMessage<Integer, Integer>> messageBroker = new FairQueue<>();

        messageBroker.offer(FairMessage.of(1, 1));
        messageBroker.offer(FairMessage.of(2, 1));
        messageBroker.offer(FairMessage.of(2, 2));
        messageBroker.offer(FairMessage.of(3, 1));
        messageBroker.offer(FairMessage.of(3, 2));
        messageBroker.offer(FairMessage.of(3, 3));
        messageBroker.offer(FairMessage.of(4, 1));
        messageBroker.offer(FairMessage.of(4, 2));
        messageBroker.offer(FairMessage.of(4, 3));
        messageBroker.offer(FairMessage.of(4, 4));

        List<FairMessage<Integer, Integer>> expectedList = new ArrayList<>();
        expectedList.add(FairMessage.of(1, 1));
        expectedList.add(FairMessage.of(2, 1));
        expectedList.add(FairMessage.of(3, 1));
        expectedList.add(FairMessage.of(4, 1));
        expectedList.add(FairMessage.of(2, 2));
        expectedList.add(FairMessage.of(3, 2));
        expectedList.add(FairMessage.of(4, 2));
        expectedList.add(FairMessage.of(3, 3));
        expectedList.add(FairMessage.of(4, 3));
        expectedList.add(FairMessage.of(4, 4));

        int expectedIndex = 0;
        while (true) {
            FairMessage<Integer, Integer> message = messageBroker.poll();

            if (message == null) {
                continue;
            }

            assertThat(message).isEqualTo(expectedList.get(expectedIndex++));

            if (expectedIndex == expectedList.size()) {
                break;
            }
        }
    }

    @DisplayName("미리 준비된 메시지를 소비자 N개 (멀티쓰레드)로 처리")
    @RepeatedTest(10)
    void handlePreparedMessagesByMultipleConsumers() throws InterruptedException {
        BlockingEventLoggingWrapper<FairMessage<Integer, Integer>> messageBroker = new BlockingEventLoggingWrapper<>(new FairQueue<>());

        messageBroker.offer(FairMessage.of(1, 1));
        messageBroker.offer(FairMessage.of(2, 1));
        messageBroker.offer(FairMessage.of(2, 2));
        messageBroker.offer(FairMessage.of(3, 1));
        messageBroker.offer(FairMessage.of(3, 2));
        messageBroker.offer(FairMessage.of(3, 3));
        messageBroker.offer(FairMessage.of(4, 1));
        messageBroker.offer(FairMessage.of(4, 2));
        messageBroker.offer(FairMessage.of(4, 3));
        messageBroker.offer(FairMessage.of(4, 4));

        int threadCount = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch countDownLatch = new CountDownLatch(10);

        for (int i = 0; i < threadCount; i++) {
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

        List<FairMessage<Integer, Integer>> expectedList = new ArrayList<>();
        expectedList.add(FairMessage.of(1, 1));
        expectedList.add(FairMessage.of(2, 1));
        expectedList.add(FairMessage.of(3, 1));
        expectedList.add(FairMessage.of(4, 1));
        expectedList.add(FairMessage.of(2, 2));
        expectedList.add(FairMessage.of(3, 2));
        expectedList.add(FairMessage.of(4, 2));
        expectedList.add(FairMessage.of(3, 3));
        expectedList.add(FairMessage.of(4, 3));
        expectedList.add(FairMessage.of(4, 4));

        List<EventLog<FairMessage<Integer, Integer>>> eventLogs = messageBroker.getEventLogs();

        int expectedMessageIndex = 0;
        for (EventLog<FairMessage<Integer, Integer>> eventLog : eventLogs) {
            FairMessage<Integer, Integer> message = eventLog.getMessage();
            if (eventLog.getEventType().equals(EventLog.EventType.POLL) && message != null) {
                assertThat(message).isEqualTo(expectedList.get(expectedMessageIndex++));
            }
        }

    }

}
