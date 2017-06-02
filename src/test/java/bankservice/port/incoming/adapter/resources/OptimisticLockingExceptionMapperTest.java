package bankservice.port.incoming.adapter.resources;

import bankservice.domain.model.OptimisticLockingException;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import static javax.ws.rs.client.Entity.json;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class OptimisticLockingExceptionMapperTest {

    @Rule
    public final ResourceTestRule resources = ResourceTestRule.builder()
            .addProvider(OptimisticLockingExceptionMapper.class)
            .addResource(new ConcurrentlyModifiedResource())
            .build();

    @Test
    public void returnConflict() throws Exception {
        Response response = resources.client().target("/concurrently-modified-resource").request().put(json("{}"));
        response.close();
        assertThat(response.getStatus(), equalTo(409));
    }

    @Consumes(APPLICATION_JSON)
    @Path("/concurrently-modified-resource")
    public static class ConcurrentlyModifiedResource {
        @PUT
        public Response put(String entity) throws OptimisticLockingException {
            throw new OptimisticLockingException("Testing exception mapper");
        }
    }
}
