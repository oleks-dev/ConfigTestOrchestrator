package org.configtestorchestrator.orchestrator;

import org.configtestorchestrator.model.ConfigSet;

public class ConsoleConfigApplier implements ConfigApplier {

    @Override
    public void apply(ConfigSet config) {
        System.out.println("[CONFIG APPLY] Applying config: " + config);
    }

}