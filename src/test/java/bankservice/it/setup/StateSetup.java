package bankservice.it.setup;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import bankservice.it.client.ResourcesClient;
import bankservice.it.client.ResourcesDtos;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.uri.UriTemplate;

public class StateSetup {

    private final ResourcesClient resourcesClient;
    private final ResourcesDtos resourcesDtos;

    public StateSetup(ResourcesClient resourcesClient, ResourcesDtos resourcesDtos) {
        this.resourcesClient = checkNotNull(resourcesClient);
        this.resourcesDtos = checkNotNull(resourcesDtos);
    }

    public String newClient(String name, String email) {
        ObjectNode clientDto = resourcesDtos.clientDto(name, email);
        Response response = resourcesClient.postClient(clientDto);
        response.close();
        assertThat(response.getStatus(), equalTo(201));
        Map<String, String> params = new HashMap<>();
        UriTemplate clientUriTemplate = resourcesClient.getResourcesUrls().clientUriTemplate();
        assertTrue(clientUriTemplate.match(response.getHeaderString("Location"), params));
        return params.get("id");
    }

    public String newAccount(String clientId) {
        Response response = resourcesClient.postAccount(resourcesDtos.accountDto(clientId));
        response.close();
        assertThat(response.getStatus(), equalTo(201));
        Map<String, String> params = new HashMap<>();
        UriTemplate accountUriTemplate = resourcesClient.getResourcesUrls().accountUriTemplate();
        assertTrue(accountUriTemplate.match(response.getHeaderString("Location"), params));
        return params.get("id");
    }

    public String newAccountWithBalance(String clientId, BigDecimal balance) {
        String accountId = newAccount(clientId);
        ObjectNode deposit = resourcesDtos.depositDto(balance);
        Response response = resourcesClient.postDeposit(accountId, deposit);
        response.close();
        assertThat(response.getStatus(), equalTo(204));
        return accountId;
    }
}
