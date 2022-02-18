package bankservice.service.account;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import bankservice.domain.model.EventStore;
import bankservice.domain.model.OptimisticLockingException;
import bankservice.domain.model.account.Account;
import bankservice.domain.model.account.AccountDepositedEvent;
import bankservice.domain.model.account.AccountOpenedEvent;
import bankservice.domain.model.account.AccountWithdrawnEvent;
import bankservice.port.outgoing.adapter.eventstore.InMemoryEventStore;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountServiceTest {

  private EventStore eventStore;
  private EventBusCounter eventBusCounter;
  private AccountService accountService;

  @BeforeEach
  void setUp() {
    eventStore = spy(new InMemoryEventStore());
    EventBus eventBus = new EventBus();
    eventBusCounter = new EventBusCounter();
    eventBus.register(eventBusCounter);
    accountService = new AccountService(eventStore, eventBus);
  }

  @Test
  void retryOnAccountWithdrawalConflictsUpToThreeAttempts() {
    Account account = accountService.process(new OpenAccountCommand(randomUUID()));
    UUID id = account.getId();
    accountService.process(new DepositAccountCommand(id, TEN));
    doThrow(OptimisticLockingException.class)
        .doThrow(OptimisticLockingException.class)
        .doCallRealMethod()
        .when(eventStore).store(eq(id), anyList(), anyInt());

    accountService.process(new WithdrawAccountCommand(id, ONE));
    int creationAttempts = 1;
    int depositAttempts = 1;
    int withdrawalAttempts = 3;
    int loadTimes = depositAttempts + withdrawalAttempts;
    int storeTimes = creationAttempts + depositAttempts + withdrawalAttempts;
    verify(eventStore, times(loadTimes)).load(eq(id));
    verify(eventStore, times(storeTimes)).store(eq(id), anyList(), anyInt());
    assertThat(eventBusCounter.eventsCount.get(AccountOpenedEvent.class), equalTo(1));
    assertThat(eventBusCounter.eventsCount.get(AccountDepositedEvent.class), equalTo(1));
    assertThat(eventBusCounter.eventsCount.get(AccountWithdrawnEvent.class), equalTo(1));
    assertThat(eventBusCounter.eventsCount.size(), equalTo(3));
  }

  private static class EventBusCounter {

    Map<Class<?>, Integer> eventsCount = new ConcurrentHashMap<>();

    @Subscribe
    @SuppressWarnings("unused")
    public void handle(Object event) {
      eventsCount.merge(event.getClass(), 1, Integer::sum);
    }
  }
}
