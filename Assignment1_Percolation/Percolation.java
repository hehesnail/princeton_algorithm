import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    //0: blocked, 1:open
    private int N;
    private boolean[] grid;
    private WeightedQuickUnionUF uf1;
    private WeightedQuickUnionUF uf2;

    public Percolation(int n) {
        //n*n for top virtual site, n*n+1 for bottom virtual site
        if (n <= 0) {
            throw new IllegalArgumentException("invalid n");
        }
        this.N = n;
        grid = new boolean[n*n+2];
        uf1 = new WeightedQuickUnionUF(n*n+2); //use top and bottom
        uf2 = new WeightedQuickUnionUF(n*n+1); //only use top

        for (int i = 0; i < n*n+2; i++) {
            grid[i] = false;
        }
    }

    public void open(int row, int col) {
        if (row < 1 || row > this.N || col < 1 || col > this.N)
            throw new IllegalArgumentException("index out of range");
        if (isOpen(row, col))
        	return;
        
        //open the site
        int index = (row-1)*this.N + (col-1);
        grid[index] = true;

        //if the site in the top, union with the top virtual site
        if (row - 1 == 0) {
        	uf1.union(index, this.N*this.N);
            uf2.union(index, this.N*this.N);
        }

        //if the site in the bottom, union with the bottom virtual site
        //only use uf1, the uf1 is to track the connectivity
        //uf2 is used to track the connectivity to the top
        if (row - 1 == this.N-1)
        	uf1.union(index, this.N*this.N+1);
        
        //if up site open, union two sites.
        if ((row-1)>=1 && (row-1)<=this.N && col>=1 && col<=this.N) {
            int up = ((row-1)-1)*this.N + col-1;
            if(grid[up] == true) {
                uf1.union(index, up);
                uf2.union(index, up);
            }
        }

        //if left site open, union two sites
        if (row>=1 && row<=this.N && (col-1)>=1 && (col-1)<=this.N) {
            int left = (row-1)*this.N + (col-1)-1;
            if (grid[left] == true) {
                uf1.union(index, left);
                uf2.union(index, left);
            } 
        }

        //if right site open, union two sites
        if (row>=1 && row<=this.N && (col+1)>=1 && (col+1)<=this.N) {
            int right = (row-1)*this.N + col;
            if (grid[right] == true) {
                uf1.union(index, right);
                uf2.union(index, right);
            }
        }

        //if down site open, union two sites
        if ((row+1)>=1 && (row+1)<=this.N && col>=1 && col<=this.N) {
            int down = row*this.N + col-1;
            if (grid[down] == true) {
                uf1.union(index, down);
                uf2.union(index, down);
            }
        }
    } 

    public boolean isOpen(int row, int col) {
        if (row < 1 || row > this.N || col < 1 || col > this.N)
            throw new IllegalArgumentException("index out of range");
        return grid[(row-1)*this.N+(col-1)] == true;
    }

    public boolean isFull(int row, int col) {
        if (row < 1 || row > this.N || col < 1 || col > this.N)
            throw new IllegalArgumentException("index out of range");
        int index = (row-1)*this.N + col-1;
        return uf2.connected(index, this.N*this.N);
    }

    public int numberOfOpenSites() {
        int count = 0;
        for (int i = 0; i < this.N*this.N; i++) {
            if (grid[i] == true) {
                count++;
            }
        }
        return count;
    }

    public boolean percolates() {
        return uf1.connected(this.N*this.N, this.N*this.N+1);
    }
}