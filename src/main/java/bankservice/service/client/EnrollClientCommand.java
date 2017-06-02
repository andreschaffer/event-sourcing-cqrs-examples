package bankservice.service.client;

import static com.google.common.base.Preconditions.checkNotNull;

import bankservice.domain.model.client.Email;

public class EnrollClientCommand {

    private final String name;
    private final Email email;

    public EnrollClientCommand(String name, Email email) {
        this.name = checkNotNull(name);
        this.email = checkNotNull(email);
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }
}
