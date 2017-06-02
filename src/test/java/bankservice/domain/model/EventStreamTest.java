package bankservice.domain.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

public class EventStreamTest {

    @Test
    public void copiesEventsOnCreation() throws Exception {
        List<Event> events = new ArrayList<>();
        events.add(mock(Event.class));
        EventStream eventStream = new EventStream(events);
        events.add(mock(Event.class));
        assertThat(eventStream.getEvents().size(), equalTo(1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void eventsListIsImmutable() throws Exception {
        List<Event> events = new ArrayList<>();
        events.add(mock(Event.class));
        EventStream eventStream = new EventStream(events);
        eventStream.getEvents().add(mock(Event.class));
    }
}