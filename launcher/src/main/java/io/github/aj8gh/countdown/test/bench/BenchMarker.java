package io.github.aj8gh.countdown.test.bench;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BenchMarker {
    private static final Logger LOG = LoggerFactory.getLogger(BenchMarker.class);

    public static void main(String[] args) {
        try {
            org.openjdk.jmh.Main.main(args);
        } catch (IOException e) {
            LOG.error("IOException whilst benchmarking: {}", e.getMessage());
        }
    }
}
