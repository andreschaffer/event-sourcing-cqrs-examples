package bankservice.service;

import org.junit.Test;

import java.util.function.Supplier;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class RetrierTest {

    private RuntimeException exceptionToRetryOn =  new IllegalStateException();
    private int maxAttempts = 10;
    private Retrier retrier = new Retrier(singletonList(exceptionToRetryOn.getClass()), maxAttempts);

    @Test
    public void retriesGetUpToMaxAttemptsWithoutSharedStateBetweenCalls() throws Exception {
        retryGetUpToMaxAttempts();
        retryGetUpToMaxAttempts();
    }

    private void retryGetUpToMaxAttempts() {
        Supplier supplier = mock(Supplier.class);
        when(supplier.get()).thenThrow(exceptionToRetryOn);
        try {
            retrier.get(() -> supplier.get());
            fail("Should have thrown exception after max attempts");
        } catch (RuntimeException e) {
            verify(supplier, times(maxAttempts)).get();
            assertThat(e, instanceOf(exceptionToRetryOn.getClass()));
        }
    }
}
