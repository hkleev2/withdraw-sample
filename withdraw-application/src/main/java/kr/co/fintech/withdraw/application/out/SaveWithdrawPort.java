package kr.co.fintech.withdraw.application.out;


import kr.co.fintech.withdraw.domain.Withdraw;

public interface SaveWithdrawPort {
    void save(Withdraw withdraw);
}
