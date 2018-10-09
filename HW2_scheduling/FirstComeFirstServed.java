import java.util.Queue;
import java.util.LinkedList;
import java.util.PriorityQueue;

/**
 * Extends Util as a First Come First Served algorithm
 * Reads a PriorityQueue<Process>, schedules it, and returns a new Queue<Process>
 */
public class FirstComeFirstServed extends Util
{
    @Override
    public Queue<Process> util(PriorityQueue<Process> queue)
    {
        int startTime;
        int finishTime = 0;
        int queueSize = queue.size();
        Process process;
        Process scheduled;
        Stats stats = this.getStats();
        Queue<Process> processQueue = new LinkedList<>();
        
        for (int i = 0; i < queueSize; ++i)
        {
            process = queue.remove();
            startTime = Math.max((int) Math.ceil(process.getArrivalTime()), finishTime);
            finishTime = startTime + process.getBurstTime();
            
            if (startTime > 100)
                break;

            statsState(startTime, finishTime, process, stats);
            scheduled = setScheduled(startTime, process);
            processQueue.add(scheduled);
        }
        stats.addQuanta(finishTime);
        printTimeChart(processQueue);
        printRoundAvg();
        stats.nextRound();
        
        return processQueue;
    }

    private Process setScheduled(int startTime, Process process) {
        Process scheduled;
        scheduled = new Process();
        scheduled.setBurstTime(process.getBurstTime());
        scheduled.setStartTime(startTime);
        scheduled.setName(process.getName());
        return scheduled;
    }

    private void statsState(int startTime, int finishTime, Process process, Stats stats) {
        stats.addWaitTime(startTime - process.getArrivalTime());
        stats.addTurnaroundTime(finishTime - process.getArrivalTime());
        stats.addResponseTime(finishTime - startTime);
        stats.addProcess();
    }
}
