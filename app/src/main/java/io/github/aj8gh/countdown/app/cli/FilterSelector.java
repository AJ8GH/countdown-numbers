package io.github.aj8gh.countdown.app.cli;

import io.github.aj8gh.countdown.generator.FilterFactory;
import io.github.aj8gh.countdown.generator.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.IntPredicate;

public class FilterSelector {
    private static final Logger LOG = LoggerFactory.getLogger(FilterSelector.class);
    private static final Map<String, IntPredicate> filterMap = Map.of(
            "ODD", FilterFactory.Filter.ODD.getPredicate(),
            "FIVE", FilterFactory.Filter.NOT_FIVE.getPredicate(),
            "TEN", FilterFactory.Filter.NOT_TEN.getPredicate(),
            "PRIME", FilterFactory.Filter.PRIME.getPredicate()
    );

    public void addFilter(String name, Generator generator) {
        var filter = filterMap.get(name.toUpperCase());
        if (filter != null) {
            generator.addFilter(filter);
        } else {
            LOG.warn("FilterFactory {} not found", name);
        }
    }
}
