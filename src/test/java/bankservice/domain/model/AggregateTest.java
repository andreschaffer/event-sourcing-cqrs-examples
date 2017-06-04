package bankservice.domain.model;

import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.joda.time.DateTime.now;
import static org.joda.time.DateTimeZone.UTC;
import static org.junit.Assert.assertThat;

public class AggregateTest {

    @Test
    public void newAggregateHasBaseVersion0() throws Exception {
        Aggregate aggregate = new Aggregate(randomUUID()){};
        assertThat(aggregate.getBaseVersion(), equalTo(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void newEventsListIsImmutable() throws Exception {
        UUID id = randomUUID();
        Aggregate aggregate = new Aggregate(id){};
        aggregate.getNewEvents().add(new Event(id, now(UTC), 1){});
    }

    @Test
    public void replayEventStreamUsingChildClassMethods() throws Exception {
        UUID id = randomUUID();
        DummyEvent eventWithCorrespondingHandler = new DummyEvent(id, now(UTC), 1);
        List<Event> eventStream = singletonList(eventWithCorrespondingHandler);
        new BackCallerAggregate(id, eventStream);
        assertThat(eventWithCorrespondingHandler.getCalledBackTimes(), equalTo(1));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void failReplayOfEventWithoutProperChildClassMethodHandler() throws Exception {
        UUID id = randomUUID();
        Event eventWithoutCorrespondingHandler = new Event(id, now(UTC), 1){};
        List<Event> eventStream = singletonList(eventWithoutCorrespondingHandler);
        new Aggregate(id, eventStream){};
    }

    @Test(expected = ArithmeticException.class)
    public void propagatesExceptionOfFailingReplay() throws Exception {
        UUID id = randomUUID();
        ArithmeticException replayException = new ArithmeticException();
        ProblematicEvent problematicEvent = new ProblematicEvent(id, now(UTC), 1, replayException);
        List<Event> eventStream = singletonList(problematicEvent);
        new BackCallerAggregate(id, eventStream);
    }

    @Test
    public void replayedAggregateKeepsEventStreamVersionAsItsBaseVersion() throws Exception {
        UUID id = randomUUID();
        List<Event> eventStream = singletonList(new DummyEvent(id, now(UTC), 1));
        Aggregate aggregate = new BackCallerAggregate(id, eventStream);
        assertThat(aggregate.getBaseVersion(), equalTo(1));
        aggregate.applyNewEvent(new DummyEvent(id, now(UTC), 2));
        assertThat(aggregate.getBaseVersion(), equalTo(1));
    }

    @Test
    public void nextVersionOfEmptyEventStreamIs1() throws Exception {
        Aggregate aggregate = new Aggregate(randomUUID()){};
        assertThat(aggregate.getNextVersion(), equalTo(1));
        assertThat(aggregate.getNextVersion(), equalTo(1));
    }

    @Test
    public void nextVersionOfExistingEventStreamIsTotalOfEventsPlus1() throws Exception {
        UUID id = randomUUID();
        List<Event> eventStream = singletonList(new DummyEvent(id, now(UTC), 1));
        Aggregate aggregate = new BackCallerAggregate(id, eventStream);
        assertThat(aggregate.getNextVersion(), equalTo(2));

        aggregate.applyNewEvent(new DummyEvent(id, now(UTC), 2));
        assertThat(aggregate.getNextVersion(), equalTo(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void failOnWrongNewEventVersion() throws Exception {
        UUID id = randomUUID();
        List<Event> eventStream = singletonList(new DummyEvent(id, now(UTC), 1));
        Aggregate aggregate = new BackCallerAggregate(id, eventStream);
        aggregate.applyNewEvent(new DummyEvent(id, now(UTC), 1));
    }

    private static class BackCallerAggregate extends Aggregate {
        private BackCallerAggregate(UUID id, List<Event> eventStream) {
            super(id, eventStream);
        }

        @SuppressWarnings("unused")
        private void apply(DummyEvent e) {
            e.callback();
        }

        @SuppressWarnings("unused")
        private void apply(ProblematicEvent e) {
            e.callback();
        }
    }

    private static class DummyEvent extends Event {
        private int calledBackTimes = 0;

        private DummyEvent(UUID aggregateId, DateTime timestamp, int version) {
            super(aggregateId, timestamp, version);
        }

        void callback() {
            calledBackTimes++;
        }

        int getCalledBackTimes() {
            return calledBackTimes;
        }
    }

    private static class ProblematicEvent extends Event {
        private RuntimeException exception;

        private ProblematicEvent(UUID aggregateId, DateTime timestamp, int version, RuntimeException exception) {
            super(aggregateId, timestamp, version);
            this.exception = exception;
        }

        void callback() {
            throw exception;
        }
    }
}
