package bankservice.it;

import static java.util.logging.Level.INFO;
import static java.util.logging.Logger.getLogger;
import static javax.ws.rs.core.UriBuilder.fromUri;
import static org.glassfish.jersey.logging.LoggingFeature.DEFAULT_LOGGER_NAME;
import static org.glassfish.jersey.logging.LoggingFeature.Verbosity.PAYLOAD_ANY;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import io.dropwizard.client.JerseyClientBuilder;
import java.net.URI;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.BeforeClass;
import org.junit.Test;

public class HealthCheckIT extends BaseIT {

    private static Client client;

    @BeforeClass
    public static void setUpClass() throws Exception {
        client = new JerseyClientBuilder(BANK_SERVICE.getEnvironment())
                .build(HealthCheckIT.class.getName())
                .register(new LoggingFeature(getLogger(DEFAULT_LOGGER_NAME), INFO, PAYLOAD_ANY, 1024));
    }

    @Test
    public void overallHealth() throws Exception {
        Response response = client.target(healthCheckUrl()).request().get();
        response.close();
        assertThat(response.getStatus(), equalTo(200));
    }

    private URI healthCheckUrl() {
        return fromUri("http://localhost").port(BANK_SERVICE.getAdminPort()).path("healthcheck").build();
    }
}
