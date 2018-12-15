import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
	private SearchNode currentNode;
	private SearchNode currentTwinNode;
	private Stack<Board> sols;

	//Def of search node
	private class SearchNode implements Comparable<SearchNode>{
		private Board b;
		private int moves;
		private int priority;
		private SearchNode prev;

		public SearchNode(Board b, SearchNode prev) {
			this.b = b;
			this.prev = prev;

			if (this.prev == null) {
				moves = 0;
			} else {
				moves = prev.getMoves() + 1;
			}

			priority = b.manhattan() + moves;
		}

		public int getMoves() {
			return moves;
		}

		public int getPriority() {
			return priority;
		}

		public Board getBoard() {
			return b;
		}

		public int compareTo(SearchNode that) {
			if (priority < that.getPriority()) return -1;
			else if (priority > that.getPriority()) return +1;
			else return 0;
		}

	}

	public Solver(Board initial) {

		//start 
		currentNode = new SearchNode(initial, null);
		currentTwinNode = new SearchNode(initial.twin(), null);
		sols = new Stack<Board>(); 

		MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
		MinPQ<SearchNode> pqt = new MinPQ<SearchNode>();

		pq.insert(currentNode);
		pqt.insert(currentTwinNode);

		while (true) {

			currentNode = pq.delMin();
			Board currentBoard = currentNode.getBoard();
			if (currentBoard.isGoal()) break;
			else {
				for (Board b1: currentBoard.neighbors()) {
					SearchNode nextNode = new SearchNode(b1, currentNode);
					//if neighbour board == pre board, skip
					if (currentNode.prev != null && nextNode.getBoard().equals(currentNode.prev.getBoard()))
						continue;
					pq.insert(nextNode);
				}
			}

			currentTwinNode = pqt.delMin();
			Board currentTwinBoard = currentTwinNode.getBoard();
			if (currentTwinBoard.isGoal()) break;
			else {
				for (Board b1 : currentTwinBoard.neighbors()) {
					SearchNode nextwinNode = new SearchNode(b1, currentTwinNode);
					if (currentTwinNode.prev != null && nextwinNode.getBoard().equals(currentTwinNode.prev.getBoard()))
						continue;
					pqt.insert(nextwinNode);
				}
			}
		}
	}

	public boolean isSolvable() {
		return currentNode.getBoard().isGoal();
	}
	
	public int moves() {
		if (isSolvable())
			return currentNode.getMoves();
		else 
			return -1;
	}
	
	public Iterable<Board> solution() {
		
		if (isSolvable()) {
			while (currentNode != null) {
				sols.push(currentNode.getBoard());
				currentNode = currentNode.prev;
			}
			return sols;
		} else {
			return null;
		}
	}


	public static void main(String[] args) {

    	// create initial board from file
    	In in = new In(args[0]);
    	int n = in.readInt();
    	int[][] blocks = new int[n][n];
    	for (int i = 0; i < n; i++)
        	for (int j = 0; j < n; j++)
            	blocks[i][j] = in.readInt();
    	Board initial = new Board(blocks);

    	// solve the puzzle
    	Solver solver = new Solver(initial);

    	// print solution to standard output
    	if (!solver.isSolvable())
        	StdOut.println("No solution possible");
    	else {
        	StdOut.println("Minimum number of moves = " + solver.moves());
        	for (Board board : solver.solution())
            	StdOut.println(board);
    	}
	}
}