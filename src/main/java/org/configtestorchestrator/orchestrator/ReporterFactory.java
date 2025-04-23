package org.configtestorchestrator.orchestrator;

public class ReporterFactory {

    public static ExecutionReporter create(String[] args) {
        boolean verbose = containsArg(args, "--verbose");
        String reporterType = getArgValue(args, "--reporter");

        if (reporterType == null || reporterType.equalsIgnoreCase("console")) {
            return new ConsoleExecutionReporter(verbose);
        }

        throw new IllegalArgumentException("Unsupported reporter type: " + reporterType);
    }

    private static boolean containsArg(String[] args, String flag) {
        for (String arg : args) {
            if (arg.equals(flag)) return true;
        }
        return false;
    }

    private static String getArgValue(String[] args, String flag) {
        for (int i = 0; i < args.length - 1; i++) {
            if (args[i].equals(flag)) {
                return args[i + 1];
            }
        }
        return null;
    }
}