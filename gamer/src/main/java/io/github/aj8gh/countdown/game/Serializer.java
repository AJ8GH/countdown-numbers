package io.github.aj8gh.countdown.game;

import io.github.aj8gh.countdown.calc.Calculation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Serializer {
    private static final Logger LOG = LoggerFactory.getLogger(Serializer.class);
    private static final String COLON = ":";

    public void serializeGenerator(Calculation genResult, long time, String file) {
        var data = genResult.getRpn() + COLON + genResult.getValue() + COLON + time + "\n";
        serialize(file, data);
    }

    public void serializeSolver(Calculation solResult, long time, String file) {
        var data = solResult.getRpn() + COLON + time + "\n";
        serialize(file, data);
    }

    private void serialize(String file, String data) {
        try (var writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, true)))) {
            writer.write(data);
            writer.flush();
        } catch (IOException e) {
            LOG.error("Error writing {} to file {}, {}", data, file, e.getMessage());
        }
    }
}
