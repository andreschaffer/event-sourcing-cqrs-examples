package bankservice.projection.transactions;

import java.util.List;
import java.util.UUID;

public interface TransactionsRepository {

    List<TransactionProjection> listByAccount(UUID accountId);

    void save(TransactionProjection transactionProjection);

}
