package kata;

import java.time.LocalDateTime;

public class TestFactory {

    public static Delivery createDeliveryWithId(long id) {
        return new Delivery(id, "any@example.com", 58.377047f, 26.727889f,
                localDateTime(18, 28), false, false);
    }

    public static Delivery createDeliveryWithEmail(String contactEmail) {
        return new Delivery(789L, contactEmail, 58.377047f, 26.727889f, localDateTime(18, 28), false, false);
    }

    public static Delivery createDeliveryWithEmailForLocation(String contactEmail, float latitude, float longitude) {
        return new Delivery(999L, contactEmail, latitude, longitude, localDateTime(18, 28), false, false);
    }

    public static Delivery createDeliveryOrderedAt(long l, int hour, int minute) {
        return new Delivery(123L, "any@example.com", 58.377047f, 26.727889f,
                localDateTime(hour, minute), false, false);
    }

    public static Delivery createCompletedDeliveryAt(int hour, int minute) {
        return new Delivery(356L, "any@example.com", 58.377047f, 26.727889f,
                localDateTime(hour, minute), false, false);
    }

    public static DeliveryEvent createDeliveryEventWithId(long id) {
        return new DeliveryEvent(id, localDateTime(18, 28), 58.377047f, 26.727889f);
    }

    public static DeliveryEvent deliveryAnyEventAt() {
        return new DeliveryEvent(999L, localDateTime(23, 59), 58.377047f, 26.727889f);
    }

    public static DeliveryEvent deliveryEventAt(int hour, int minute) {
        return new DeliveryEvent(123L, localDateTime(hour, minute), 58.377047f, 26.727889f);
    }

    public static DeliveryEvent deliveryEventForDelivery(Delivery delivery, int hour, int minute) {
        return new DeliveryEvent(delivery.getId(), localDateTime(hour, minute), 58.377047f, 26.727889f);
    }

    public static LocalDateTime localDateTime(int hour, int minute) {
        return LocalDateTime.of(2022, 4, 7, hour, minute);
    }
}
