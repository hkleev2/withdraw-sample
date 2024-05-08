package kr.co.fintech.withdraw.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@Getter
@EqualsAndHashCode(of = "value")
public class AccountNo {

    private final String value;

    private AccountNo(String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static AccountNo of(String value) {
        return new AccountNo(value);
    }

}
