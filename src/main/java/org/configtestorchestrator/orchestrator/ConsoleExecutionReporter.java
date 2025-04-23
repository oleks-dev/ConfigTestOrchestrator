package org.configtestorchestrator.orchestrator;

import org.configtestorchestrator.model.ConfigSet;

import java.lang.reflect.Method;
import java.util.List;

public class ConsoleExecutionReporter implements ExecutionReporter {

    private final boolean verbose;

    public ConsoleExecutionReporter(boolean verbose) {
        this.verbose = verbose;
    }

    @Override
    public void onConfigApply(ConfigSet config) {
        if (verbose) {
            System.out.println("[CONFIG APPLY] " + config);
        }
    }

    @Override
    public void onTestStart(Method testMethod) {
        if (verbose) {
            System.out.println("[TEST START] " + testMethod.getDeclaringClass().getSimpleName() + "." + testMethod.getName());
        }
    }

    @Override
    public void onTestSuccess(Method testMethod) {
        if (verbose) {
            System.out.println("[TEST PASS] " + testMethod.getDeclaringClass().getSimpleName() + "." + testMethod.getName());
        }
    }

    @Override
    public void onTestFailure(Method testMethod, Exception e) {
        System.out.println("[TEST FAIL] " + testMethod.getDeclaringClass().getSimpleName() + "." + testMethod.getName());
        e.printStackTrace();
    }

    @Override
    public void onConfigReset(ConfigSet resetConfig) {
        if (verbose) {
            System.out.println("[CONFIG RESET] " + resetConfig);
        }
    }

    @Override
    public void onSummary(List<String> passedTests, List<String> failedTests) {
        System.out.println("\n=== TEST SUMMARY ===");
        System.out.println("Total tests run: " + (passedTests.size() + failedTests.size()));
        System.out.println("Passed: " + passedTests.size());
        System.out.println("Failed: " + failedTests.size());

        if (!failedTests.isEmpty()) {
            System.out.println("\nFailed Tests:");
            failedTests.forEach(test -> System.out.println("  - " + test));
        }
    }
}