package bankservice.it;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;

import bankservice.bootstrap.BankServiceApplication;
import bankservice.it.client.ResourcesClient;
import bankservice.it.client.ResourcesDtos;
import bankservice.it.setup.StateSetup;
import io.dropwizard.core.Configuration;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
abstract class BaseIT {

  protected static final DropwizardAppExtension<Configuration> BANK_SERVICE =
      new DropwizardAppExtension<>(BankServiceApplication.class,
          resourceFilePath("integration.yml"));

  protected static ResourcesClient resourcesClient;
  protected static ResourcesDtos resourcesDtos;
  protected static StateSetup stateSetup;

  @BeforeAll
  public static void setUpBaseClass() {
    resourcesClient = new ResourcesClient(BANK_SERVICE.getEnvironment(),
        BANK_SERVICE.getLocalPort());
    resourcesDtos = new ResourcesDtos(BANK_SERVICE.getObjectMapper());
    stateSetup = new StateSetup(resourcesClient, resourcesDtos);
  }
}
