package kr.co.fintech.withdraw.queue;

import kr.co.fintech.lib.queue.FairMessage;
import kr.co.fintech.lib.queue.FintechQueue;
import kr.co.fintech.withdraw.application.usecase.ExecuteWithdrawUseCase;
import kr.co.fintech.withdraw.domain.WithdrawId;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WithdrawConsumer {

    private final ExecuteWithdrawUseCase executeWithdrawUseCase;

    private final FintechQueue<FairMessage<Long, Long>> withdrawQueue;

    public WithdrawConsumer(ExecuteWithdrawUseCase executeWithdrawUseCase, FintechQueue<FairMessage<Long, Long>> withdrawQueue) {
        this.executeWithdrawUseCase = executeWithdrawUseCase;
        this.withdrawQueue = withdrawQueue;
    }

    public void exec() {
        while (!Thread.currentThread().isInterrupted()) {
            FairMessage<Long, Long> message = withdrawQueue.poll();

            if (message == null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                continue;
            }

            executeWithdrawUseCase.withdraw(WithdrawId.of(message.getValue()));
        }
    }
}
