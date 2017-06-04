package bankservice.port.outgoing.adapter.eventstore;

import bankservice.domain.model.Event;
import bankservice.domain.model.EventStore;
import bankservice.domain.model.OptimisticLockingException;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class InMemoryEventStore implements EventStore {

    private final Map<UUID, List<Event>> eventStore = new ConcurrentHashMap<>();

    @Override
    public void store(UUID aggregateId, List<Event> newEvents, int baseVersion) throws OptimisticLockingException {
        eventStore.merge(aggregateId, newEvents, (oldValue, value) -> {
            if (baseVersion != oldValue.get(oldValue.size() - 1).getVersion())
                throw new OptimisticLockingException("Base version does not match current stored version");

            return Stream.concat(oldValue.stream(), value.stream()).collect(toList());
        });
    }

    @Override
    public List<Event> load(UUID aggregateId) {
        return ImmutableList.copyOf(eventStore.getOrDefault(aggregateId, emptyList()));
    }
}
