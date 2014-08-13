package mm.com;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ConnectMysql {

	static Connection conn;

	static {

		connect();

	}

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

//	public static String query() throws SQLException {
//
//		if (conn == null)
//			connect();
//		// ??§è??SQLè¯????
//		Statement stmt = conn.createStatement();// ???å»ºè????¥å?¹è±¡ï¼???¨ä»¥??§è??sqlè¯?è¨?
//		ResultSet rs = stmt.executeQuery("select * from note limit 0,7");
//		String res = "res: ";
//		// å¤????ç»???????
//		while (rs.next()) {
//			String name = rs.getString("digest");
//			res = res + "digest : " + name + "  id:" + rs.getInt("_id")
//					+ "  content:" + rs.getString("content");
//		}
//		rs.close();
//		// conn.close();
//
//		return res;
//	}

	public static String query( ) throws SQLException {
		if (conn == null)
			connect();
		// ??§è??SQLè¯????
		Statement stmt = conn.createStatement();// ???å»ºè????¥å?¹è±¡ï¼???¨ä»¥??§è??sqlè¯?è¨?
		ResultSet rs = stmt.executeQuery("select * from note order by update_date_time desc limit 0,7");

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
					+ "\"create_date_time\":" + "\""
					+ rs.getString("create_date_time") + "\","
					+ "\"update_date_time\":" + "\""
					+ rs.getString("update_date_time") + "\" }";

		}

		res += "]}";

		rs.close();
		// conn.close();

		return res;
	}

	public static String query(int  limit) throws SQLException {
		if (conn == null)
			connect();
		// ??§è??SQLè¯????
		Statement stmt = conn.createStatement();// ???å»ºè????¥å?¹è±¡ï¼???¨ä»¥??§è??sqlè¯?è¨?
		String sql = "select * from note order by update_date_time desc limit "+ limit+",5";
		System.out.println("sql:" + sql);
		ResultSet rs = stmt.executeQuery(sql);

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
					+ "\"create_date_time\":" + "\""
					+ rs.getString("create_date_time") + "\","
					+ "\"update_date_time\":" + "\""
					+ rs.getString("update_date_time") + "\" }";
		}

		res += "]}";

		rs.close();
		// conn.close();

		return res;
	}
	public static String queryAsJsonString(String id) throws SQLException {
		if (conn == null)
			connect();
		// ??§è??SQLè¯????
		Statement stmt = conn.createStatement();// ???å»ºè????¥å?¹è±¡ï¼???¨ä»¥??§è??sqlè¯?è¨?
		ResultSet rs = stmt.executeQuery("select * from note where _id=" + id);
		String res = "";

		if (rs.next()) {
			res += "{ \"digest\":" + "\"" + rs.getString("digest") + "\","
					+ "\"_id\":" + "\"" + rs.getInt("_id") + "\","
					+ "\"content\":" + "\"" + rs.getString("content") + "\","
					+ "\"create_date_time\":" + "\""
					+ rs.getString("create_date_time") + "\","
					+ "\"update_date_time\":" + "\""
					+ rs.getString("update_date_time") + "\" }";

		}

		rs.close();
		// conn.close();

		return res;
	}

	public static void insert(Map<String, String> vals) {
		StringBuilder pre = new StringBuilder();
		StringBuilder values = new StringBuilder();
		pre.append("insert into note(");

		for (String key : vals.keySet()) {
			pre.append(key).append(",");
			values.append("'").append(vals.get(key)).append("',");
		}
		pre.deleteCharAt(pre.length() - 1);
		values.deleteCharAt(values.length() - 1);

		pre.append(")").append(" values(").append(values).append(")");

		String sql = pre.toString();

		try {
			Statement sm = conn.createStatement();
			int insert_ret = sm.executeUpdate(sql);
			System.out.println("insert ret:" + insert_ret);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("sql: " + sql);

	}

	public static int update(Map<String, String> vals, String id) {

		int ret = -1;
		StringBuilder pre = new StringBuilder();

		pre.append("update note set ");

		for (String key : vals.keySet()) {
			pre.append(key).append("=").append("'").append(vals.get(key))
					.append("' ,");

		}

		// pre.deleteCharAt(pre.length() -1);
		pre.append("update_date_time=current_timestamp() ")
				.append(" where _id=").append(id);

		String sql = pre.toString();
		try {
			Statement sm = conn.createStatement();
			ret = sm.executeUpdate(sql);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("update sql: " + sql);
		return ret;

	}

	public static int delete(String id) {
		int ret = -1;
		try {
			try {
				Integer.valueOf(id);

			} catch (NumberFormatException e) {
				System.out.println("delete: invalid id : " + id);				
			}
			Statement sm = conn.createStatement();
			ret = sm.executeUpdate("delete from note where _id=" + id);
			return ret;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		System.out.println("deleted  id: " + id);
		return ret;

	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (conn != null) {
			conn.close();
			conn = null;
		}
	}

	public static String getTestString() {
		return " test string";
	}

	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("digest", "t'est ä¸???????äº?");
		map.put("content", "test content");

		insert(map);
		update(map, "3");
	}

}
