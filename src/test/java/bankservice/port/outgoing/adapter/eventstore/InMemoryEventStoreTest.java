package bankservice.port.outgoing.adapter.eventstore;

import static com.google.common.collect.Lists.newArrayList;
import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.now;
import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import bankservice.domain.model.Event;
import bankservice.domain.model.EventStore;
import bankservice.domain.model.OptimisticLockingException;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class InMemoryEventStoreTest {

  private EventStore eventStore = new InMemoryEventStore();

  @Test
  void storeEventsInOrder() {
    UUID aggregateId = randomUUID();
    Event e1 = new Event(aggregateId, now(UTC), 1) {
    };
    Event e2 = new Event(aggregateId, now(UTC), 2) {
    };
    Event e3 = new Event(aggregateId, now(UTC), 3) {
    };
    eventStore.store(aggregateId, newArrayList(e1), 0);
    eventStore.store(aggregateId, newArrayList(e2), 1);
    eventStore.store(aggregateId, newArrayList(e3), 2);

    List<Event> eventStream = eventStore.load(aggregateId);
    assertThat(eventStream.size(), equalTo(3));
    assertThat(eventStream.get(0), equalTo(e1));
    assertThat(eventStream.get(1), equalTo(e2));
    assertThat(eventStream.get(2), equalTo(e3));
  }

  @Test
  void optimisticLocking() {
    UUID aggregateId = randomUUID();
    Event e1 = new Event(aggregateId, now(UTC), 1) {
    };
    Event e2 = new Event(aggregateId, now(UTC), 2) {
    };
    Event e3 = new Event(aggregateId, now(UTC), 2) {
    };
    eventStore.store(aggregateId, newArrayList(e1), 0);
    eventStore.store(aggregateId, newArrayList(e2), 1);
    assertThrows(
        OptimisticLockingException.class,
        () -> eventStore.store(aggregateId, newArrayList(e3), 1)
    );
  }

  @Test
  void loadedEventStreamIsImmutable() {
    UUID aggregateId = randomUUID();
    Event e1 = new Event(aggregateId, now(UTC), 1) {
    };
    eventStore.store(aggregateId, newArrayList(e1), 0);
    assertThrows(
        UnsupportedOperationException.class,
        () -> eventStore.load(aggregateId).add(mock(Event.class))
    );
  }
}