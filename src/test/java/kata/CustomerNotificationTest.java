package kata;

import com.github.larseckart.tcr.CommitOnGreenExtension;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static kata.TestFactory.createDeliveryWithEmail;
import static kata.TestFactory.deliveryEventForDelivery;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(CommitOnGreenExtension.class)
public class CustomerNotificationTest {

    private RecordingEmailGateway emailGateway = new RecordingEmailGateway();
    private DeliveryService deliveryService = new DeliveryService(emailGateway);

    @Test
    void asks_for_recommendation_after_delivery() {
        var delivery = createDeliveryWithEmail("test@example.com");
        var deliveryEvent = deliveryEventForDelivery(delivery, 17, 45);

        deliveryService.on(deliveryEvent, List.of(delivery));

        Approvals.verify(emailGateway.invocations());
    }
}
