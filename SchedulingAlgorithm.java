package scheduler;

import java.util.List;

public interface SchedulingAlgorithm {
    String getName();

    SchedulerResult schedule(List<Process> inputProcesses);
}
