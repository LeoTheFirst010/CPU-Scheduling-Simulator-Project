package scheduler;

public class Process {
    private final String processId;
    private final int arrivalTime;
    private final int burstTime;
    private final int priority;

    private int remainingTime;
    private int startTime = -1;
    private int completionTime = -1;
    private int waitingTime;
    private int turnaroundTime;
    private int responseTime;

    public Process(String processId, int arrivalTime, int burstTime, int priority) {
        if (processId == null || processId.isBlank()) {
            throw new IllegalArgumentException("Process ID cannot be blank.");
        }
        if (arrivalTime < 0) {
            throw new IllegalArgumentException("Arrival time cannot be negative.");
        }
        if (burstTime <= 0) {
            throw new IllegalArgumentException("Burst time must be positive.");
        }
        if (priority <= 0) {
            throw new IllegalArgumentException("Priority must be positive.");
        }

        this.processId = processId;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
    }

    public Process(Process other) {
        this(other.processId, other.arrivalTime, other.burstTime, other.priority);
    }

    public String getProcessId() {
        return processId;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getStartTime() {
        return startTime;
    }

    public void markStarted(int time) {
        if (startTime == -1) {
            startTime = time;
            responseTime = startTime - arrivalTime;
        }
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void completeAt(int time) {
        completionTime = time;
        turnaroundTime = completionTime - arrivalTime;
        waitingTime = turnaroundTime - burstTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public boolean isComplete() {
        return remainingTime == 0;
    }

    @Override
    public String toString() {
        return String.format(
                "%s(arrival=%d, burst=%d, priority=%d)",
                processId, arrivalTime, burstTime, priority
        );
    }
}
