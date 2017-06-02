package bankservice.service.account;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

public class OpenAccountCommand {
    private final UUID clientId;

    public OpenAccountCommand(UUID clientId) {
        this.clientId = checkNotNull(clientId);
    }

    public UUID getClientId() {
        return clientId;
    }
}
