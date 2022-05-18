package io.github.aj8gh.countdown.in.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

// TODO: implement tailing
public class Deserializer {
    private static final Logger LOG = LoggerFactory.getLogger(Deserializer.class);
    private static final String COMMA = ",";
    private static final String COLON = ":";

    public List<Integer> forGenerator(String file) {
        LOG.info("*** Reading from {} ***", file);
        try (var reader = new Scanner(new FileReader(file))) {
            List<Integer> inputs = new ArrayList<>();
            while (reader.hasNextInt()) {
                inputs.add(reader.nextInt());
            }
            return inputs;
        } catch (IOException e) {
            LOG.error("Error reading generator input file {}, {}", file, e.getMessage());
            return Collections.emptyList();
        }
    }

    // TODO - take all files as a complete file path as program arg
    // Chernyang file location: /Users/leec2/
    public List<List<Integer>> forSolver(String file) {
        LOG.info("*** Reading from {} ***", file);
        try (var reader = new Scanner(new FileReader(file))) {
            List<List<Integer>> inputs = new ArrayList<>();
            while (reader.hasNextLine()) {
                var line = reader.nextLine();
                // start timer here
                var questionAndTarget = line.split(COLON);
                var question = new ArrayList<>(Arrays
                        .stream(questionAndTarget[0].split(COMMA))
                        .map(Integer::valueOf)
                        .toList());
                question.add(Integer.valueOf(questionAndTarget[1]));
                inputs.add(question);
            }
            return inputs;
        } catch (IOException e) {
            LOG.error("Error reading solver input file {}, {}", file, e.getMessage());
            return Collections.emptyList();
        }
    }
}
