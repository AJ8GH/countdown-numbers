package io.github.aj8gh.countdown.util.serialisation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Deserializer {
    private static final Logger LOG = LoggerFactory.getLogger(Deserializer.class);
    private static final String IO_DIRECTORY = "./input-output/";
    private static final String SOL_OUT_HANDLE = IO_DIRECTORY + "sol.in";
    private static final String GEN_OUT_HANDLE = IO_DIRECTORY + "gen.in";

    private static final String COMMA = ",";
    private static final String COLON = ":";

    public int forGenerator(String file) {
        file = file == null ? GEN_OUT_HANDLE : file;
        try (var reader = new Scanner(new FileReader(file))) {
            return reader.nextInt();
        } catch (IOException e) {
            LOG.error("Error reading generator input file {}, {}", file, e.getMessage());
            return -1;
        }
    }

    public List<Integer> forSolver(String file) {
        file = file == null ? SOL_OUT_HANDLE : file;
        try (var reader = new Scanner(new FileReader(file))) {
            var line = reader.nextLine();
            var questionAndTarget = line.split(COLON);
            var question = new ArrayList<>(Arrays.stream(questionAndTarget[0].split(COMMA))
                    .map(Integer::valueOf)
                    .toList());
            question.add(Integer.valueOf(questionAndTarget[1]));
            return question;
        } catch (IOException e) {
            LOG.error("Error reading solver input file {}, {}", file, e.getMessage());
            return Collections.emptyList();
        }
    }
}
