package kr.co.fintech.withdraw.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = {"bank", "accountNo"})
public class Account {

    private final Bank bank;

    private final AccountNo accountNo;

    private Account(Bank bank, AccountNo accountNo) {
        this.bank = bank;
        this.accountNo = accountNo;
    }

    public static Account of(Bank bank, AccountNo accountNo) {
        return new Account(bank, accountNo);
    }

}
