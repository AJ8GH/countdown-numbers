package io.github.aj8gh.countdown.out.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SlackClient {
    private static final Logger LOG = LoggerFactory.getLogger(SlackClient.class);
    private final MethodsClient methods;

    public SlackClient(String token) {
        Slack slack = Slack.getInstance();
        this.methods = slack.methods(token);
    }

    public void postMessage(String channel, String message) {
        try {
            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channel)
                    .text(message)
                    .build();
            logResponse(channel, message, methods.chatPostMessage(request));
        } catch (Exception e) {
            LOG.error("Error writing Slack message to channel {}: {} \n{}",
                    channel, message, e);
        }
    }

    private void logResponse(String channel, String message,
                             ChatPostMessageResponse response) {
        if (response.isOk()) {
            LOG.info("Slack message sent to channel {}: \n{}", channel, message);
        } else {
            LOG.error("Error writing Slack message to channel {}: {} \n{}",
                    channel, message, response.getError());
        }
    }
}
