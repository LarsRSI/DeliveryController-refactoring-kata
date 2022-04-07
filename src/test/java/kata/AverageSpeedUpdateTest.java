package kata;

import com.github.larseckart.tcr.CommitOnGreenExtension;
import kata.testdoubles.NoOpEmailGateway;
import kata.testdoubles.RecordingMapService;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static kata.TestFactory.createCompletedDeliveryAt;
import static kata.TestFactory.createDeliveryOrderedAt;
import static kata.TestFactory.createDeliveryWithId;
import static kata.TestFactory.deliveryEventForDelivery;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(CommitOnGreenExtension.class)
public class AverageSpeedUpdateTest {

    private final RecordingMapService mapService = new RecordingMapService();
    private final DeliveryService deliveryService = new DeliveryService(new NoOpEmailGateway(), mapService);

    @Test
    void update_average_speed_when_multiple_deliveries_and_current_delivery_is_late() {
        var delivery = createCompletedDeliveryAt(17, 0);
        var currentDelivery = createDeliveryOrderedAt(2L, 17, 2);
        var delivery3 = createDeliveryWithId(3L);
        var deliveryEvent = deliveryEventForDelivery(currentDelivery, 17, 19);

        deliveryService.on(deliveryEvent, List.of(delivery, currentDelivery, delivery3));

        Approvals.verify(mapService.invocations());
    }
}
