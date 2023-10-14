package bankservice.it;

import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.ws.rs.core.Response;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AccountIT extends BaseIT {

  @Test
  void returnAccount() {
    String clientId = UUID.randomUUID().toString();
    String accountId = stateSetup.newAccount(clientId);
    Response response = resourcesClient.getAccount(accountId);
    JsonNode account = response.readEntity(JsonNode.class);
    assertThat(account.get("id").asText(), equalTo(accountId));
    assertThat(account.get("clientId").asText(), equalTo(clientId.toString()));
    assertThat(account.get("balance").asDouble(), equalTo(0.0));
    assertThat(response.getStatus(), equalTo(200));
  }

  @Test
  void returnAccountNotFound() {
    Response response = resourcesClient.getAccount(randomUUID().toString());
    response.close();
    assertThat(response.getStatus(), equalTo(404));
  }
}
