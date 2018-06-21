package com.bonkan.brao.server.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>Class that handles and performs the MySQL queries</p>
 * @author bon3
 */
public class MySQLHandler {
    
	private static Connection localConn;
	
	/**
	 * <p>Connects to the database and stores the connection in the <b>localConn</b> variable. Some suggest that the connection
	 * should be closed when unusued and reopened everytime a query is performed; but I don't know if that's the case for
	 * this kind of server (queries are going to be performed pretty much all the time)</p>
	 * @throws SQLException
	 * @author bon3
	 */
	public static void connect() throws SQLException
	{
		String url = "jdbc:mysql://localhost:3306/test?serverTimezone=GMT";
        String user = "root";
        String password = "";
        
		localConn = DriverManager.getConnection(url, user, password);
	}
	
	/**
	 * <p>Executes a query and returns the data (usually, these are <b>SELECT</b> queries)</p>
	 * @param query				&emsp;(<b>String</b>) query to execute
	 * @throws SQLException
	 * @return ResultSet		&emsp;({@link java.sql.ResultSet ResultSet}) query results
	 * @author bon3
	 */
	public static ResultSet retrieveData(String query) throws SQLException
	{
		PreparedStatement pst = localConn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();

	    return rs;
    }

	/**
	 * <p>Executes a query that <b>INSERTS</b> data into the database</p>
	 * @param query				&emsp;(<b>String</b>) query to execute
	 * @throws SQLException
	 * @author bon3
	 */
	public static void insertData(String query) throws SQLException
	{
		PreparedStatement pst = localConn.prepareStatement(query);
        pst.executeUpdate();
	}
}
