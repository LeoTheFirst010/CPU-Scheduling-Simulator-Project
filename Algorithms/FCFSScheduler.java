package scheduler.algorithms;

import scheduler.ExecutionSlice;
import scheduler.Process;
import scheduler.SchedulerResult;
import scheduler.SchedulingAlgorithm;
import scheduler.SchedulingUtils;

import java.util.ArrayList;
import java.util.List;

public class FCFSScheduler implements SchedulingAlgorithm {
    @Override
    public String getName() {
        return "FCFS";
    }

    @Override
    public SchedulerResult schedule(List<Process> inputProcesses) {
        SchedulingUtils.validateInput(inputProcesses);
        List<Process> processes = SchedulingUtils.sortedByArrival(
                SchedulingUtils.deepCopy(inputProcesses)
        );
        List<ExecutionSlice> timeline = new ArrayList<>();

        int start = processes.get(0).getArrivalTime();
        int currentTime = start;

        for (Process process : processes) {
            if (currentTime < process.getArrivalTime()) {
                SchedulingUtils.addSlice(
                        timeline, "IDLE", currentTime, process.getArrivalTime()
                );
                currentTime = process.getArrivalTime();
            }

            process.markStarted(currentTime);
            int runStart = currentTime;
            currentTime += process.getBurstTime();
            process.setRemainingTime(0);
            process.completeAt(currentTime);

            SchedulingUtils.addSlice(
                    timeline, process.getProcessId(), runStart, currentTime
            );
        }

        return new SchedulerResult(
                getName(), processes, timeline, start, currentTime
        );
    }
}
