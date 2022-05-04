package io.github.aj8gh.countdown.cli;

import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.sol.Solver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.github.aj8gh.countdown.cli.Commands.ADD_FILTER;
import static io.github.aj8gh.countdown.cli.Commands.EXIT;
import static io.github.aj8gh.countdown.cli.Commands.GENERATE;
import static io.github.aj8gh.countdown.cli.Commands.GENERATE_TO_SOLVE;
import static io.github.aj8gh.countdown.cli.Commands.GET_GENERATOR_MODE;
import static io.github.aj8gh.countdown.cli.Commands.GET_MODE_SWITCH_THRESHOLD;
import static io.github.aj8gh.countdown.cli.Commands.GET_SOLVE_MODE;
import static io.github.aj8gh.countdown.cli.Commands.RESET_FILTERS;
import static io.github.aj8gh.countdown.cli.Commands.SET_GEN_MODE;
import static io.github.aj8gh.countdown.cli.Commands.SET_MODE_SWITCH_THRESHOLD;
import static io.github.aj8gh.countdown.cli.Commands.SET_SOLVE_MODE;
import static io.github.aj8gh.countdown.cli.Commands.SET_TIME_SCALE;
import static io.github.aj8gh.countdown.cli.Commands.SOLVE;
import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode;

public class CountdownApp implements Consumer<String[]> {
    private static final Logger LOG = LoggerFactory.getLogger(CountdownApp.class);
    private static final String ARG_DELIMITER = " ";
    private static final int MAX_LARGE = 4;
    private static final int MIN_LARGE = 0;
    private static final FilterSelector FILTER_SELECTOR = new FilterSelector();

    private final Printer printer = new Printer();
    private final OutputHandler outputHandler;
    private final Supplier<String> inputSupplier;
    private final Generator generator;
    private final Solver solver;

    private String input;
    private String command;
    private List<String> args;

    public CountdownApp(OutputHandler outputHandler,
                        Supplier<String> inputSupplier,
                        Generator generator,
                        Solver solver) {
        this.outputHandler = outputHandler;
        this.inputSupplier = inputSupplier;
        this.generator = generator;
        this.solver = solver;
    }

    @Override
    public void accept(String... args) {
        while (input == null || !input.equalsIgnoreCase(EXIT)) {
            this.input = inputSupplier.get();
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
            case GET_GENERATOR_MODE -> print(generator.getMode());
            case GET_MODE_SWITCH_THRESHOLD -> print(solver.getModeSwitchThreshold());
            case GET_SOLVE_MODE -> print(solver.getMode());
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

    private void print(Object attribute) {
        printer.info(attribute);
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
        generator.setMode(CalculationMode.fromString(args.get(0).toUpperCase()));
    }

    private void setSolveMode() {
        var mode = CalculationMode.fromString(args.get(0).toUpperCase());
        if (mode == null) {
            LOG.warn("Invalid mode {}", args.get(0));
        } else {
            solver.setMode(mode);
            LOG.info("Solve mode: {}", mode);
        }
    }

    private void generate() {
        args.stream().map(Integer::parseInt)
                .forEach(number -> {
                    validateGeneratorInput(number);
                    generator.warmUp();
                    generator.generate(number);
                    outputHandler.handleGenerator(generator);
                    generator.reset();
                });
    }

    private void solve() {
        validateSolverInput();
        solver.warmUp();
        solver.solve(new ArrayList<>(args.stream().map(Integer::parseInt).toList()));
        outputHandler.handleSolver(solver);
        solver.reset();
    }

    private void generateToSolve() {
        args.stream().map(Integer::parseInt).forEach(number -> {
            validateGeneratorInput(number);
            solver.warmUp();
            generator.generate(number);
            solver.solve(generator.getQuestionNumbers());
            outputHandler.handleGenerator(generator);
            outputHandler.handleSolver(solver);
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
        printer.logExitMessage();
        System.exit(0);
    }

    private void reset() {
        generator.reset();
        solver.reset();
    }
}
