package bankservice.projection.accounttransactions;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Produces(APPLICATION_JSON)
@Path("/accounts/{id}/transactions")
public class AccountTransactionsResource {

    private TransactionsRepository transactionsRepository;

    public AccountTransactionsResource(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = checkNotNull(transactionsRepository);
    }

    @GET
    public Response get(@PathParam("id") UUID accountId) {
        List<TransactionProjection> transactionProjections = transactionsRepository.listByAccount(accountId);
        return Response.ok(transactionProjections).build();
    }
}
