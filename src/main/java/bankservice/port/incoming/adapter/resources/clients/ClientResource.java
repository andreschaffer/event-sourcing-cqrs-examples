package bankservice.port.incoming.adapter.resources.clients;

import static com.google.common.base.Preconditions.checkNotNull;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

import bankservice.domain.model.client.Client;
import bankservice.domain.model.client.Email;
import bankservice.service.client.ClientService;
import bankservice.service.client.UpdateClientCommand;
import io.dropwizard.jersey.params.UUIDParam;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.util.Optional;

@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("/clients/{id}")
public class ClientResource {

  private ClientService clientService;

  public ClientResource(ClientService clientService) {
    this.clientService = checkNotNull(clientService);
  }

  @GET
  public Response get(@PathParam("id") UUIDParam clientId) {
    Optional<Client> possibleClient = clientService.loadClient(clientId.get());
    if (!possibleClient.isPresent()) {
      return Response.status(NOT_FOUND).build();
    }
    ClientDto clientDto = toDto(possibleClient.get());
    return Response.ok(clientDto).build();
  }

  @PUT
  public Response put(@PathParam("id") UUIDParam clientId, @Valid @NotNull ClientDto clientDto) {
    UpdateClientCommand command = new UpdateClientCommand(
        clientId.get(), clientDto.getName(), new Email(clientDto.getEmail()));
    clientService.process(command);
    return Response.noContent().build();
  }

  private ClientDto toDto(Client client) {
    ClientDto dto = new ClientDto();
    dto.setId(client.getId());
    dto.setName(client.getName());
    dto.setEmail(client.getEmail().getValue());
    return dto;
  }
}
