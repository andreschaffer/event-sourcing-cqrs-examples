package bankservice.it;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.UUID;
import javax.ws.rs.core.Response;
import org.junit.Test;

public class AccountIT extends BaseIT {

    @Test
    public void returnAccount() throws Exception {
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
    public void returnAccountNotFound() throws Exception {
        Response response = resourcesClient.getAccount(randomUUID().toString());
        response.close();
        assertThat(response.getStatus(), equalTo(404));
    }
}
