//step 2
//now tagging the sentences using stanFord post tagger
package sentiment;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class TagText {

	static void findPOC(String s, Long sentenceId) throws ClassNotFoundException, SQLException {

		String[] Stringparts = s.split("/");

		Connection conn = null;
		Statement stmt = null;
		java.sql.PreparedStatement ps = null;

		Config config = new Config();
		conn = config.getConnection();
		
		stmt = (Statement) conn.createStatement();

		String sql = "INSERT INTO tagwords VALUES(NULL,?,?,?)";
		ps = conn.prepareStatement(sql);
		ps.setString(1, Stringparts[0]);
		ps.setString(2, Stringparts[1]);
		ps.setLong(3, sentenceId);
		ps.executeUpdate();
		stmt.close();
		conn.close();
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {

		// Initialize the tagger
		MaxentTagger tagger = new MaxentTagger("taggers/bidirectional-distsim-wsj-0-18.tagger");

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		Config config = new Config();
		conn = config.getConnection();
		stmt = (Statement) conn.createStatement();

		String sql = "SELECT id,sentence from reviewsentence where reviewId>5700";
		rs = stmt.executeQuery(sql);

		String reviewsentence;
		String taggedReview;
		Long id;
		while (rs.next()) {
			id = rs.getLong("id");
			reviewsentence = rs.getString("sentence");
			reviewsentence = reviewsentence.replaceAll("[^a-zA-Z0-9 '-]", " ");
			taggedReview = tagger.tagString(reviewsentence);
			StringTokenizer st = new StringTokenizer(taggedReview);
			while (st.hasMoreTokens()) {
				String term = st.nextToken();
				System.out.println(term);
				findPOC(term, id);
			}
		}

	}
}
