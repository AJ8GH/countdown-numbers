package io.github.aj8gh.countdown.cli;

import java.util.Scanner;
import java.util.function.Supplier;

public class CliInputSupplier implements Supplier<String> {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String PROMPT = ">> Ready for input...\n>> ";

    public String get() {
        System.out.print(PROMPT);
        return SCANNER.nextLine();
    }
}
