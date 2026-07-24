package scheduler.algorithms;

import scheduler.ExecutionSlice;
import scheduler.Process;
import scheduler.SchedulerResult;
import scheduler.SchedulingAlgorithm;
import scheduler.SchedulingUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;

public class RoundRobinScheduler implements SchedulingAlgorithm {
    private final int quantum;

    public RoundRobinScheduler(int quantum) {
        if (quantum <= 0) {
            throw new IllegalArgumentException("Quantum must be positive.");
        }
        this.quantum = quantum;
    }

    @Override
    public String getName() {
        return "Round Robin (q=" + quantum + ")";
    }

    @Override
    public SchedulerResult schedule(List<Process> inputProcesses) {
        SchedulingUtils.validateInput(inputProcesses);

        List<Process> processes = SchedulingUtils.deepCopy(inputProcesses);
        List<Process> arrivals = processes.stream()
                .sorted(Comparator.comparingInt(Process::getArrivalTime)
                        .thenComparing(Process::getProcessId))
                .toList();

        List<ExecutionSlice> timeline = new ArrayList<>();
        Deque<Process> readyQueue = new ArrayDeque<>();

        int start = arrivals.get(0).getArrivalTime();
        int currentTime = start;
        int nextArrivalIndex = 0;
        int completed = 0;

        while (completed < processes.size()) {
            while (nextArrivalIndex < arrivals.size()
                    && arrivals.get(nextArrivalIndex).getArrivalTime() <= currentTime) {
                readyQueue.addLast(arrivals.get(nextArrivalIndex));
                nextArrivalIndex++;
            }

            if (readyQueue.isEmpty()) {
                int nextArrival = arrivals.get(nextArrivalIndex).getArrivalTime();
                SchedulingUtils.addSlice(
                        timeline, "IDLE", currentTime, nextArrival
                );
                currentTime = nextArrival;
                continue;
            }

            Process current = readyQueue.removeFirst();
            current.markStarted(currentTime);

            int runFor = Math.min(quantum, current.getRemainingTime());
            int runStart = currentTime;
            currentTime += runFor;
            current.setRemainingTime(current.getRemainingTime() - runFor);

            SchedulingUtils.addSlice(
                    timeline, current.getProcessId(), runStart, currentTime
            );

            while (nextArrivalIndex < arrivals.size()
                    && arrivals.get(nextArrivalIndex).getArrivalTime() <= currentTime) {
                readyQueue.addLast(arrivals.get(nextArrivalIndex));
                nextArrivalIndex++;
            }

            if (current.isComplete()) {
                current.completeAt(currentTime);
                completed++;
            } else {
                readyQueue.addLast(current);
            }
        }

        return new SchedulerResult(
                getName(), processes, timeline, start, currentTime
        );
    }
}
