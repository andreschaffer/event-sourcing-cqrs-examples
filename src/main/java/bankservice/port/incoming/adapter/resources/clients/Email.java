package bankservice.port.incoming.adapter.resources.clients;

import bankservice.domain.model.client.Email.EmailSpecification;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@NotNull
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = Email.EmailValidator.class)
public @interface Email {

    String message() default "not a well-formed email address";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class EmailValidator implements ConstraintValidator<Email, String> {
        @Override
        public void initialize(Email constraintAnnotation) {}

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return new EmailSpecification().isSatisfiedBy(value);
        }
    }
}
