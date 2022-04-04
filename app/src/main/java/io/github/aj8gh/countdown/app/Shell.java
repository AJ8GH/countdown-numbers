package io.github.aj8gh.countdown.app;

import java.util.Scanner;

public class Shell {
    private static final Scanner SCANNER = new Scanner(System.in);
    String line;

    public String getInput() {
        System.out.println("Let's go ...");
        this.line = SCANNER.nextLine();
        return this.line;
    }
}
