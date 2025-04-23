package org.configtestorchestrator.orchestrator;

import org.configtestorchestrator.model.ConfigSet;

public interface ConfigApplier {
    void apply(ConfigSet config);
}
