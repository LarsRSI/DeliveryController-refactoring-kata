package kata;

import java.time.LocalDateTime;

public class TestFactory {
    public static DeliveryEvent createDeliveryEventWithId(long id) {
        return new DeliveryEvent(id, localDateTime(18, 28), 58.377047f, 26.727889f);
    }

    public static Delivery createDeliveryWithId(long id) {
        return new Delivery(id, "any@example.com", 58.377047f, 26.727889f,
                localDateTime(18, 28), false, false);
    }

    public static DeliveryEvent deliveryEventAt(int hour, int minute) {
        return new DeliveryEvent(123L, localDateTime(hour, minute), 58.377047f, 26.727889f);
    }

    public static Delivery deliveryOrderedAt(int hour, int minute) {
        return new Delivery(123L, "any@example.com", 58.377047f, 26.727889f,
                localDateTime(hour, minute), false, false);
    }

    public static LocalDateTime localDateTime(int hour, int minute) {
        return LocalDateTime.of(2022, 4, 7, hour, minute);
    }
}
