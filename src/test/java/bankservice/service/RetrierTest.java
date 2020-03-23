package bankservice.service;

import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Supplier;
import org.junit.jupiter.api.Test;

class RetrierTest {

  private RuntimeException exceptionToRetryOn = new IllegalStateException();
  private int maxAttempts = 10;
  private Retrier retrier = new Retrier(singletonList(exceptionToRetryOn.getClass()), maxAttempts);

  @Test
  void retriesGetUpToMaxAttemptsWithoutSharedStateBetweenCalls() {
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
