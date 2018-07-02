package com.bonkan.brao.server;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import com.bonkan.brao.server.mysql.MySQLHandler;
import com.bonkan.brao.server.packets.Packet;
import com.bonkan.brao.server.ui.ServerInterface;
import com.bonkan.brao.server.users.LobbyUser;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

/**
 * <p>Clase principal.</p>
 */
public class Main {
	
	public static void main(String args[])
	{
		// inicializamos la interfaz
	    ServerInterface.init();

	    // inicialiazamos el protocolo (clase auxiliar que maneja la entrada y salida de paquetes)
	    Protocol.init();
	    
	    // conectamos a la BD
 		try 
 		{
 			MySQLHandler.connect();
 			ServerInterface.addMessage("Connected to database.");
 			
 		} catch (SQLException e1) {
 			e1.printStackTrace();
 		}
	    
		// esta instanciación ya crea un thread nuevo
		Server server = new Server(); // el constrctor de server puede recibir un buffersize, si hay errores probablemente sean por paquetes muy grandes, hay que tocar aca
		
		// inicializa el thread que escucha conexiones
	    server.start();
	    
	    // registramos las clases a usar
	    Kryo kryo = server.getKryo();
	    kryo.register(Packet.class);
	    kryo.register(ArrayList.class);
	    kryo.register(String.class);
	    kryo.register(LobbyUser.class);
	    
	    try {
			server.bind(7666, 54777);

		    server.addListener(new Listener() {
		        public void received (Connection connection, Object object) {
		        	if (object instanceof Packet) {
		        		Protocol.handleData(connection, (Packet) object);
		        	}
		        }
		    });
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
