package bankservice.it;

import static org.junit.Assert.assertTrue;

import bankservice.port.incoming.adapter.resources.OptimisticLockingExceptionMapper;
import bankservice.port.incoming.adapter.resources.accounts.AccountNotFoundExceptionMapper;
import java.util.Set;
import org.junit.Test;

public class ExceptionMappersIT extends BaseIT {

    @Test
    public void registeredExceptionMappers() throws Exception {
        Set<Class<?>> classes = BANK_SERVICE.getEnvironment().jersey().getResourceConfig().getClasses();
        assertTrue(classes.contains(OptimisticLockingExceptionMapper.class));
        assertTrue(classes.contains(AccountNotFoundExceptionMapper.class));
    }
}
