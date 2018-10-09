import java.util.ArrayList;
import java.util.PriorityQueue;

public class Run
{
    private static final int RUNS_TIME = 5;
    private static final int RUN_MAX = 20;
    private static final int COUNT_ALGORITHM = 8;
    
    public static void main(String[] args) throws CloneNotSupportedException 
    {
        // Create different scheduling algorithms
        Util fcfs = new FirstComeFirstServed();
        Util sjf = new ShortestJobFirst();
        Util srt = new ShortestRemainingTime();
        Util rr = new RoundRobin();
        Util nhpf = new NonpreemptiveHighestPriorityFirstNoAging();
        Util phpf = new PreemptiveHighestPriorityFirstNoAging();
        Util nhpfa = new NonpreemptiveHighestPriorityFirstAging();
        Util phpfa = new PreemptiveHighestPriorityFirstAging();

        PriorityQueue<Process>[] priorityQueues = new PriorityQueue[COUNT_ALGORITHM + 1];
        
        // Test each scheduling algorithm SIMULATION_RUNS times
        for (int i = 0; i < RUNS_TIME; ++i)
        {
            System.out.println("\n****************************************************************\n");

            System.out.println("---------------------------");
            System.out.format("Scheduling Process Queue %d:\n", i + 1);
            System.out.println("---------------------------");
            
            //generate a new process queue for this testing round then duplicate it
            priorityQueues[0] = ProcessFactory.generateProcesses(RUN_MAX);
            for (int j = 1; j < COUNT_ALGORITHM + 1; ++j)
                priorityQueues[j] = copyQueue(priorityQueues[0]);
            
            // Print the process list by ascending arrival time   
            while (!priorityQueues[COUNT_ALGORITHM].isEmpty())
                System.out.println(priorityQueues[COUNT_ALGORITHM].poll());
                        
            // Run each algorithm and show the results
            System.out.println("\nFirst Come First Served");
            fcfs.util(priorityQueues[0]);

            System.out.println("\nShortest Job First");
            sjf.util(priorityQueues[1]);
            
            System.out.println("\nShortest Remaining Time");
            srt.util(priorityQueues[2]);

            System.out.println("\nRound Robin");
            rr.util(priorityQueues[3]);

            System.out.println("\nNon-Preemptive Highest Priority First");
            nhpf.util(priorityQueues[4]);
            
            System.out.println("\nPreemptive Highest Priority First  (RR switching between same priority processes)");
            phpf.util(priorityQueues[5]);

            System.out.println("\n*******(Bonus)********* Non-Preemptive Highest Priority First with Aging");
            nhpfa.util(priorityQueues[6]);

            System.out.println("\n*******(Bonus)********* Preemptive Highest Priority First with Aging");
            phpfa.util(priorityQueues[7]);

        }
        System.out.println("\n-------------------------------------------");
        System.out.println("Average Statistics");
        System.out.println("-------------------------------------------");

        System.out.println("\nFirst Come First Served");
        fcfs.printAvgStats();

        System.out.println("\nShortest Job First");
        sjf.printAvgStats();

        System.out.println("\nShortest Remaining Time");
        srt.printAvgStats();

        System.out.println("\nRound Robin");
        rr.printAvgStats();

        System.out.println("\nNon-Preemptive Highest Priority First");
        nhpf.printAvgStats();

        System.out.println("\nPreemptive Highest Priority First  (RR switching between same priority processes)");
        phpf.printAvgStats();

        System.out.println("\n*******(Bonus)********* Non-Preemptive Highest Priority First with Aging");
        nhpfa.printAvgStats();

        System.out.println("\n*******(Bonus)********* Preemptive Highest Priority First with Aging");
        phpfa.printAvgStats();
    }
    
    private static PriorityQueue<Process> copyQueue(PriorityQueue<Process> q) throws CloneNotSupportedException
    {        
        PriorityQueue<Process> qcopy = new PriorityQueue<>();
        ArrayList<Process> qoriginal = new ArrayList<>();
        while (!q.isEmpty())
        {
            Process p = q.poll();
            qcopy.add((Process) p.clone());
            qoriginal.add(p);
        }
        q.addAll(qoriginal);
        return qcopy;
    }
}
