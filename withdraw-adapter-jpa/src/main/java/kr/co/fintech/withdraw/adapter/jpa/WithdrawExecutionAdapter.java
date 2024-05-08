package kr.co.fintech.withdraw.adapter.jpa;


import kr.co.fintech.bank.api.BankingApiClient;
import kr.co.fintech.bank.api.WithdrawRequest;
import kr.co.fintech.bank.api.WithdrawResponse;
import kr.co.fintech.withdraw.application.out.FindWithdrawPort;
import kr.co.fintech.withdraw.application.out.SaveWithdrawPort;
import kr.co.fintech.withdraw.application.out.WithdrawExecutionPort;
import kr.co.fintech.withdraw.application.out.WithdrawResult;
import kr.co.fintech.withdraw.domain.Account;
import kr.co.fintech.withdraw.domain.Amount;
import kr.co.fintech.withdraw.domain.Withdraw;
import kr.co.fintech.withdraw.domain.WithdrawId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class WithdrawExecutionAdapter implements WithdrawExecutionPort, FindWithdrawPort, SaveWithdrawPort {

    private final BankingApiClient bankingApiClient;

    private final WithdrawRepository withdrawRepository;

    @Override
    public WithdrawResult withdraw(Account source, Account destination, Amount amount) {
        WithdrawResponse withdrawResponse = bankingApiClient.withdraw(new WithdrawRequest());

        return map(withdrawResponse);
    }

    private WithdrawResult map(WithdrawResponse withdrawResponse) {
        return new WithdrawResult();
    }

    @Override
    public Optional<Withdraw> findById(WithdrawId withdrawId) {
        return withdrawRepository.findById(1L).map(WithdrawEntity::toDomain);
    }

    @Override
    public List<Withdraw> findAll() {
        return List.of();
    }

    @Override
    public void save(Withdraw withdraw) {
        WithdrawEntity withdrawEntity = WithdrawEntity.of(withdraw);
        withdrawRepository.save(withdrawEntity);
    }


}
