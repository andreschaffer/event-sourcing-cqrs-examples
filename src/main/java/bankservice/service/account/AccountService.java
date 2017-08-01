package bankservice.service.account;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.singletonList;
import static java.util.UUID.randomUUID;

import bankservice.domain.model.Event;
import bankservice.domain.model.EventStore;
import bankservice.domain.model.OptimisticLockingException;
import bankservice.domain.model.account.Account;
import bankservice.domain.model.account.NonSufficientFundsException;
import bankservice.service.Retrier;
import com.google.common.eventbus.EventBus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class AccountService {

    private final EventStore eventStore;
    private final EventBus eventBus;
    private final Retrier conflictRetrier;

    public AccountService(EventStore eventStore, EventBus eventBus) {
        this.eventStore = checkNotNull(eventStore);
        this.eventBus = checkNotNull(eventBus);
        int maxAttempts = 3;
        this.conflictRetrier = new Retrier(singletonList(OptimisticLockingException.class), maxAttempts);
    }

    public Optional<Account> loadAccount(UUID id) {
        List<Event> eventStream = eventStore.load(id);
        if (eventStream.isEmpty()) return Optional.empty();
        return Optional.of(new Account(id, eventStream));
    }

    public Account process(OpenAccountCommand command) {
        Account account = new Account(randomUUID(), command.getClientId());
        storeAndPublishEvents(account);
        return account;
    }

    public Account process(DepositAccountCommand command) throws AccountNotFoundException, OptimisticLockingException {
        return process(command.getId(), (account) -> account.deposit(command.getAmount()));
    }

    public Account process(WithdrawAccountCommand command)
            throws AccountNotFoundException, OptimisticLockingException, NonSufficientFundsException {
        return process(command.getId(), (account) -> account.withdraw(command.getAmount()));
    }

    private Account process(UUID accountId, Consumer<Account> consumer)
            throws AccountNotFoundException, OptimisticLockingException {

        return conflictRetrier.get(() -> {
            Optional<Account> possibleAccount = loadAccount(accountId);
            Account account = possibleAccount.orElseThrow(() -> new AccountNotFoundException(accountId));
            consumer.accept(account);
            storeAndPublishEvents(account);
            return account;
        });
    }

    private void storeAndPublishEvents(Account account) throws OptimisticLockingException {
        eventStore.store(account.getId(), account.getNewEvents(), account.getBaseVersion());
        account.getNewEvents().forEach(eventBus::post);
    }
}
