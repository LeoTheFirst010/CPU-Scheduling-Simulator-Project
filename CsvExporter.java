package scheduler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public final class CsvExporter {
    private CsvExporter() {
    }

    public static void writeHeader(Path path) throws IOException {
        Files.createDirectories(path.getParent());
        try (BufferedWriter writer = Files.newBufferedWriter(
                path,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {
            writer.write(
                    "workload_type,workload_size,process_count,algorithm,"
                            + "avg_waiting,avg_turnaround,avg_response,"
                            + "cpu_utilization,throughput,simulation_time"
            );
            writer.newLine();
        }
    }

    public static void append(
            Path path,
            String workloadType,
            String workloadSize,
            SchedulerResult result,
            Metrics metrics
    ) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                path,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        )) {
            writer.write(String.format(
                    "%s,%s,%d,%s,%.4f,%.4f,%.4f,%.4f,%.6f,%d",
                    workloadType,
                    workloadSize,
                    metrics.completedProcesses(),
                    result.getAlgorithmName().replace(",", " "),
                    metrics.averageWaitingTime(),
                    metrics.averageTurnaroundTime(),
                    metrics.averageResponseTime(),
                    metrics.cpuUtilization(),
                    metrics.throughput(),
                    metrics.simulationTime()
            ));
            writer.newLine();
        }
    }
}
