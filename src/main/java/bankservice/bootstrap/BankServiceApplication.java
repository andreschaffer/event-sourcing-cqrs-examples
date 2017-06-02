package bankservice.bootstrap;

import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.logging.Level.INFO;
import static java.util.logging.Logger.getLogger;
import static org.glassfish.jersey.logging.LoggingFeature.DEFAULT_LOGGER_NAME;
import static org.glassfish.jersey.logging.LoggingFeature.Verbosity.PAYLOAD_ANY;

import bankservice.domain.model.EventStore;
import bankservice.projection.accounttransactions.AccountTransactionsRepository;
import bankservice.port.incoming.adapter.resources.OptimisticLockingExceptionMapper;
import bankservice.port.incoming.adapter.resources.accounts.AccountNotFoundExceptionMapper;
import bankservice.port.incoming.adapter.resources.accounts.AccountResource;
import bankservice.port.incoming.adapter.resources.accounts.deposit.AccountDepositsResource;
import bankservice.projection.accounttransactions.AccountTransactionsResource;
import bankservice.port.incoming.adapter.resources.accounts.withdrawal.AccountWithdrawalsResource;
import bankservice.port.incoming.adapter.resources.clients.ClientResource;
import bankservice.port.incoming.adapter.resources.clients.ClientsResource;
import bankservice.port.incoming.adapter.resources.clients.accounts.ClientAccountsResource;
import bankservice.port.outgoing.adapter.eventstore.InMemoryEventStore;
import bankservice.projection.clientaccountssummary.InMemoryClientAccountsSummaryRepository;
import bankservice.projection.accounttransactions.InMemoryAccountTransactionsRepository;
import bankservice.projection.clientaccountssummary.ClientAccountsSummaryListener;
import bankservice.projection.clientaccountssummary.ClientAccountsSummaryRepository;
import bankservice.projection.clientaccountssummary.ClientAccountsSummaryResource;
import bankservice.service.account.AccountService;
import bankservice.service.client.ClientService;
import bankservice.projection.accounttransactions.AccountTransactionsListener;
import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.logging.LoggingFeature;

public class BankServiceApplication extends Application<Configuration> {

    public static void main(String[] args) throws Exception {
        new BankServiceApplication().run(args);
    }

    @Override
    public void run(Configuration configuration, Environment environment) throws Exception {
        registerFilters(environment);
        registerExceptionMappers(environment);
        registerHypermediaSupport(environment);
        registerResources(environment);
    }

    private void registerFilters(Environment environment) {
        environment.jersey().register(new LoggingFeature(getLogger(DEFAULT_LOGGER_NAME), INFO, PAYLOAD_ANY, 1024));
    }

    private void registerExceptionMappers(Environment environment) {
        environment.jersey().register(AccountNotFoundExceptionMapper.class);
        environment.jersey().register(OptimisticLockingExceptionMapper.class);
    }

    private void registerHypermediaSupport(Environment environment) {
        environment.jersey().getResourceConfig().register(DeclarativeLinkingFeature.class);
    }

    private void registerResources(Environment environment) {
        EventStore eventStore = new InMemoryEventStore();
        EventBus eventBus = new AsyncEventBus(newSingleThreadExecutor());

        // write model
        AccountService accountService = new AccountService(eventStore, eventBus);
        environment.jersey().register(new ClientAccountsResource(accountService));
        environment.jersey().register(new AccountResource(accountService));
        environment.jersey().register(new AccountDepositsResource(accountService));
        environment.jersey().register(new AccountWithdrawalsResource(accountService));

        ClientService clientService = new ClientService(eventStore);
        environment.jersey().register(new ClientsResource(clientService));
        environment.jersey().register(new ClientResource(clientService));

        // read model
        AccountTransactionsRepository accountTransactionsRepository = new InMemoryAccountTransactionsRepository();
        eventBus.register(new AccountTransactionsListener(accountTransactionsRepository));
        environment.jersey().register(new AccountTransactionsResource(accountTransactionsRepository));

        ClientAccountsSummaryRepository clientAccountsSummaryRepository =
            new InMemoryClientAccountsSummaryRepository();
        eventBus.register(new ClientAccountsSummaryListener(clientAccountsSummaryRepository));
        environment.jersey().register(new ClientAccountsSummaryResource(clientAccountsSummaryRepository));
    }
}
