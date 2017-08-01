package bankservice.port.incoming.adapter.resources.accounts.deposits;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

public class DepositDto {

    @JsonProperty(access = READ_ONLY)
    private UUID accountId;

    @NotNull
    private BigDecimal amount;


    @SuppressWarnings("unused")
    public UUID getAccountId() {
        return accountId;
    }

    @SuppressWarnings("unused")
    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    @SuppressWarnings("unused")
    public BigDecimal getAmount() {
        return amount;
    }

    @SuppressWarnings("unused")
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
