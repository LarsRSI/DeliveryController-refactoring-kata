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

    @Test
    void marks_delivery_as_arrived() {
      DeliveryService deliveryService = new DeliveryService(new NoOpEmailGateway());
      Delivery delivery = new Delivery(123L, "any@example.com", 58.377047f, 26.727889f,
              LocalDateTime.of(2022, 4, 7, 18, 28), false, false);

      deliveryService.on(new DeliveryEvent(123L, LocalDateTime.of(2022, 4, 7, 18, 28), 58.377047f, 26.727889f),
              List.of(delivery));

      assertThat(delivery.isArrived()).isTrue();
    }

    private static class NoOpEmailGateway extends SendgridEmailGateway {
        @Override
        public void send(String to, String subject, String message) {

        }
    }
}
