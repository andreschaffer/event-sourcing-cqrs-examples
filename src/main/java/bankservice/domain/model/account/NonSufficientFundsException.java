package bankservice.domain.model.account;

import static java.lang.String.format;

import java.math.BigDecimal;
import java.util.UUID;

public class NonSufficientFundsException extends RuntimeException {

  public NonSufficientFundsException(UUID accountId, BigDecimal balance, BigDecimal amount) {
    super(format("Withdrawal of '%s' failed as there is only '%s' in account '%s'", amount, balance,
        accountId));
  }
}
