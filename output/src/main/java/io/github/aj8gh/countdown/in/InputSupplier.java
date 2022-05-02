package io.github.aj8gh.countdown.in;

import java.util.Scanner;
import java.util.function.Supplier;

public class InputSupplier implements Supplier<String> {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String PROMPT = ">> Ready for input...\n>> ";

    @Override
    public String get() {
        System.out.print(PROMPT);
        return SCANNER.nextLine();
    }
}
