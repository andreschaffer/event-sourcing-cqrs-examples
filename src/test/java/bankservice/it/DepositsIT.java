package bankservice.it;

import static java.math.BigDecimal.TEN;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.math.BigDecimal;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

class DepositsIT extends BaseIT {

    @Test
    void returnAccountNotFound() {
        ObjectNode deposit = resourcesDtos.depositDto(TEN);
        Response response = resourcesClient.postDeposit(randomUUID().toString(), deposit);
        response.close();
        assertThat(response.getStatus(), equalTo(404));
    }

    @Test
    void depositAccount() {
        String accountId = stateSetup.newAccount(randomUUID().toString());
        BigDecimal amount = TEN;
        {
            ObjectNode deposit = resourcesDtos.depositDto(amount);
            Response response = resourcesClient.postDeposit(accountId, deposit);
            response.close();
            assertThat(response.getStatus(), equalTo(204));
        }
        {
            Response response = resourcesClient.getAccount(accountId);
            JsonNode account = response.readEntity(JsonNode.class);
            assertThat(account.get("balance").asDouble(), equalTo(amount.doubleValue()));
            assertThat(response.getStatus(), equalTo(200));
        }
    }
}
