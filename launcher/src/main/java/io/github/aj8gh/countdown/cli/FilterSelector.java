package io.github.aj8gh.countdown.cli;

import io.github.aj8gh.countdown.gen.FilterFactory.Filter;
import io.github.aj8gh.countdown.gen.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Map;
import java.util.function.IntPredicate;

import static java.util.stream.Collectors.toMap;

public class FilterSelector {
    private static final Logger LOG = LoggerFactory.getLogger(FilterSelector.class);
    private static final Map<String, IntPredicate> filterMap = Arrays.stream(Filter.values())
            .collect(toMap(Filter::key, Filter::getPredicate));

    public void addFilter(String name, Generator generator) {
        var filter = filterMap.get(name.toUpperCase());
        if (filter != null) {
            generator.addFilter(filter);
        } else {
            LOG.warn("FilterFactory {} not found", name);
        }
    }
}
