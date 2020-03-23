package bankservice.it;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.uri.UriTemplate;
import org.junit.jupiter.api.Test;

class ClientsIT extends BaseIT {

  @Test
  void newClient() {
    ObjectNode clientDto = resourcesDtos.clientDto("John", "john@example.com");
    Response response = resourcesClient.postClient(clientDto);
    response.close();
    assertThat(response.getStatus(), equalTo(201));
    UriTemplate clientUriTemplate = resourcesClient.getResourcesUrls().clientUriTemplate();
    assertTrue(clientUriTemplate.match(response.getHeaderString("Location"), new ArrayList<>()));
  }
}
