import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class NonpreemptiveHighestPriorityFirstAging extends Util
{
    public PriorityQueue<Process> makePQ(int size) {
        PriorityQueue<Process> pq = new PriorityQueue<>(size,
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
        return pq;
    }

    @Override
    public Queue<Process> util(PriorityQueue<Process> q)
    {
        int finishTime = 0;
        int startTime;
        Process p;
        Process scheduled;
        Util.Stats stats = this.getStats();
        Queue<Process> scheduledQueue = new LinkedList<>();

        // Queue processes that are ready to run, and order by shortest run time
        // break ties with arrival time so they are readied in the correct order
        PriorityQueue<Process> readyQueue = makePQ(10);

        while (!q.isEmpty() || !readyQueue.isEmpty())
        {
            // add processes that have arrived by now to the ready queue
            while (!q.isEmpty() && q.peek().getArrivalTime() <= finishTime)
                readyQueue.add(q.poll());

            p = readyQueue.isEmpty() ? q.poll() : readyQueue.poll();

            //add aging
            PriorityQueue<Process> tempPQ = new PriorityQueue<>();
            while (!q.isEmpty())
            {
                Process tempP = q.poll();
                tempP.addAge();
                tempPQ.add(tempP);
            }
            q = tempPQ;


            startTime = Math.max((int) Math.ceil(p.getArrivalTime()), finishTime);
            finishTime = startTime + p.getBurstTime();

            // Don't start any processes after 100 time slices
            if (startTime > 100)
                break;

            // Record the statistics for this process
            stats.addWaitTime(startTime - p.getArrivalTime());
            stats.addTurnaroundTime(finishTime - p.getArrivalTime());
            stats.addResponseTime(finishTime - startTime);
            stats.addProcess();

            // Create a new process with the calculated start time and add to a new queue
            scheduled = new Process();
            scheduled.setBurstTime(p.getBurstTime());
            scheduled.setStartTime(startTime);
            scheduled.setName(p.getName());
            scheduledQueue.add(scheduled);
        }
        stats.addQuanta(finishTime); // Add the total quanta to finish all jobs
        printTimeChart(scheduledQueue);
        printRoundAvg();
        stats.nextRound();

        return scheduledQueue;
    }
}