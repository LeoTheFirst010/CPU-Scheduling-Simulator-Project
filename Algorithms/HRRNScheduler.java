package scheduler.algorithms;

import scheduler.ExecutionSlice;
import scheduler.Process;
import scheduler.SchedulerResult;
import scheduler.SchedulingAlgorithm;
import scheduler.SchedulingUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HRRNScheduler implements SchedulingAlgorithm {
    @Override
    public String getName() {
        return "HRRN";
    }

    @Override
    public SchedulerResult schedule(List<Process> inputProcesses) {
        SchedulingUtils.validateInput(inputProcesses);
        List<Process> processes = SchedulingUtils.deepCopy(inputProcesses);
        List<ExecutionSlice> timeline = new ArrayList<>();

        int start = processes.stream()
                .mapToInt(Process::getArrivalTime)
                .min()
                .orElse(0);
        int currentTime = start;
        int completed = 0;

        while (completed < processes.size()) {
            final int time = currentTime;

            Process next = processes.stream()
                    .filter(p -> !p.isComplete())
                    .filter(p -> p.getArrivalTime() <= time)
                    .max(Comparator
                            .comparingDouble((Process p) ->
                                    responseRatio(p, time))
                            .thenComparingInt(p -> -p.getArrivalTime())
                            .thenComparing(Process::getProcessId))
                    .orElse(null);

            if (next == null) {
                int nextArrival = processes.stream()
                        .filter(p -> !p.isComplete())
                        .mapToInt(Process::getArrivalTime)
                        .min()
                        .orElse(currentTime);

                SchedulingUtils.addSlice(
                        timeline, "IDLE", currentTime, nextArrival
                );
                currentTime = nextArrival;
                continue;
            }

            next.markStarted(currentTime);
            int runStart = currentTime;
            currentTime += next.getBurstTime();
            next.setRemainingTime(0);
            next.completeAt(currentTime);
            completed++;

            SchedulingUtils.addSlice(
                    timeline, next.getProcessId(), runStart, currentTime
            );
        }

        return new SchedulerResult(
                getName(), processes, timeline, start, currentTime
        );
    }

    private double responseRatio(Process process, int currentTime) {
        int waitingTime = currentTime - process.getArrivalTime();
        return (waitingTime + process.getBurstTime())
                / (double) process.getBurstTime();
    }
}
