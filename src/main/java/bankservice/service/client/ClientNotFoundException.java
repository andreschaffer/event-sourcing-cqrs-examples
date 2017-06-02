package bankservice.service.client;

import java.util.UUID;

import static java.lang.String.format;

public class ClientNotFoundException extends RuntimeException {

    public ClientNotFoundException(UUID id) {
        super(format("Client with id '%s' could not be found", id));
    }
}
