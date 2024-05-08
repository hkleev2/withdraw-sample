package kr.co.fintech.withdraw.application.out;

import java.time.LocalDateTime;

public class WithdrawResult {

    private final static String SUCCESS_CODE = "0000";

    private LocalDateTime transactionDateTime;

    private String code;

    public boolean isSuccess() {
        return SUCCESS_CODE.equals(code);
    }

}
