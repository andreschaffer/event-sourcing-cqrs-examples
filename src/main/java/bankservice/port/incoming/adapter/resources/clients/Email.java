package bankservice.port.incoming.adapter.resources.clients;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import bankservice.domain.model.client.Email.EmailSpecification;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.NotNull;

@NotNull
@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = Email.EmailValidator.class)
public @interface Email {

    String message() default "not a well-formed email address";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


    class EmailValidator implements ConstraintValidator<Email, String> {

        private EmailSpecification emailSpecification = new EmailSpecification();

        @Override
        public void initialize(Email constraintAnnotation) {}

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return emailSpecification.isSatisfiedBy(value);
        }
    }
}
