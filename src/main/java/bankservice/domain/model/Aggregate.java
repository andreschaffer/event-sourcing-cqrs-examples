package bankservice.domain.model;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;

public abstract class Aggregate {

    private UUID id;
    private int baseVersion;
    private List<Event> newEvents;

    protected Aggregate(UUID id) {
        this(id, 0);
    }

    protected Aggregate(UUID id, EventStream eventStream) {
        this(id, eventStream.getVersion());
        eventStream.getEvents().forEach(this::apply);
    }

    private Aggregate(UUID id, int baseVersion) {
        checkNotNull(id);
        checkArgument(baseVersion >= 0);
        this.id = id;
        this.baseVersion = baseVersion;
        this.newEvents = new ArrayList<>();
    }

    protected void applyNewEvent(Event event) {
        checkArgument(event.getVersion() == getNextVersion(),
                "New event version '%d' does not match expected next version '%d'",
                event.getVersion(), getNextVersion());
        apply(event);
        addNewEvent(event);
    }

    private void apply(Event event) {
        try {
            Method method = this.getClass().getDeclaredMethod("apply", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);
        } catch (InvocationTargetException e) {
            Throwables.propagate(e.getCause());
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new UnsupportedOperationException(
                    format("Aggregate '%s' doesn't apply event type '%s'", this.getClass(), event.getClass()), e);
        }
    }

    private void addNewEvent(Event event) {
        newEvents.add(event);
    }

    public UUID getId() {
        return id;
    }

    public int getBaseVersion() {
        return baseVersion;
    }

    public List<Event> getNewEvents() {
        return ImmutableList.copyOf(newEvents);
    }

    protected int getNextVersion() {
        return baseVersion + newEvents.size() + 1;
    }
}
