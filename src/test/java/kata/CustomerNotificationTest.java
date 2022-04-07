package kata;

import kata.testdoubles.RecordingEmailGateway;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static kata.TestFactory.createDeliveryWithEmail;
import static kata.TestFactory.createDeliveryWithEmailForLocation;
import static kata.TestFactory.deliveryEventForDelivery;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
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

    @Test
    void informs_next_recipient_about_estimated_time_of_arrival() {
        var delivery = createDeliveryWithEmail("test@example.com");
        var deliveryEvent = deliveryEventForDelivery(delivery, 17, 45);
        var nextDelivery = createDeliveryWithEmailForLocation("next@example.com", 58.376125f,24.496687f);

        deliveryService.on(deliveryEvent, List.of(delivery, nextDelivery));

        Approvals.verify(emailGateway.invocations());
    }
}
