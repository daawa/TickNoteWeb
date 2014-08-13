package sevlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mm.com.ConnectMysql;
import mm.com.Constants;

/**
 * Servlet implementation class UpdateNote
 */
@WebServlet("/UpdateNote")
public class UpdateNote extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateNote() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("doget ...nothing done..update.. test");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 String digest = request.getParameter("digest");
		 String content = request.getParameter("content");
		 String create_date_time = request.getParameter("create_date_time");
		 String update_date_time = request.getParameter("update_date_time");
		 
		 Map<String, String> map = new HashMap<String, String>();
		 
		 if(digest != null) map.put("digest", digest);
		 if(content != null) map.put("content", content);
		 if(create_date_time != null) map.put("create_date_time", create_date_time);
		 if(update_date_time != null) map.put("update_date_time", update_date_time);
		 
		 System.out.println("map size:" + map.size() + " params: size" + request.getParameterMap().size());
		 
		 String update_mode = request.getParameter(mm.com.Constants.UPDATE_MODE.name);
		 int ret = -1;
		 
		 if( update_mode == null || Integer.valueOf(update_mode) == Constants.UPDATE_MODE.CREATE){
			 
			 ConnectMysql.insert(map);
			 
		 }
		 else if(Integer.valueOf(update_mode) == Constants.UPDATE_MODE.UPDATE){
			 String id = request.getParameter("recordId");
			 ret = ConnectMysql.update(map, id);
			 
		 }
		 else if(Integer.valueOf(update_mode) == Constants.UPDATE_MODE.DELETE) {
			 String id = request.getParameter("recordId");
			 ret = ConnectMysql.delete(id);
		 }
		 else{
			 System.out.println("UPdateNote:  unrecognized updae_mode!!!");
		 }
		 
		 response.getOutputStream().write(String.valueOf(ret).getBytes());
	}

}
