package io.github.aj8gh.countdown.app.cli;

import io.github.aj8gh.countdown.generator.Generator;
import io.github.aj8gh.countdown.solver.Solver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

import static io.github.aj8gh.countdown.app.cli.Commands.ADD_FILTER;
import static io.github.aj8gh.countdown.app.cli.Commands.EXIT;
import static io.github.aj8gh.countdown.app.cli.Commands.GENERATE;
import static io.github.aj8gh.countdown.app.cli.Commands.GENERATE_TO_SOLVE;
import static io.github.aj8gh.countdown.app.cli.Commands.GET_GENERATOR_MODE;
import static io.github.aj8gh.countdown.app.cli.Commands.GET_MODE_SWITCH_THRESHOLD;
import static io.github.aj8gh.countdown.app.cli.Commands.GET_SOLVE_MODE;
import static io.github.aj8gh.countdown.app.cli.Commands.RESET_FILTERS;
import static io.github.aj8gh.countdown.app.cli.Commands.SET_GEN_MODE;
import static io.github.aj8gh.countdown.app.cli.Commands.SET_MODE_SWITCH_THRESHOLD;
import static io.github.aj8gh.countdown.app.cli.Commands.SET_SOLVE_MODE;
import static io.github.aj8gh.countdown.app.cli.Commands.SET_TIME_SCALE;
import static io.github.aj8gh.countdown.app.cli.Commands.SOLVE;
import static io.github.aj8gh.countdown.util.calculator.Calculator.CalculationMode;
import static java.util.stream.Collectors.toList;

public class CountdownApp {
    private static final Logger LOG = LoggerFactory.getLogger(CountdownApp.class);
    private static final FilterSelector FILTER_SELECTOR = new FilterSelector();
    private static final Shell SHELL = new Shell();
    private static final String ARG_DELIMITER = " ";
    private static final int MIN_LARGE = 0;
    private static final int MAX_LARGE = 4;
    private static final int WARM_UPS = 20;

    private final Generator generator;
    private final Solver solver;

    private String input;
    private String command;
    private List<String> args;

    public CountdownApp(Generator generator, Solver solver) {
        this.generator = generator;
        this.solver = solver;
    }

    public void run() {
        while (input == null || !input.equalsIgnoreCase(EXIT)) {
            this.input = SHELL.getInput();
            processInput();
        }
    }

    private void processInput() {
        try {
            var argsArray = input.split(ARG_DELIMITER);
            this.command = argsArray[0];
            this.args = Arrays.asList(argsArray).subList(1, argsArray.length);
            handleCommand();
        } catch (Exception e) {
            handleError(e);
        }
    }

    private void handleCommand() {
        switch (command) {
            case ADD_FILTER -> addFilter();
            case EXIT -> exit();
            case RESET_FILTERS -> resetFilters();
            case SOLVE -> solve();
            case GENERATE -> generate();
            case GENERATE_TO_SOLVE -> generateToSolve();
            case GET_GENERATOR_MODE -> printAttribute(generator.getMode().toString());
            case GET_MODE_SWITCH_THRESHOLD -> printAttribute(solver.getModeSwitchThreshold());
            case GET_SOLVE_MODE -> printAttribute(solver.getMode().toString());
            case SET_SOLVE_MODE -> setSolveMode();
            case SET_GEN_MODE -> setGenMode();
            case SET_MODE_SWITCH_THRESHOLD -> setModeSwitchThreshold();
            case SET_TIME_SCALE -> setTimeScale();
            default -> handleUnknown();
        }
    }

    private void setTimeScale() {
        var timeScale = Integer.parseInt(args.get(0));
        solver.setTimeScale(timeScale);
        generator.setTimeScale(timeScale);
    }

    private void printAttribute(String attribute) {
        SHELL.print(attribute);
    }

    private void printAttribute(long attribute) {
        SHELL.print(NumberFormat.getInstance().format(attribute));
    }

    private void setModeSwitchThreshold() {
        solver.setModeSwitchThreshold(Integer.parseInt(args.get(0)));
    }

    private void addFilter() {
        FILTER_SELECTOR.addFilter(args.get(0), generator);
    }

    private void resetFilters() {
        generator.resetFilters();
    }

    private void setGenMode() {
        generator.setMode(CalculationMode.valueOf(args.get(0).toUpperCase()));
    }

    private void setSolveMode() {
        solver.setMode(CalculationMode.valueOf(args.get(0).toUpperCase()));
    }

    private void generate() {
        args.stream().map(Integer::parseInt)
                .forEach(number -> {
                    validateGeneratorInput(number);
                    generator.warmUp(WARM_UPS);
                    generator.generate(number);
                    SHELL.logGenerator(generator);
                    generator.reset();
                });
    }

    private void solve() {
        validateSolverInput();
        warmUpSolver();
        solver.solve(args.stream().map(Integer::parseInt).collect(toList()));
        SHELL.logSolver(solver);
        solver.reset();
    }

    private void generateToSolve() {
        args.stream().map(Integer::parseInt).forEach(number -> {
            validateGeneratorInput(number);
            warmUpSolver();
            generator.generate(number);
            solver.solve(generator.getQuestionNumbers());
            SHELL.logGenerator(generator);
            SHELL.logSolver(solver);
            reset();
        });
    }

    private void handleUnknown() {
        LOG.warn("Unknown command {}", command);
    }

    private void handleError(Exception e) {
        LOG.error(e.getMessage());
    }

    private void validateGeneratorInput(int number) {
        if (number < MIN_LARGE || number > MAX_LARGE) {
            throw new IllegalArgumentException(number + " Not in range");
        }
    }

    private void validateSolverInput() {
        if (args.size() != 7) {
            throw new IllegalArgumentException("Solver needs 7 ints");
        }
    }

    private void exit() {
        SHELL.logExitMessage();
        System.exit(0);
    }

    private void reset() {
        generator.reset();
        solver.reset();
    }

    private void warmUpSolver() {
        for (int i = 0; i < WARM_UPS; i++) {
            generator.generate(i % 5);
            solver.solve(generator.getQuestionNumbers());
            reset();
        }
    }
}
