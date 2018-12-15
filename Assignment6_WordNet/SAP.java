import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;

public class SAP {
	private final Digraph G;

	public SAP(Digraph G) {
		if (G == null) throw new java.lang.IllegalArgumentException("G null");
		this.G = new Digraph(G);
	}

	public int length(int v, int w) {
		validateVertex(v);
		validateVertex(w);

		BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);

		int leng = Integer.MAX_VALUE;
		int anc = -1;

		for (int i = 0; i < G.V(); i++) {
			if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
				int tempLen = bfsv.distTo(i) + bfsw.distTo(i);
				if (tempLen < leng) {
					anc = i;
					leng = tempLen;
				}
			}
		}

		if (anc == -1)
			return -1;

		return leng;
	}

	public int ancestor(int v, int w) {
		//vertices v can arrive
		validateVertex(v);
		validateVertex(w);

		BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);

		int leng = Integer.MAX_VALUE;
		int anc = -1;

		for (int i = 0; i < G.V(); i++) {
			if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
				int tempLen = bfsv.distTo(i) + bfsw.distTo(i);
				if (tempLen < leng) {
					anc = i;
					leng = tempLen;
				}
			}
		}

		return anc;
	}
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		if (v == null || w == null) throw new java.lang.IllegalArgumentException("v or w null");
		validateIter(v);
		validateIter(w);

		BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);

		int leng = Integer.MAX_VALUE;
		int anc = -1;

		for (int i = 0; i < G.V(); i++) {
			if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
				int tempLen = bfsv.distTo(i) + bfsw.distTo(i);
				if (tempLen < leng) {
					anc = i;
					leng = tempLen;
				}
			}
		}

		if (anc == -1)
			return -1;

		return leng;
	}
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		if (v == null || w == null) throw new java.lang.IllegalArgumentException("v or w null");
		validateIter(v);
		validateIter(w);

		BreadthFirstDirectedPaths bfsv = new BreadthFirstDirectedPaths(G, v);
		BreadthFirstDirectedPaths bfsw = new BreadthFirstDirectedPaths(G, w);

		int leng = Integer.MAX_VALUE;
		int anc = -1;

		for (int i = 0; i < G.V(); i++) {
			if (bfsv.hasPathTo(i) && bfsw.hasPathTo(i)) {
				int tempLen = bfsv.distTo(i) + bfsw.distTo(i);
				if (tempLen < leng) {
					anc = i;
					leng = tempLen;
				}
			}
		}

		return anc;
	}

	private void validateVertex(int v) {
		int V = G.V();
		if (v < 0 || v >= V) {
			throw new java.lang.IllegalArgumentException("out of range");
		}
	}

	private void validateIter(Iterable<Integer> v) {
		for (Integer i : v) {
			if (i == null)
				throw new java.lang.IllegalArgumentException("null element");
		}
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
    	Digraph G = new Digraph(in);
    	SAP sap = new SAP(G);
    	while (!StdIn.isEmpty()) {
        	int v = StdIn.readInt();
        	int w = StdIn.readInt();
        	int length   = sap.length(v, w);
        	int ancestor = sap.ancestor(v, w);
        	StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    	}
	}
}
	
