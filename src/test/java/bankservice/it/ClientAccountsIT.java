package bankservice.it;

import static java.math.BigDecimal.TEN;
import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.fasterxml.jackson.databind.node.ArrayNode;
import jakarta.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ClientAccountsIT extends BaseIT {

  @Test
  void returnClientAccounts() {
    String clientId = UUID.randomUUID().toString();
    verifyClientAccounts(clientId, emptyMap());

    String accountId1 = stateSetup.newAccountWithBalance(clientId, BigDecimal.valueOf(100));
    String accountId2 = stateSetup.newAccountWithBalance(clientId, BigDecimal.valueOf(100));
    String accountId3 = stateSetup.newAccountWithBalance(clientId, BigDecimal.valueOf(100));
    resourcesClient.postWithdrawal(accountId2, resourcesDtos.withdrawalDto(TEN)).close();
    resourcesClient.postDeposit(accountId3, resourcesDtos.depositDto(BigDecimal.valueOf(0.01)))
        .close();

    Map<String, BigDecimal> expectedAccountsBalances = new HashMap<>();
    expectedAccountsBalances.put(accountId1, BigDecimal.valueOf(100.0));
    expectedAccountsBalances.put(accountId2, BigDecimal.valueOf(90.0));
    expectedAccountsBalances.put(accountId3, BigDecimal.valueOf(100.01));
    verifyClientAccounts(clientId, expectedAccountsBalances);
  }

  private void verifyClientAccounts(String clientId,
      Map<String, BigDecimal> expectedAccountsBalances) {
    Response response = resourcesClient.getClientAccounts(clientId);
    ArrayNode accounts = response.readEntity(ArrayNode.class);

    accounts.forEach(account -> {
      String accountId = account.get("accountId").asText();
      BigDecimal balance = new BigDecimal(account.get("balance").asText());
      assertThat(balance, equalTo(expectedAccountsBalances.get(accountId)));
    });
  }
}
