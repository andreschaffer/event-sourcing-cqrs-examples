package bankservice.domain.model.client;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.now;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import bankservice.domain.model.Aggregate;
import bankservice.domain.model.Event;
import java.util.List;
import java.util.UUID;

public class Client extends Aggregate {

  private String name;
  private Email email;

  public Client(UUID id, String name, Email email) {
    super(id);
    validateName(name);
    validateEmail(email);
    ClientEnrolledEvent clientEnrolledEvent = new ClientEnrolledEvent(
        id, now(UTC), getNextVersion(), name, email);
    applyNewEvent(clientEnrolledEvent);
  }

  public Client(UUID id, List<Event> eventStream) {
    super(id, eventStream);
  }

  public void update(String name, Email email) {
    ClientUpdatedEvent clientUpdatedEvent = new ClientUpdatedEvent(
        getId(), now(UTC), getNextVersion(), name, email);
    applyNewEvent(clientUpdatedEvent);
  }

  @SuppressWarnings("unused")
  public void apply(ClientEnrolledEvent event) {
    this.name = event.getName();
    this.email = event.getEmail();
  }

  @SuppressWarnings("unused")
  private void apply(ClientUpdatedEvent event) {
    this.name = event.getName();
    this.email = event.getEmail();
  }

  public String getName() {
    return name;
  }

  public Email getEmail() {
    return email;
  }

  private void validateName(String name) {
    checkArgument(isNotBlank(name));
  }

  private void validateEmail(Email email) {
    checkNotNull(email);
  }
}
