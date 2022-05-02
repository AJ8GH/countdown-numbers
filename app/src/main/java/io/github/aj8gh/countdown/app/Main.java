package io.github.aj8gh.countdown.app;

import io.github.aj8gh.countdown.app.conf.AppConfig;

import java.util.function.Consumer;

public class Main {
    private static final Consumer<String[]> APP = AppConfig.app();

    public static void main(String... args) {
        APP.accept(args);
    }
}
