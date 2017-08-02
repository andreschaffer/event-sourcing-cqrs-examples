package bankservice.projection.clientaccounts;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import io.dropwizard.jersey.params.UUIDParam;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("clients/{id}/accounts")
public class ClientAccountsResource {

    private final AccountsRepository accountsRepository;

    public ClientAccountsResource(AccountsRepository accountsRepository) {
        this.accountsRepository = checkNotNull(accountsRepository);
    }

    @GET
    public Response get(@PathParam("id") UUIDParam clientId) {
        List<AccountProjection> accounts = accountsRepository.getAccounts(clientId.get());
        return Response.ok(accounts).build();
    }
}
