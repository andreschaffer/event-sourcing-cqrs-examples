package bankservice.projection.clientaccountssummary;

import org.junit.Test;

import java.util.UUID;

import static java.math.BigDecimal.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class InMemoryClientAccountsSummaryRepositoryTest {

    private ClientAccountsSummaryRepository clientAccountsSummaryRepository =
            new InMemoryClientAccountsSummaryRepository();

    @Test
    public void ignoreEventOutOfOrder() throws Exception {
        UUID clientId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        clientAccountsSummaryRepository.addNewAccount(clientId, accountId, ZERO, 1);
        clientAccountsSummaryRepository.updateBalance(accountId, TEN, 3);
        clientAccountsSummaryRepository.updateBalance(accountId, ONE, 2);
        assertThat(clientAccountsSummaryRepository.getAccounts(clientId).get(0).getBalance(), equalTo(TEN));
    }
}