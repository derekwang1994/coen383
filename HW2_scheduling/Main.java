import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Main
{
    private static final int SIMULATION_RUNS = 5;
    private static final int MAX_PROCESSES_PER_RUN = 20;
    private static final int ALGORITHM_COUNT = 6;
    
    public static void main(String[] args) throws CloneNotSupportedException 
    {
        // Create different scheduling algorithms
        Scheduler fcfs = new FirstComeFirstServed();
        Scheduler sjf = new ShortestJobFirst();
        Scheduler srt = new ShortestRemainingTime();
        Scheduler rr = new RoundRobin();
        Scheduler nhpf = new NonpreemptiveHighestPriorityFirstNoAging();
        Scheduler phpf = new PreemptiveHighestPriorityFirstNoAging();

        PriorityQueue<Process>[] q = new PriorityQueue[ALGORITHM_COUNT + 1];
        
        // Test each scheduling algorithm SIMULATION_RUNS times
        for (int i = 0; i < SIMULATION_RUNS; ++i)
        {
            System.out.println("\n****************************************************************\n");

            System.out.println("---------------------------");
            System.out.format("Scheduling Process Queue %d:\n", i + 1);
            System.out.println("---------------------------");
            
            //generate a new process queue for this testing round then duplicate it
            q[0] = ProcessFactory.generateProcesses(MAX_PROCESSES_PER_RUN);
            for (int j = 1; j < ALGORITHM_COUNT + 1; ++j)
                q[j] = copyQueue(q[0]);
            
            // Print the process list by ascending arrival time   
            while (!q[ALGORITHM_COUNT].isEmpty())
                System.out.println(q[ALGORITHM_COUNT].poll());
                        
            // Run each algorithm and show the results
            System.out.println("\nFirst Come First Served");
            fcfs.schedule(q[0]);

            System.out.println("\nShortest Job First");
            sjf.schedule(q[1]);
            
            System.out.println("\nShortest Remaining Time");
            srt.schedule(q[2]);

            System.out.println("\nRound Robin");
            rr.schedule(q[3]);

            System.out.println("\nNon-Preemptive Highest Priority First");
            nhpf.schedule(q[4]);
            
            System.out.println("\nPreemptive Highest Priority First  (RR switching between same priority processes)");
            phpf.schedule(q[5]);

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
