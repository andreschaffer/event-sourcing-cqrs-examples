package bankservice.port.incoming.adapter.resources.clients;

import static com.google.common.base.Preconditions.checkNotNull;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.UriBuilder.fromResource;

import bankservice.domain.model.client.Client;
import bankservice.domain.model.client.Email;
import bankservice.service.client.ClientService;
import bankservice.service.client.EnrollClientCommand;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.net.URI;

@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("/clients")
public class ClientsResource {

  private ClientService clientService;

  public ClientsResource(ClientService clientService) {
    this.clientService = checkNotNull(clientService);
  }

  @POST
  public Response post(@Valid ClientDto newClientDto) {
    EnrollClientCommand enrollClientCommand = new EnrollClientCommand(
        newClientDto.getName(), new Email(newClientDto.getEmail()));
    Client client = clientService.process(enrollClientCommand);
    URI clientUri = fromResource(ClientResource.class).build(client.getId());
    return Response.created(clientUri).build();
  }
}
