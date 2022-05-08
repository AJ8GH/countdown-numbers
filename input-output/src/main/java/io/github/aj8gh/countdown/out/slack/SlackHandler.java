package io.github.aj8gh.countdown.out.slack;

import io.github.aj8gh.countdown.gen.GenResult;
import io.github.aj8gh.countdown.out.OutputHandler;
import io.github.aj8gh.countdown.sol.SolResult;

import static io.github.aj8gh.countdown.out.OutputHandler.OutputType.SLACK;

public class SlackHandler implements OutputHandler {
    private static final OutputType TYPE = SLACK;
    private final SlackClient slackClient;
    private String channel;

    public SlackHandler(SlackClient slackClient) {
        this.slackClient = slackClient;
    }

    @Override
    public void handleSolver(SolResult solResult) {
        slackClient.postMessage(channel, getSolverMessage(solResult));
    }

    @Override
    public void handleGenerator(GenResult genResult) {
        slackClient.postMessage(channel, getGeneratorMessage(genResult));
    }

    private String getSolverMessage(SolResult solResult) {
        return String.format("Solution: %s = %s%nrpn: %s%ntime: %s%nattempts: %s",
                solResult.getSolution(), solResult.getTarget(), solResult.getRpn(),
                solResult.getTime(), solResult.getAttempts());
    }

    private String getGeneratorMessage(GenResult result) {
        return String.format("Question: %s%nMethod: %s = %s%nrpn: %s%ntime: %s%nattempts: %s",
                result.getQuestionNumbers(), result.getSolution(), result.getTarget(),
                result.getRpn(), result.getTime(), result.getAttempts());
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public OutputType getType() {
        return TYPE;
    }
}
