package bankservice.domain.model;

import com.google.common.collect.ImmutableList;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class EventStream {

    private final List<Event> events;
    private final int version;

    public EventStream(List<Event> events) {
        checkNotNull(events);
        this.events = ImmutableList.copyOf(events);
        this.version = this.events.isEmpty() ? 0 : this.events.get(this.events.size() - 1).getVersion();
    }

    public List<Event> getEvents() {
        return events;
    }

    public int getVersion() {
        return version;
    }
}
