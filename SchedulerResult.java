package scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SchedulerResult {
    private final String algorithmName;
    private final List<Process> processes;
    private final List<ExecutionSlice> timeline;
    private final int simulationStart;
    private final int simulationEnd;

    public SchedulerResult(
            String algorithmName,
            List<Process> processes,
            List<ExecutionSlice> timeline,
            int simulationStart,
            int simulationEnd
    ) {
        this.algorithmName = algorithmName;
        this.processes = Collections.unmodifiableList(new ArrayList<>(processes));
        this.timeline = Collections.unmodifiableList(new ArrayList<>(timeline));
        this.simulationStart = simulationStart;
        this.simulationEnd = simulationEnd;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public List<Process> getProcesses() {
        return processes;
    }

    public List<ExecutionSlice> getTimeline() {
        return timeline;
    }

    public int getSimulationStart() {
        return simulationStart;
    }

    public int getSimulationEnd() {
        return simulationEnd;
    }
}
