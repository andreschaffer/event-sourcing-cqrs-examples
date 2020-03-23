package bankservice.projection.accounttransactions;

import static bankservice.projection.accounttransactions.TransactionProjection.TransactionType.DEPOSIT;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.now;
import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class InMemoryTransactionsRepositoryTest {

  private TransactionsRepository transactionsRepository =
      new InMemoryTransactionsRepository();

  @Test
  void listEventsSortedByVersion() {
    UUID accountId = randomUUID();
    TransactionProjection tx2 = new TransactionProjection(accountId, DEPOSIT, TEN, now(UTC), 2);
    TransactionProjection tx1 = new TransactionProjection(accountId, DEPOSIT, ONE, now(UTC), 1);
    transactionsRepository.save(tx2);
    transactionsRepository.save(tx1);

    List<TransactionProjection> transactions = transactionsRepository.listByAccount(accountId);
    assertThat(transactions.get(0), equalTo(tx1));
    assertThat(transactions.get(1), equalTo(tx2));
  }
}