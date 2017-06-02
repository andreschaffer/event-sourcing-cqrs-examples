package bankservice.it;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import javax.ws.rs.core.Response;

import static java.util.UUID.randomUUID;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ClientIT extends BaseIT {

    @Test
    public void returnClient() throws Exception {
        String name = "Jensen", email = "jensen@example.com";
        String clientId = stateSetup.newClient(name, email);
        Response response = resourcesClient.getClient(clientId);
        JsonNode client = response.readEntity(JsonNode.class);
        assertThat(client.get("id").textValue(), equalTo(clientId));
        assertThat(client.get("name").textValue(), equalTo(name));
        assertThat(client.get("email").textValue(), equalTo(email));
    }

    @Test
    public void returnClientNotFound() throws Exception {
        Response response = resourcesClient.getClient(randomUUID().toString());
        response.close();
        assertThat(response.getStatus(), equalTo(404));
    }

    @Test
    public void updateClient() throws Exception {
        String clientId = stateSetup.newClient("Jaya", "jaya@example.com");
        String newName = "Jayan", newEmail = "jayan@example.com";
        {
            ObjectNode clientDto = resourcesDtos.clientDto(newName, newEmail);
            Response response = resourcesClient.putClient(clientId, clientDto);
            response.close();
            assertThat(response.getStatus(), equalTo(204));
        }
        {
            Response response = resourcesClient.getClient(clientId);
            JsonNode clientDto = response.readEntity(JsonNode.class);
            assertThat(clientDto.get("name").textValue(), equalTo(newName));
            assertThat(clientDto.get("email").textValue(), equalTo(newEmail));
        }
    }
}
