package io.github.aj8gh.countdown.out.slack;

import io.github.aj8gh.countdown.gen.Generator;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.sol.Solver;

import static io.github.aj8gh.countdown.out.OutputHandler.OutputType.SLACK;

public class SlackHandler implements OutputHandler {
    private static final OutputType TYPE = SLACK;
    private final SlackClient slackClient;
    private String channel;

    public SlackHandler(SlackClient slackClient) {
        this.slackClient = slackClient;
    }

    @Override
    public void handleSolver(Solver solver) {
        slackClient.postMessage(channel, getSolverMessage(solver));
    }

    @Override
    public void handleGenerator(Generator generator) {
        slackClient.postMessage(channel, getGeneratorMessage(generator));
    }

    private String getSolverMessage(Solver solver) {
        return String.format("Solution: %s = %s%nrpn: %s%ntime: %s%nattempts: %s",
                solver.getSolution(), solver.getSolution().getValue(), solver.getSolution().getRpn(),
                solver.getTime(), solver.getAttempts());
    }

    private String getGeneratorMessage(Generator generator) {
        return String.format("Question: %s%nMethod: %s = %s%nrpn: %s%ntime: %s%nattempts: %s",
                generator.getQuestionNumbers(), generator.getTarget(), generator.getTarget().getValue(),
                generator.getTarget().getRpn(), generator.getTime(), generator.getAttempts());
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public OutputType getType() {
        return TYPE;
    }
}
