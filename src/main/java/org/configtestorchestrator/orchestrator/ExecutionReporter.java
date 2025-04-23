package org.configtestorchestrator.orchestrator;

import org.configtestorchestrator.model.ConfigSet;

import java.lang.reflect.Method;
import java.util.List;

public interface ExecutionReporter {
    void onConfigApply(ConfigSet config);
    void onTestStart(Method testMethod);
    void onTestSuccess(Method testMethod);
    void onTestFailure(Method testMethod, Exception e);
    void onConfigReset(ConfigSet resetConfig);
    void onSummary(List<String> passedTests, List<String> failedTests);
}
