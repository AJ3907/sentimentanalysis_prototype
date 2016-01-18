package sentiment;

import java.io.IOException;
import java.util.StringTokenizer;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class temp {

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		String s = "While comparison get u a present.";
		s=s.replaceAll("[^a-zA-Z0-9 '-]" , "");
		MaxentTagger tagger = new MaxentTagger("taggers/bidirectional-distsim-wsj-0-18.tagger");
		String taggeds = tagger.tagString(s);
		System.out.println(taggeds);
		StringTokenizer st = new StringTokenizer(s);
		while (st.hasMoreTokens()) {
			String term = st.nextToken();
			System.out.println(term);
		}
		
	}

}
