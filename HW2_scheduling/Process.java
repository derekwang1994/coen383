public class Process implements Cloneable, Comparable
{
    private char name;
    private double arrivalTime;
    private int priority;
    private int burstTime;
    private int startTime;

    public double getArrivalTime() { return arrivalTime; }
    public int getBurstTime() { return burstTime; }
    public int getPriority() { return priority; }
    public int getStartTime() { return startTime; }
    public char getName() { return this.name; }
    
    public void setArrivalTime(double arrivalTime) { this.arrivalTime = arrivalTime; }
    public void setBurstTime(int burstTime) { this.burstTime = burstTime; }
    public void setPriority(int priority) { this.priority = priority; }
    public void setStartTime(int startTime) { this.startTime = startTime; }
    public void setName(char name){ this.name = name; }
    public void addAge() {this.priority++;}

    @Override
    public String toString() 
    {
        return String.format(
                "    Process %c [arrivalTime=%f, expectedRunTime=%d, priority=%d]",
                name, arrivalTime, burstTime, priority);
    }

    @Override
    public int compareTo(Object o)
    {
        Process p = (Process) o;
        return this.arrivalTime < p.arrivalTime ? -1 : 1;
    }

    @Override
    public Object clone() throws CloneNotSupportedException
    {
        Process cloned = new Process();
        cloned.name = this.name;
        cloned.arrivalTime = this.arrivalTime;
        cloned.priority = this.priority;
        cloned.burstTime = this.burstTime;
        cloned.startTime = this.startTime;
        return cloned;
    }
}
