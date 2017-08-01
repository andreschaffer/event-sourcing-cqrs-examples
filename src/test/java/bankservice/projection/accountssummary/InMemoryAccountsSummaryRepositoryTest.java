package bankservice.projection.accountssummary;

import org.junit.Test;

import java.util.UUID;

import static java.math.BigDecimal.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class InMemoryAccountsSummaryRepositoryTest {

    private AccountsSummaryRepository accountsSummaryRepository =
            new InMemoryAccountsSummaryRepository();

    @Test
    public void ignoreEventOutOfOrder() throws Exception {
        UUID clientId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        accountsSummaryRepository.addNewAccount(clientId, accountId, ZERO, 1);
        accountsSummaryRepository.updateBalance(accountId, TEN, 3);
        accountsSummaryRepository.updateBalance(accountId, ONE, 2);
        assertThat(accountsSummaryRepository.getAccounts(clientId).get(0).getBalance(), equalTo(TEN));
    }
}