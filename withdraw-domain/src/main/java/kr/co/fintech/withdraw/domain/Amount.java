package kr.co.fintech.withdraw.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@EqualsAndHashCode(of = "value")
public class Amount {

    private final BigDecimal value;

    private Amount(BigDecimal value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static Amount of(BigDecimal value) {
        return new Amount(value);
    }

}
