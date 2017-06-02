package bankservice.port.incoming.adapter.resources.clients.accounts;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.UriBuilder.fromResource;

import bankservice.domain.model.account.Account;
import bankservice.port.incoming.adapter.resources.accounts.AccountResource;
import bankservice.service.account.AccountService;
import bankservice.service.account.OpenAccountCommand;
import io.dropwizard.jersey.params.UUIDParam;
import java.net.URI;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("/clients/{id}/accounts")
public class ClientAccountsResource {

    private final AccountService accountService;

    public ClientAccountsResource(AccountService accountService) {
        this.accountService = checkNotNull(accountService);
    }

    @POST
    public Response post(@PathParam("id") UUIDParam clientId) {
        OpenAccountCommand command = new OpenAccountCommand(clientId.get());

        Account account = accountService.process(command);

        URI accountUri = fromResource(AccountResource.class).build(account.getId());
        return Response.created(accountUri).build();
    }
}
