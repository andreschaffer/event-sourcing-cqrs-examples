package bankservice.it;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class AccountTransactionsIT extends BaseIT {

  @Test
  void returnEmptyTransactions() {
    Response response = resourcesClient.getAccountTransactions(randomUUID().toString());
    ArrayNode transactions = response.readEntity(ArrayNode.class);
    assertThat(transactions.size(), equalTo(0));
    assertThat(response.getStatus(), equalTo(200));
  }

  @Test
  void returnTransactions() {
    String accountId = stateSetup.newAccount(randomUUID().toString());
    resourcesClient.postDeposit(accountId, resourcesDtos.depositDto(BigDecimal.valueOf(99)))
        .close();
    resourcesClient.postDeposit(accountId, resourcesDtos.depositDto(ONE)).close();
    resourcesClient.postWithdrawal(accountId, resourcesDtos.withdrawalDto(TEN)).close();

    Response response = resourcesClient.getAccountTransactions(accountId);
    ArrayNode transactions = response.readEntity(ArrayNode.class);
    assertThat(transactions.size(), equalTo(3));
    verifyTransaction(transactions.get(0), "DEPOSIT", 99.0);
    verifyTransaction(transactions.get(1), "DEPOSIT", 1.0);
    verifyTransaction(transactions.get(2), "WITHDRAWAL", 10.0);
    assertThat(response.getStatus(), equalTo(200));
  }

  private void verifyTransaction(JsonNode transaction, String type, double amount) {
    assertThat(transaction.get("type").asText(), equalTo(type));
    assertThat(transaction.get("amount").asDouble(), equalTo(amount));
    assertThat(transaction.get("timestamp"), notNullValue());
  }
}
