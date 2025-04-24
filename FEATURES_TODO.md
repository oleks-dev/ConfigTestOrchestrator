# ConfigTestOrchestrator – Feature TODOs

This document tracks potential enhancements and features that may be added to ConfigTestOrchestrator in future versions.

---

## 🚧 Selective Sequential Execution

Some test groups may need to run sequentially, even when their config matches others, due to side effects, shared logs, or system limitations.

### ✅ Design Options:

#### Option A: CLI-driven control (Preferred initially)
```bash
--force-sequential-tags "sensitive,slow"
--force-sequential-config "fraudCheck.enabled=false,loan.approval.required=true"
```

- Runner marks the config set(s) as "isolation required"
- Those groups are excluded from parallel execution
- Others run as usual in parallel

#### Option B: Annotation-level flag (Advanced/flexible usage)
```java
@Config(global = {"x=y"}, forceSequential = true)
```
- Less clean for batching logic since tests with same config might differ on this
- Potential for confusion in mixed-preference groups
- May still be added later for fine-grained use

---

## Notes
- Final design needs to reconcile CLI and annotation if both are allowed
- CLI may override annotation
- Tag- or config-based CLI matching is simpler to implement early

Status: ✅ Logged for design — to be explored in v0.2 or later
