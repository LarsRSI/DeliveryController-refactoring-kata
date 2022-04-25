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

    public List<Delivery> on(DeliveryEvent deliveryEvent, List<Delivery> deliveryList) {
        var deliverySchedule = new DeliverySchedule(deliveryList);
        Optional<Delivery> currentDelivery = deliverySchedule.findCurrentDelivery(deliveryEvent);

        currentDelivery.ifPresent(delivery -> {
            delivery.markArrived(deliveryEvent);
            sendFeedbackEmail(delivery);

            if (!delivery.isOnTime()) {
                deliverySchedule.findPreviousDelivery(delivery).ifPresent(previousDelivery -> {
                    Duration elapsedTime = Duration.between(previousDelivery.getTimeOfDelivery(), delivery.getTimeOfDelivery());
                    mapService.updateAverageSpeed(elapsedTime, previousDelivery.getLocation(), delivery.getLocation());
                });
            }
        });

        deliverySchedule.findNextDelivery(deliveryEvent).ifPresent(delivery -> {
            var nextEta = mapService.calculateETA(deliveryEvent.getLocation(), delivery.getLocation());
            sendSoonArrivingEmail(delivery, nextEta);
        });

        return deliveryList;
    }

    private void sendSoonArrivingEmail(Delivery delivery, Duration nextEta) {
        var message =
                "Your delivery to %s is next, estimated time of arrival is in %s minutes. Be ready!".formatted(
                        delivery.getLocation(), nextEta.getSeconds() / 60);
        emailGateway.send(delivery.getContactEmail(), "Your delivery will arrive soon", message);
    }

    private void sendFeedbackEmail(Delivery delivery) {
        String message = "Regarding your delivery today at %s. How likely would you be to recommend this delivery service to a friend? Click <a href='url'>here</a>".formatted(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(delivery.getTimeOfDelivery()));
        emailGateway.send(delivery.getContactEmail(), "Your feedback is important to us", message);
    }
}
