package io.github.aj8gh.countdown.app.conf;

import java.io.IOException;
import java.util.Properties;

public class PropsConfig {
    private static final String APP_PROPS_FILE = "application.properties";
    private static final String SLACK_PROPS_FILE = "slack.properties";

    private final Properties props = new Properties();

    public PropsConfig() {
        loadProperties(APP_PROPS_FILE);
        loadProperties(SLACK_PROPS_FILE);
    }

    public Properties getProps() {
        return props;
    }

    private void loadProperties(String propsFile) {
        try (var in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(propsFile)) {
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Error loading app properties", e);
        }
    }
}
