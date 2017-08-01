package bankservice.it;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.valueOf;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class WithdrawalsIT extends BaseIT {

    @Test
    public void returnAccountNotFound() throws Exception {
        ObjectNode deposit = resourcesDtos.withdrawalDto(TEN);
        Response response = resourcesClient.postWithdrawal(randomUUID().toString(), deposit);
        response.close();
        assertThat(response.getStatus(), equalTo(404));
    }

    @Test
    public void withdrawAccount() throws Exception {
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
    public void returnBadRequestForNonSufficientFunds() throws Exception {
        String accountId = stateSetup.newAccount(randomUUID().toString());
        ObjectNode withdrawal = resourcesDtos.withdrawalDto(valueOf(1000));
        Response response = resourcesClient.postWithdrawal(accountId, withdrawal);
        response.close();
        assertThat(response.getStatus(), equalTo(400));
    }
}
