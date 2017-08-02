package bankservice.projection.clientaccounts;

import java.math.BigDecimal;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class AccountProjection {

    private final UUID accountId;
    private final UUID clientId;
    private final BigDecimal balance;
    private final int version;

    public AccountProjection(UUID accountId, UUID clientId, BigDecimal balance, int version) {
        this.accountId = checkNotNull(accountId);
        this.clientId = checkNotNull(clientId);
        this.balance = checkNotNull(balance);
        this.version = version;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public UUID getClientId() {
        return clientId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public int getVersion() {
        return version;
    }
}
