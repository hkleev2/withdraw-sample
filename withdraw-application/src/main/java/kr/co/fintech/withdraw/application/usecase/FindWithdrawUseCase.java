package kr.co.fintech.withdraw.application.usecase;

import kr.co.fintech.withdraw.application.out.FindWithdrawPort;
import kr.co.fintech.withdraw.domain.Withdraw;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FindWithdrawUseCase {

    private final FindWithdrawPort findWithdrawPort;

    public List<Withdraw> findAll() {
        return findWithdrawPort.findAll();
    }

}
