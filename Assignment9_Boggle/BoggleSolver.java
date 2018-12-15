import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
	private int m, n;
	private char[][] board1;
	private SET<String> valid;
	private Node root;

	private static class Node {
		private boolean isWord;
		private Node[] next = new Node[26];
	}

	private void addDict(String word) {
		if (word == null) throw new java.lang.IllegalArgumentException("word null");
		root = add(root, word, 0);
	}

	private Node add(Node x, String word, int d) {
		if (x == null) x = new Node();
		if (d == word.length()) {
			x.isWord = true;
			return x;
		}

		char c = word.charAt(d);
		x.next[c-'A'] = add(x.next[c-'A'], word, d+1);
		return x;
	}

	private boolean contains(String word) {
		if (word == null) throw new java.lang.IllegalArgumentException("word null");
		Node x = get(root, word, 0);
		if (x == null) return false;
		else
			return x.isWord;
	}

	private Node get(Node x, String word, int d) {
		if (x == null) return null;
		if (d == word.length()) return x;
		char c = word.charAt(d);
		return get(x.next[c-'A'], word, d+1);
	}

	public BoggleSolver(String[] dictionary) {
		root = new Node();
		for (String s : dictionary) {
			addDict(s);
		}
	}

	public Iterable<String> getAllValidWords(BoggleBoard board) {
		m = board.rows();
		n = board.cols();
		board1 = new char[m][n];
		valid = new SET<String>();

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				board1[i][j] = board.getLetter(i, j);
			}
		}

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				boolean[][] marked = new boolean[m][n];
				String word = "";
				dfs(i, j, marked, word, root);
			}
		}

		return valid;
	}

	private void dfs(int x, int y, boolean[][] marked, String word, Node r) {
		char c = board1[x][y];
		Node next = r.next[c-'A'];

		word += c;

		if (c == 'Q' && next != null)
			next = next.next['U'-'A'];

		if (next == null)
			return;

		if (c == 'Q')
			word += 'U';

		if (word.length() > 2 && next.isWord)
			valid.add(word);

		marked[x][y] = true;


		// up left, x-1, y-1
		if (x-1 >= 0 && y-1 >= 0 && !marked[x-1][y-1]) {
			dfs(x-1, y-1, marked, word, next);
		}

		// up, x-1, y
		if (x-1 >= 0 && !marked[x-1][y]) {
			dfs(x-1, y, marked, word, next);
		}

		//up right, x-1, y+1
		if (x-1 >= 0 && y+1 < n && !marked[x-1][y+1]) {
			dfs(x-1, y+1, marked, word, next);
		}

		//left, x, y-1
		if (y-1 >= 0 && !marked[x][y-1]) {
			dfs(x, y-1, marked, word, next);
		}

		//right, x, y+1
		if (y+1 < n && !marked[x][y+1]) {
			dfs(x, y+1, marked, word, next);
		}

		//down left, x+1, y-1
		if (x+1 < m && y-1 >= 0 && !marked[x+1][y-1]) {
			dfs(x+1, y-1, marked, word, next);
		}

		//down, x+1, y
		if (x+1 < m && !marked[x+1][y]) {
			dfs(x+1, y, marked, word, next);
		}

		//down right, x+1, y+1
		if (x+1 < m && y+1 < n && !marked[x+1][y+1]) {
			dfs(x+1, y+1, marked, word, next);
		}

		marked[x][y] = false;

		return;
	}

	public int scoreOf(String word) {
		if (word == null) throw new java.lang.IllegalArgumentException("word null");
		if (!contains(word)) return 0;
		if (word.length() >= 0 && word.length() <= 2) return 0;
		if (word.length() >= 3 && word.length() <= 4) return 1;
		if (word.length() == 5) return 2;
		if (word.length() == 6) return 3;
		if (word.length() == 7) return 5;
		if (word.length() >= 8) return 11;

		return 0;
	}

	public static void main(String[] args) {
    	In in = new In(args[0]);
    	String[] dictionary = in.readAllStrings();
    	BoggleSolver solver = new BoggleSolver(dictionary);
    	BoggleBoard board = new BoggleBoard(args[1]);
    	int score = 0;
    	for (String word : solver.getAllValidWords(board)) {
        	StdOut.println(word);
        	score += solver.scoreOf(word);
    	}
    	StdOut.println("Score = " + score);
	}
}