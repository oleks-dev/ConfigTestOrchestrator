# Contributing to ConfigTestOrchestrator

🎉 First off, thanks for your interest in contributing! This project thrives with community input, and we welcome ideas, improvements, bug reports, and feature extensions.

---

## 🛠️ Local Development Setup

### 1. Clone the repo
```bash
git clone git@github.com:oleks-dev/ConfigTestOrchestrator.git
cd ConfigTestOrchestrator
```

### 2. Run a dry-run with example tests
```bash
./gradlew run --args='org.configtestorchestrator.examples default.properties --dry-run --verbose'
```

### 3. Run a specific test method
```bash
./gradlew run --args='org.configtestorchestrator.examples default.properties --filter org.configtestorchestrator.examples.SampleTest.testSomething'
```

### 4. Run grouped tests in parallel
```bash
./gradlew runGroupedTests -PexecArgs='org.configtestorchestrator.examples default.properties --max-parallel 4'
```

---

## 💡 How You Can Contribute

- ✏️ Improve **documentation** or clarify onboarding
- 🧪 Add **new example tests** (e.g., complex config sets, tricky edge cases)
- 🔌 Build **custom reporters** (JSON, file-based, CI-friendly)
- 🧱 Extend core interfaces:
  - `ConfigApplier` (custom backend integration)
  - `DefaultConfigProvider` (load defaults from other sources)
- 🧠 Help implement **tag filtering logic** with AND/OR support
- 🐛 Report bugs or submit enhancements via [Issues](../../issues)

---

## 🗂️ Project Structure Overview

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

## 🧹 Code Style & Guidelines

- Use **Java 17+**
- Core module: **no external dependencies**
- Prefer **`ExecutionReporter`** over direct `System.out.println()`
- If tagging tests, use `@Test(groups = {...})` — future-proof for filters

---

## ✅ Submitting a Pull Request

1. Fork the repo & clone your fork
2. Create a new branch for your changes
3. Write clear, isolated commits
4. Run tests locally before submitting
5. Open a PR with a short summary of what you did and why

---

Thanks for helping make test automation less painful and more powerful ✨
