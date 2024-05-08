package kr.co.fintech.withdraw.queue;

import kr.co.fintech.lib.queue.FairMessage;
import kr.co.fintech.lib.queue.FintechQueue;
import kr.co.fintech.withdraw.application.usecase.FindWithdrawUseCase;
import kr.co.fintech.withdraw.domain.Withdraw;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class WithdrawProducer {

    private final FindWithdrawUseCase findWithdrawUseCase;
    private final FintechQueue<FairMessage<Long, Long>> fintechQueue;

    @Scheduled(fixedDelay = 1000)
    public void scrap() {
        // 마지막 ID 기준으로 가져오도록 수정 필요
        // 외부에서 조건을 쓰면 테이블 컬럼이 외부에 노출되지 않을까..?
        // -> 추상화 해서 노출한다면 OCP는 괜찮을듯..
//        List<Withdraw> withdraws = findWithdrawUseCase.findAll();
//
//        List<FairMessage<Long, Long>> withdrawMessages = withdraws.stream()
//                .map(this::map)
//                .toList();

//        fintechQueue.offer(withdrawMessages);

        List<FairMessage<Long, Long>> messages = new ArrayList<>();
        for (long i = 1; i <= 3; i++) {
            for (long j = 1; j <= 10; j++) {
                messages.add(FairMessage.of(i, j));
            }
        }

        fintechQueue.offer(messages);
    }

    private FairMessage<Long, Long> map(Withdraw withdraw) {
        return FairMessage.of(withdraw.getMerchantId().getValue(), withdraw.getId().getValue());
    }


}
