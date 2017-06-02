package bankservice.projection.clientaccountssummary;

import com.google.common.collect.ImmutableList;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.emptyMap;

public class InMemoryClientAccountsSummaryRepository implements ClientAccountsSummaryRepository {

    private final Map<UUID, Map<UUID, AccountProjection>> clientAccounts = new ConcurrentHashMap<>();
    private final Map<UUID, UUID> accountClientIndex = new ConcurrentHashMap<>();

    @Override
    public void addNewAccount(UUID clientId, UUID accountId, BigDecimal balance, int version) {
        AccountProjection accountProjection = new AccountProjection(accountId, balance, version);
        clientAccounts.merge(clientId, new HashMap<UUID, AccountProjection>(){{ put(accountId, accountProjection); }},
            (oldValue, value) -> {
                oldValue.putAll(value);
                return oldValue;
            }
        );
        accountClientIndex.put(accountId, clientId);
    }

    @Override
    public void updateBalance(UUID accountId, BigDecimal balance, int version) {
        UUID clientId = accountClientIndex.get(accountId);
        clientAccounts.get(clientId).merge(accountId, new AccountProjection(accountId, balance, version),
                (oldValue, value) -> {
                    if (value.getVersion() > oldValue.getVersion()) return value;
                    return oldValue; // ignore old event
                });
    }

    @Override
    public List<AccountProjection> getAccounts(UUID clientId) {
        Map<UUID, AccountProjection> accounts = clientAccounts.getOrDefault(clientId, emptyMap());
        return ImmutableList.copyOf(accounts.values());
    }
}
