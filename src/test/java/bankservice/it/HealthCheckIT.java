package bankservice.it;

import static jakarta.ws.rs.core.UriBuilder.fromUri;
import static java.util.logging.Level.INFO;
import static java.util.logging.Logger.getLogger;
import static org.glassfish.jersey.logging.LoggingFeature.DEFAULT_LOGGER_NAME;
import static org.glassfish.jersey.logging.LoggingFeature.Verbosity.PAYLOAD_ANY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import io.dropwizard.client.JerseyClientBuilder;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.core.Response;
import java.net.URI;
import org.glassfish.jersey.logging.LoggingFeature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class HealthCheckIT extends BaseIT {

  private static Client client;

  @BeforeAll
  static void setUpClass() {
    client = new JerseyClientBuilder(BANK_SERVICE.getEnvironment())
        .build(HealthCheckIT.class.getName())
        .register(new LoggingFeature(getLogger(DEFAULT_LOGGER_NAME), INFO, PAYLOAD_ANY, 1024));
  }

  @Test
  void overallHealth() {
    Response response = client.target(healthCheckUrl()).request().get();
    response.close();
    assertThat(response.getStatus(), equalTo(200));
  }

  private URI healthCheckUrl() {
    return fromUri("http://localhost").port(BANK_SERVICE.getAdminPort()).path("healthcheck")
        .build();
  }
}
