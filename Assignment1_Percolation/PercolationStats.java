import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.lang.Math;

public class PercolationStats {
    private int T;
    private double[] thresholds;
    private double mean;
    private double stddev;

    public PercolationStats(int n, int trials) {
        if (n<=0 || trials <= 0)
            throw new IllegalArgumentException("invalid input");

        this.T = trials;
        thresholds = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation p  = new Percolation(n);
            while(!p.percolates()) {
                int row = StdRandom.uniform(1, n+1);
                int col = StdRandom.uniform(1, n+1);
                p.open(row, col);
            }
            thresholds[i] = p.numberOfOpenSites() / (double)(n*n);
        }
        mean = StdStats.mean(thresholds);
        stddev = StdStats.stddev(thresholds);
    }
    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev; 
    }
    public double confidenceLo() {
        return mean() - 1.96*stddev() / Math.sqrt(this.T);
    }

    public double confidenceHi() {
        return mean() + 1.96*stddev() / Math.sqrt(this.T);
    }

    
    public static void main(String[] args) {
        //int n = StdIn.readInt();
        //int t = StdIn.readInt();
        //PercolationStats s = new PercolationStats(n, t);
        //StdOut.println("mean=" + s.mean());
        //StdOut.println("stddev:=" + s.stddev());
        //StdOut.println("95% confidence interval" + s.confidenceLo() + " " + s.confidenceHi());
    }
    
}