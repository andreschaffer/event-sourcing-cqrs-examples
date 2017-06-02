package bankservice.it;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.glassfish.jersey.uri.UriTemplate;
import org.junit.Test;

import javax.ws.rs.core.Response;
import java.util.ArrayList;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ClientsIT extends BaseIT {

    @Test
    public void newClient() throws Exception {
        ObjectNode clientDto = resourcesDtos.clientDto("John", "john@example.com");
        Response response = resourcesClient.postClient(clientDto);
        response.close();
        assertThat(response.getStatus(), equalTo(201));
        UriTemplate clientUriTemplate = resourcesClient.getResourcesUrls().clientUriTemplate();
        assertTrue(clientUriTemplate.match(response.getHeaderString("Location"), new ArrayList<>()));
    }
}
