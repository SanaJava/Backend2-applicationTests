package se.nackademin.java20.lab1.application;

import org.junit.jupiter.api.Test;
import se.nackademin.java20.lab1.domain.Account;
import se.nackademin.java20.lab1.domain.UserService;
import se.nackademin.java20.lab1.persistance.AccountRepository;
import se.nackademin.java20.lab1.domain.RiskAssesment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PersonalFinanceServiceTest {

    @Test
    void shouldOpenAccountWhenRiskAssessmentPasses() {
        final AccountRepository accountRepository = mock(AccountRepository.class);
        final RiskAssesment riskAssessment = mock(RiskAssesment.class);
        final UserService userService = mock(UserService.class);
        final PersonalFinanceService personalFinanceService = new PersonalFinanceService(accountRepository, riskAssessment, userService);

        when(accountRepository.save(any(Account.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        when(riskAssessment.isPassingCreditCheck("Dan")).thenReturn(true);
        doNothing().when(userService).enroll(any(), any());

        final Account account = personalFinanceService.openAccount("Dan", "password");

        assertEquals(account.getHolder(), "Dan");
        assertEquals(account.getBalance(), 0L);
        verify(riskAssessment).isPassingCreditCheck(eq("Dan"));
        verify(accountRepository).save(any());
    }

    @Test
    void shouldNotOpenAccountWhenRiskAssessmentFailes() {
        final AccountRepository accountRepository = mock(AccountRepository.class);
        final RiskAssesment riskAssessment = mock(RiskAssesment.class);
        final UserService userService = mock(UserService.class);
        final PersonalFinanceService personalFinanceService = new PersonalFinanceService(accountRepository, riskAssessment, userService);

        when(riskAssessment.isPassingCreditCheck("Dan")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> personalFinanceService.openAccount("Dan", "password"));

        verifyNoInteractions(accountRepository);
    }

}