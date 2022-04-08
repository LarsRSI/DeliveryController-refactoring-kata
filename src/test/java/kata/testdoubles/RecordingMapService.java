package kata.testdoubles;

import kata.Location;
import kata.MapService;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecordingMapService extends MapService {

    private final List<Arguments> invocations = new ArrayList<>();

    @Override
    public void updateAverageSpeed(Duration elapsedTime, Location location, Location location1) {
        invocations.add(new Arguments(elapsedTime, location.latitude(), location.longitude(), location1.getLatitude(), location1.getLongitude()));
    }

    public String invocations() {
        return invocations.stream().map(Record::toString).collect(Collectors.joining("\n"));
    }

    record Arguments(Duration elapsedTime, float latitude, float longitude, float otherLatitude, float otherLongitude) {
    }

}
