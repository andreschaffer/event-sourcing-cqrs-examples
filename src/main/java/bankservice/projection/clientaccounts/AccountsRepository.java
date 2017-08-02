package bankservice.projection.clientaccounts;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface AccountsRepository {

    void save(AccountProjection accountProjection);

    void updateBalance(UUID accountId, BigDecimal balance, int version);

    List<AccountProjection> getAccounts(UUID clientId);
}
