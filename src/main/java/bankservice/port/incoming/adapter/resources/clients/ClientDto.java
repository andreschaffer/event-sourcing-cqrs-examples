package bankservice.port.incoming.adapter.resources.clients;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;
import org.hibernate.validator.constraints.NotBlank;

public class ClientDto {

    @JsonProperty(access = READ_ONLY)
    private UUID id;

    @NotBlank
    private String name;

    @Email
    private String email;


    @SuppressWarnings("unused")
    public UUID getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(UUID id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public void setName(String name) {
        this.name = name;
    }

    @SuppressWarnings("unused")
    public String getEmail() {
        return email;
    }

    @SuppressWarnings("unused")
    public void setEmail(String email) {
        this.email = email;
    }
}
