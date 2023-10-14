package bankservice.port.incoming.adapter.resources.accounts;

import static com.google.common.base.Preconditions.checkNotNull;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.UriBuilder.fromResource;

import bankservice.domain.model.account.Account;
import bankservice.service.account.AccountService;
import bankservice.service.account.OpenAccountCommand;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.net.URI;

@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("/accounts")
public class AccountsResource {

  private final AccountService accountService;

  public AccountsResource(AccountService accountService) {
    this.accountService = checkNotNull(accountService);
  }

  @POST
  public Response post(@Valid AccountDto accountDto) {
    OpenAccountCommand command = new OpenAccountCommand(accountDto.getClientId());
    Account account = accountService.process(command);
    URI accountUri = fromResource(AccountResource.class).build(account.getId());
    return Response.created(accountUri).build();
  }
}
