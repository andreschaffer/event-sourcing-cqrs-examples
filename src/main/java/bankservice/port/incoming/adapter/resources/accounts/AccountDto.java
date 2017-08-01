package bankservice.port.incoming.adapter.resources.accounts;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static java.math.BigDecimal.ROUND_HALF_UP;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.UUID;

public class AccountDto {

    @JsonProperty(access = READ_ONLY)
    private UUID id;

    @JsonProperty(access = READ_ONLY)
    private BigDecimal balance;

    private UUID clientId;

    @SuppressWarnings("unused")
    public UUID getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public void setId(UUID id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public BigDecimal getBalance() {
        return balance;
    }

    @SuppressWarnings("unused")
    public void setBalance(BigDecimal balance) {
        this.balance = balance.setScale(2, ROUND_HALF_UP);
    }

    @SuppressWarnings("unused")
    public UUID getClientId() {
        return clientId;
    }

    @SuppressWarnings("unused")
    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }
}
