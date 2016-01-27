package sentiment;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.util.CoreMap;
import rita.support.PorterStemmer;

public class temp {

	static String stemTerm (String term) {
	    PorterStemmer stemmer = new PorterStemmer();
	    return stemmer.stem(term);
	}
	
	protected StanfordCoreNLP pipeline;

    public temp() {
        // Create StanfordCoreNLP object properties, with POS tagging
        // (required for lemmatization), and lemmatization
        Properties props;
        props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma");
        
        /*
         * This is a pipeline that takes in a string and returns various analyzed linguistic forms. 
         * The String is tokenized via a tokenizer (such as PTBTokenizerAnnotator), 
         * and then other sequence model style annotation can be used to add things like lemmas, 
         * POS tags, and named entities. These are returned as a list of CoreLabels. 
         * Other analysis components build and store parse trees, dependency graphs, etc. 
         * 
         * This class is designed to apply multiple Annotators to an Annotation. 
         * The idea is that you first build up the pipeline by adding Annotators, 
         * and then you take the objects you wish to annotate and pass them in and 
         * get in return a fully annotated object.
         * 
         *  StanfordCoreNLP loads a lot of models, so you probably
         *  only want to do this once per execution
         */
        this.pipeline = new StanfordCoreNLP(props);
    }

    public String lemmatize(String documentText)
    {
        String lemmas = "";
        // Create an empty Annotation just with the given text
        Annotation document = new Annotation(documentText);
        // run all Annotators on this text
        this.pipeline.annotate(document);
        // Iterate over all of the sentences found
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            // Iterate over all tokens in a sentence
            for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
                // Retrieve and add the lemma for each word into the
                // list of lemmas
                lemmas+=(token.get(LemmaAnnotation.class)).toString();
                lemmas+=" ";
            }
        }
        return lemmas.toString();
    }
	
	public static void main(String[] args) throws ClassNotFoundException, IOException {
		// TODO Auto-generated method stub
		
		String text = "This World is an amazing place";
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Annotation annotation = pipeline.process(text);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            System.out.println(sentiment + "\t" + sentence);
        }
		
		
		String s = " I don't likes walking by walk to be cats teased";
		
		s=s.replaceAll("n't", " not ");
		s=stemTerm(s);
		System.out.println(s);
		s=s.replaceAll("[^a-zA-Z0-9 '-]" , "");
		temp slem = new temp();
        s=slem.lemmatize(s);
        System.out.println(s);
        
        s=s.substring(0, s.length()-1); 
        
		MaxentTagger tagger = new MaxentTagger("models/wsj-0-18-bidirectional-distsim.tagger");
		String taggeds = tagger.tagString(s);
		System.out.println(taggeds);
		StringTokenizer st = new StringTokenizer(s);
		while (st.hasMoreTokens()) {
			String term = st.nextToken();
			System.out.println(term);
		}
		
	}

}
