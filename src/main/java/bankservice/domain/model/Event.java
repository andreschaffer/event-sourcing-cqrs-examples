package bankservice.domain.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.time.ZonedDateTime;
import java.util.UUID;

public abstract class Event {

  private final UUID aggregateId;
  private final ZonedDateTime timestamp;
  private final int version;

  protected Event(UUID aggregateId, ZonedDateTime timestamp, int version) {
    this.aggregateId = checkNotNull(aggregateId);
    this.timestamp = checkNotNull(timestamp);
    this.version = version;
  }

  public UUID getAggregateId() {
    return aggregateId;
  }

  public ZonedDateTime getTimestamp() {
    return this.timestamp;
  }

  public int getVersion() {
    return version;
  }
}
