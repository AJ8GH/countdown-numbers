package io.github.aj8gh.countdown.cli;

import io.github.aj8gh.countdown.BaseApp;
import io.github.aj8gh.countdown.gen.GenAdaptor;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.sol.SolAdaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.github.aj8gh.countdown.calc.Calculator.CalculationMode;
import static io.github.aj8gh.countdown.cli.Commands.*;

public class CliApp implements Consumer<String[]> {
    private static final Logger LOG = LoggerFactory.getLogger(CliApp.class);
    private static final String ARG_DELIMITER = " ";
    private static final int MAX_LARGE = 4;
    private static final int MIN_LARGE = 0;
    private static final FilterSelector FILTER_SELECTOR = new FilterSelector();

    private final Printer printer = new Printer();
    private final OutputHandler outputHandler;
    private final Supplier<String> inputSupplier;
    private final GenAdaptor generator;
    private final SolAdaptor solver;

    private String input;
    private String command;
    private List<String> args;

    public CliApp(BaseApp baseApp, Supplier<String> inputSupplier) {
        this.outputHandler = baseApp.outputHandler();
        this.inputSupplier = inputSupplier;
        this.generator = baseApp.genAdaptor();
        this.solver = baseApp.solAdaptor();
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
            case GET_GENERATOR_MODE -> print(generator.getGenerator().getMode());
            case GET_MODE_SWITCH_THRESHOLD -> print(solver.getSolver().getModeSwitchThreshold());
            case GET_SOLVE_MODE -> print(solver.getSolver().getMode());
            case SET_SOLVE_MODE -> setSolveMode();
            case SET_GEN_MODE -> setGenMode();
            case SET_MODE_SWITCH_THRESHOLD -> setModeSwitchThreshold();
            default -> handleUnknown();
        }
    }

    private void print(Object attribute) {
        printer.info(attribute);
    }

    private void setModeSwitchThreshold() {
        solver.getSolver().setModeSwitchThreshold(Integer.parseInt(args.get(0)));
    }

    private void addFilter() {
        FILTER_SELECTOR.addFilter(args.get(0), generator.getGenerator());
    }

    private void resetFilters() {
        generator.getGenerator().resetFilters();
    }

    private void setGenMode() {
        generator.getGenerator().setMode(CalculationMode.fromString(args.get(0).toUpperCase()));
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
                    generator.generate(number);
                    outputHandler.handleGenerator(generator.getResult());
                });
    }

    private void solve() {
        validateSolverInput();
        solver.solve(new ArrayList<>(args.stream().map(Integer::parseInt).toList()));
        outputHandler.handleSolver(solver.getResult());
    }

    private void generateToSolve() {
        args.stream().map(Integer::parseInt).forEach(number -> {
            validateGeneratorInput(number);
            generator.generate(number);
            solver.solve(generator.getResult().getQuestionNumbers());
            outputHandler.handleGenerator(generator.getResult());
            outputHandler.handleSolver(solver.getResult());
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
}
