package com.bonkan.brao.server;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.bonkan.brao.server.mysql.MySQLHandler;
import com.bonkan.brao.server.packets.PacketIDs;
import com.bonkan.brao.server.ui.ServerInterface;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class Main {

	public static void main(String args[])
	{
		// inicializamos la interfaz
	    ServerInterface.init();

	    // inicializamos la lista de users
	    //userList = new ConcurrentHashMap<UUID, User>();
	    
		// esta instanciación ya crea un thread nuevo
		Server server = new Server(); // el constrctor de server puede recibir un buffersize, si hay errores probablemente sean por paquetes muy grandes, hay que tocar aca
		
		// inicializa el thread que escucha conexiones
	    server.start();
	    
	    // registramos las clases a usar
	    Kryo kryo = server.getKryo();
	    kryo.register(Packet.class);
	    kryo.register(ArrayList.class);
	    
	    try {
			server.bind(7666, 54777);

		    server.addListener(new Listener() {
		        public void received (Connection connection, Object object) {
		        	if (object instanceof Packet) {
		        		handleData(connection, (Packet) object);
		        	}
		        }
		    });
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    // conectamos a la BD
		try 
		{
			MySQLHandler.connect();
			ServerInterface.addMessage("Connected to database.");
			
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void handleData(Connection conn, Packet p)
	{
		switch(p.getID())
		{
			case PacketIDs.PACKET_LOGIN:
				// vemos si logueo CORRETAMENTE
				try {
					ResultSet rs = MySQLHandler.retrieveData(
							"SELECT * FROM `users` WHERE `username` = '" + p.getArgs().get(0) + "' AND `password` = '" + p.getArgs().get(1) + "'");
				
					if(rs.next())
						conn.sendTCP(new Packet(PacketIDs.PACKET_LOGIN_SUCCESS, "", null));
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
		}
	}
}
