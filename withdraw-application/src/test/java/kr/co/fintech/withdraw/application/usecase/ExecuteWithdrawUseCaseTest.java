package kr.co.fintech.withdraw.application.usecase;

import kr.co.fintech.withdraw.application.out.FindWithdrawPort;
import kr.co.fintech.withdraw.application.out.SaveWithdrawPort;
import kr.co.fintech.withdraw.application.out.WithdrawExecutionPort;
import kr.co.fintech.withdraw.application.out.WithdrawResult;
import kr.co.fintech.withdraw.domain.*;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ExecuteWithdrawUseCaseTest {

    private Map<WithdrawId, Withdraw> mockStorage;
    private ExecuteWithdrawUseCase executeWithdrawUseCase;
    private FindWithdrawPort findWithdrawPort;
    private SaveWithdrawPort saveWithdrawPort;


    @BeforeEach
    void setup() {
        ThrowExceptionWithdrawExecutionExecutionPort withdrawPort = new ThrowExceptionWithdrawExecutionExecutionPort();
        this.mockStorage = new HashMap<>();
        this.findWithdrawPort = new MockFindWithdrawPort(mockStorage);
        this.saveWithdrawPort = new MockSaveWithdrawPort(mockStorage);
        this.executeWithdrawUseCase = new ExecuteWithdrawUseCase(findWithdrawPort, saveWithdrawPort, withdrawPort);
    }

    @RequiredArgsConstructor
    private static class MockFindWithdrawPort implements FindWithdrawPort {
        private final Map<WithdrawId, Withdraw> mockStorage;

        @Override
        public Optional<Withdraw> findById(WithdrawId withdrawId) {
            return Optional.ofNullable(mockStorage.get(withdrawId));
        }

        @Override
        public List<Withdraw> findAll() {
            return List.of();
        }
    }

    @RequiredArgsConstructor
    private static class MockSaveWithdrawPort implements SaveWithdrawPort {
        private final Map<WithdrawId, Withdraw> mockStorage;

        @Override
        public void save(Withdraw withdraw) {
            mockStorage.put(withdraw.getId(), withdraw);
        }
    }

    private static class ThrowExceptionWithdrawExecutionExecutionPort implements WithdrawExecutionPort {
        @Override
        public WithdrawResult withdraw(Account source, Account destination, Amount amount) {
            throw new RuntimeException();
        }
    }

    @DisplayName("출금 API 요청 후 예외가 발생해도 롤백되지 않음")
    @Test
    void t() {
        // given
        Withdraw givenWithdraw = Withdraw.builder()
                .id(WithdrawId.of(1L))
                .merchantId(MerchantId.of(1L))
                .build();

        saveWithdrawPort.save(givenWithdraw);

        // when
        executeWithdrawUseCase.withdraw(WithdrawId.of(1L));

        // then
        Withdraw withdraw = findWithdrawPort.findById(WithdrawId.of(1L)).orElseThrow();
        assertThat(withdraw.getState()).isEqualTo(WithdrawState.UNKNOWN_ERROR);
    }

}