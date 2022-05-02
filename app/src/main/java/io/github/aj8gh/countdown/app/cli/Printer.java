package io.github.aj8gh.countdown.app.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Printer {
    private static final Logger LOG = LoggerFactory.getLogger(Printer.class);
    private static final String EXIT_MESSAGE = "\n*** Countdown App Shutting Down ***";

    public void info(Object message) {
        LOG.info("{}", message);
    }

    public void logExitMessage() {
        LOG.info(EXIT_MESSAGE);
    }
}
