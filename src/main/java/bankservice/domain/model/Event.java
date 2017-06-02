package bankservice.domain.model;

import org.joda.time.DateTime;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class Event {

    private final UUID aggregateId;
    private final DateTime timestamp;
    private final int version;

    public Event(UUID aggregateId, DateTime timestamp, int version) {
        this.aggregateId = checkNotNull(aggregateId);
        this.timestamp = checkNotNull(timestamp);
        this.version = checkNotNull(version);
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public DateTime getTimestamp() {
        return this.timestamp;
    }

    public int getVersion() {
        return version;
    }
}
