package org.configtestorchestrator.orchestrator;

import org.configtestorchestrator.annotations.Config;
import org.configtestorchestrator.model.ConfigSet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigGroupRunner {

    public static Map<ConfigSet, List<Method>> groupTestsByConfig(List<Class<?>> testClasses) {
        Map<ConfigSet, List<Method>> configGroups = new HashMap<>();

        for (Class<?> testClass : testClasses) {
            for (Method method : testClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Config.class)) {
                    Config configAnnotation = method.getAnnotation(Config.class);
                    ConfigSet configSet = new ConfigSet(configAnnotation.global());

                    configGroups.computeIfAbsent(configSet, k -> new ArrayList<>()).add(method);
                }
            }
        }

        return configGroups;
    }

    public static void printConfigGroups(Map<ConfigSet, List<Method>> groupedTests) {
        System.out.println("\n=== CONFIG GROUPS ===");
        for (Map.Entry<ConfigSet, List<Method>> entry : groupedTests.entrySet()) {
            System.out.println("\nConfig Hash: " + entry.getKey().getHash());
            System.out.println("Config: " + entry.getKey());
            System.out.println("Tests:");
            for (Method m : entry.getValue()) {
                System.out.println("  - " + m.getDeclaringClass().getSimpleName() + "." + m.getName());
            }
        }
    }

    public static List<Class<?>> getClassesInPackage(String packageName) throws IOException, ClassNotFoundException {
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
        List<String> classNames = new ArrayList<>();

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            resource.getFile();
            classNames.addAll(ClasspathScanner.findClasses(path)); // Use a helper utility for scanning class names
        }

        return classNames.stream()
                .map(className -> {
                    try {
                        return Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static void main(String[] args) throws Exception {

         String targetPackage = args.length > 0 ? args[0] : "examples";
         List<Class<?>> testClasses = getClassesInPackage(targetPackage);
         Map<ConfigSet, List<Method>> grouped = groupTestsByConfig(testClasses);
         printConfigGroups(grouped);

    }
}
