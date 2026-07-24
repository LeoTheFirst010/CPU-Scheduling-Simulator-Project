package scheduler;

import java.util.Comparator;

public final class ConsolePrinter {
    private ConsolePrinter() {
    }

    public static void printWorkload(java.util.List<Process> processes) {
        System.out.println();
        System.out.printf("%-8s %-8s %-8s %-8s%n",
                "Process", "Arrival", "Burst", "Priority");
        System.out.println("------------------------------------");

        processes.stream()
                .sorted(Comparator.comparing(Process::getProcessId))
                .forEach(p -> System.out.printf(
                        "%-8s %-8d %-8d %-8d%n",
                        p.getProcessId(),
                        p.getArrivalTime(),
                        p.getBurstTime(),
                        p.getPriority()
                ));
    }

    public static void printResult(SchedulerResult result, Metrics metrics) {
        System.out.println();
        System.out.println("-------------------------------------------");
        System.out.println("Algorithm: " + result.getAlgorithmName()+"\n");
        System.out.printf("%-8s %-8s %-8s %-8s %-10s %-10s %-10s%n",
                "Process", "Arrival", "Burst", "Finish",
                "Waiting", "Turnaround", "Response");

        result.getProcesses().stream()
                .sorted(Comparator.comparing(Process::getProcessId))
                .forEach(p -> System.out.printf(
                        "%-8s %-8d %-8d %-8d %-10d %-10d %-10d%n",
                        p.getProcessId(),
                        p.getArrivalTime(),
                        p.getBurstTime(),
                        p.getCompletionTime(),
                        p.getWaitingTime(),
                        p.getTurnaroundTime(),
                        p.getResponseTime()
                ));

        System.out.println();
        System.out.printf("Average Waiting Time:    %.2f%n",
                metrics.averageWaitingTime());
        System.out.printf("Average Turnaround Time: %.2f%n",
                metrics.averageTurnaroundTime());
        System.out.printf("Average Response Time:   %.2f%n",
                metrics.averageResponseTime());
        System.out.printf("CPU Utilization:         %.2f%%%n",
                metrics.cpuUtilization());
        System.out.printf("Throughput:              %.6f processes/time unit%n",
                metrics.throughput());
        System.out.printf("Simulation Time:         %d time units%n",
                metrics.simulationTime());

        printTimeline(result);
    }

    public static void printTimeline(SchedulerResult result) {
        System.out.println();
        System.out.println("Timeline:");
        for (ExecutionSlice slice : result.getTimeline()) {
            System.out.printf("[%d-%d: %s] ",
                    slice.startTime(),
                    slice.endTime(),
                    slice.processId());
        }
        System.out.println();
    }
}
