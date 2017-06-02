package bankservice.projection.accounttransactions;

import java.util.List;
import java.util.UUID;

public interface AccountTransactionsRepository {

    List<TransactionProjection> listByAccount(UUID accountId);
    void save(TransactionProjection transactionProjection);

}
