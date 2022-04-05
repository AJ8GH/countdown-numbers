package io.github.aj8gh.countdown.app.cli;

import io.github.aj8gh.countdown.generator.Filter;
import io.github.aj8gh.countdown.generator.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.IntPredicate;

public class FilterSelector {
    private static final Logger LOG = LoggerFactory.getLogger(FilterSelector.class);
    private static final Map<String, IntPredicate> filterMap = Map.of(
            "ODD", Filter.ODD,
            "FIVE", Filter.NOT_FIVE,
            "TEN", Filter.NOT_TEN,
            "PRIME", Filter.PRIME
    );

    public void addFilter(String name, Generator generator) {
        var filter = filterMap.get(name.toUpperCase());
        if (filter != null) {
            generator.addFilter(filter);
        } else {
            LOG.warn("Filter {} not found", name);
        }
    }
}
