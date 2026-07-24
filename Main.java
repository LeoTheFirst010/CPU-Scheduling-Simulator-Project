package scheduler;

import scheduler.algorithms.FCFSScheduler;
import scheduler.algorithms.HRRNScheduler;
import scheduler.algorithms.PriorityScheduler;
import scheduler.algorithms.RoundRobinScheduler;
import scheduler.algorithms.SJFScheduler;
import scheduler.algorithms.SRTFScheduler;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final long DEFAULT_SEED = 3502L;

    public static void main(String[] args) {
        System.out.println("CPU Scheduling Simulator");
        boolean running = true;

        while (running) {
            printMenu();
            int selection = readInt("Enter selection: ", 1, 4);

            switch (selection) {
                case 1 -> runSampleWorkload();
                case 2 -> runGeneratedWorkload();
                case 3 -> runFullExperiment();
                case 4 -> running = false;
                default -> throw new IllegalStateException("Unexpected selection.");
            }
        }

        System.out.println("Simulator closed.");
    }

    private static void printMenu() {
        System.out.println("-------------------------------------------------------");
        System.out.println("\nUser Main Menu");
        System.out.println("1. Run sample workload");
        System.out.println("2. Generate and run one workload");
        System.out.println("3. Run complete experiment and export CSV");
        System.out.println("4. Exit\n");
    }

    private static List<SchedulingAlgorithm> algorithms(int quantum) {
        return List.of(
                new FCFSScheduler(),
                new SJFScheduler(),
                new RoundRobinScheduler(quantum),
                new PriorityScheduler(),
                new SRTFScheduler(),
                new HRRNScheduler()
        );
    }

    private static void runSampleWorkload() {
        List<Process> workload = WorkloadGenerator.sampleWorkload();
        ConsolePrinter.printWorkload(workload);
        runAlgorithms(workload, 3);
    }

    private static void runGeneratedWorkload() {
        WorkloadType type = chooseWorkloadType();
        int count = readInt(
                "Number of processes (1-1000, 5 recommended): ",
                1,
                10000
        );
        int quantum = readInt(
                "Round Robin quantum (1-1000): ",
                1,
                1000
        );

        List<Process> workload = WorkloadGenerator.generate(
                count, type, DEFAULT_SEED
        );

        ConsolePrinter.printWorkload(workload);
        runAlgorithms(workload, quantum);
    }

    private static void runAlgorithms(
            List<Process> workload,
            int quantum
    ) {
        for (SchedulingAlgorithm algorithm : algorithms(quantum)) {
            SchedulerResult result = algorithm.schedule(workload);
            Metrics metrics = MetricsCalculator.calculate(result);
            ConsolePrinter.printResult(result, metrics);
        }
    }

    private static void runFullExperiment() {
        int quantum = readInt(
                "Round Robin quantum for all experiments (1-1000): ",
                1,
                1000
        );

        Path output = Path.of("results", "scheduling_results.csv");

        try {
            CsvExporter.writeHeader(output);

            List<ExperimentScenario> scenarios = new ArrayList<>();
            for (WorkloadType type : WorkloadType.values()) {
                scenarios.add(new ExperimentScenario(type, "SMALL", 8));
                scenarios.add(new ExperimentScenario(type, "MEDIUM", 30));
                scenarios.add(new ExperimentScenario(type, "LARGE", 120));
            }

            int scenarioNumber = 1;

            for (ExperimentScenario scenario : scenarios) {
                long seed = DEFAULT_SEED + scenarioNumber;
                List<Process> workload = WorkloadGenerator.generate(
                        scenario.processCount(),
                        scenario.type(),
                        seed
                );

                System.out.printf(
                        "Running %s %s workload with %d processes...%n",
                        scenario.type(),
                        scenario.size(),
                        scenario.processCount()
                );

                for (SchedulingAlgorithm algorithm : algorithms(quantum)) {
                    SchedulerResult result = algorithm.schedule(workload);
                    Metrics metrics = MetricsCalculator.calculate(result);

                    CsvExporter.append(
                            output,
                            scenario.type().name(),
                            scenario.size(),
                            result,
                            metrics
                    );
                }

                scenarioNumber++;
            }

            System.out.println();
            System.out.println("Experiment complete.");
            System.out.println("CSV file created at: "
                    + output.toAbsolutePath());
        } catch (IOException exception) {
            System.err.println("Could not export results: "
                    + exception.getMessage());
        }
    }

    private static WorkloadType chooseWorkloadType() {
        System.out.println();
        System.out.println("1. CPU-bound");
        System.out.println("2. I/O-bound");
        System.out.println("3. Mixed");

        int selection = readInt("Choose workload type: ", 1, 3);

        return switch (selection) {
            case 1 -> WorkloadType.CPU_BOUND;
            case 2 -> WorkloadType.IO_BOUND;
            case 3 -> WorkloadType.MIXED;
            default -> throw new IllegalStateException("Unexpected selection.");
        };
    }

    private static int readInt(
            String prompt,
            int minimum,
            int maximum
    ) {
        while (true) {
            System.out.print(prompt);
            String input = SCANNER.nextLine().trim();

            try {
                int value = Integer.parseInt(input);

                if (value < minimum || value > maximum) {
                    System.out.printf(
                            "Enter a value from %d to %d.%n",
                            minimum,
                            maximum
                    );
                    continue;
                }

                return value;
            } catch (NumberFormatException exception) {
                System.out.println("Enter a valid integer.");
            }
        }
    }

    private record ExperimentScenario(
            WorkloadType type,
            String size,
            int processCount
    ) {
    }
}
