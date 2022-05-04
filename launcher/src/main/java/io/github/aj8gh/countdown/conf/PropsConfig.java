package io.github.aj8gh.countdown.conf;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PropsConfig {
    private static final String APP_PROPS_FILE = "application.properties";
    private static final String SLACK_PROPS_FILE = "slack.properties";
    private static final String COMMA = ",";

    private final Properties props = new Properties();

    public PropsConfig() {
        loadProperties(APP_PROPS_FILE);
        loadProperties(SLACK_PROPS_FILE);
    }

    public String getString(String key) {
        return props.getProperty(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(props.getProperty(key));
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(props.getProperty(key));
    }

    public long getLong(String key) {
        return Long.parseLong(props.getProperty(key));
    }

    public List<String> getStrings(String key) {
        return Arrays.stream(props.getProperty(key).split(COMMA))
                .map(String::trim)
                .toList();
    }

    public List<Integer> getInts(String key) {
        return Arrays.stream(props.getProperty(key).split(COMMA))
                .map(e -> Integer.valueOf(e.trim()))
                .toList();
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
