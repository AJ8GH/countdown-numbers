package io.github.aj8gh.countdown.ser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class Serializer {
    private static final Logger LOG = LoggerFactory.getLogger(Serializer.class);
    private static final String COLON = ":";

    public void serializeGenerator(String rpn, int target, long time, String file) {
        var data = rpn + COLON + target + COLON + time + "\n";
        serialize(file, data);
    }

    public void serializeSolver(String rpn, long time, String file) {
        var data = rpn + COLON + time + "\n";
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
