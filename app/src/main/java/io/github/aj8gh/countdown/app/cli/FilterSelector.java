package io.github.aj8gh.countdown.app.cli;

import io.github.aj8gh.countdown.generator.Filter;
import io.github.aj8gh.countdown.generator.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterSelector {
    private static final Logger LOG = LoggerFactory.getLogger(FilterSelector.class);

    public void addFilter(String name, Generator generator) {
        name = name.toLowerCase();
        switch (name) {
            case "ODD" -> generator.addFilter(Filter.ODD);
            case "NOT_FIVE" -> generator.addFilter(Filter.NOT_FIVE);
            case "NOT_TEN" -> generator.addFilter(Filter.NOT_TEN);
            case "PRIME" -> generator.addFilter(Filter.PRIME);
            default -> LOG.warn("Filter {} not found", name);
        }
    }
}
