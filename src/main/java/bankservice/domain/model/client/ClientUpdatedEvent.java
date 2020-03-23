package bankservice.domain.model.client;

import static com.google.common.base.Preconditions.checkNotNull;

import bankservice.domain.model.Event;
import java.time.ZonedDateTime;
import java.util.UUID;

public class ClientUpdatedEvent extends Event {

  private final String name;
  private final String email;

  public ClientUpdatedEvent(UUID aggregateId, ZonedDateTime timestamp, int version, String name,
      Email email) {
    super(aggregateId, timestamp, version);
    this.name = checkNotNull(name);
    this.email = checkNotNull(email).getValue();
  }

  public String getName() {
    return name;
  }

  public Email getEmail() {
    return new Email(email);
  }
}
