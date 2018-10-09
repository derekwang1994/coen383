import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class NonpreemptiveHighestPriorityFirstNoAging extends Util
{    
    @Override
    public Queue<Process> util(PriorityQueue<Process> priorityQueue)
    {
        int finishTime = 0;
        int startTime;
        Process process;
        Process scheduled;
        Util.Stats stats = this.getStats();
        Queue<Process> scheduledQueue = new LinkedList<>();

        PriorityQueue<Process> readyQueue = createReadyQueue();

        while (!priorityQueue.isEmpty() || !readyQueue.isEmpty())
        {
            while (!priorityQueue.isEmpty() && priorityQueue.peek().getArrivalTime() <= finishTime)
                readyQueue.add(priorityQueue.poll());
            process = readyQueue.isEmpty() ? priorityQueue.poll() : readyQueue.poll();
            startTime = Math.max((int) Math.ceil(process.getArrivalTime()), finishTime);
            finishTime = startTime + process.getBurstTime();
            
            if (startTime > 100)
                break;
            
            recordStatistics(finishTime, startTime, process, stats);
            scheduled = createNewProcess(startTime, process);
            scheduledQueue.add(scheduled);              
        }        
        stats.addQuanta(finishTime);
        printTimeChart(scheduledQueue);
        printRoundAvg();
        stats.nextRound();
        
        return scheduledQueue;
    }

    private PriorityQueue<Process> createReadyQueue() {
        return new PriorityQueue<>(10,
                new Comparator()
                {
                    @Override
                    public int compare(Object o1, Object o2)
                    {
                        Process p1 = (Process) o1;
                        Process p2 = (Process) o2;
                        if (p1.getPriority() == p2.getPriority())
                            return p1.getArrivalTime() < p2.getArrivalTime() ? -1 : 1;
                        else
                            return p1.getPriority() < p2.getPriority() ? -1 : 1;
                    }
                });
    }

    private Process createNewProcess(int startTime, Process process) {
        Process scheduled;
        scheduled = new Process();
        scheduled.setBurstTime(process.getBurstTime());
        scheduled.setStartTime(startTime);
        scheduled.setName(process.getName());
        return scheduled;
    }

    private void recordStatistics(int finishTime, int startTime, Process process, Stats stats) {
        stats.addWaitTime(startTime - process.getArrivalTime());
        stats.addTurnaroundTime(finishTime - process.getArrivalTime());
        stats.addResponseTime(finishTime - startTime);
        stats.addProcess();
    }
}
