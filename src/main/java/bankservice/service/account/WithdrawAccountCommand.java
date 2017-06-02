package bankservice.service.account;

import java.math.BigDecimal;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class WithdrawAccountCommand {

    private final UUID id;
    private final BigDecimal amount;

    public WithdrawAccountCommand(UUID id, BigDecimal amount) {
        this.id = checkNotNull(id);
        this.amount = checkNotNull(amount);
    }

    public UUID getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
