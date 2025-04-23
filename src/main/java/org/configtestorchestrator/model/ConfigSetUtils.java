package org.configtestorchestrator.model;

import java.util.*;
import java.util.stream.Collectors;

public class ConfigSetUtils {

    public static ConfigSet mergeConfigSets(Set<ConfigSet> configSets) {
        Map<String, String> merged = new TreeMap<>();
        for (ConfigSet set : configSets) {
            merged.putAll(set.getConfigMap());
        }
        List<String> entries = merged.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.toList());
        return new ConfigSet(entries.toArray(new String[0]));
    }
}
