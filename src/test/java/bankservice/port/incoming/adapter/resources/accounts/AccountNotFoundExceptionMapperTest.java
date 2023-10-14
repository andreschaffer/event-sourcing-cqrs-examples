package bankservice.port.incoming.adapter.resources.accounts;

import static bankservice.port.incoming.adapter.resources.accounts.AccountNotFoundExceptionMapperTest.NeverFoundAccountResource.calls;
import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import bankservice.service.account.AccountNotFoundException;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
class AccountNotFoundExceptionMapperTest {

  static final ResourceExtension RESOURCES = ResourceExtension.builder()
      .addProvider(AccountNotFoundExceptionMapper.class)
      .addResource(new NeverFoundAccountResource())
      .build();

  @Test
  void returnNotFound() {
    Response response = RESOURCES.client()
        .target(format("/never-found-account-resource/%s", randomUUID()))
        .request().get();
    response.close();
    assertThat(response.getStatus(), equalTo(404));
    assertThat(calls, equalTo(1));
  }

  @Produces(APPLICATION_JSON)
  @Path("/never-found-account-resource/{id}")
  public static class NeverFoundAccountResource {

    static int calls = 0;

    @GET
    public Response get(@PathParam("id") String id) throws AccountNotFoundException {
      calls++;
      throw new AccountNotFoundException(UUID.fromString(id));
    }
  }
}