package bankservice.port.incoming.adapter.resources.accounts;

import static bankservice.port.incoming.adapter.resources.accounts.AccountNotFoundExceptionMapperTest.NeverFoundAccountResource.calls;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import bankservice.service.account.AccountNotFoundException;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import java.util.UUID;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
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