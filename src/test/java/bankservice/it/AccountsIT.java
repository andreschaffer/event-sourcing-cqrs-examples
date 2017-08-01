package bankservice.it;

import org.glassfish.jersey.uri.UriTemplate;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class AccountsIT extends BaseIT {

    @Test
    public void newAccount() throws Exception {
        String clientId = stateSetup.newClient("John", "john@example.com");
        Response response = resourcesClient.postAccount(resourcesDtos.accountDto(clientId));
        response.close();
        assertThat(response.getStatus(), equalTo(201));
        UriTemplate accountUriTemplate = resourcesClient.getResourcesUrls().accountUriTemplate();
        assertTrue(accountUriTemplate.match(response.getHeaderString("Location"), new ArrayList<>()));
    }
}
