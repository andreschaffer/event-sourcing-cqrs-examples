package bankservice.domain.model.client;

import static java.util.UUID.randomUUID;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import bankservice.domain.model.Event;
import java.util.List;
import java.util.UUID;
import org.junit.Test;

public class ClientTest {

    @Test
    public void newClientHasBeenEnrolled() throws Exception {
        UUID id = randomUUID();
        String name = "john";
        Email email = new Email("john@example.com");

        Client client = new Client(id, name, email);

        List<Event> newEvents = client.getNewEvents();
        assertThat(newEvents.size(), equalTo(1));
        assertThat(newEvents.get(0), instanceOf(ClientEnrolledEvent.class));
        ClientEnrolledEvent event = (ClientEnrolledEvent) newEvents.get(0);
        assertThat(event.getAggregateId(), equalTo(id));
        assertThat(event.getName(), equalTo(name));
        assertThat(event.getEmail(), equalTo(email));

        assertThat(client.getId(), equalTo(id));
        assertThat(client.getName(), equalTo(name));
        assertThat(client.getEmail(), equalTo(email));
    }
}
