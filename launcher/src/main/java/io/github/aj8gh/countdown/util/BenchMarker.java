package io.github.aj8gh.countdown.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class BenchMarker {
    public static void main(String[] args) {
        try {
            org.openjdk.jmh.Main.main(args);
        } catch (IOException e) {
            log.error("IOException whilst benchmarking: {}", e.getMessage());
        }
    }
}
