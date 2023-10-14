package bankservice.it;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import org.glassfish.jersey.uri.UriTemplate;
import org.junit.jupiter.api.Test;

class AccountsIT extends BaseIT {

  @Test
  void newAccount() {
    String clientId = stateSetup.newClient("John", "john@example.com");
    Response response = resourcesClient.postAccount(resourcesDtos.accountDto(clientId));
    response.close();
    assertThat(response.getStatus(), equalTo(201));
    UriTemplate accountUriTemplate = resourcesClient.getResourcesUrls().accountUriTemplate();
    assertTrue(accountUriTemplate.match(response.getHeaderString("Location"), new ArrayList<>()));
  }
}
