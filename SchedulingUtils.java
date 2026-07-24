package scheduler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class SchedulingUtils {
    private SchedulingUtils() {
    }

    public static List<Process> deepCopy(List<Process> input) {
        List<Process> copy = new ArrayList<>();
        for (Process process : input) {
            copy.add(new Process(process));
        }
        return copy;
    }

    public static void validateInput(List<Process> processes) {
        if (processes == null || processes.isEmpty()) {
            throw new IllegalArgumentException("At least one process is required.");
        }
    }

    public static List<Process> sortedByArrival(List<Process> processes) {
        return processes.stream()
                .sorted(Comparator.comparingInt(Process::getArrivalTime)
                        .thenComparing(Process::getProcessId))
                .toList();
    }

    public static void addSlice(
            List<ExecutionSlice> timeline,
            String processId,
            int start,
            int end
    ) {
        if (end <= start) {
            return;
        }

        if (!timeline.isEmpty()) {
            ExecutionSlice last = timeline.get(timeline.size() - 1);
            if (last.processId().equals(processId) && last.endTime() == start) {
                timeline.set(
                        timeline.size() - 1,
                        new ExecutionSlice(processId, last.startTime(), end)
                );
                return;
            }
        }

        timeline.add(new ExecutionSlice(processId, start, end));
    }
}
