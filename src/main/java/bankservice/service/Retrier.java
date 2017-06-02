package bankservice.service;

import java.util.List;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;

public class Retrier {

    private final List<Class<? extends Exception>> exceptions;
    private final int maxAttempts;

    public Retrier(List<Class<? extends Exception>> exceptions, int maxAttempts) {
        checkArgument(isNotEmpty(exceptions));
        checkArgument(maxAttempts > 1);
        this.exceptions = exceptions;
        this.maxAttempts = maxAttempts;
    }

    public <T> T get(Supplier<T> supplier) {
        for (int attempt = 1; ; attempt++) {
            try {
                return supplier.get();
            } catch (Exception exception) {
                if (exceptions.stream().anyMatch(e -> e.isAssignableFrom(exception.getClass()))
                        && attempt < maxAttempts) {
                    continue;
                }
                throw exception;
            }
        }
    }
}
