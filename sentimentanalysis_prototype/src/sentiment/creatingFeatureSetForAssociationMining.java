//step 3
//generating set of nouns for doing association mining
package sentiment;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.xml.sax.InputSource;

import rita.wordnet.jwnl.util.factory.Createable;
public class creatingFeatureSetForAssociationMining {
	 
	 public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
		  Connection conn = null;
		  Statement stmt = null, stmt2 = null;
		  ResultSet rs = null, rs2 = null;
		  
		  Config config = new Config();
		  conn = config.getConnection();
	 
		  // stmt = (Statement) conn.createStatement();
		  stmt2 = (Statement) conn.createStatement();
		  // String sql = "SELECT distinct prodId FROM electronics_review";
		  //rs = stmt.executeQuery(sql);
		  String productId = "B000123CG3";
		 // while (rs.next()) {
		   BufferedWriter writer,writer2 = null;
		  // productId = rs.getString("prodId");
		  String sql = "Select w.sentenceId,w.word,w.id from electronics_review e "
		  			+ "inner join (SELECT r.reviewId,t.sentenceId,t.word,t.id FROM tagwords t "
		  			+ "INNER JOIN reviewsentence r on t.sentenceId = r.id and (t.posTag='NN' OR t.posTag='NNS')) w on w.reviewId = e.id and e.prodId="
		  			+ "'" + productId + "'";
		   rs2 = stmt2.executeQuery(sql);
		   writer = new BufferedWriter(new FileWriter(config.getFilePath()+"input"+ productId + ".txt"));
		   writer2 = new BufferedWriter(new FileWriter(config.getFilePath()+"temp"+ productId + ".txt"));
		   String word = "";
		   Long prevsentenceId = -99l, sentenceId;
		   StringBuilder buffer = new StringBuilder();
		   String out ="";
		   Long prevwordId = -99L,wordId;
		   while (rs2.next()) {
			    sentenceId = rs2.getLong("sentenceId");
			    word = rs2.getString("word");
			    word = word.toLowerCase();
			    
			    wordId = rs2.getLong("id");
			    
			    if (sentenceId > prevsentenceId) {
			     if(prevsentenceId!=-99L){
			    	 writer.write(buffer.toString());
				     out=out+"~"+prevsentenceId;
				     System.out.println(out);
				     writer2.write(out);
				     
			     }

			    // System.out.println(sentenceId + " text :"+out + prevsentenceId);
			     if(prevsentenceId!=-99l){
			    	 writer.newLine(); 
				     writer2.newLine();
			     }
			      
			     
			     out="";
			    // out="";
			     buffer = new StringBuilder();
			    }
			    
			    if((wordId-prevwordId)<=3l && (sentenceId-prevsentenceId)<1l){
			    	out = out + " "+word+","+wordId;
			    	
			    }
			    else
			    	out=out+"."+word+","+wordId;

			    if (!buffer.toString().contains(word)) {
			     buffer.append(word + " ");
			    }
			    prevwordId = wordId;
			    prevsentenceId = sentenceId;
			    
		   }
		   writer.write(buffer.toString());
		   out=out+"~"+prevsentenceId;
		     System.out.println(out);
		     writer2.write(out);
		     writer2.close();
		   if (writer != null)
		    writer.close();
		//  }
		 // stmt.close();
		  conn.close();
	 }
}