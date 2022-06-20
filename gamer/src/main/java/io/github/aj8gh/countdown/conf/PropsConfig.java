package io.github.aj8gh.countdown.conf;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class PropsConfig {
    private static final String APP_PROPS_FILE = "application.properties";
    private static final String COMMA = ",";

    private final Properties props = new Properties();

    public PropsConfig() {
        loadProperties();
    }

    public String getString(String key) {
        return props.getProperty(key);
    }

    public boolean getBoolean(String key) {
        return Boolean.parseBoolean(props.getProperty(key));
    }

    public int getInt(String key) {
        return Integer.parseInt(props.getProperty(key).replace("_", ""));
    }

    public long getLong(String key) {
        return Long.parseLong(props.getProperty(key).replace("_", ""));
    }

    public List<String> getStrings(String key) {
        return Arrays.stream(props.getProperty(key).split(COMMA))
                .map(String::trim)
                .toList();
    }

    private void loadProperties() {
        try (var in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(APP_PROPS_FILE)) {
            props.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Error loading app properties", e);
        }
    }
}
