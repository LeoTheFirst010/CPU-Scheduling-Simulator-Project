package scheduler.algorithms;

import scheduler.ExecutionSlice;
import scheduler.Process;
import scheduler.SchedulerResult;
import scheduler.SchedulingAlgorithm;
import scheduler.SchedulingUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SJFScheduler implements SchedulingAlgorithm {
    @Override
    public String getName() {
        return "SJF";
    }

    @Override
    public SchedulerResult schedule(List<Process> inputProcesses) {
        SchedulingUtils.validateInput(inputProcesses);
        List<Process> processes = SchedulingUtils.deepCopy(inputProcesses);
        List<Process> completed = new ArrayList<>();
        List<ExecutionSlice> timeline = new ArrayList<>();

        int start = processes.stream()
                .mapToInt(Process::getArrivalTime)
                .min()
                .orElse(0);
        int currentTime = start;

        while (completed.size() < processes.size()) {
            final int time = currentTime;
            Process next = processes.stream()
                    .filter(p -> !p.isComplete())
                    .filter(p -> p.getArrivalTime() <= time)
                    .min(Comparator.comparingInt(Process::getBurstTime)
                            .thenComparingInt(Process::getArrivalTime)
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
            completed.add(next);

            SchedulingUtils.addSlice(
                    timeline, next.getProcessId(), runStart, currentTime
            );
        }

        return new SchedulerResult(
                getName(), processes, timeline, start, currentTime
        );
    }
}
