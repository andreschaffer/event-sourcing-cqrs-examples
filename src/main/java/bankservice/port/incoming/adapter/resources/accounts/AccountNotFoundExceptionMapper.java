package bankservice.port.incoming.adapter.resources.accounts;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

import bankservice.service.account.AccountNotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class AccountNotFoundExceptionMapper implements ExceptionMapper<AccountNotFoundException> {

  @Override
  public Response toResponse(AccountNotFoundException exception) {
    return Response.status(NOT_FOUND).build();
  }
}
