package bankservice.port.outgoing.adapter.eventstore;

import bankservice.domain.model.Event;
import bankservice.domain.model.EventStore;
import bankservice.domain.model.EventStream;
import bankservice.domain.model.OptimisticLockingException;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.joda.time.DateTime.now;
import static org.joda.time.DateTimeZone.UTC;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class InMemoryEventStoreTest {

    private EventStore eventStore = new InMemoryEventStore();

    @Test
    public void storeEventsInOrder() throws Exception {
        UUID aggregateId = randomUUID();
        Event e1 = new Event(aggregateId, now(UTC), 1){};
        Event e2 = new Event(aggregateId, now(UTC), 2){};
        Event e3 = new Event(aggregateId, now(UTC), 3){};
        eventStore.store(aggregateId, newArrayList(e1), 0);
        eventStore.store(aggregateId, newArrayList(e2), 1);
        eventStore.store(aggregateId, newArrayList(e3), 2);

        EventStream eventStream = eventStore.load(aggregateId);
        List<Event> events = eventStream.getEvents();
        assertThat(events.size(), equalTo(3));
        assertThat(events.get(0), equalTo(e1));
        assertThat(events.get(1), equalTo(e2));
        assertThat(events.get(2), equalTo(e3));
        assertThat(eventStream.getVersion(), equalTo(3));
    }

    @Test(expected = OptimisticLockingException.class)
    public void optimisticLocking() throws Exception {
        UUID aggregateId = randomUUID();
        Event e1 = new Event(aggregateId, now(UTC), 1){};
        Event e2 = new Event(aggregateId, now(UTC), 2){};
        Event e3 = new Event(aggregateId, now(UTC), 2){};
        eventStore.store(aggregateId, newArrayList(e1), 0);
        eventStore.store(aggregateId, newArrayList(e2), 1);
        eventStore.store(aggregateId, newArrayList(e3), 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void loadedEventsListIsImmutable() throws Exception {
        EventStream eventStream = eventStore.load(randomUUID());
        eventStream.getEvents().add(mock(Event.class));
    }
}