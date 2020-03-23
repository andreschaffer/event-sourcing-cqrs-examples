package bankservice.port.incoming.adapter.resources;

import static javax.ws.rs.core.Response.Status.CONFLICT;

import bankservice.domain.model.OptimisticLockingException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class OptimisticLockingExceptionMapper implements
    ExceptionMapper<OptimisticLockingException> {

  @Override
  public Response toResponse(OptimisticLockingException exception) {
    return Response.status(CONFLICT).build();
  }
}
