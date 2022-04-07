package kata;

import org.junit.jupiter.api.Test;

public class CustomerNotificationTest {

    @Test
    void asks_for_recommendation_after_delivery() {
        RecordingEmailGateway emailGateway = new RecordingEmailGateway();

        DeliveryService deliveryService = new DeliveryService(emailGateway);


    }
}
