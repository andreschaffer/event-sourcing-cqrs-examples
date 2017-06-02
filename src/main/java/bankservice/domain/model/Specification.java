package bankservice.domain.model;

public interface Specification<T> {

    boolean isSatisfiedBy(T value);

}
