package bankservice.service.account;

import static java.lang.String.format;

import java.util.UUID;

public class AccountNotFoundException extends RuntimeException {

  public AccountNotFoundException(UUID id) {
    super(format("Account with id '%s' could not be found", id));
  }
}
