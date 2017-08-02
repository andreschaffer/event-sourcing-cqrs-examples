package bankservice.projection.accounttransactions;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Collections.emptyList;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class InMemoryTransactionsRepository implements TransactionsRepository {

    private Map<UUID, List<TransactionProjection>> accountTransactions = new ConcurrentHashMap<>();

    @Override
    public List<TransactionProjection> listByAccount(UUID accountId) {
        return accountTransactions.getOrDefault(accountId, emptyList()).stream()
                .sorted(comparing(TransactionProjection::getVersion))
                .collect(toList());
    }

    @Override
    public void save(TransactionProjection transactionProjection) {
        accountTransactions.merge(
            transactionProjection.getAccountId(),
            newArrayList(transactionProjection),
            (oldValue, value) -> Stream.concat(oldValue.stream(), value.stream()).collect(toList()));
    }
}
