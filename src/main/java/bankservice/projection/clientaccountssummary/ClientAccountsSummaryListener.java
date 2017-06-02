package bankservice.projection.clientaccountssummary;

import bankservice.domain.model.account.AccountDepositedEvent;
import bankservice.domain.model.account.AccountOpenedEvent;
import bankservice.domain.model.account.AccountWithdrawnEvent;
import com.google.common.eventbus.Subscribe;

import static com.google.common.base.Preconditions.checkNotNull;

public class ClientAccountsSummaryListener {

    private final ClientAccountsSummaryRepository clientAccountsSummaryRepository;

    public ClientAccountsSummaryListener(ClientAccountsSummaryRepository clientAccountsSummaryRepository) {
        this.clientAccountsSummaryRepository = checkNotNull(clientAccountsSummaryRepository);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(AccountOpenedEvent event) {
        clientAccountsSummaryRepository.addNewAccount(
                event.getClientId(), event.getAggregateId(), event.getBalance(), event.getVersion());
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(AccountDepositedEvent event) {
        clientAccountsSummaryRepository.updateBalance(event.getAggregateId(), event.getBalance(), event.getVersion());
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(AccountWithdrawnEvent event) {
        clientAccountsSummaryRepository.updateBalance(event.getAggregateId(), event.getBalance(), event.getVersion());
    }
}
