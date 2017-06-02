package bankservice.it.client;

import org.glassfish.jersey.uri.UriTemplate;

import java.net.URI;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static javax.ws.rs.core.UriBuilder.fromUri;

public class ResourcesUrls {

    private int port;

    public ResourcesUrls(int port) {
        checkArgument(port > 0);
        this.port = port;
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

    public URI accountsSummaryUrl(String clientId) {
        return fromUri("http://localhost").port(port).path("clients").path("{id}").path("accounts").path("summary")
            .build(clientId.toString());
    }

    public URI transactionsUrl(String accountId) {
        return fromUri("http://localhost").port(port)
            .path("accounts").path("{id}").path("transactions").build(accountId);
    }

    public URI clientsUrl() {
        return fromUri("http://localhost").port(port).path("clients").build();
    }

    public URI clientUrl(String clientId) {
        return fromUri(clientUriTemplate().createURI(clientId.toString())).build();
    }

    public UriTemplate clientUriTemplate() {
        return new UriTemplate(format("http://localhost:%d/clients/{id}", port));
    }

    public URI clientAccountsUrl(String clientId) {
        return fromUri("http://localhost").port(port).path("clients").path("{id}").path("accounts").build(clientId);
    }
}
