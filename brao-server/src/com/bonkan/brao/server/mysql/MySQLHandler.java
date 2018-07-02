package com.bonkan.brao.server.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>Clase que maneja y realiza las queries a la BD.</p>
 */
public class MySQLHandler {
    
	private static Connection localConn;
	
	/**
	 * <p>Conecta con la BD y guarda la conexión en la variable <b>localConn</b>. 
	 * Generalmente las conexiones se cierran cuando no se usan y se vuelven a abrir cada vez
	 * que se va a realizar una query; pero no sé si es tan util para un server de este estilo
	 * (las queries se realizan constantemente), así que, por lo pronto, la conexión queda abierta
	 * hasta que se cierra el server.</p>
	 * @throws SQLException
	 */
	public static void connect() throws SQLException
	{
		String url = "jdbc:mysql://localhost:3306/test?serverTimezone=GMT";
        String user = "root";
        String password = "";
        
		localConn = DriverManager.getConnection(url, user, password);
	}
	
	/**
	 * <p>Ejecuta la query y retorna los valores (generalmente se usa para queries <b>SELECT</b>).</p>
	 * @param query				&emsp;(<b>String</b>) query a ejecutar
	 * @throws SQLException
	 * @return ResultSet		&emsp;({@link java.sql.ResultSet ResultSet}) los resultados de la query
	 */
	public static ResultSet retrieveData(String query) throws SQLException
	{
		PreparedStatement pst = localConn.prepareStatement(query);
        ResultSet rs = pst.executeQuery();

	    return rs;
    }

	/**
	 * <p>Ejecuta una query que inserta datos a la BD (generalmente <b>INSERTS</b>, <b>ALTER</b> o <b>UPDATE</b>).</p>
	 * @param query				&emsp;(<b>String</b>) query a ejecutar
	 * @throws SQLException
	 */
	public static void insertData(String query) throws SQLException
	{
		PreparedStatement pst = localConn.prepareStatement(query);
        pst.executeUpdate();
	}
}
