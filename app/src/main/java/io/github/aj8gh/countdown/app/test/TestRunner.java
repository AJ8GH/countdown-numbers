package io.github.aj8gh.countdown.app.test;

import io.github.aj8gh.countdown.app.conf.AppConfig;

import java.util.List;

public class TestRunner {
    public static void main(String[] args) {
        AppConfig.solver().warmUp();
        AppConfig.solver().solve(List.of(5,6,25,50,75,100,426));
        System.out.println(AppConfig.solver().getTime() + "ms" + " " + " " + AppConfig.solver().getSolution());
    }
}
