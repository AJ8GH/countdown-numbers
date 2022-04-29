package io.github.aj8gh.countdown.slack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.function.Supplier;

public class TokenSupplier implements Supplier<String> {
    private static final Logger LOG = LoggerFactory.getLogger(TokenSupplier.class);

    private final String propsFilePath;
    private final String tokenProperty;
    private final Properties props = new Properties();

    private String token;

    public TokenSupplier(String propsFilePath, String tokenProperty) {
        this.propsFilePath = propsFilePath;
        this.tokenProperty = tokenProperty;
        loadToken();
    }

    @Override
    public String get() {
        if (token == null) loadToken();
        return token;
    }

    private void loadToken() {
        try (FileInputStream in = new FileInputStream(propsFilePath)) {
            props.load(in);
            this.token = props.getProperty(tokenProperty);
        } catch (IOException e) {
            LOG.error("Error loading Slack token\n", e);
        }
    }
}
