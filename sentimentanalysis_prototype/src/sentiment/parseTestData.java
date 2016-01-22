package sentiment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class parseTestData {

	public static void main(String[] args) throws IOException, SQLException {
		// TODO Auto-generated method stub
		
		Connection conn=null;
		Statement stmt=null;
		PreparedStatement ps = null;
		
		Config config = new Config();
		conn=config.getConnection();
		
		FileInputStream fstream;
		BufferedReader br;
		
		fstream = new FileInputStream(config.getTestDataPath()+"Canon G3"+".txt");
		
		br = new BufferedReader(new InputStreamReader(fstream)); 
	    String line;
	    int count=0;
	    
	    int reviewNum;
	    
	    String summary="";
	    String text="";
	    String productId="B000123CG3";
	    String sql = "INSERT INTO electronics_review VALUES(?,?,?,?,?,?,?,?,?,?,?)";
	    ps = conn.prepareStatement(sql);
	    HashMap<String, Integer> hm = new HashMap<>();
	    while ((line = br.readLine()) != null) {
	    	
	    	
	    	if(line.contains("[t]")){
	    		count++;
	    		if(count>=2){
	    			//add review to database.
	    			ps.setString(1, null);
	    			ps.setString(2, productId);
	    			ps.setString(3, null);
	    			ps.setInt(4, 0);
	    			ps.setInt(5, 0);
	    			ps.setString(6, text);
	    			ps.setDouble(7, 0);
	    			ps.setString(8, summary);
	    			ps.setInt(9, 0);
	    			ps.setString(10, null);
	    			ps.setInt(11, 5700+count-1);
	    			//ps.executeUpdate();
	    			//System.out.println(summary);
	    		}
	    		//for the next review.
	    		summary = line.replace("[t]", "");
	    		text="";
	    	}
	    	else{
	    		if(line.contains("##")){
	    			text+=line.split("##")[1];
	    			String[] features;
	    			if((line.split("##")[0]).contains(", "))
	    				features=(line.split("##")[0]).split(", ");
	    			else
	    				features=(line.split("##")[0]).split(",");
	    			for(int i=0;i<features.length;i++){
	    				if(!hm.containsKey(features[i].split("\\[")[0]))
	    					hm.put(features[i].split("\\[")[0], 0);
	    			}
	    			
	    		}
	    			
	    	}
	   
		}
	    
	    Set set = hm.entrySet();
	      // Get an iterator
	    Iterator i = set.iterator();
	   // System.out.println(hm.size());
	      // Display elements
	    count=0;
	    while(i.hasNext()) {
	         Map.Entry<String,Integer> me = (Map.Entry)i.next();
	         if(!me.getKey().equals("")){
	        	 System.out.println(me.getKey());
		         count++;
	         }
	        	
	        	
	         //System.out.println(line+"::"+me.getKey());
	     }
	    System.out.println(count);
	    fstream.close();
		
		
		
		
		
		

	}

}
