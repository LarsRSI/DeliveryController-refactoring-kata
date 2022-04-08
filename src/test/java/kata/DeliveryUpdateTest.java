package kata;

import kata.testdoubles.NoOpEmailGateway;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static kata.TestFactory.createDeliveryEventWithId;
import static kata.TestFactory.createDeliveryOrderedAt;
import static kata.TestFactory.createDeliveryWithId;
import static kata.TestFactory.deliveryAnyEventAt;
import static kata.TestFactory.deliveryEventAt;
import static kata.TestFactory.localDateTime;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
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
    void returns_list_with_updated_delivery() {
        var delivery = createDeliveryWithId(123L);
        var deliveryEvent = createDeliveryEventWithId(123L);
        List<Delivery> deliverySchedule = List.of(delivery);

        List<Delivery> deliveries = deliveryService.on(deliveryEvent, deliverySchedule);

        assertThat(deliveries).isEqualTo(deliverySchedule);
    }

    @Test
    void marks_delivery_on_time_when_took_less_than_10_minutes() {
        var delivery = createDeliveryOrderedAt(2L, 10, 40);
        var deliveryEvent = deliveryEventAt(10, 49);

        deliveryService.on(deliveryEvent, List.of(delivery));

        assertThat(delivery.isOnTime()).isTrue();
    }

    @Test
    void marks_delivery_not_on_time_when_took_more_or_equal_than_10_minutes() {
        var delivery = createDeliveryOrderedAt(2L, 19, 30);
        var deliveryEvent = deliveryEventAt(19, 40);

        deliveryService.on(deliveryEvent, List.of(delivery));

        assertThat(delivery.isOnTime()).isFalse();
    }

    @Test
    void sets_delivery_time_on_delivery() {
        var delivery = createDeliveryOrderedAt(2L, 19, 30);
        var deliveryEvent = deliveryEventAt(19, 40);

        deliveryService.on(deliveryEvent, List.of(delivery));

        assertThat(delivery.getTimeOfDelivery()).isEqualTo(localDateTime(19, 40));
    }

    @Test
    void delivery_not_in_our_schedule_will_not_throw() {
        var delivery = createDeliveryOrderedAt(1L, 19, 30);
        var deliveryEvent = deliveryAnyEventAt();

        deliveryService.on(deliveryEvent, List.of(delivery));
    }

}
