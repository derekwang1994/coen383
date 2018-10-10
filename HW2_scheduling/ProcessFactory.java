import java.util.PriorityQueue;
import java.util.Random;

public class ProcessFactory
{
    /*
     * Create processCount random processes and add to a priority queue
     * @return pq A PriorityQueue ordered with lowest arrival time first
     **/
    public static PriorityQueue<Process> generateProcesses(int processCount)
    {
        String names ="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

        PriorityQueue<Process> pq = new PriorityQueue<>();

        Random randomArrival = new Random();
        Random randomPriority = new Random();        
        Random randomExpectedTime = new Random();
        
        double nextArrival = 0.0;

        for(int i = 0; i != processCount && nextArrival < 100; ++i)
        {		
            Process p = new Process();
            p.setArrivalTime(nextArrival); 
            p.setBurstTime(randomExpectedTime.nextInt(10) + 1);
            p.setPriority(randomPriority.nextInt(4) + 1);
            p.setName(names.charAt(i));
            pq.add(p);
            
            nextArrival += randomArrival.nextFloat() * 10;
        }
        return pq;
    }
}
