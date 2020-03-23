package bankservice.service.account;

import static com.google.common.base.Preconditions.checkNotNull;

import java.math.BigDecimal;
import java.util.UUID;

public class DepositAccountCommand {

  private final UUID id;
  private final BigDecimal amount;

  public DepositAccountCommand(UUID id, BigDecimal amount) {
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
