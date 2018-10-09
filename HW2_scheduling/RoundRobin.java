import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RoundRobin extends Util
{    
    @Override
    public Queue<Process> util(PriorityQueue<Process> q)
    {
        int startTime, finishTime = 0;
        Process p, scheduled, remaining;
        Util.Stats stats = this.getStats();
           
        Map<Character, Integer> startTimes = new HashMap<>();        
        Map<Character, Integer> finishTimes = new HashMap<>();
        
        Queue<Process> readyQueue = new LinkedList<>();        
        Queue<Process> waitingQueue = new LinkedList<>();
        Queue<Process> scheduledQueue = new LinkedList<>();     
        
        while (!q.isEmpty() || !readyQueue.isEmpty() || !waitingQueue.isEmpty())
        {
            while (!q.isEmpty() && q.peek().getArrivalTime() <= finishTime)
                readyQueue.add(q.poll());

            //选择顺序：Ready > Q > Waiting
            if (!readyQueue.isEmpty())
                p = readyQueue.poll();
            else if (!q.isEmpty() && waitingQueue.isEmpty())
                p = q.poll();
            else
                p = waitingQueue.poll(); 

            startTime = Math.max((int) Math.ceil(p.getArrivalTime()), finishTime);
            finishTime = startTime + 1;

            if (!startTimes.containsKey(p.getName()))
            {
                if (startTime > 100)
                    break;
                startTimes.put(p.getName(), startTime);
                stats.addWaitTime(startTime - p.getArrivalTime());
                stats.addResponseTime(startTime - p.getArrivalTime() + 1);
            }else
                stats.addWaitTime(startTime - finishTimes.get(p.getName()));

            if (p.getBurstTime() > 1)
            {
                try 
                {
                    remaining = (Process) p.clone();
                    remaining.setBurstTime(remaining.getBurstTime() - 1);
                    waitingQueue.add(remaining);
                    finishTimes.put(remaining.getName(), finishTime);
                } 
                catch (CloneNotSupportedException ex) 
                {
                    Logger.getLogger(NonpreemptiveHighestPriorityFirstNoAging.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else
            {
                stats.addTurnaroundTime(finishTime - startTimes.get(p.getName()));
                stats.addProcess();
            }

            scheduled = new Process();
            scheduled.setBurstTime(1);
            scheduled.setStartTime(startTime);
            scheduled.setName(p.getName());
            scheduledQueue.add(scheduled);            
        }        
        stats.addQuanta(finishTime);
        printTimeChart(scheduledQueue);
        printRoundAvg();
        stats.nextRound();
        
        return scheduledQueue;
    }
}
