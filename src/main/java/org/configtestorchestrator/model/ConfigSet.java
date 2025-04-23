package org.configtestorchestrator.model;

import java.util.*;
import java.util.stream.Collectors;

public class ConfigSet {
    private final Map<String, String> configMap = new TreeMap<>();

    public ConfigSet(String[] rawConfigEntries) {
        for (String entry : rawConfigEntries) {
            String[] split = entry.split("=", 2);
            if (split.length == 2) {
                String key = split[0].trim();
                String value = split[1].trim();
                configMap.put(key, value);
            }
        }
    }

    public Map<String, String> getConfigMap() {
        return Collections.unmodifiableMap(configMap);
    }

    public Set<String> getKeys() {
        return configMap.keySet();
    }

    public ConfigSet extractKeys(Set<String> keys) {
        List<String> entries = keys.stream()
                .filter(configMap::containsKey)
                .map(k -> k + "=" + configMap.get(k))
                .collect(Collectors.toList());
        return new ConfigSet(entries.toArray(new String[0]));
    }

    public ConfigSet difference(ConfigSet other) {
        List<String> diffEntries = this.configMap.entrySet().stream()
                .filter(entry -> {
                    String key = entry.getKey();
                    return !other.configMap.containsKey(key) || !Objects.equals(other.configMap.get(key), entry.getValue());
                })
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.toList());
        return new ConfigSet(diffEntries.toArray(new String[0]));
    }

    public String getHash() {
        return Integer.toHexString(configMap.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConfigSet that = (ConfigSet) o;
        return Objects.equals(configMap, that.configMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(configMap);
    }

    @Override
    public String toString() {
        return configMap.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining(", "));
    }
}
