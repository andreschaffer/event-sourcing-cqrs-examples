package bankservice.port.incoming.adapter.resources.clients;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.UriBuilder.fromResource;

import bankservice.domain.model.client.Email;
import bankservice.domain.model.client.Client;
import bankservice.service.client.ClientService;
import bankservice.service.client.EnrollClientCommand;
import java.net.URI;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

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
