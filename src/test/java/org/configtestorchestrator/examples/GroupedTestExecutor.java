package org.configtestorchestrator.examples;

import org.configtestorchestrator.model.ConfigSet;
import org.configtestorchestrator.model.ConfigSetUtils;
import org.configtestorchestrator.orchestrator.*;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class GroupedTestExecutor {

    private final ConfigApplier configApplier;
    private final DefaultConfigProvider defaultConfigProvider;
    private final ExecutionReporter reporter;
    private final boolean dryRun;
    private final int maxThreads;
    private final List<String> passedTests = Collections.synchronizedList(new ArrayList<>());
    private final List<String> failedTests = Collections.synchronizedList(new ArrayList<>());

    public GroupedTestExecutor(ConfigApplier configApplier, DefaultConfigProvider defaultConfigProvider, ExecutionReporter reporter, boolean dryRun, int maxThreads) {
        this.configApplier = configApplier;
        this.defaultConfigProvider = defaultConfigProvider;
        this.reporter = reporter;
        this.dryRun = dryRun;
        this.maxThreads = maxThreads;
    }

    public void execute(Map<ConfigSet, List<Method>> groupedTests) throws InterruptedException {
        ConfigSet allUsed = ConfigSetUtils.mergeConfigSets(groupedTests.keySet());
        if (defaultConfigProvider instanceof PropertiesFileDefaultConfigProvider provider) {
            provider.validateAgainst(allUsed);
        }

        reporter.onConfigApply(defaultConfigProvider.getDefaults());
        if (!dryRun) configApplier.apply(defaultConfigProvider.getDefaults());

        for (Map.Entry<ConfigSet, List<Method>> entry : groupedTests.entrySet()) {
            ConfigSet config = entry.getKey();
            List<Method> tests = entry.getValue();

            ConfigSet toApply = config.difference(defaultConfigProvider.getDefaults());
            reporter.onConfigApply(toApply);
            if (!dryRun) configApplier.apply(toApply);

            ExecutorService executor = Executors.newFixedThreadPool(maxThreads);
            List<Callable<Void>> callables = new ArrayList<>();
            for (Method m : tests) {
                callables.add(() -> {
                    reporter.onTestStart(m);
                    try {
                        if (!dryRun) {
                            Object instance = m.getDeclaringClass().getDeclaredConstructor().newInstance();
                            m.invoke(instance);
                            passedTests.add(m.getDeclaringClass().getSimpleName() + "." + m.getName());
                            reporter.onTestSuccess(m);
                        }
                    } catch (Exception e) {
                        failedTests.add(m.getDeclaringClass().getSimpleName() + "." + m.getName());
                        reporter.onTestFailure(m, e);
                    }
                    return null;
                });
            }
            executor.invokeAll(callables);
            executor.shutdown();

            Set<String> touchedKeys = config.getKeys();
            ConfigSet resetSet = defaultConfigProvider.getDefaults().extractKeys(touchedKeys);
            reporter.onConfigReset(resetSet);
            if (!dryRun) configApplier.apply(resetSet);
        }

        reporter.onConfigApply(defaultConfigProvider.getDefaults());
        if (!dryRun) configApplier.apply(defaultConfigProvider.getDefaults());

        reporter.onSummary(passedTests, failedTests);
    }

    private static List<String> getFilters(String[] args) {
        List<String> filters = new ArrayList<>();
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("--filter")) {
                filters.add(args[i + 1]);
            }
        }
        return filters;
    }

    private static int getMaxParallel(String[] args) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals("--max-parallel")) {
                try {
                    return Integer.parseInt(args[i + 1]);
                } catch (NumberFormatException e) {
                    System.err.println("[WARN] Invalid --max-parallel value, falling back to default.");
                }
            }
        }
        return Runtime.getRuntime().availableProcessors();
    }

    public static void main(String[] args) throws Exception {
        String targetPackage = args.length > 0 ? args[0] : "org.configtestorchestrator.examples";
        String defaultConfigPath = args.length > 1 ? args[1] : "default.properties";
        boolean dryRun = Arrays.asList(args).contains("--dry-run");
        List<String> filters = getFilters(args);
        boolean verbose = Arrays.asList(args).contains("--verbose");
        int maxThreads = getMaxParallel(args);

        List<Class<?>> testClasses = ConfigGroupRunner.getClassesInPackage(targetPackage);
        Map<ConfigSet, List<Method>> grouped = ConfigGroupRunner.groupTestsByConfig(testClasses);

        if (!filters.isEmpty()) {
            grouped.replaceAll((config, methods) -> methods.stream()
                    .filter(m -> filters.contains(m.getDeclaringClass().getName() + "." + m.getName()))
                    .collect(Collectors.toList()));
        }

        if (Arrays.asList(args).contains("--tags")) {
            System.out.println("[WARN] --tags filtering not yet implemented. This is a planned feature.");
        }

        DefaultConfigProvider defaultProvider = new PropertiesFileDefaultConfigProvider(defaultConfigPath);
        ExecutionReporter reporter = ReporterFactory.create(args);
        GroupedTestExecutor executor = new GroupedTestExecutor(new ConsoleConfigApplier(), defaultProvider, reporter, dryRun, maxThreads);
        executor.execute(grouped);
    }
}
