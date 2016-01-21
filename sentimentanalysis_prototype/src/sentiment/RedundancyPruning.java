package sentiment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RedundancyPruning {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Config config = new Config();
		FileInputStream fstream;
		BufferedReader br;
		String productId="B000123CG3";
		
		ArrayList<String> featurelist = new ArrayList<String>();
		HashMap<String, Integer> hm = new HashMap<>();
		BufferedWriter writer=null;
		writer = new BufferedWriter(new FileWriter(config.getFilePath()+"rp"+ productId + ".txt"));
		fstream = new FileInputStream(config.getFilePath()+"cp"+productId+".txt");
		br = new BufferedReader(new InputStreamReader(fstream)); 
	    String line;
	   
	    while ((line = br.readLine()) != null) {
			   // process the line.
	    	featurelist.add(line.split(":")[0]);
	    	hm.put(line.split(":")[0], Integer.parseInt(line.split(":")[1]));
		}
	    
	    int n = featurelist.size();
	    
	    for(int i=0;i<n;i++){
	    
	    	String[] s=featurelist.get(i).split(" ");
	    	int l = s.length;
	    	String regex="";
	    	for(int k=0;k<l;k++){
	    		regex+="(?=.*("+s[k]+"( |)))";
	    	}
	    	regex+="[^\\n]+";
	    	Pattern r = Pattern.compile(regex);
	    	
	    	for(int j=0;j<n;j++){
	    		if(j!=i){
	    		//	
	    			Matcher m = r.matcher(featurelist.get(j));
	    			
	    			if(m.find()){
	    				int p = hm.get(featurelist.get(i));
	    				int q = hm.get(featurelist.get(j));
	    				
	    				hm.put(featurelist.get(i),p-q);
	    			}
	    		}
	    	
	    	}
	    }
	    
	    Set set = hm.entrySet();
	      // Get an iterator
	    Iterator i = set.iterator();
	   // System.out.println(hm.size());
	      // Display elements
	    int count=0;
	    while(i.hasNext()) {
	         Map.Entry<String,Integer> me = (Map.Entry)i.next();
	         if(me.getValue()>=3){
	         		//	System.out.println(me.getKey()+me.getValue());
	        	 	writer.write(me.getKey()+":"+me.getValue());
	        	 	writer.newLine();
	         			count++;
	         }
	         else{
	        	 System.out.println(me.getKey());
	         }
	        	
	         //System.out.println(line+"::"+me.getKey());
	     }
	    
	    System.out.println(count);
	    writer.close();
	    fstream.close();
		
		
		
		
	}

}
