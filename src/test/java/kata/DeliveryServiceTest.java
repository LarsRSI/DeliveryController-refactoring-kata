package kata;

import com.github.larseckart.tcr.CommitOnGreenExtension;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(CommitOnGreenExtension.class)
class DeliveryServiceTest {

    private DeliveryService deliveryService = new DeliveryService(new NoOpEmailGateway());

    @Test
    void marks_delivery_as_arrived() {
        var delivery = createDeliveryWithId(123L);
        var deliveryEvent = createDeliveryEventWithId(123L);

        deliveryService.on(deliveryEvent, List.of(delivery));

        assertThat(delivery.isArrived()).isTrue();
    }

    private DeliveryEvent createDeliveryEventWithId(long id) {
        return new DeliveryEvent(id, LocalDateTime.of(2022, 4, 7, 18, 28), 58.377047f, 26.727889f);
    }

    private Delivery createDeliveryWithId(long id) {
        return new Delivery(id, "any@example.com", 58.377047f, 26.727889f,
                LocalDateTime.of(2022, 4, 7, 18, 28), false, false);
    }

    @Test
    void marks_delivery_on_time_when_took_less_than_10_minutes() {
        var delivery = deliveryOrderedAt(10, 40);
        var deliveryEvent = deliveryEventAt(10, 49);

        deliveryService.on(deliveryEvent, List.of(delivery));

        assertThat(delivery.isOnTime()).isTrue();
    }

    private DeliveryEvent deliveryEventAt(int hour, int minute) {
        return new DeliveryEvent(123L, LocalDateTime.of(2022, 4, 7, hour, minute), 58.377047f, 26.727889f);
    }

    private Delivery deliveryOrderedAt(int hour, int minute) {
        return new Delivery(123L, "any@example.com", 58.377047f, 26.727889f,
                LocalDateTime.of(2022, 4, 7, hour, minute), false, false);
    }

    @Test
    void marks_delivery_not_on_time_when_took_more_or_equal_than_10_minutes() {
        var delivery = deliveryOrderedAt(19, 30);
        var deliveryEvent = deliveryEventAt(19, 40);

        deliveryService.on(deliveryEvent, List.of(delivery));

        assertThat(delivery.isOnTime()).isFalse();
    }

    private static class NoOpEmailGateway extends SendgridEmailGateway {
        @Override
        public void send(String to, String subject, String message) {

        }
    }
}
