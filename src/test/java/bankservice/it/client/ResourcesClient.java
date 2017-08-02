package bankservice.it.client;

import com.fasterxml.jackson.databind.JsonNode;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.logging.LoggingFeature;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.logging.Level.INFO;
import static java.util.logging.Logger.getLogger;
import static javax.ws.rs.client.Entity.json;
import static org.glassfish.jersey.client.ClientProperties.CONNECT_TIMEOUT;
import static org.glassfish.jersey.client.ClientProperties.READ_TIMEOUT;
import static org.glassfish.jersey.logging.LoggingFeature.DEFAULT_LOGGER_NAME;
import static org.glassfish.jersey.logging.LoggingFeature.Verbosity.PAYLOAD_ANY;

public class ResourcesClient {

    private final Client client;
    private final ResourcesUrls resourcesUrls;

    public ResourcesClient(Environment environment, int port) {
        this.client = new JerseyClientBuilder(checkNotNull(environment))
                .build(ResourcesClient.class.getName())
                .property(CONNECT_TIMEOUT, 2000)
                .property(READ_TIMEOUT, 3000)
                .register(new LoggingFeature(getLogger(DEFAULT_LOGGER_NAME), INFO, PAYLOAD_ANY, 1024));
        this.resourcesUrls = new ResourcesUrls(port);
    }

    public ResourcesUrls getResourcesUrls() {
        return resourcesUrls;
    }

    public Response postClient(JsonNode clientDto) {
        return client.target(resourcesUrls.clientsUrl()).request().post(json(clientDto));
    }

    public Response getClient(String clientId) {
        return client.target(resourcesUrls.clientUrl(clientId)).request().get();
    }

    public Response putClient(String clientId, JsonNode clientDto) {
        return client.target(resourcesUrls.clientUrl(clientId)).request().put(json(clientDto));
    }

    public Response postAccount(JsonNode accountDto) {
        return client.target(resourcesUrls.accountsUrl()).request().post(json(accountDto));
    }

    public Response getAccount(String accountId) {
        return client.target(resourcesUrls.accountUrl(accountId)).request().get();
    }

    public Response postDeposit(String accountId, JsonNode depositDto) {
        return client.target(resourcesUrls.depositsUrl(accountId)).request().post(json(depositDto));
    }

    public Response postWithdrawal(String accountId, JsonNode withdrawalDto) {
        return client.target(resourcesUrls.withdrawalsUrl(accountId)).request().post(json(withdrawalDto));
    }

    public Response getClientAccounts(String clientId) {
        return client.target(resourcesUrls.clientAccountsUrl(clientId)).request().get();
    }

    public Response getAccountTransactions(String accountId) {
        return client.target(resourcesUrls.accountTransactionsUrl(accountId)).request().get();
    }
}
