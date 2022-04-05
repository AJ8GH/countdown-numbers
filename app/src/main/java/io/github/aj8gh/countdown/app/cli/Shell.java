package io.github.aj8gh.countdown.app.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Shell {
    private static final String EXIT_MESSAGE = "\n*** Countdown App Shutting Down ***";
    private static final String PROMPT = "\n>> Ready for input...\n>> ";

    private static final Logger LOG = LoggerFactory.getLogger(Shell.class);
    private static final Scanner SCANNER = new Scanner(System.in);

    public String getInput() {
        System.out.print(PROMPT);
        return SCANNER.nextLine();
    }

    void logExitMessage() {
        LOG.info(EXIT_MESSAGE);
    }
}
