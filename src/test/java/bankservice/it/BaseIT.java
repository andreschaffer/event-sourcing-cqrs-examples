package bankservice.it;

import static io.dropwizard.testing.ResourceHelpers.resourceFilePath;

import bankservice.bootstrap.BankServiceApplication;
import bankservice.it.client.ResourcesClient;
import bankservice.it.client.ResourcesDtos;
import bankservice.it.setup.StateSetup;
import io.dropwizard.Configuration;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.BeforeClass;
import org.junit.ClassRule;

public abstract class BaseIT {

    @ClassRule
    public static final DropwizardAppRule<Configuration> BANK_SERVICE =
        new DropwizardAppRule<>(BankServiceApplication.class, resourceFilePath("integration.yml"));

    protected static ResourcesClient resourcesClient;
    protected static ResourcesDtos resourcesDtos;
    protected static StateSetup stateSetup;

    @BeforeClass
    public static void setUpBaseClass() throws Exception {
        resourcesClient = new ResourcesClient(BANK_SERVICE.getEnvironment(), BANK_SERVICE.getLocalPort());
        resourcesDtos = new ResourcesDtos(BANK_SERVICE.getObjectMapper());
        stateSetup = new StateSetup(resourcesClient, resourcesDtos);
    }
}
