package kr.co.fintech.withdraw.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@Getter
@EqualsAndHashCode(of = "value")
public class WithdrawId {

    private final Long value;

    private WithdrawId(Long value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static WithdrawId of(Long value) {
        return new WithdrawId(value);
    }

}
