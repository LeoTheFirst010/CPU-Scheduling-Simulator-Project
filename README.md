# CPU Scheduling Simulator

## Overview

This project is a Java console application that simulates and compares several CPU scheduling algorithms. It was developed from scratch for the CS 3502 Operating Systems CPU Scheduling Project.

The simulator runs the same processes through each scheduling algorithm and calculates performance metrics. Using the same workload for every algorithm creates a fair comparison.

## Scheduling Algorithms

The simulator includes six algorithms:

1. **First Come, First Served (FCFS)**  
   Executes processes according to their arrival order.

2. **Shortest Job First (SJF)**  
   Selects the available process with the shortest CPU burst.

3. **Round Robin (RR)**  
   Gives each process a limited amount of CPU time based on a configurable time quantum.

4. **Priority Scheduling**  
   Selects the available process with the highest priority. A smaller priority number represents a higher priority.

5. **Shortest Remaining Time First (SRTF)**  
   A preemptive version of SJF that always executes the process with the shortest remaining time.

6. **Highest Response Ratio Next (HRRN)**  
   Selects a process using its waiting time and burst time. This helps reduce starvation.

SRTF and HRRN are the two additional scheduling algorithms implemented for this project.

## Performance Metrics

The simulator calculates:

- Average waiting time
- Average turnaround time
- Average response time
- CPU utilization
- Throughput
- Total simulation time

The program also generates a scheduling timeline showing when each process used the CPU.

## Workload Types

The application can generate three types of workloads:

- **CPU-bound:** Mostly long CPU burst times
- **I/O-bound:** Mostly short CPU burst times
- **Mixed:** A combination of short and long CPU bursts

The complete experiment tests three workload sizes:

| Size | Processes |
|---|---:|
| Small | 8 |
| Medium | 30 |
| Large | 120 |

A fixed random seed is used so that the workloads can be reproduced.

## Project Structure

```text
src/
└── scheduler/
    ├── Main.java
    ├── Process.java
    ├── ExecutionSlice.java
    ├── SchedulerResult.java
    ├── SchedulingAlgorithm.java
    ├── SchedulingUtils.java
    ├── Metrics.java
    ├── MetricsCalculator.java
    ├── WorkloadGenerator.java
    ├── WorkloadType.java
    ├── CsvExporter.java
    ├── ConsolePrinter.java
    └── algorithms/
        ├── FCFSScheduler.java
        ├── SJFScheduler.java
        ├── RoundRobinScheduler.java
        ├── PriorityScheduler.java
        ├── SRTFScheduler.java
        └── HRRNScheduler.java
