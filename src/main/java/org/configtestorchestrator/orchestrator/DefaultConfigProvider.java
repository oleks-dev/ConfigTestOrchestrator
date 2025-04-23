package org.configtestorchestrator.orchestrator;

import org.configtestorchestrator.model.ConfigSet;

public interface DefaultConfigProvider {
    ConfigSet getDefaults();
}