package scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class WorkloadGenerator {
    private WorkloadGenerator() {
    }

    public static List<Process> generate(
            int processCount,
            WorkloadType type,
            long seed
    ) {
        if (processCount <= 0) {
            throw new IllegalArgumentException("Process count must be positive.");
        }

        Random random = new Random(seed);
        List<Process> processes = new ArrayList<>();

        for (int i = 1; i <= processCount; i++) {
            int arrivalTime = random.nextInt(Math.max(5, processCount / 2 + 1));
            int burstTime = switch (type) {
                case CPU_BOUND -> 20 + random.nextInt(81);
                case IO_BOUND -> 1 + random.nextInt(10);
                case MIXED -> random.nextBoolean()
                        ? 1 + random.nextInt(10)
                        : 20 + random.nextInt(81);
            };
            int priority = 1 + random.nextInt(10);

            processes.add(new Process(
                    "P" + i,
                    arrivalTime,
                    burstTime,
                    priority
            ));
        }

        return processes;
    }

    public static List<Process> sampleWorkload() {
        return List.of(
                new Process("P1", 0, 8, 3),
                new Process("P2", 1, 4, 1),
                new Process("P3", 2, 9, 4),
                new Process("P4", 3, 5, 2),
                new Process("P5", 5, 2, 5)
        );
    }
}
