import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Digraph;
import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

public class WordNet {
	//w2id used to implement nouns and is noun method.
	private LinearProbingHashST<String, ArrayList<Integer>> w2id;
	//Also maintain id2w for sap method.
	private LinearProbingHashST<Integer, String> id2w;
	private Digraph G;
	private SAP _sap;

	public WordNet(String synsets, String hypernyms) {
		if (synsets == null || hypernyms == null) 
			throw new java.lang.IllegalArgumentException("null arugement");
		In sysIn = new In(synsets);
		In hypIn = new In(hypernyms);

		w2id = new LinearProbingHashST<String, ArrayList<Integer>>();
		id2w = new LinearProbingHashST<Integer, String>();

		//read the sysnsets, construct the map between words and ids.
		//Also initilize the graph with V vertices.
		int count = 0;
		while(!sysIn.isEmpty()) {
			String line = sysIn.readLine();
			String[] fileds = line.split(",");
			int id = Integer.parseInt(fileds[0]);
			String[] words = fileds[1].split(" ");
			ArrayList<Integer> ids;

			id2w.put(id, fileds[1]);

			for (int i = 0; i < words.length; i++) {
				if (w2id.contains(words[i]))
					ids = w2id.get(words[i]);
				else 
					ids = new ArrayList<Integer>();
				ids.add(id);
				w2id.put(words[i], ids);
			}

			count++;
		}

		G = new Digraph(count);

		//read the hypernyms, construct the graph via read ids.
		//i.e. add edges to the graph.
		while(!hypIn.isEmpty()) {
			String line = hypIn.readLine();
			String[] fileds = line.split(",");
			int v = Integer.parseInt(fileds[0]);
			for (int i = 1; i < fileds.length; i++) {
				int w = Integer.parseInt(fileds[i]);
				G.addEdge(v, w);
			}
		}

		_sap = new SAP(G);

		Topological topo = new Topological(G);
		if (!topo.hasOrder())
			throw new java.lang.IllegalArgumentException("not a DAG");

		int num_root = 0;
		for (int v = 0; v < G.V(); v++) {
			if (G.outdegree(v) == 0) {
				num_root++;
			}
		}

		if (num_root > 1) throw new java.lang.IllegalArgumentException("more than one root");

		//System.out.println(G.V());
		//System.out.println(G.E());
	}

	public Iterable<String> nouns() {
		return w2id.keys();
	}

	public boolean isNoun(String word) {
		if (word == null) throw new java.lang.IllegalArgumentException("word is null");
		return w2id.contains(word);
	}

	public int distance(String nounA, String nounB) {
		if (!isNoun(nounA)) throw new java.lang.IllegalArgumentException("noun not in wordnet nouns");
		if (!isNoun(nounB)) throw new java.lang.IllegalArgumentException("noun not in wordnet nouns");

		return _sap.length(w2id.get(nounA), w2id.get(nounB));
	}
	
	public String sap(String nounA, String nounB) {
		if (!isNoun(nounA)) throw new java.lang.IllegalArgumentException("noun not in wordnet nouns");
		if (!isNoun(nounB)) throw new java.lang.IllegalArgumentException("noun not in wordnet nouns");

		int anc = _sap.ancestor(w2id.get(nounA), w2id.get(nounB));
		return id2w.get(anc);
	}
	public static void main(String[] args) {
		WordNet w = new WordNet("synsets.txt", "hypernyms.txt");
		System.out.println(w.sap("worm", "bird"));
		System.out.println(w.distance("worm", "bird"));
		System.out.println(w.distance("municipality", "region"));
		System.out.println(w.sap("individual", "edible_fruit"));
		System.out.println(w.distance("white_marlin", "mileage"));
		System.out.println(w.distance("Black_Plague", "black_marlin"));
		System.out.println(w.distance("American_water_spaniel", "histology"));
		System.out.println(w.distance("Brown_Swiss", "barrel_roll"));

		int i = 0;
		for (String s : w.nouns()) {
			i++;
		}
		System.out.println(i);
	}
}
