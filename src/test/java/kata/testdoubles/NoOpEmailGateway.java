package kata.testdoubles;

import kata.SendgridEmailGateway;

public class NoOpEmailGateway extends SendgridEmailGateway {
    @Override
    public void send(String to, String subject, String message) {

    }
}
