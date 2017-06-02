package bankservice.domain.model.account;

import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;
import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import bankservice.domain.model.Event;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.Test;

public class AccountTest {

    @Test
    public void newAccountHasNewAccountOpenedEvent() throws Exception {
        UUID id = randomUUID();
        UUID clientId = randomUUID();
        Account account = new Account(id, clientId);
        List<Event> newEvents = account.getNewEvents();

        assertThat(newEvents.size(), equalTo(1));
        assertThat(newEvents.get(0), instanceOf(AccountOpenedEvent.class));
        AccountOpenedEvent event = (AccountOpenedEvent) newEvents.get(0);
        assertThat(event.getAggregateId(), equalTo(id));
        assertThat(event.getClientId(), equalTo(clientId));
        assertThat(event.getBalance(), equalTo(ZERO));

        assertThat(account.getId(), equalTo(id));
        assertThat(account.getClientId(), equalTo(clientId));
        assertThat(account.getBalance(), equalTo(ZERO));

    }

    @Test
    public void depositedAccountHasAccountDepositedEvent() throws Exception {
        Account account = new Account(randomUUID(), randomUUID());
        BigDecimal amount = BigDecimal.valueOf(100.50);

        account.deposit(amount);

        List<Event> newEvents = account.getNewEvents();
        assertThat(newEvents.size(), equalTo(2));
        assertThat(newEvents.get(1), instanceOf(AccountDepositedEvent.class));
        AccountDepositedEvent event = (AccountDepositedEvent) newEvents.get(1);
        assertThat(event.getBalance(), equalTo(amount));

        assertThat(account.getBalance(), equalTo(amount));
    }

    @Test
    public void withdrawnAccountHasAccountWithdrawnEvent() throws Exception {
        Account account = new Account(randomUUID(), randomUUID());
        account.deposit(BigDecimal.valueOf(30));

        account.withdraw(BigDecimal.valueOf(20));

        List<Event> newEvents = account.getNewEvents();
        assertThat(newEvents.size(), equalTo(3));
        assertThat(newEvents.get(2), instanceOf(AccountWithdrawnEvent.class));
        AccountWithdrawnEvent event = (AccountWithdrawnEvent) newEvents.get(2);
        assertThat(event.getBalance(), equalTo(TEN));

        assertThat(account.getBalance(), equalTo(TEN));
    }

    @Test(expected = NonSufficientFundsException.class)
    public void failsWithdrawalWithNonSufficientFunds() throws Exception {
        Account account = new Account(randomUUID(), randomUUID());
        account.withdraw(BigDecimal.valueOf(1));
    }
}
