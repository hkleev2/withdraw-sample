package kr.co.fintech.withdraw.application.out;

import kr.co.fintech.withdraw.domain.Account;
import kr.co.fintech.withdraw.domain.Amount;

public interface WithdrawExecutionPort {

    /**
     * 출금 서비스 제공업체에 출금을 요청한다. ex) 저축은행 소켓 전문 요청
     * @param source 출금처 계좌
     * @param destination 입금처 계좌
     * @param amount 이체 금액
     * @throws TimeoutException 통신 타임아웃
     * @throws InvalidReqMessage 유효하지 않은 요청, 응답 메시지
     * @throws IllegalArgumentException 잘못된 파라미터
     */
    WithdrawResult withdraw(Account source, Account destination, Amount amount);

    class TimeoutException extends RuntimeException {

    }

    class InvalidReqMessage extends RuntimeException {

    }

    class InvalidResMessage extends RuntimeException {

    }

}
