package bankservice.projection.accounttransactions;

import static bankservice.projection.accounttransactions.TransactionProjection.TransactionType.DEPOSIT;
import static bankservice.projection.accounttransactions.TransactionProjection.TransactionType.WITHDRAWAL;
import static com.google.common.base.Preconditions.checkNotNull;

import bankservice.domain.model.account.AccountDepositedEvent;
import bankservice.domain.model.account.AccountWithdrawnEvent;
import com.google.common.eventbus.Subscribe;

public class TransactionsListener {

    private TransactionsRepository transactionsRepository;

    public TransactionsListener(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = checkNotNull(transactionsRepository);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(AccountDepositedEvent event) {
        TransactionProjection tx = new TransactionProjection(
                event.getAggregateId(), DEPOSIT, event.getAmount(), event.getTimestamp(), event.getVersion());
        transactionsRepository.save(tx);
    }

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(AccountWithdrawnEvent event) {
        TransactionProjection tx = new TransactionProjection(
                event.getAggregateId(), WITHDRAWAL, event.getAmount(), event.getTimestamp(), event.getVersion());
        transactionsRepository.save(tx);
    }
}
