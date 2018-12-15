import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
	private WordNet wordnet;

	public Outcast(WordNet wordnet) {
		this.wordnet = wordnet;
	} 

	public String outcast (String[] nouns) {
		String selected = nouns[0];
		int dis = -1;

		for (int i = 0; i < nouns.length; i++) {
			int temp_dis = 0;
			for (int j = 0; j < nouns.length; j++) {
				temp_dis += wordnet.distance(nouns[i], nouns[j]);
			}

			if (temp_dis > dis) {
				dis = temp_dis;
				selected = nouns[i];
			}
		}

		return selected;
	}

	public static void main(String[] args) {
    	WordNet wordnet = new WordNet(args[0], args[1]);
    	Outcast outcast = new Outcast(wordnet);
    	for (int t = 2; t < args.length; t++) {
        	In in = new In(args[t]);
        	String[] nouns = in.readAllStrings();
        	StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    	}
	}
}