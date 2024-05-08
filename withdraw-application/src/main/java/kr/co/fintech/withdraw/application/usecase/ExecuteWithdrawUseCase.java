package kr.co.fintech.withdraw.application.usecase;

import kr.co.fintech.withdraw.application.out.FindWithdrawPort;
import kr.co.fintech.withdraw.application.out.SaveWithdrawPort;
import kr.co.fintech.withdraw.application.out.WithdrawExecutionPort;
import kr.co.fintech.withdraw.application.out.WithdrawResult;
import kr.co.fintech.withdraw.domain.Withdraw;
import kr.co.fintech.withdraw.domain.WithdrawId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ExecuteWithdrawUseCase {

    private final FindWithdrawPort findWithdrawPort;
    private final SaveWithdrawPort saveWithdrawPort;
    private final WithdrawExecutionPort withdrawExecutionPort;

    public void withdraw(WithdrawId withdrawId) {
        Withdraw withdraw = getWithdraw(withdrawId);
        withdraw.onProgress();
        saveWithdrawPort.save(withdraw);

        WithdrawResult withdrawResult = null;

        try {
            withdrawResult = withdrawExecutionPort.withdraw(withdraw.getSourceAccount(), withdraw.getDestinationAccount(), withdraw.getAmount());
        } catch (WithdrawExecutionPort.InvalidReqMessage e) {
            log.error("Withdraw invalid request message.", e);
            withdraw.errorBeforeExecution();
        } catch (WithdrawExecutionPort.InvalidResMessage e) {
            log.error("Withdraw invalid response message.", e);
            withdraw.errorAfterExecution();
        } catch (WithdrawExecutionPort.TimeoutException e) {
            log.error("Withdraw timeout.", e);
            withdraw.errorAfterExecution();
        } catch (Exception e) {
            log.error("Withdraw unknown exception.", e);
            withdraw.unknownError();
        }

        // 메시지 발행 전
        if (withdrawResult != null) {
            withdraw.reflect();
        }

        saveWithdrawPort.save(withdraw);
    }

    private Withdraw getWithdraw(WithdrawId withdrawId) {
        return findWithdrawPort.findById(withdrawId).orElseThrow();
    }

}
