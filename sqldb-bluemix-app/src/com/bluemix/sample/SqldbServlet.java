package com.bluemix.sample;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

/**
 * Directly getConnetction() to DB2 using information in VCAP_SERVICES
 */
@WebServlet("/")
public class SqldbServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SqldbServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		JSONObject vcap_services_obj = null;
		Connection conn = null;
		Statement stmt = null;
		String greeting = "<Empty>";
		
		
		System.out.println("Start to get connection with system var........");
		
		String driver = "com.ibm.db2.jcc.DB2Driver";
				
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("Start to read system var to connect to DB2");
		
		String icap_services_string = System.getenv("VCAP_SERVICES");
		
		System.out.println("VCAP_SERVICES =" + icap_services_string);
		if (null != icap_services_string && icap_services_string.length() > 0) {
			 vcap_services_obj = JSONObject.parse(icap_services_string);
			
			 System.out.println("VCAP_SERVICES_JSON_Obj =" + vcap_services_obj );
		} else {
			System.out.println("VCAP_SERVICES_JSON_Obj IS NULL" );
		}

		if (null != vcap_services_obj) {
			System.out.println("getting vcap_services_obj details, size:"
					+ vcap_services_obj.size());
			JSONArray vcap_services_array = (JSONArray) vcap_services_obj.get("sqldb");
			
			JSONObject first_db2 = (JSONObject) vcap_services_array.get(0);
			JSONObject first_credential = (JSONObject) first_db2.get("credentials");		
			
			System.out.println("(JSONObject) vcap_services_array.get(0): first_db2 =" + first_db2 );
							
			String host = (String) first_credential.get("host");
			String port = first_credential.get("port").toString();
			String uid = (String) first_credential.get("username");
			String pwd = (String) first_credential.get("password");
			String dbname = (String) first_credential.get("db");
			//String dbname = "testdb";
			
			String dburl = "jdbc:db2:"+"//" + host + ":" + port + "/" + dbname;
			System.out.println("getConnection : host:" +  "   host:"
					+ host + "   port:" + port + "   uid:" + uid
					+ "   pwd:" + pwd + "   dbname:" + dbname + "   dburl:"
					+ dburl);
			try {
				conn = DriverManager.getConnection(dburl, uid, pwd);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		try{
			if(conn != null){
				 stmt = conn.createStatement();
				 if(stmt != null){
						// create a table
					    stmt.executeUpdate("create table cities(name varchar(50) not null primary key, population int, county varchar(30))");
					    // insert a test record
					    stmt.executeUpdate("insert into cities values ('Beijing', 20000000, 'China')");
					    stmt.executeUpdate("insert into cities values ('ShangHai', 15000000, 'China')");
					    stmt.executeUpdate("insert into cities values ('TianJin', 10000000, 'China')");
					    // select a record
					    ResultSet resultSet = stmt.executeQuery("select * from cities where county='China'");
					    StringBuffer sb = new StringBuffer();
					    sb.append("<table style=\"width=500px;\" border=\"1\">");
					    sb.append("<tr><td>City</td><td>Population</td><td>Country</td></tr>");
					    while(resultSet.next()){
				    		sb.append(oneRow(resultSet));
				    	}
					    sb.append("</table>");
					    greeting = sb.toString();
				}
			}
			
		}catch(SQLException e) {
			greeting = e.getMessage();
		}finally {
		   try {
		      // drop the table to clean up and to be able to rerun the test.
			   if(stmt!=null){
				   stmt.executeUpdate("drop table cities");
			   }
			   if(conn != null){
				   conn.close();
			   }
		    } 
		    catch (SQLException e) {
		    	greeting = e.getMessage();
		    }				
		}
		PrintWriter out = response.getWriter();
		out.write("<html> " + "<title>" + greeting + "</title>"
		        + "<body><h1>" + greeting + " ^_^ DB2 is good!" + "</h1></body>" + "</html> ");
		out.flush();
			
		
	}
	
	private String oneRow(ResultSet resultSet){
		String template = "<tr><td>{0}</td><td>{1}</td><td>{2}</td></tr>";
		try {
			return MessageFormat.format(template, resultSet.getString(1),resultSet.getString(2),resultSet.getString(3));
		} catch (SQLException e) {
			return template;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		doGet (request, response);
	}

}
