package kr.co.fintech.withdraw.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Withdraw {

    private WithdrawId id;

    private MerchantId merchantId;

    private WithdrawState state;

    private Account sourceAccount;

    private Account destinationAccount;

    private Amount amount;

    public void onProgress() {
        this.state = WithdrawState.ON_PROGRESS;
    }

    public void reflect() {
        this.state = WithdrawState.SUCCESS;
    }

    public void errorAfterExecution() {
        this.state = WithdrawState.ERROR_AFTER_EXECUTION;
    }

    public void errorBeforeExecution() {
        this.state = WithdrawState.ERROR_BEFORE_EXECUTION;
    }

    public void unknownError() {
        this.state = WithdrawState.UNKNOWN_ERROR;
    }

}
