package kata;

import java.time.Duration;

public class MapService {

    // in km/h
    private double averageSpeed = 50.0;

    private final int MINUTES_PER_HOUR = 60;
    private final int SECONDS_PER_HOUR = 3600;
    private final double R = 6373.0;

    public Duration calculateETA(Location location, Location otherLocation) {
        var distance = this.calculateDistance(
                new Location(location.getLatitude(), location.getLongitude()), new Location(otherLocation.getLatitude(), otherLocation.getLongitude()));
        Double v = distance / this.averageSpeed * MINUTES_PER_HOUR;
        return Duration.ofMinutes(v.longValue());
    }

    public void updateAverageSpeed(Duration elapsedTime, Location location, Location otherLocation) {
        var distance = this.calculateDistance(location, otherLocation);
        var updatedSpeed = distance / (elapsedTime.getSeconds() / (double) SECONDS_PER_HOUR);
        this.averageSpeed = updatedSpeed;
    }

    private double calculateDistance(Location location, Location otherLocation) {
        var d1 = location.getLatitude() * (Math.PI / 180.0);
        var num1 = location.getLongitude() * (Math.PI / 180.0);
        var d2 = otherLocation.getLatitude() * (Math.PI / 180.0);
        var num2 = otherLocation.getLongitude() * (Math.PI / 180.0) - num1;
        var d3 = Math.pow(Math.sin((d2 - d1) / 2.0), 2.0) + Math.cos(d1) * Math.cos(d2) * Math.pow(
                Math.sin(num2 / 2.0), 2.0);

        return R * (2.0 * Math.atan2(Math.sqrt(d3), Math.sqrt(1.0 - d3)));
    }
}
