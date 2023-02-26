package bankservice.domain.model.account;

import static com.google.common.base.Preconditions.checkNotNull;

import bankservice.domain.model.Event;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

public class AccountOpenedEvent extends Event {

  private final String clientId;
  private final BigDecimal balance;

  public AccountOpenedEvent(
      UUID aggregateId, ZonedDateTime timestamp, int version, UUID clientId, BigDecimal balance) {
    super(aggregateId, timestamp, version);
    this.clientId = checkNotNull(clientId).toString();
    this.balance = checkNotNull(balance);
  }

  public UUID getClientId() {
    return UUID.fromString(clientId);
  }

  public BigDecimal getBalance() {
    return balance;
  }
}
