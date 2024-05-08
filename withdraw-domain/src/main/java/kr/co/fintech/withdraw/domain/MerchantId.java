package kr.co.fintech.withdraw.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@Getter
@EqualsAndHashCode(of = "value")
public class MerchantId {

    private final Long value;

    private MerchantId(Long value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static MerchantId of(Long value) {
        return new MerchantId(value);
    }

}
