package bankservice.port.incoming.adapter.resources.accounts.deposits;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import bankservice.domain.model.OptimisticLockingException;
import bankservice.service.account.AccountNotFoundException;
import bankservice.service.account.AccountService;
import bankservice.service.account.DepositAccountCommand;
import io.dropwizard.jersey.params.UUIDParam;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

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
