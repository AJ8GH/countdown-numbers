package io.github.aj8gh.countdown;

import io.github.aj8gh.countdown.conf.AppConfig;

import java.util.function.Consumer;

public class Main {
    private static final AppConfig CONFIG = new AppConfig();
    private static final Consumer<String[]> APP = CONFIG.app();

    public static void main(String... args) {
        APP.accept(args);
    }
}
