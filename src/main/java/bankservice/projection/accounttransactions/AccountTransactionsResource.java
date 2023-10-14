package bankservice.projection.accounttransactions;

import static com.google.common.base.Preconditions.checkNotNull;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Produces(APPLICATION_JSON)
@Path("/accounts/{id}/transactions")
public class AccountTransactionsResource {

  private TransactionsRepository transactionsRepository;

  public AccountTransactionsResource(TransactionsRepository transactionsRepository) {
    this.transactionsRepository = checkNotNull(transactionsRepository);
  }

  @GET
  public Response get(@PathParam("id") UUID accountId) {
    List<TransactionProjection> transactionProjections = transactionsRepository
        .listByAccount(accountId);
    return Response.ok(transactionProjections).build();
  }
}
