package scheduler;

public record Metrics(
        double averageWaitingTime,
        double averageTurnaroundTime,
        double averageResponseTime,
        double cpuUtilization,
        double throughput,
        int completedProcesses,
        int simulationTime
) {
}
