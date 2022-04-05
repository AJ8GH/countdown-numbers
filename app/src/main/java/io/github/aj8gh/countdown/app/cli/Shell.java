package io.github.aj8gh.countdown.app.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Shell {
    private static final Logger LOG = LoggerFactory.getLogger(Shell.class);
    private static final Scanner SCANNER = new Scanner(System.in);

    public String getInput() {
        LOG.info("Ready for input...");
        return SCANNER.nextLine();
    }
}
