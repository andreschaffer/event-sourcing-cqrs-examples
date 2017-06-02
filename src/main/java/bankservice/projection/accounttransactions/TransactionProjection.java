package bankservice.projection.accounttransactions;

import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class TransactionProjection {

    private final UUID accountId;
    private final TransactionType type;
    private final BigDecimal amount;
    private final DateTime timestamp;
    private final int version;

    public TransactionProjection(UUID accountId, TransactionType type, BigDecimal amount,
                                 DateTime timestamp, int version) {
        this.accountId = checkNotNull(accountId);
        this.type = checkNotNull(type);
        this.amount = checkNotNull(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
        this.timestamp = checkNotNull(timestamp);
        this.version = version;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public int getVersion() {
        return version;
    }

    public enum TransactionType {
        DEPOSIT, WITHDRAWAL
    }
}
