package timingtest;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
       AList<Integer> Ns=new AList<Integer>();
       AList<Double> times=new AList<Double>();
       AList<Integer> opCounts=new AList<Integer>();

       Ns.addLast(1000);
        Ns.addLast(2000);
        Ns.addLast(4000);
        Ns.addLast(8000);
        Ns.addLast(16000);
        Ns.addLast(32000);
        Ns.addLast(64000);
        Ns.addLast(128000);
       for(int j=0;j<Ns.size();j++){
           AList<Integer> test=new AList<Integer>();
        Stopwatch sw=new Stopwatch();
       for (int i =0;i<Ns.get(j);i++){
           test.addLast(1);
       }
        double timeInSeconds = sw.elapsedTime();
       times.addLast(timeInSeconds);
       opCounts.addLast(Ns.get(j));}
        printTimingTable(Ns,times,opCounts);
        // TODO: YOUR CODE HERE
    }
}
