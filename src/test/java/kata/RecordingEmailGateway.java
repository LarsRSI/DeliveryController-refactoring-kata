package kata;

import java.util.ArrayList;
import java.util.List;

public class RecordingEmailGateway extends SendgridEmailGateway {

    private List<Arguments> invocations = new ArrayList<>();

    @Override
    public void send(String to, String subject, String message) {
        invocations.add(new Arguments(to, subject, message));
    }

    public String invocations() {
        return invocations.toString();
    }

    record Arguments(String to, String subject, String message) {
    }

}
