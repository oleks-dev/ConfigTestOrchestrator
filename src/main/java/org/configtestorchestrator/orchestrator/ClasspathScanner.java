package org.configtestorchestrator.orchestrator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClasspathScanner {

    public static List<String> findClasses(String path) throws IOException {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        if (resource == null) return List.of();

        File directory = new File(resource.getFile());
        if (!directory.exists()) return List.of();

        List<String> classNames = new ArrayList<>();
        for (String file : Objects.requireNonNull(directory.list())) {
            if (file.endsWith(".class")) {
                classNames.add(path.replace('/', '.') + '.' + file.substring(0, file.length() - 6));
            }
        }

        return classNames;
    }
}
