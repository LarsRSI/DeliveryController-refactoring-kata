package kata;

import jakarta.inject.Singleton;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Singleton
public class DeliveryService {

    private final SendgridEmailGateway emailGateway;
    private final MapService mapService;

    public DeliveryService(SendgridEmailGateway emailGateway) {
        this(emailGateway, new MapService());
    }

    public DeliveryService(SendgridEmailGateway emailGateway, MapService mapService) {
        this.emailGateway = emailGateway;
        this.mapService = mapService;
    }

    public List<Delivery> on(DeliveryEvent deliveryEvent, List<Delivery> deliverySchedule) {

        for (int i = 0; i < deliverySchedule.size(); i++) {
            Delivery delivery = deliverySchedule.get(i);
            if (deliveryEvent.id() == delivery.getId()) {
                updateDelivery(deliveryEvent, delivery);
                sendFeedbackEmail(delivery);
                maybeUpdateAverageSpeed(deliverySchedule, i, delivery);
            }
        }

        findNextDelivery(deliveryEvent, deliverySchedule).ifPresent(delivery -> {
            var nextEta = mapService.calculateETA(
                    deliveryEvent.latitude(), deliveryEvent.longitude(),
                    delivery.getLatitude(), delivery.getLongitude());
            sendSoonArrivingEmail(delivery, nextEta);
        });

        return deliverySchedule;
    }

    private Optional<Delivery> findNextDelivery(DeliveryEvent deliveryEvent, List<Delivery> deliverySchedule) {
        Optional<Delivery> nextDelivery = Optional.empty();
        for (int i = 0; i < deliverySchedule.size(); i++) {
            Delivery delivery = deliverySchedule.get(i);
            if (deliveryEvent.id() == delivery.getId()) {
                nextDelivery = getNextDelivery(deliverySchedule, i);
            }
        }
        return nextDelivery;
    }

    private Optional<Delivery> getNextDelivery(List<Delivery> deliverySchedule, int index) {
        if (deliverySchedule.size() > index + 1) {
            return Optional.of(deliverySchedule.get(index + 1));
        }
        return Optional.empty();
    }

    private void maybeUpdateAverageSpeed(List<Delivery> deliverySchedule, int index, Delivery delivery) {
        if (!delivery.isOnTime() && deliverySchedule.size() > 1 && index > 0) {
            var previousDelivery = deliverySchedule.get(index - 1);
            Duration elapsedTime = Duration.between(previousDelivery.getTimeOfDelivery(), delivery.getTimeOfDelivery());
            mapService.updateAverageSpeed(
                    elapsedTime,
                    previousDelivery.getLatitude(), previousDelivery.getLongitude(),
                    delivery.getLatitude(), delivery.getLongitude());
        }
    }

    private void updateDelivery(DeliveryEvent deliveryEvent, Delivery delivery) {
        delivery.setArrived(true);
        Duration duration = Duration.between(delivery.getTimeOfDelivery(), deliveryEvent.timeOfDelivery());

        if (duration.toMinutes() < 10) {
            delivery.setOnTime(true);
        }
        delivery.setTimeOfDelivery(deliveryEvent.timeOfDelivery());
    }

    private void sendSoonArrivingEmail(Delivery delivery, Duration nextEta) {
        var message =
                "Your delivery to [%s,%s] is next, estimated time of arrival is in %s minutes. Be ready!".formatted(
                        delivery.getLatitude(), delivery.getLongitude(), nextEta.getSeconds() / 60);
        emailGateway.send(delivery.getContactEmail(), "Your delivery will arrive soon", message);
    }

    private void sendFeedbackEmail(Delivery delivery) {
        String message = "Regarding your delivery today at %s. How likely would you be to recommend this delivery service to a friend? Click <a href='url'>here</a>".formatted(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(delivery.getTimeOfDelivery()));
        emailGateway.send(delivery.getContactEmail(), "Your feedback is important to us", message);
    }
}
