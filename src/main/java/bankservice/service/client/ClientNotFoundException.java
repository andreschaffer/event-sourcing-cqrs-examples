package bankservice.service.client;

import static java.lang.String.format;

import java.util.UUID;

public class ClientNotFoundException extends RuntimeException {

  public ClientNotFoundException(UUID id) {
    super(format("Client with id '%s' could not be found", id));
  }
}
