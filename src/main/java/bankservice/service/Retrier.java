package bankservice.service;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.List;
import java.util.function.Supplier;

public class Retrier {

  private final List<Class<? extends Exception>> retriableExceptions;
  private final int maxAttempts;

  public Retrier(List<Class<? extends Exception>> retriableExceptions, int maxAttempts) {
    checkArgument(!retriableExceptions.isEmpty());
    checkArgument(maxAttempts > 1);
    this.retriableExceptions = retriableExceptions;
    this.maxAttempts = maxAttempts;
  }

  public <T> T get(Supplier<T> supplier) {
    for (int attempt = 1; ; attempt++) {
      try {
        return supplier.get();
      } catch (Exception exception) {
        if (!isRetriable(exception) || attempt == maxAttempts) {
          throw exception;
        }
      }
    }
  }

  private boolean isRetriable(Exception exception) {
    return retriableExceptions.stream().anyMatch(e -> e.isAssignableFrom(exception.getClass()));
  }
}
