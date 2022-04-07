package kata;

import com.github.larseckart.tcr.CommitOnGreenExtension;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static kata.TestFactory.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(CommitOnGreenExtension.class)
class DeliveryUpdateTest {

    private final DeliveryService deliveryService = new DeliveryService(new NoOpEmailGateway());

    @Test
    void marks_delivery_as_arrived() {
        var delivery = createDeliveryWithId(123L);
        var deliveryEvent = createDeliveryEventWithId(123L);

        deliveryService.on(deliveryEvent, List.of(delivery));

        assertThat(delivery.isArrived()).isTrue();
    }

    @Test
    void marks_delivery_on_time_when_took_less_than_10_minutes() {
        var delivery = deliveryOrderedAt(10, 40);
        var deliveryEvent = deliveryEventAt(10, 49);

        deliveryService.on(deliveryEvent, List.of(delivery));

        assertThat(delivery.isOnTime()).isTrue();
    }

    @Test
    void marks_delivery_not_on_time_when_took_more_or_equal_than_10_minutes() {
        var delivery = deliveryOrderedAt(19, 30);
        var deliveryEvent = deliveryEventAt(19, 40);

        deliveryService.on(deliveryEvent, List.of(delivery));

        assertThat(delivery.isOnTime()).isFalse();
    }

    @Test
    void sets_delivery_time_on_delivery() {
        var delivery = deliveryOrderedAt(19, 30);
        var deliveryEvent = deliveryEventAt(19, 40);

        deliveryService.on(deliveryEvent, List.of(delivery));

        assertThat(delivery.getTimeOfDelivery()).isEqualTo(localDateTime(19, 40));
    }

    private static class NoOpEmailGateway extends SendgridEmailGateway {
        @Override
        public void send(String to, String subject, String message) {

        }
    }
}
