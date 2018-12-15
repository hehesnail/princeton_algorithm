import java.util.ArrayList;

public class Board {
	private int[][] blocks;

	public Board(int[][] blocks) {
		int N = blocks.length;
		this.blocks = new int[N][N];

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++)
				this.blocks[i][j] = blocks[i][j];
		}
	}
	
	public int dimension() {
		return blocks.length;
	}

	public int hamming() {
		int N = dimension();
		int dis = 0;
		int[][] right = new int[N][N];

		int temp=1;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				right[i][j] = temp;
				temp++;
			}
		}

		right[N-1][N-1] = 0;

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				// skip blank square
				if (blocks[i][j] == 0)
					continue;
				if (blocks[i][j] != right[i][j])
					dis++;
			}
		}

		return dis;
	}

	private int abs(int i) {
		if (i > 0) return i;
		else return -i;
	}

	public int manhattan() {
		int N = dimension();
		int dis = 0;

		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				//skip blank 
				if (blocks[i][j] == 0) continue;
				int row = (blocks[i][j]-1) / N;
				int col = (blocks[i][j]-1) % N;

				dis += abs(row-i) + abs(col-j);
			}
		}

		return dis;
	}
	
	public boolean isGoal() {
		if (manhattan() == 0) return true;
		else return false;
	}

	public Board twin() {
		int N = dimension();
		int i1=0, j1=0, i2=1, j2=1;

		if (blocks[i1][j1] == 0)
			i1 = 1;
		if (blocks[i2][j2] == 0)
			i2 = 0;

		swap(i1, j1, i2, j2);
		Board twin = new Board(blocks);
		swap(i1, j1, i2, j2);

		return twin;
	}

	public boolean equals(Object y) {
		if (y == this) return true;
		if (y == null) return false;
		if (y.getClass() != this.getClass()) return false;

		Board that = (Board) y;

		int m = dimension();
		int n = that.dimension();

		if (m != n) return false;

		boolean flag = true;
		int N = dimension();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (blocks[i][j] != that.blocks[i][j]) {
					flag = false;
					break;
				}
			}
		}

		return flag;
	}

	private void swap(int i1, int j1, int i2, int j2) {
		int temp = blocks[i1][j1];
		blocks[i1][j1] = blocks[i2][j2];
		blocks[i2][j2] = temp;
	}

	public Iterable<Board> neighbors() {
		ArrayList<Board> neighs = new ArrayList<Board>();
		int N = dimension();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (blocks[i][j] == 0) {
					//up
					if (i-1 >= 0) {
						swap(i-1, j, i, j);
						Board up = new Board(blocks);
						neighs.add(up);
						swap(i-1, j, i, j);
					}
					//down
					if (i+1 < N) {
						swap(i+1, j, i, j);
						Board down = new Board(blocks);
						neighs.add(down);
						swap(i+1, j, i, j);
					}
					//left
					if (j-1 >= 0) {
						swap(i, j-1, i, j);
						Board left = new Board(blocks);
						neighs.add(left);
						swap(i, j-1, i, j);
					}
					//right
					if (j+1 < N) {
						swap(i, j+1, i, j);
						Board right = new Board(blocks);
						neighs.add(right);
						swap(i, j+1, i, j);
					}
				}
			}
		}

		return neighs;
	}

	public String toString() {
    	StringBuilder s = new StringBuilder();
    	int n = dimension();
    	s.append(n + "\n");
    	for (int i = 0; i < n; i++) {
        	for (int j = 0; j < n; j++) {
            	s.append(String.format("%2d ", blocks[i][j]));
        	}
        	s.append("\n");
    	}
    	return s.toString();
	}

	public static void main(String[] args) // unit tests (not graded)
    {
    	int [][]blocks = new int[3][3];
    	
    	blocks[0][0] = 8;
    	blocks[0][1] = 1;
    	blocks[0][2] = 3;
    	
    	blocks[1][0] = 4;
    	blocks[1][1] = 0;
    	blocks[1][2] = 2;
    	
    	blocks[2][0] = 7;
    	blocks[2][1] = 6;
    	blocks[2][2] = 5;
    	Board board = new Board(blocks);
    	
    	System.out.println(board.manhattan());
    	System.out.println(board.hamming());

    	System.out.println(board.toString());
    	
    	for (Board it : board.neighbors()) {
			System.out.println(it.toString());
    	}
    	
    	System.out.println(board.twin().toString());
    }
}