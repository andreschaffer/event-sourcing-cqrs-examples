package bankservice.domain.model;

import static java.time.ZoneOffset.UTC;
import static java.time.ZonedDateTime.now;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AggregateTest {

  @Test
  void newAggregateHasBaseVersion0() {
    Aggregate aggregate = new Aggregate(randomUUID()) {
    };
    assertThat(aggregate.getBaseVersion(), equalTo(0));
  }

  @Test
  void newEventsListIsImmutable() {
    UUID id = randomUUID();
    Aggregate aggregate = new Aggregate(id) {
    };
    assertThrows(
        UnsupportedOperationException.class,
        () -> aggregate.getNewEvents().add(new Event(id, now(UTC), 1) {
        })
    );
  }

  @Test
  void replayEventStreamUsingChildClassMethods() {
    UUID id = randomUUID();
    DummyEvent eventWithCorrespondingHandler = new DummyEvent(id, now(UTC), 1);
    List<Event> eventStream = singletonList(eventWithCorrespondingHandler);
    new BackCallerAggregate(id, eventStream);
    assertThat(eventWithCorrespondingHandler.getCalledBackTimes(), equalTo(1));
  }

  @Test
  void failReplayOfEventWithoutProperChildClassMethodHandler() {
    UUID id = randomUUID();
    Event eventWithoutCorrespondingHandler = new Event(id, now(UTC), 1) {
    };
    List<Event> eventStream = singletonList(eventWithoutCorrespondingHandler);
    assertThrows(
        UnsupportedOperationException.class,
        () -> new Aggregate(id, eventStream) {
        }
    );
  }

  @Test
  void propagatesExceptionOfFailingReplay() {
    UUID id = randomUUID();
    ArithmeticException replayException = new ArithmeticException();
    ProblematicEvent problematicEvent = new ProblematicEvent(id, now(UTC), 1, replayException);
    List<Event> eventStream = singletonList(problematicEvent);
    assertThrows(
        ArithmeticException.class,
        () -> new BackCallerAggregate(id, eventStream)
    );
  }

  @Test
  void replayedAggregateKeepsEventStreamVersionAsItsBaseVersion() {
    UUID id = randomUUID();
    List<Event> eventStream = singletonList(new DummyEvent(id, now(UTC), 1));
    Aggregate aggregate = new BackCallerAggregate(id, eventStream);
    assertThat(aggregate.getBaseVersion(), equalTo(1));
    aggregate.applyNewEvent(new DummyEvent(id, now(UTC), 2));
    assertThat(aggregate.getBaseVersion(), equalTo(1));
  }

  @Test
  void nextVersionOfEmptyEventStreamIs1() {
    Aggregate aggregate = new Aggregate(randomUUID()) {
    };
    assertThat(aggregate.getNextVersion(), equalTo(1));
    assertThat(aggregate.getNextVersion(), equalTo(1));
  }

  @Test
  void nextVersionOfExistingEventStreamIsTotalOfEventsPlus1() {
    UUID id = randomUUID();
    List<Event> eventStream = singletonList(new DummyEvent(id, now(UTC), 1));
    Aggregate aggregate = new BackCallerAggregate(id, eventStream);
    assertThat(aggregate.getNextVersion(), equalTo(2));

    aggregate.applyNewEvent(new DummyEvent(id, now(UTC), 2));
    assertThat(aggregate.getNextVersion(), equalTo(3));
  }

  @Test
  void failOnWrongNewEventVersion() {
    UUID id = randomUUID();
    List<Event> eventStream = singletonList(new DummyEvent(id, now(UTC), 1));
    Aggregate aggregate = new BackCallerAggregate(id, eventStream);
    assertThrows(
        IllegalArgumentException.class,
        () -> aggregate.applyNewEvent(new DummyEvent(id, now(UTC), 1))
    );
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

    private DummyEvent(UUID aggregateId, ZonedDateTime timestamp, int version) {
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

    private ProblematicEvent(UUID aggregateId, ZonedDateTime timestamp, int version,
        RuntimeException exception) {
      super(aggregateId, timestamp, version);
      this.exception = exception;
    }

    void callback() {
      throw exception;
    }
  }
}
