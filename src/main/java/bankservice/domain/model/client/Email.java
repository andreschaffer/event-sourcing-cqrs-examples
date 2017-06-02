package bankservice.domain.model.client;

import bankservice.domain.model.Specification;
import bankservice.domain.model.ValueObject;
import org.apache.commons.validator.routines.EmailValidator;

import static com.google.common.base.Preconditions.checkArgument;

public class Email extends ValueObject {

    private final String value;

    public Email(String value) {
        checkArgument(new EmailSpecification().isSatisfiedBy(value));
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static class EmailSpecification implements Specification<String> {
        @Override
        public boolean isSatisfiedBy(String value) {
            return EmailValidator.getInstance().isValid(value);
        }
    }
}
