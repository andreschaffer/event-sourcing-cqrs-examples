package bankservice.projection.accountssummary;

import java.math.BigDecimal;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class AccountProjection {

    private final UUID accountId;
    private final BigDecimal balance;
    private final int version;

    public AccountProjection(UUID accountId, BigDecimal balance, int version) {
        this.accountId = checkNotNull(accountId);
        this.balance = checkNotNull(balance);
        this.version = version;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public int getVersion() {
        return version;
    }
}
