import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShortestRemainingTime extends Util
{    
    @Override
    public Queue<Process> util(PriorityQueue<Process> q)
    {
        int finishTime = 0;
        int startTime;
        Process p;
        Process scheduled;
        Process remaining;
        Util.Stats stats = this.getStats();
        Queue<Process> scheduledQueue = new LinkedList<>();
        

        Map<Character, Integer> startTimes = new HashMap<>();
        Map<Character, Integer> finishTimes = new HashMap<>();

        PriorityQueue<Process> readyQueue = new PriorityQueue<>(10, 
            new Comparator<Process>()
            {
                @Override
                public int compare(Process p1, Process p2)
                {
                    if (p1.getBurstTime() == p2.getBurstTime())
                        return p1.getArrivalTime() <= p2.getArrivalTime() ? -1 : 1;
                    else
                        return p1.getBurstTime() < p2.getBurstTime() ? -1 : 1;
                }            
            });
        

        PriorityQueue<Process> waitingQueue = new PriorityQueue<>(10, 
            new Comparator<Process>()
            {
                @Override
                public int compare(Process p1, Process p2)
                {
                    if (p1.getBurstTime() == p2.getBurstTime())
                        return p1.getArrivalTime() <= p2.getArrivalTime() ? -1 : 1;
                    else
                        return p1.getBurstTime() < p2.getBurstTime() ? -1 : 1;
                }            
            });
        
        while (!q.isEmpty() || !readyQueue.isEmpty() || !waitingQueue.isEmpty())
        {

            while (!q.isEmpty() && q.peek().getArrivalTime() <= finishTime)
                readyQueue.add(q.poll());
            

            //顺序：Waiting > Ready > Q
            if (readyQueue.isEmpty())
                p = (waitingQueue.isEmpty()) ? q.poll() : waitingQueue.poll();
            else if (waitingQueue.isEmpty())
                p = readyQueue.poll();
            else
                p = (readyQueue.peek().getBurstTime() < waitingQueue.peek().getBurstTime()) 
                  ? readyQueue.poll()
                  : waitingQueue.poll();

            startTime = Math.max((int) Math.ceil(p.getArrivalTime()), finishTime);
            finishTime = startTime + 1;
            

            if (!startTimes.containsKey(p.getName()))
            {
                if (startTime > 100)
                    break;
                startTimes.put(p.getName(), startTime);
                stats.addWaitTime(startTime - p.getArrivalTime());
                stats.addResponseTime(startTime - p.getArrivalTime() + 1);
            }
            else
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
