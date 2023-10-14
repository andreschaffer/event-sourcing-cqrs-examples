package bankservice.port.incoming.adapter.resources;


import static jakarta.ws.rs.core.Response.Status.CONFLICT;

import bankservice.domain.model.OptimisticLockingException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class OptimisticLockingExceptionMapper implements
    ExceptionMapper<OptimisticLockingException> {

  @Override
  public Response toResponse(OptimisticLockingException exception) {
    return Response.status(CONFLICT).build();
  }
}
