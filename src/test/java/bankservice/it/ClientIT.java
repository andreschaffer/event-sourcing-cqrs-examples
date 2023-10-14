package bankservice.it;

import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;

class ClientIT extends BaseIT {

  @Test
  void returnClient() {
    String name = "Jensen";
    String email = "jensen@example.com";
    String clientId = stateSetup.newClient(name, email);
    Response response = resourcesClient.getClient(clientId);
    JsonNode client = response.readEntity(JsonNode.class);
    assertThat(client.get("id").textValue(), equalTo(clientId));
    assertThat(client.get("name").textValue(), equalTo(name));
    assertThat(client.get("email").textValue(), equalTo(email));
  }

  @Test
  void returnClientNotFound() {
    Response response = resourcesClient.getClient(randomUUID().toString());
    response.close();
    assertThat(response.getStatus(), equalTo(404));
  }

  @Test
  void updateClient() {
    String clientId = stateSetup.newClient("Jaya", "jaya@example.com");
    String newName = "Jayan";
    String newEmail = "jayan@example.com";
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
