package sentiment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.mysql.fabric.xmlrpc.base.Array;
import com.mysql.jdbc.Statement;

public class pruning {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		Config config = new Config();
		
		String sentence;
		Long sentenceId;
		FileInputStream fstream;
		BufferedReader br;
		String productId="B000233WJ6";
		HashMap<String,Integer> hm=new HashMap();
		BufferedWriter writer=null;
		writer = new BufferedWriter(new FileWriter(config.getFilePath()+"cp"+ productId + ".txt"));
		fstream = new FileInputStream(config.getFilePath()+"output"+productId+".txt");
		
		br = new BufferedReader(new InputStreamReader(fstream)); 
	    String line;
	    int count=0;
	    while ((line = br.readLine()) != null) {
			   // process the line.
				if(line.contains(" ")){
					//means it has more than two words.
					//line = line.split(":")[0];
					hm.put(line, 0);
					count++;
					//System.out.println(line);
				}
				else{
					writer.write(line);
					writer.newLine();
				}
		}
	    System.out.println(count);
	    fstream.close();
	    count=0;
	    fstream = new FileInputStream(config.getFilePath()+"temp"+productId+".txt");
		
		br = new BufferedReader(new InputStreamReader(fstream)); 
	    while ((line = br.readLine()) != null) {
			   // process the line.
	    	
	    	
	    	Set set = hm.entrySet();
		      // Get an iterator
		    Iterator i = set.iterator();
		   // System.out.println(hm.size());
		      // Display elements
		    while(i.hasNext()) {
		         Map.Entry<String,Integer> me = (Map.Entry)i.next();
		         if(processLine(hm, line, me.getKey())==3){
		        	 System.out.println(me.getKey()+me.getValue());
		        	 count++;
		        	 writer.write(me.getKey());
		        	 writer.newLine();
		        	 i.remove();
		         }
		        	
		         //System.out.println(line+"::"+me.getKey());
		     }
		    
		    
				
		}
		
	   writer.close();
		System.out.println(count);
		
	}
	
	private static int processLine(HashMap<String,Integer> hm, String line,String phrase){
		String pattern="";
		
		
		
		//split phrase into words.
		String temp[] = line.split("~");
		
		line = temp[0];
		
		Long sentenceId = Long.parseLong(temp[1]);
		String words[]=(phrase.split(":")[0]).split(" ");
		String blocks[] = line.split("\\.");
		
		
		int m = words.length;
		int n = blocks.length;
		
		Long[] wids = new Long[m];
		int count=0;
		for(int i=0;i<n;i++){
			count=0;
			String ids = "";
			if(blocks[i].contains(" ")){
				
				String s[] = blocks[i].split(" ");
				
				int l = s.length;
				for(int j=0;j<l;j++){
					
					//here we get "word,wordId"
					String w = s[j].split(",")[0];
					String id = s[j].split(",")[1];
					int flag=0 ;

					for(int k=0;k<m;k++){
						if(w.equals(words[k])){
							
							if(wids[k]==null)
								count++;
							wids[k]=Long.parseLong(id);
							
							if(count==m){
								
								for(int q=0;q<m-1;q++){
									if(wids[q+1]-wids[q]>3 || wids[q]-wids[q+1]>3)
										flag=1;	
								}
								if(flag==0){
									i=n;
									break;
								}
							}
							
						}
							
					}
					
				}
				if(count==m){
					
					for(int q=0;q<m-1;q++){
						if(wids[q+1]-wids[q]>3 || wids[q]-wids[q+1]>3)
							return 2;	
					}
					
					int val = hm.get(phrase);
					//System.out.println(val);
					hm.put(phrase, val+1);
					if(val==1)
						return 3;
					return 0;
					
				}
				
				
				
			}
			
		}
		return 1;	
		
			}

}
