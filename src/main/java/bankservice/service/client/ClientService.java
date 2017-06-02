package bankservice.service.client;

import bankservice.domain.model.EventStore;
import bankservice.domain.model.EventStream;
import bankservice.domain.model.OptimisticLockingException;
import bankservice.domain.model.client.Client;
import bankservice.service.Retrier;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;

public class ClientService {

    private final EventStore eventStore;
    private final Retrier conflictRetrier;

    public ClientService(EventStore eventStore) {
        this.eventStore = checkNotNull(eventStore);
        int maxAttempts = 3;
        this.conflictRetrier = new Retrier(singletonList(OptimisticLockingException.class), maxAttempts);
    }

    public Client process(EnrollClientCommand command) {
        Client client = new Client(randomUUID(), command.getName(), command.getEmail());
        storeEvents(client);
        return client;
    }

    public Optional<Client> loadClient(UUID id) {
        EventStream eventStream = eventStore.load(id);
        if (eventStream.getEvents().isEmpty()) return Optional.empty();
        return Optional.of(new Client(id, eventStream));
    }

    public void process(UpdateClientCommand command) {
        process(command.getId(), c -> c.update(command.getName(), command.getEmail()));
    }

    private Client process(UUID clientId, Consumer<Client> consumer)
            throws ClientNotFoundException, OptimisticLockingException {

        return conflictRetrier.get(() -> {
            Optional<Client> possibleClient = loadClient(clientId);
            if (!possibleClient.isPresent()) throw new ClientNotFoundException(clientId);
            Client client = possibleClient.get();
            consumer.accept(client);
            storeEvents(client);
            return client;
        });
    }

    private void storeEvents(Client client) {
        eventStore.store(client.getId(), client.getNewEvents(), client.getBaseVersion());
    }
}
