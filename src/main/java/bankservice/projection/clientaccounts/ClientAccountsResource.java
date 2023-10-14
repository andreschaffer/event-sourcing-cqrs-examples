package bankservice.projection.clientaccounts;

import static com.google.common.base.Preconditions.checkNotNull;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import io.dropwizard.jersey.params.UUIDParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.util.List;

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
