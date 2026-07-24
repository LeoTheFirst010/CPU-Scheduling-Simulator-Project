package scheduler;

public final class MetricsCalculator {
    private MetricsCalculator() {
    }

    public static Metrics calculate(SchedulerResult result) {
        int count = result.getProcesses().size();

        double totalWaiting = result.getProcesses().stream()
                .mapToInt(Process::getWaitingTime)
                .sum();

        double totalTurnaround = result.getProcesses().stream()
                .mapToInt(Process::getTurnaroundTime)
                .sum();

        double totalResponse = result.getProcesses().stream()
                .mapToInt(Process::getResponseTime)
                .sum();

        int busyTime = result.getTimeline().stream()
                .filter(slice -> !slice.processId().equals("IDLE"))
                .mapToInt(ExecutionSlice::duration)
                .sum();

        int simulationTime = Math.max(
                1,
                result.getSimulationEnd() - result.getSimulationStart()
        );

        double utilization = (busyTime * 100.0) / simulationTime;
        double throughput = count / (double) simulationTime;

        return new Metrics(
                totalWaiting / count,
                totalTurnaround / count,
                totalResponse / count,
                utilization,
                throughput,
                count,
                simulationTime
        );
    }
}
