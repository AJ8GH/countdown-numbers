package io.github.aj8gh.countdown.app.cli;

import java.util.Scanner;

public class Shell {
    private static final Scanner SCANNER = new Scanner(System.in);
    String line;

    public String getInput() {
        System.out.println("Waiting for input...");
        this.line = SCANNER.nextLine();
        return this.line;
    }
}
