package bankservice.it.client;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static javax.ws.rs.core.UriBuilder.fromUri;

import java.net.URI;
import org.glassfish.jersey.uri.UriTemplate;

public class ResourcesUrls {

    private int port;

    public ResourcesUrls(int port) {
        checkArgument(port > 0);
        this.port = port;
    }

    public URI clientsUrl() {
        return fromUri("http://localhost").port(port).path("clients").build();
    }

    public URI clientUrl(String clientId) {
        return fromUri(clientUriTemplate().createURI(clientId)).build();
    }

    public UriTemplate clientUriTemplate() {
        return new UriTemplate(format("http://localhost:%d/clients/{id}", port));
    }

    public URI accountsUrl() {
        return fromUri("http://localhost").port(port).path("accounts").build();
    }

    public URI accountUrl(String accountId) {
        return fromUri(accountUriTemplate().createURI(accountId)).build();
    }

    public UriTemplate accountUriTemplate() {
        return new UriTemplate(format("http://localhost:%d/accounts/{id}", port));
    }

    public URI depositsUrl(String accountId) {
        return fromUri("http://localhost").port(port)
            .path("accounts").path("{id}").path("deposits").build(accountId);
    }

    public URI withdrawalsUrl(String accountId) {
        return fromUri("http://localhost").port(port)
            .path("accounts").path("{id}").path("withdrawals").build(accountId);
    }

    public URI clientAccountsUrl(String clientId) {
        return fromUri("http://localhost").port(port).path("clients").path("{id}").path("accounts")
            .build(clientId.toString());
    }

    public URI accountTransactionsUrl(String accountId) {
        return fromUri("http://localhost").port(port)
            .path("accounts").path("{id}").path("transactions").build(accountId);
    }
}
