package io.github.aj8gh.countdown.out.file;

import io.github.aj8gh.countdown.gen.GenResult;
import io.github.aj8gh.countdown.sol.SolResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

// TODO: Should program take output file argument as well?
public class Serializer {
    private static final Logger LOG = LoggerFactory.getLogger(Serializer.class);
    private static final String COLON = ":";

    public void serializeGenerator(GenResult genResult, String file) {
        var data = genResult.getRpn() + COLON + genResult.getTarget() + COLON + genResult.getTime();
        serialize(file, data);

    }

    public void serializeSolver(SolResult solResult, String file) {
        var data = solResult.getRpn() + COLON + solResult.getTime();

        serialize(file, data);
    }

    public void createGeneratorInput(String file, String numLarge) {
        serialize(file, numLarge);
    }

    private void serialize(String file, String data) {
        try (var writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file, true)))) {
            writer.write(data + "\n");
            writer.flush();
        } catch (IOException e) {
            LOG.error("Error writing {} to file {}, {}", data, file, e.getMessage());
        }
    }
}
