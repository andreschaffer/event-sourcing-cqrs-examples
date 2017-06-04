package bankservice.domain.model;

import java.util.List;
import java.util.UUID;

public interface EventStore {

    void store(UUID aggregateId, List<Event> newEvents, int baseVersion) throws OptimisticLockingException;
    List<Event> load(UUID aggregateId);

}
