package bankservice.service.client;


import bankservice.domain.model.client.Email;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class UpdateClientCommand {

    private final UUID id;
    private final String name;
    private final Email email;

    public UpdateClientCommand(UUID id, String name, Email email) {
        this.id = checkNotNull(id);
        this.name = checkNotNull(name);
        this.email = checkNotNull(email);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }
}
