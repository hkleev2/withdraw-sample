package kr.co.fintech.withdraw.application.out;


import kr.co.fintech.withdraw.domain.Withdraw;
import kr.co.fintech.withdraw.domain.WithdrawId;

import java.util.List;
import java.util.Optional;

public interface FindWithdrawPort {
    Optional<Withdraw> findById(WithdrawId withdrawId);

    List<Withdraw> findAll();
}
