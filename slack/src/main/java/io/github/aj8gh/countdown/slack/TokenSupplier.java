package io.github.aj8gh.countdown.slack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.function.Supplier;

public class TokenSupplier implements Supplier<String> {
    private static final Logger LOG = LoggerFactory.getLogger(TokenSupplier.class);
    private static final Properties PROPS = new Properties();
    private static final String FILE_PATH = "slack/src/main/resources/slack.properties";
    private static final String SLACK_TOKEN_KEY = "slack.oauth.token";

    private String token;

    public TokenSupplier() {
        loadToken();
    }

    @Override
    public String get() {
        if (token == null) loadToken();
        return token;
    }

    private void loadToken() {
        try (FileInputStream in = new FileInputStream(FILE_PATH)) {
            PROPS.load(in);
            this.token = PROPS.getProperty(SLACK_TOKEN_KEY);
        } catch (IOException e) {
            LOG.error("Error loading Slack token\n", e);
        }
    }
}
