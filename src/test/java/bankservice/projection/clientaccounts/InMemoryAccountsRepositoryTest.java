package bankservice.projection.clientaccounts;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.UUID;
import org.junit.Test;

public class InMemoryAccountsRepositoryTest {

    private AccountsRepository accountsRepository =
            new InMemoryAccountsRepository();

    @Test
    public void ignoreEventOutOfOrder() throws Exception {
        UUID clientId = UUID.randomUUID();
        UUID accountId = UUID.randomUUID();
        accountsRepository.save(new AccountProjection(accountId, clientId, ZERO, 1));
        accountsRepository.updateBalance(accountId, TEN, 3);
        accountsRepository.updateBalance(accountId, ONE, 2);
        assertThat(accountsRepository.getAccounts(clientId).get(0).getBalance(), equalTo(TEN));
    }
}