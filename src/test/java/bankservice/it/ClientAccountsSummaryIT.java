package bankservice.it;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.valueOf;
import static java.util.Collections.emptyMap;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import bankservice.it.BaseIT;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.core.Response;
import org.junit.Test;

public class ClientAccountsSummaryIT extends BaseIT {

    @Test
    public void returnSummary() throws Exception {
        String clientId = UUID.randomUUID().toString();
        verifySummary(clientId, emptyMap());

        String accountId1 = stateSetup.newAccountWithBalance(clientId, valueOf(100));
        String accountId2 = stateSetup.newAccountWithBalance(clientId, valueOf(100));
        String accountId3 = stateSetup.newAccountWithBalance(clientId, valueOf(100));
        resourcesClient.postWithdrawal(accountId2, resourcesDtos.withdrawalDto(TEN)).close();
        resourcesClient.postDeposit(accountId3, resourcesDtos.depositDto(valueOf(0.01))).close();

        Map<String, BigDecimal> expectedResult = new HashMap<>();
        expectedResult.put(accountId1, BigDecimal.valueOf(100.0));
        expectedResult.put(accountId2, BigDecimal.valueOf(90.0));
        expectedResult.put(accountId3, BigDecimal.valueOf(100.01));
        verifySummary(clientId, expectedResult);
    }

    private void verifySummary(String clientId, Map<String, BigDecimal> accountInformation) {
        Response response = resourcesClient.getAccountsSummary(clientId);
        ArrayNode summary = response.readEntity(ArrayNode.class);

        summary.forEach(node -> {
            String accountId = node.get("accountId").asText();
            BigDecimal balance = new BigDecimal(node.get("balance").asText());
            assertThat(accountInformation.get(accountId), equalTo(balance));
        });
    }
}
