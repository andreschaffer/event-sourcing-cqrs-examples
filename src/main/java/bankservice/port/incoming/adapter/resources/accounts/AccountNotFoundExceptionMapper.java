package bankservice.port.incoming.adapter.resources.accounts;


import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

import bankservice.service.account.AccountNotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class AccountNotFoundExceptionMapper implements ExceptionMapper<AccountNotFoundException> {

  @Override
  public Response toResponse(AccountNotFoundException exception) {
    return Response.status(NOT_FOUND).build();
  }
}
