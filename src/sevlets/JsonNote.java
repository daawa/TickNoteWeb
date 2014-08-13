package sevlets;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Connector
 */
@WebServlet("/Connector")
public class JsonNote extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public JsonNote() {
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String limit = request.getParameter("limit_start");
		int limit_start = -1;
		if(limit != null){
			limit_start = Integer.valueOf(limit);
			System.out.println("limit:" + limit);
		}
		
		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");

		
		java.io.BufferedWriter bw = new java.io.BufferedWriter(response.getWriter());
		try {
			if(limit_start > 0){
				String res = mm.com.ConnectMysql.query( limit_start);
				System.out.println(res);
				bw.write(res);
			}
			else{
				String res = mm.com.ConnectMysql.query();
				System.out.println("res returned:" + res);
				bw.write(res);
			}
			
		} catch (SQLException e) {

			e.printStackTrace();
		}
		bw.flush();
		bw.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
		response.setHeader("content-type","text/html;charset=UTF-8");
		System.out.println("processing post request. ..");
		
		String id = (String)request.getParameter("recordId");
		if(null != id){
			
			java.io.BufferedWriter bw = new java.io.BufferedWriter(response.getWriter());
			try{
				String res = mm.com.ConnectMysql.queryAsJsonString(id);
				System.out.println("json string: " + res);
				bw.write(res);
			}
			catch (SQLException e) {

				e.printStackTrace();
			}
			bw.flush();
			bw.close();

			
		}
	}

}
