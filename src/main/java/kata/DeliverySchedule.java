package kata;

import java.util.List;
import java.util.Optional;

public final class DeliverySchedule {
    private final List<Delivery> deliverySchedule;

    public DeliverySchedule(List<Delivery> deliverySchedule) {
        this.deliverySchedule = deliverySchedule;
    }

    public Optional<Delivery> findCurrentDelivery(DeliveryEvent deliveryEvent) {
        for (Delivery delivery : deliverySchedule) {
            if (deliveryEvent.id() == delivery.getId()) {
                return Optional.of(delivery);
            }
        }
        return Optional.empty();
    }

    public Optional<Delivery> findNextDelivery(DeliveryEvent deliveryEvent) {
        Optional<Delivery> nextDelivery = Optional.empty();
        for (int i = 0; i < deliverySchedule.size(); i++) {
            Delivery delivery = deliverySchedule.get(i);
            if (deliveryEvent.id() == delivery.getId()) {
                Optional<Delivery> result;
                List<Delivery> deliverySchedule = this.deliverySchedule;
                if (deliverySchedule.size() > i + 1) {
                    result = Optional.of(deliverySchedule.get(i + 1));
                } else {
                    result = Optional.empty();
                }
                nextDelivery = result;
            }
        }
        return nextDelivery;
    }

    public Optional<Delivery> findPreviousDelivery(Delivery current) {
        for (int i = 0; i < deliverySchedule.size(); i++) {
            if (deliverySchedule.size() > 1 && i > 0) {
                if (current.getId() == deliverySchedule.get(i).getId()) {
                    return Optional.of(deliverySchedule.get(i - 1));
                }
            }
        }
        return Optional.empty();
    }
}
