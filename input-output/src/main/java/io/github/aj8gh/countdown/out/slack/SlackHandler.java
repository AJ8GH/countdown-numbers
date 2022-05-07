package io.github.aj8gh.countdown.out.slack;

import io.github.aj8gh.countdown.gen.GenResult;
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
    public void handleGenerator(GenResult genResult) {
        slackClient.postMessage(channel, getGeneratorMessage(genResult));
    }

    private String getSolverMessage(Solver solver) {
        return String.format("Solution: %s = %s%nrpn: %s%ntime: %s%nattempts: %s",
                solver.getSolution(), solver.getSolution().getValue(), solver.getSolution().getRpn(),
                solver.getTime(), solver.getAttempts());
    }

    private String getGeneratorMessage(GenResult result) {
        return String.format("Question: %s%nMethod: %s = %s%nrpn: %s%ntime: %s%nattempts: %s",
                result.questionNumbers(), result.target(), result.target(),
                result.rpn(), result.time(), result.attempts());
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public OutputType getType() {
        return TYPE;
    }
}
