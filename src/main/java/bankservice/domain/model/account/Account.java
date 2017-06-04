package bankservice.domain.model.account;

import bankservice.domain.model.Aggregate;
import bankservice.domain.model.Event;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static java.math.BigDecimal.ZERO;
import static org.joda.time.DateTime.now;
import static org.joda.time.DateTimeZone.UTC;

public class Account extends Aggregate {

    private BigDecimal balance;
    private UUID clientId;

    public Account(UUID id, UUID clientId) {
        super(id);
        AccountOpenedEvent accountOpenedEvent = new AccountOpenedEvent(
                id, now(UTC), getNextVersion(), clientId, ZERO);
        applyNewEvent(accountOpenedEvent);
    }

    public Account(UUID id, List<Event> eventStream) {
        super(id, eventStream);
    }

    public void deposit(BigDecimal amount) {
        BigDecimal newBalance = balance.add(amount);
        AccountDepositedEvent accountDepositedEvent = new AccountDepositedEvent(
                getId(), now(UTC), getNextVersion(), amount, newBalance);
        applyNewEvent(accountDepositedEvent);
    }

    public void withdraw(BigDecimal amount) {
        BigDecimal newBalance = balance.subtract(amount);
        if (newBalance.signum() == -1) throw new NonSufficientFundsException(getId(), balance, amount);
        AccountWithdrawnEvent accountWithdrawnEvent = new AccountWithdrawnEvent(
                getId(), now(UTC), getNextVersion(), amount, newBalance);
        applyNewEvent(accountWithdrawnEvent);
    }

    @SuppressWarnings("unused")
    private void apply(AccountOpenedEvent event) {
        clientId = event.getClientId();
        balance = event.getBalance();
    }

    @SuppressWarnings("unused")
    private void apply(AccountDepositedEvent event) {
        balance = event.getBalance();
    }

    @SuppressWarnings("unused")
    private void apply(AccountWithdrawnEvent event) { balance = event.getBalance(); }

    public BigDecimal getBalance() {
        return balance;
    }

    public UUID getClientId() {
        return clientId;
    }
}
