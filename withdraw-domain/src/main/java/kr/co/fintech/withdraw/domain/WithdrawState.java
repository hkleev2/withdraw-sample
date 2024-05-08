package kr.co.fintech.withdraw.domain;

public enum WithdrawState {

    /**
     * 출금 처리 여부 알 수 없음
     * 출금서비스 제공 업체에 실제 출금 여부 확인 필수
     */
    ON_PROGRESS,

    /**
     * 출금 성공
     */
    SUCCESS,

    /**
     * 출금 처리 전 에러
     */
    ERROR_BEFORE_EXECUTION,

    /**
     * 출금 처리 여부 알 수 없는 상태
     * 출금서비스 제공 업체에 실제 출금 여부 확인 필수
     */
    ERROR_AFTER_EXECUTION,

    /**
     * 출금 처리 여부 알 수 없는 상태
     * 출금서비스 제공 업체에 실제 출금 여부 확인 필수
     */
    UNKNOWN_ERROR
}
