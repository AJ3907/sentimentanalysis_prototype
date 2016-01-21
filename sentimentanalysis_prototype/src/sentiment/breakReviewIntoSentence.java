//step 1
//breaking review into sentences because post tagger takes input sentence wise
package sentiment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.BreakIterator;
import java.util.Locale;

public class breakReviewIntoSentence {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		java.sql.PreparedStatement ps = null;

		Config config = new Config();
		conn = config.getConnection();
		
		stmt = (Statement) conn.createStatement();

		String sql = "SELECT id,reviewText from electronics_review where prodId="+'"'+"B000123CG3"+'"';
		rs = stmt.executeQuery(sql);

		Long reviewId;
		String reviewText;
		while (rs.next()) {
			reviewId = rs.getLong("id");
			reviewText = rs.getString("reviewText").toUpperCase();
			
			BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.US);
			iterator.setText(reviewText);
			int start = iterator.first();
			for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
				sql = "INSERT INTO reviewsentence VALUES(NULL,?,?)";
				ps = conn.prepareStatement(sql);
				System.out.println("review id =" + reviewId);
				System.out.println("senetnce = " + reviewText.substring(start, end));

				ps.setLong(1, reviewId);
				ps.setString(2, reviewText.substring(start, end).toLowerCase());
				ps.executeUpdate();

			}
		}
		stmt.close();
		conn.close();
	}
}
