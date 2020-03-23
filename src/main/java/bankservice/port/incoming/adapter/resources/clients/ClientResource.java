package bankservice.port.incoming.adapter.resources.clients;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import bankservice.domain.model.client.Client;
import bankservice.domain.model.client.Email;
import bankservice.service.client.ClientService;
import bankservice.service.client.UpdateClientCommand;
import io.dropwizard.jersey.params.UUIDParam;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

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
