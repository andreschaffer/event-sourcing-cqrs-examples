package bankservice.port.incoming.adapter.resources.accounts;

import static com.google.common.base.Preconditions.checkNotNull;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

import bankservice.domain.model.account.Account;
import bankservice.service.account.AccountService;
import io.dropwizard.jersey.params.UUIDParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.util.Optional;

@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("/accounts/{id}")
public class AccountResource {

  private final AccountService accountService;

  public AccountResource(AccountService accountService) {
    this.accountService = checkNotNull(accountService);
  }

  @GET
  public Response get(@PathParam("id") UUIDParam accountId) {
    Optional<Account> possibleAccount = accountService.loadAccount(accountId.get());
    if (!possibleAccount.isPresent()) {
      return Response.status(NOT_FOUND).build();
    }
    AccountDto accountDto = toDto(possibleAccount.get());
    return Response.ok(accountDto).build();
  }

  private AccountDto toDto(Account account) {
    AccountDto dto = new AccountDto();
    dto.setId(account.getId());
    dto.setBalance(account.getBalance());
    dto.setClientId(account.getClientId());
    return dto;
  }
}
