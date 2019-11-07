package bankservice.service;

import java.util.List;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

public class Retrier {

    private final List<Class<? extends Exception>> retriableExceptions;
    private final int maxAttempts;

    public Retrier(List<Class<? extends Exception>> retriableExceptions, int maxAttempts) {
        checkArgument(isNotEmpty(retriableExceptions));
        checkArgument(maxAttempts > 1);
        this.retriableExceptions = retriableExceptions;
        this.maxAttempts = maxAttempts;
    }

    public <T> T get(Supplier<T> supplier) {
        for (int attempt = 1; ; attempt++) {
            try {
                return supplier.get();
            } catch (Exception exception) {
                if (!isRetriable(exception) || attempt == maxAttempts) throw exception;
            }
        }
    }

    private boolean isRetriable(Exception exception) {
        return retriableExceptions.stream().anyMatch(e -> e.isAssignableFrom(exception.getClass()));
    }
}
