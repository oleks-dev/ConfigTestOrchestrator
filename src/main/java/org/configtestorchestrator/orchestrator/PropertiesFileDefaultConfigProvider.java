package org.configtestorchestrator.orchestrator;

import org.configtestorchestrator.model.ConfigSet;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class PropertiesFileDefaultConfigProvider implements DefaultConfigProvider {

    private final ConfigSet defaultConfigSet;

    public PropertiesFileDefaultConfigProvider(String resourcePath) throws IOException {
        List<String> lines = loadFromResource(resourcePath);
        this.defaultConfigSet = new ConfigSet(lines.toArray(new String[0]));
    }

    @Override
    public ConfigSet getDefaults() {
        return defaultConfigSet;
    }

    public void validateAgainst(ConfigSet required) {
        Set<String> missingKeys = new HashSet<>(required.getConfigMap().keySet());
        missingKeys.removeAll(defaultConfigSet.getConfigMap().keySet());

        if (!missingKeys.isEmpty()) {
            throw new IllegalStateException("Missing keys in default config: " + missingKeys);
        }
    }

    private List<String> loadFromResource(String resourcePath) throws IOException {
        InputStream in = getClass().getClassLoader().getResourceAsStream(resourcePath);
        if (in == null) throw new IOException("Could not load resource: " + resourcePath);

        try (Scanner scanner = new Scanner(in)) {
            List<String> lines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    lines.add(line);
                }
            }
            return lines;
        }
    }
}
