package scheduler;

public record ExecutionSlice(String processId, int startTime, int endTime) {
    public ExecutionSlice {
        if (processId == null || processId.isBlank()) {
            throw new IllegalArgumentException("Process ID cannot be blank.");
        }
        if (startTime < 0 || endTime <= startTime) {
            throw new IllegalArgumentException("Invalid execution slice.");
        }
    }

    public int duration() {
        return endTime - startTime;
    }
}
