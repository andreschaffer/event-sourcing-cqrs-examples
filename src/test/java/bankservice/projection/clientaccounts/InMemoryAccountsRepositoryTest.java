package bankservice.projection.clientaccounts;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.UUID;
import org.junit.jupiter.api.Test;

class InMemoryAccountsRepositoryTest {

  private AccountsRepository accountsRepository =
      new InMemoryAccountsRepository();

  @Test
  void ignoreEventOutOfOrder() {
    UUID clientId = UUID.randomUUID();
    UUID accountId = UUID.randomUUID();
    accountsRepository.save(new AccountProjection(accountId, clientId, ZERO, 1));
    accountsRepository.updateBalance(accountId, TEN, 3);
    accountsRepository.updateBalance(accountId, ONE, 2);
    assertThat(accountsRepository.getAccounts(clientId).get(0).getBalance(), equalTo(TEN));
  }
}