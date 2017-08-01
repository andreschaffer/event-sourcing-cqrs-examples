package bankservice.projection.accountssummary;

import bankservice.domain.model.account.AccountDepositedEvent;
import bankservice.domain.model.account.AccountOpenedEvent;
import bankservice.domain.model.account.AccountWithdrawnEvent;
import com.google.common.eventbus.Subscribe;

import static com.google.common.base.Preconditions.checkNotNull;

public class AccountsSummaryListener {

    private final AccountsSummaryRepository accountsSummaryRepository;

    public AccountsSummaryListener(AccountsSummaryRepository accountsSummaryRepository) {
        this.accountsSummaryRepository = checkNotNull(accountsSummaryRepository);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(AccountOpenedEvent event) {
        accountsSummaryRepository.addNewAccount(
                event.getClientId(), event.getAggregateId(), event.getBalance(), event.getVersion());
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(AccountDepositedEvent event) {
        accountsSummaryRepository.updateBalance(event.getAggregateId(), event.getBalance(), event.getVersion());
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(AccountWithdrawnEvent event) {
        accountsSummaryRepository.updateBalance(event.getAggregateId(), event.getBalance(), event.getVersion());
    }
}
