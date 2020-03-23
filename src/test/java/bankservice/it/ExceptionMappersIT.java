package bankservice.it;


import static org.junit.jupiter.api.Assertions.assertTrue;

import bankservice.port.incoming.adapter.resources.OptimisticLockingExceptionMapper;
import bankservice.port.incoming.adapter.resources.accounts.AccountNotFoundExceptionMapper;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ExceptionMappersIT extends BaseIT {

  @Test
  void registeredExceptionMappers() {
    Set<Class<?>> classes = BANK_SERVICE.getEnvironment().jersey().getResourceConfig().getClasses();
    assertTrue(classes.contains(OptimisticLockingExceptionMapper.class));
    assertTrue(classes.contains(AccountNotFoundExceptionMapper.class));
  }
}
