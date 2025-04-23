# ConfigTestOrchestrator

![Build](https://github.com/oleks-dev/ConfigTestOrchestrator/actions/workflows/build.yml/badge.svg)
![GitHub tag (latest by date)](https://img.shields.io/github/v/tag/oleks-dev/ConfigTestOrchestrator)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](./LICENSE.md)
![Java](https://img.shields.io/badge/Java-17+-blue)

**Smart test runner for config-sensitive workflows. Java. No dependencies.**

**ConfigTestOrchestrator** is a smart test runner designed for config-sensitive test execution. It groups tests by their required system configuration, applies those configurations safely, runs tests in isolation (with optional parallelism), and resets the system state intelligently using default values.

---

## 🔧 Key Features

- ✅ Zero external dependencies – runs on plain Java
- Declarative `@Config` annotation to describe global config dependencies
- Automatic grouping and batching of tests by config sets
- Efficient, state-aware config reset logic (resets only touched keys)
- `default.properties` fallback to ensure consistent baseline
- CLI runner with `--dry-run`, `--verbose`, `--filter`, and `--max-parallel`
- **Pluggable reporting** via `ExecutionReporter` interface
- Controlled parallel execution within safe config boundaries

---

## 🧪 Example Test

```java
@Config(global = {
    "loan.approval.required=true",
    "fraudCheck.enabled=false"
})
@Test(groups = {"api", "regression"})
public void testLoanProcessingWithFraudCheckDisabled() {
    // Your test logic here
}
```

---

## 🚀 Running the Framework

To run tests from a given package:
```bash
./gradlew runGroupedTests -PexecArgs='org.configtestorchestrator.examples default.properties --verbose'
```

Supported CLI flags:
- `--dry-run`: Simulate config/apply/test/reset without execution
- `--verbose`: Print detailed logs during execution
- `--filter <Class.method>`: Run specific test method(s) only
- `--reporter <type>`: Choose reporter (`console` supported out of the box)
- `--max-parallel <N>`: Run tests within a config group using at most N threads (default = number of CPU cores)

---

## 🧩 Extending Reporters

Implement the `ExecutionReporter` interface to hook into all test lifecycle events:
```java
void onConfigApply(ConfigSet config);
void onTestStart(Method testMethod);
void onTestSuccess(Method testMethod);
void onTestFailure(Method testMethod, Exception e);
void onConfigReset(ConfigSet resetConfig);
void onSummary(List<String> passedTests, List<String> failedTests);
```

Plug in your custom reporter via `ReporterFactory` or pass via CLI.

---

## 🗂️ Project Structure

```
ConfigTestOrchestrator/
├── .gitignore
├── build.gradle
├── CONTRIBUTIONS.md
├── README.md
├── RELEASE_NOTES.md
├── settings.gradle
├── gradlew, gradlew.bat
│
├── src/
│   ├── main/
│   │   ├── java/org/configtestorchestrator/
│   │   │   ├── annotations/        # `@Config` annotation
│   │   │   ├── model/              # ConfigSet and helpers
│   │   │   └── orchestrator/       # Core logic, runners, reporters, config handling
│   │   └── resources/
│   │       └── default.properties  # System baseline config
│   │
│   └── test/
│       ├── java/org/configtestorchestrator/examples/
│       │   ├── GroupedTestExecutor # CLI entrypoint for grouped test runs
│       │   └── SampleTest          # Sample annotated test cases
│       └── resources/
```

---

## 📌 Status

✅ Ready for integration in real-world CI pipelines
🧠 Built for workflow-heavy systems with config-sensitive behavior
🔌 Open for contribution and extensibility

