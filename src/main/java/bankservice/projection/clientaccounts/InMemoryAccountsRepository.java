package bankservice.projection.clientaccounts;

import static java.util.Collections.emptyMap;

import com.google.common.collect.ImmutableList;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryAccountsRepository implements AccountsRepository {

    private final Map<UUID, Map<UUID, AccountProjection>> clientAccounts = new ConcurrentHashMap<>();
    private final Map<UUID, UUID> accountClientIndex = new ConcurrentHashMap<>();

    @Override
    public void save(AccountProjection accountProjection) {
        clientAccounts.merge(
            accountProjection.getClientId(),
            newAccountsMap(accountProjection),
            (oldValue, value) -> {
                oldValue.putAll(value);
                return oldValue;
            }
        );
        accountClientIndex.put(accountProjection.getAccountId(), accountProjection.getClientId());
    }

    @Override
    public void updateBalance(UUID accountId, BigDecimal balance, int version) {
        UUID clientId = accountClientIndex.get(accountId);
        clientAccounts.get(clientId).merge(
            accountId,
            new AccountProjection(accountId, clientId, balance, version),
            (oldValue, value) -> value.getVersion() > oldValue.getVersion() ? value : oldValue);
    }

    @Override
    public List<AccountProjection> getAccounts(UUID clientId) {
        Map<UUID, AccountProjection> accounts = clientAccounts.getOrDefault(clientId, emptyMap());
        return ImmutableList.copyOf(accounts.values());
    }

    private Map<UUID, AccountProjection> newAccountsMap(AccountProjection accountProjection) {
        return new HashMap<UUID, AccountProjection>() {
            { put(accountProjection.getAccountId(), accountProjection); }
        };
    }
}
