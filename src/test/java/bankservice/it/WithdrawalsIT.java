package bankservice.it;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.valueOf;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.math.BigDecimal;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

class WithdrawalsIT extends BaseIT {

    @Test
    void returnAccountNotFound() {
        ObjectNode deposit = resourcesDtos.withdrawalDto(TEN);
        Response response = resourcesClient.postWithdrawal(randomUUID().toString(), deposit);
        response.close();
        assertThat(response.getStatus(), equalTo(404));
    }

    @Test
    void withdrawAccount() {
        BigDecimal previousBalance = valueOf(9.99);
        BigDecimal withdrawalAmount = valueOf(5.55);
        BigDecimal expectedBalance = valueOf(4.44);
        String accountId = stateSetup.newAccountWithBalance(randomUUID().toString(), previousBalance);
        {
            ObjectNode withdrawal = resourcesDtos.withdrawalDto(withdrawalAmount);
            Response response = resourcesClient.postWithdrawal(accountId, withdrawal);
            response.close();
            assertThat(response.getStatus(), equalTo(204));
        }
        {
            Response response = resourcesClient.getAccount(accountId);
            JsonNode account = response.readEntity(JsonNode.class);
            assertThat(account.get("balance").asDouble(), equalTo(expectedBalance.doubleValue()));
            assertThat(response.getStatus(), equalTo(200));
        }
    }

    @Test
    void returnBadRequestForNonSufficientFunds() {
        String accountId = stateSetup.newAccount(randomUUID().toString());
        ObjectNode withdrawal = resourcesDtos.withdrawalDto(valueOf(1000));
        Response response = resourcesClient.postWithdrawal(accountId, withdrawal);
        response.close();
        assertThat(response.getStatus(), equalTo(400));
    }
}
