import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.FlowEdge;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
	private LinearProbingHashST<String, Integer> t2i;
	private LinearProbingHashST<Integer, String> i2t;
	private int N;
	private int[] w;
	private int[] l;
	private int[] r;
	private int[][] g;
	private ArrayList<Integer> minCut;
	private ArrayList<String> minCutS;

	public BaseballElimination(String filename) {
		In in = new In(filename);
		N = in.readInt();
		w = new int[N];
		l = new int[N];
		r = new int[N];
		g = new int[N][N];
		t2i = new LinearProbingHashST<String, Integer>();
		i2t = new LinearProbingHashST<Integer, String>();
		minCut = new ArrayList<Integer>();
		minCutS = new ArrayList<String>();

		for (int i = 0; i < N; i++) {
			String nt = in.readString();
			t2i.put(nt, i);
			i2t.put(i, nt);
			w[i] = in.readInt();
			l[i] = in.readInt();
			r[i] = in.readInt();

			for (int j = 0; j < N; j++) {
				g[i][j] = in.readInt();
			}
		}

	}

	public int numberOfTeams() {
		return t2i.size();
	}

	public Iterable<String> teams() {
		return t2i.keys();
	}

	private void validateTeam(String team) {
		if (team == null) throw new java.lang.IllegalArgumentException("team null");
		if (!t2i.contains(team)) throw new java.lang.IllegalArgumentException("team not in");
	}

	public int wins(String team) {
		validateTeam(team);
		return w[t2i.get(team)];
	}

	public int losses(String team) {
		validateTeam(team);
		return l[t2i.get(team)];
	}

	public int remaining(String team) {
		validateTeam(team);
		return r[t2i.get(team)];
	}

	public int against(String team1, String team2) {
		validateTeam(team1);
		validateTeam(team2);
		return g[t2i.get(team1)][t2i.get(team2)];
	}
	
	public boolean isEliminated(String team) {
		validateTeam(team);

		minCut.clear();
		minCutS.clear();

		int s = 0;
		int x = t2i.get(team);
		int team_start = (N-1)*(N-2)/2+1;
		int t = (N-1)*(N-2)/2 + N;
		int ii =0, jj = 0;
		int k = 1;

		int V = 2 + (N-1)*(N-2)/2 + N-1;
		FlowNetwork flowG = new FlowNetwork(V);

		for (int i = 0; i < N; i++) {
			if (i == x) continue;
			jj = ii+1;
			for (int j = i+1; j < N; j++) {
				if (j == x) continue;

				FlowEdge e1 = new FlowEdge(0, k, g[i][j]);
				flowG.addEdge(e1);
				FlowEdge e2 = new FlowEdge(k, ii+team_start, Double.POSITIVE_INFINITY);
				flowG.addEdge(e2);
				FlowEdge e3 = new FlowEdge(k, jj+team_start, Double.POSITIVE_INFINITY);
				flowG.addEdge(e3);

				k++;
				jj++;
			}
			ii++;
		}

		ii = 0;
		for (int i = 0; i < N; i++) {
			if (i == x) continue;
			if (w[x]+r[x] < w[i]) {
				minCut.add(i);
				return true;
			} 
			FlowEdge e4 = new FlowEdge(ii+team_start, t, w[x]+r[x]-w[i]);
			flowG.addEdge(e4);
			ii++;
			//StdOut.println(e4);
		}

		//StdOut.println(flowG);

		FordFulkerson maxflow = new FordFulkerson(flowG, s, t);

		//StdOut.println(flowG);

		ii = 0;
		for (int i = 0; i < N; i++) {
			if (i == x) continue;
			if (maxflow.inCut(team_start+ii)) {
				minCut.add(i);
			}
			ii++;
		}

		for (FlowEdge e: flowG.adj(s)) {
			int v = e.from();
			int w = e.to();

			if (e.residualCapacityTo(w) != 0)
				return true;
		}


		//StdOut.println(maxflow.value());
		
		return false;
	}
	
	public Iterable<String> certificateOfElimination(String team) {
		//minCut.clear();
		//minCutS.clear();
		validateTeam(team);

		if (!isEliminated(team))
			return null;
		
		for (int i : minCut) {
			minCutS.add(i2t.get(i));
		} 

		//StdOut.println(minCutS.size());
		//StdOut.println(minCut.size());

		return minCutS;
	}

	public static void main(String[] args) {
    	BaseballElimination division = new BaseballElimination(args[0]);
    	
    	for (String team : division.teams()) {
        	if (division.isEliminated(team)) {
            	StdOut.print(team + " is eliminated by the subset R = { ");
            	for (String t : division.certificateOfElimination(team)) {
                	StdOut.print(t + " ");
            	}
            	StdOut.println("}");
        	}
        	else {
            	StdOut.println(team + " is not eliminated");
        	}
    	}
    	
    	
    	//division.isEliminated("Ghaddafi");
	}
}