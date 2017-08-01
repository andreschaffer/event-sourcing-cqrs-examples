package bankservice.port.incoming.adapter.resources.accounts.withdrawals;

import bankservice.domain.model.OptimisticLockingException;
import bankservice.domain.model.account.NonSufficientFundsException;
import bankservice.service.account.AccountNotFoundException;
import bankservice.service.account.AccountService;
import bankservice.service.account.WithdrawAccountCommand;
import io.dropwizard.jersey.params.UUIDParam;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@Path("/accounts/{id}/withdrawals")
public class WithdrawalsResource {

    private final AccountService accountService;

    public WithdrawalsResource(AccountService accountService) {
        this.accountService = checkNotNull(accountService);
    }

    @POST
    public Response post(@PathParam("id") UUIDParam accountId, @Valid WithdrawalDto withdrawalDto)
            throws AccountNotFoundException, OptimisticLockingException {

        WithdrawAccountCommand command = new WithdrawAccountCommand(accountId.get(), withdrawalDto.getAmount());
        try {
            accountService.process(command);
        } catch (NonSufficientFundsException e) {
            return Response.status(BAD_REQUEST).build();
        }
        return Response.noContent().build();
    }
}
