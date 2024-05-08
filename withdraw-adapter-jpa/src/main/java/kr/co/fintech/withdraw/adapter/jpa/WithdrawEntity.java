package kr.co.fintech.withdraw.adapter.jpa;

import jakarta.persistence.*;
import kr.co.fintech.withdraw.domain.MerchantId;
import kr.co.fintech.withdraw.domain.Withdraw;
import kr.co.fintech.withdraw.domain.WithdrawId;
import kr.co.fintech.withdraw.domain.WithdrawState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WithdrawEntity {

    @Id
    @Column(name = "TID", nullable = false)
    private Long id;

    private Long merchantId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private WithdrawState state;

    private String resultCode;

    public static WithdrawEntity of(Withdraw withdraw) {
        WithdrawEntityBuilder builder = WithdrawEntity.builder();

        builder.id(withdraw.getId().getValue());
        builder.merchantId(withdraw.getMerchantId().getValue());
        builder.state(withdraw.getState());

        return builder.build();
    }

    public Withdraw toDomain() {
        return Withdraw.builder()
                .id(WithdrawId.of(id))
                .merchantId(MerchantId.of(merchantId))
                .build();
    }
}
