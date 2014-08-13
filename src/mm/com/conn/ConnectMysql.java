package mm.com.conn;

import java.sql.*;

public class ConnectMysql {

	static Connection conn;

	private ConnectMysql() {

	}

	private static void connect() {
		try {
			String url = "jdbc:mysql://localhost/prinote";
			String user = "root";
			String pwd = "123";

			// ???è½½é©±???ï¼?è¿?ä¸???¥ä????????ä¸ºï??Class.forName("com.mysql.jdbc.Driver");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// å»ºç?????MySQL???è¿????
			conn = DriverManager.getConnection(url, user, pwd);

			// return query();
		} catch (Exception ex) {
			System.out.println("Error : " + ex.toString());
		}

	}

	public static String query() throws SQLException {

		if (conn == null)
			connect();
		// ??§è??SQLè¯????
		Statement stmt = conn.createStatement();// ???å»ºè????¥å?¹è±¡ï¼???¨ä»¥??§è??sqlè¯?è¨?
		ResultSet rs = stmt.executeQuery("select * from note");
		String res = "res: ";
		// å¤????ç»???????
		while (rs.next()) {
			String name = rs.getString("digest");
			res = res + "digest : " + name + "  id:" + rs.getInt("_id")
					+ "  content:" + rs.getString("content");
		}
		rs.close();
		// conn.close();

		return res;
	}

	public static String query(String sql) throws SQLException {
		if (conn == null)
			connect();
		// ??§è??SQLè¯????
		Statement stmt = conn.createStatement();// ???å»ºè????¥å?¹è±¡ï¼???¨ä»¥??§è??sqlè¯?è¨?
		ResultSet rs = stmt.executeQuery("select * from note");
		String res = "{ \"records\":[";

		boolean more_than_1 = false;
		// å¤????ç»???????
		while (rs.next()) {
			if (more_than_1) {
				res += ",";
			}

			more_than_1 = true;

			res += "{ \"digest\":" + "\"" + rs.getString("digest") + "\","
					+ "\"_id\":" + "\"" + rs.getInt("_id") + "\","
					+ "\"content\":" + "\"" + rs.getString("content") + "\","
					+ "\"create_data_time\":" + "\""+ rs.getString("create_date_time") + "\"" + "}";

		}

		res += "]}";

		rs.close();
		// conn.close();

		return res;
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		if (conn != null) {
			conn.close();
			conn = null;
		}
	}

	public static String getTestString() {
		return " test string";
	}

}
