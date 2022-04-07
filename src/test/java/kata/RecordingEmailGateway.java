package kata;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecordingEmailGateway extends SendgridEmailGateway {

    private final List<Arguments> invocations = new ArrayList<>();

    @Override
    public void send(String to, String subject, String message) {
        invocations.add(new Arguments(to, subject, message));
    }

    public String invocations() {
        return invocations.stream().map(Record::toString).collect(Collectors.joining("\n"));
    }

    record Arguments(String to, String subject, String message) {
    }

}
