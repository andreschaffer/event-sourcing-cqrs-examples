package bankservice.domain.model.client;

import bankservice.domain.model.client.Email;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
public class EmailSpecificationTest {

    private final Email.EmailSpecification specification = new Email.EmailSpecification();
    private final String value;
    private final boolean isValid;

    public EmailSpecificationTest(String value, boolean isValid) {
        this.value = value;
        this.isValid = isValid;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                { "email@example.com", true },
                { "firstname.lastname@example.com", true },
                { "email@subdomain.example.com", true },
                { "firstname+lastname@example.com", true },

                { "emailexample", false },
                { "email@example", false },
                { "email@-example.com", false },
                { "email@example.web", false },
                { "email@111.222.333.4444", false },
                { "email@example..com", false },
                { "abc..123@example.com", false },
        });
    }

    @Test
    public void validate() throws Exception {
        assertThat(specification.isSatisfiedBy(value), equalTo(isValid));
    }
}
