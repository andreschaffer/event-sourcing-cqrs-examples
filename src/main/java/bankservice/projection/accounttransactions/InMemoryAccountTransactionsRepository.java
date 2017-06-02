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

public class InMemoryAccountTransactionsRepository implements AccountTransactionsRepository {

    private Map<UUID, List<TransactionProjection>> transactions = new ConcurrentHashMap<>();

    @Override
    public List<TransactionProjection> listByAccount(UUID accountId) {
        return transactions.getOrDefault(accountId, emptyList()).stream()
                .sorted(comparing(TransactionProjection::getVersion))
                .collect(toList());
    }

    @Override
    public void save(TransactionProjection transactionProjection) {
        transactions.merge(
            transactionProjection.getAccountId(), newArrayList(transactionProjection),
                (oldValue, value) -> Stream.concat(oldValue.stream(), value.stream()).collect(toList()));
    }
}
