package kata;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DeliveryServiceTest {

  Clock clock = Clock.fixed(Instant.parse("2019-01-01T10:15:30.00Z"), ZoneOffset.UTC);

  @Test
  void testDelivery() {
    var deliveryService = new DeliveryService(new SendgridEmailGateway() {
      @Override
      public void send(String to, String subject, String message) {

      }
    }, new MapService());

    var delivery = new Delivery(123L, "test@example.com", 58.377982f, 26.729038f,
            LocalDateTime.now(clock), false, false);
    LocalDateTime timeOfDelivery = LocalDateTime.now(clock).plusMinutes(5);
    deliveryService.on(new DeliveryEvent(123L, timeOfDelivery, 58.377983f, 26.729038f),
            List.of(delivery));

    Assertions.assertAll(
            () -> assertThat(delivery.isArrived()).isTrue(),
            () -> assertThat(delivery.getTimeOfDelivery()).isEqualTo(timeOfDelivery));
  }

  @Test
  void testDeliveryEmail() {
    var emailGateway = mock(SendgridEmailGateway.class);
    var deliveryService = new DeliveryService(emailGateway, new MapService());

    var delivery = new Delivery(123L, "test@example.com", 58.377982f, 26.729038f,
            LocalDateTime.now(clock), false, false);
    deliveryService.on(
            new DeliveryEvent(123L, LocalDateTime.now(clock).plusMinutes(15), 58.377983f, 26.729038f),
            List.of(delivery));

    verify(emailGateway).send("test@example.com", "Your feedback is important to us", "Regarding your delivery today at 2019-01-01 10:30. How likely would you be to recommend this delivery service to a friend? Click <a href='url'>here</a>");
  }
}
