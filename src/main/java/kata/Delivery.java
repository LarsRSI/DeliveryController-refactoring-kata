package kata;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public final class Delivery {

  private Long id;
  private String contactEmail;
  private float latitude;
  private float longitude;
  private LocalDateTime timeOfDelivery;
  private boolean arrived;
  private boolean onTime;

  public Delivery(Long id, String contactEmail, float latitude, float longitude,
      LocalDateTime timeOfDelivery, boolean arrived, boolean onTime) {
    this.id = id;
    this.contactEmail = contactEmail;
    this.latitude = latitude;
    this.longitude = longitude;
    this.timeOfDelivery = timeOfDelivery;
    this.arrived = arrived;
    this.onTime = onTime;
  }

  public long getId() {
    return id;
  }

  public String getContactEmail() {
    return contactEmail;
  }

  public LocalDateTime getTimeOfDelivery() {
    return timeOfDelivery;
  }

  public boolean isArrived() {
    return arrived;
  }

  public boolean isOnTime() {
    return onTime;
  }

  public Location getLocation() {
    return new Location(latitude, longitude);
  }

  void markArrived(DeliveryEvent deliveryEvent) {
    this.arrived = true;
    Duration duration = Duration.between(getTimeOfDelivery(), deliveryEvent.timeOfDelivery());

    if (duration.toMinutes() < 10) {
      this.onTime = true;
    }
    this.timeOfDelivery = deliveryEvent.timeOfDelivery();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Delivery delivery = (Delivery) o;
    return Float.compare(delivery.latitude, latitude) == 0
        && Float.compare(delivery.longitude, longitude) == 0 && arrived == delivery.arrived
        && onTime == delivery.onTime && id.equals(delivery.id) && contactEmail.equals(
        delivery.contactEmail) && timeOfDelivery.equals(delivery.timeOfDelivery);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, contactEmail, latitude, longitude, timeOfDelivery, arrived, onTime);
  }
}
