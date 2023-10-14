package bankservice.port.incoming.adapter.resources.accounts.deposits;

import static com.google.common.base.Preconditions.checkNotNull;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import bankservice.domain.model.OptimisticLockingException;
import bankservice.service.account.AccountNotFoundException;
import bankservice.service.account.AccountService;
import bankservice.service.account.DepositAccountCommand;
import io.dropwizard.jersey.params.UUIDParam;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("/accounts/{id}/deposits")
public class DepositsResource {

  private final AccountService accountService;

  public DepositsResource(AccountService accountService) {
    this.accountService = checkNotNull(accountService);
  }

  @POST
  public Response post(@PathParam("id") UUIDParam accountId, @Valid DepositDto depositDto)
      throws AccountNotFoundException, OptimisticLockingException {

    DepositAccountCommand command = new DepositAccountCommand(accountId.get(),
        depositDto.getAmount());
    accountService.process(command);
    return Response.noContent().build();
  }
}
