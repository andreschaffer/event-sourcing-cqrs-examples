package bankservice.projection.accounttransactions;

import java.util.List;
import java.util.UUID;

public interface TransactionsRepository {

    void save(TransactionProjection transactionProjection);

    List<TransactionProjection> listByAccount(UUID accountId);

}
