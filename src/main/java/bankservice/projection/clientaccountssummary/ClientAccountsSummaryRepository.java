package bankservice.projection.clientaccountssummary;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface ClientAccountsSummaryRepository {

    void addNewAccount(UUID clientId, UUID accountId, BigDecimal balance, int version);

    void updateBalance(UUID accountId, BigDecimal balance, int version);

    List<AccountProjection> getAccounts(UUID clientId);
}
