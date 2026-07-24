package scheduler.algorithms;

import scheduler.ExecutionSlice;
import scheduler.Process;
import scheduler.SchedulerResult;
import scheduler.SchedulingAlgorithm;
import scheduler.SchedulingUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SRTFScheduler implements SchedulingAlgorithm {
    @Override
    public String getName() {
        return "SRTF";
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
                    .min(Comparator.comparingInt(Process::getRemainingTime)
                            .thenComparingInt(Process::getArrivalTime)
                            .thenComparing(Process::getProcessId))
                    .orElse(null);

            if (next == null) {
                int nextArrival = processes.stream()
                        .filter(p -> !p.isComplete())
                        .mapToInt(Process::getArrivalTime)
                        .min()
                        .orElse(currentTime + 1);

                SchedulingUtils.addSlice(
                        timeline, "IDLE", currentTime, nextArrival
                );
                currentTime = nextArrival;
                continue;
            }

            next.markStarted(currentTime);
            SchedulingUtils.addSlice(
                    timeline, next.getProcessId(), currentTime, currentTime + 1
            );

            next.setRemainingTime(next.getRemainingTime() - 1);
            currentTime++;

            if (next.isComplete()) {
                next.completeAt(currentTime);
                completed++;
            }
        }

        return new SchedulerResult(
                getName(), processes, timeline, start, currentTime
        );
    }
}
