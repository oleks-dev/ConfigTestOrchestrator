# ConfigTestOrchestrator v0.1.0 – Initial Public Release 🚀
_Released: April 24, 2025_

## 🎯 Purpose

**ConfigTestOrchestrator** is a lightweight Java framework that helps orchestrate tests for systems with complex and mutable configurations. If your test behavior changes based on environment settings, feature flags, or toggles — this tool helps ensure safety, isolation, and performance.

---

## 🚀 Key Features

- ✅ `@Config(global = [...])` for per-test configuration
- ✅ Groups tests by config for efficient batching
- ✅ Safely applies only config diffs before each group
- ✅ Resets only the touched keys to system defaults
- ✅ Validates against `default.properties` baseline
- ✅ CLI-friendly with:
  - `--filter` (run specific test methods)
  - `--verbose` (detailed execution trace)
  - `--dry-run` (simulate config/test/reset logic)
  - `--max-parallel` (limit threads per config group)
- ✅ Pluggable `ExecutionReporter` interface
- ✅ Works standalone or as a framework component

---

## 🧪 Example

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

Run it:
```bash
./gradlew runGroupedTests -PexecArgs='org.configtestorchestrator.examples default.properties --verbose --max-parallel 4'
```

---

## 🗺 Roadmap

- ✅ Parallelism with thread limits (`--max-parallel`)
- 🧠 Tag filtering (AND/OR parsing)
- 🔌 TestNG/JUnit native runners for tighter IDE/CI integration
- 📊 JSON/HTML reporter plugins
- 📘 Dev.to / blog launch article

---

## 🤝 Contributing

We welcome contributors! Check out:
- `CONTRIBUTING.md` for local setup and extension points
- `examples/SampleTest.java` for test structure

Create an issue, suggest a feature, or just fork and go 🚀

See [CONTRIBUTING.md](./CONTRIBUTING.md) for local setup and extension points

---

## 🧠 Philosophy

State-sensitive systems need state-aware testing. This framework gives you:
- Confidence that configs are respected and reset
- Speed from safe parallelism
- Clarity from annotated declarations
- Control over test orchestration at scale

**Test smart. Test safely. Orchestrate with intent.**

---

> Built from real-world experience in enterprise-grade config-driven platforms. If you’re tired of guessing test state — this is for you.

📄 MIT Licensed — no dependencies, easy to adopt.

⭐️ Star the repo if you find it useful — feedback and ideas are welcome!
