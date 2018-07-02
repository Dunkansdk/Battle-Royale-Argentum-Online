package com.bonkan.brao.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.bonkan.brao.server.mysql.MySQLHandler;
import com.bonkan.brao.server.packets.Packet;
import com.bonkan.brao.server.packets.PacketIDs;
import com.bonkan.brao.server.ui.ServerInterface;
import com.bonkan.brao.server.users.LobbyUser;
import com.esotericsoftware.kryonet.Connection;

/**
 * <p>La clase protocolo maneja la llegada y el envío de paquetes.</p>
 */
public class Protocol {

	public static ConcurrentHashMap<UUID, LobbyUser> userList; // en el lobby

	public static void init()
	{
		userList = new ConcurrentHashMap<UUID, LobbyUser>();
	}
	
	/**
	 * <p>Maneja la llegada de paquetes.</p>
	 * @param conn		&emsp;{@link com.esotericsoftware.kryonet.Connection Connection} el cliente que mandó el paquete
	 * @param p			&emsp;{@link com.bonkan.brao.server.packets.Packet Packet} el paquete que mandó
	 */
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
					{
						// si logea exitosamente, creamos un user y lo metemos al mapa
						LobbyUser u = new LobbyUser(p.getArgs().get(0), UUID.randomUUID(), rs.getInt("default_body"));
						userList.put(u.getID(), u);
						
						ArrayList<String> args = new ArrayList<String>();
						
						args.add(u.getID().toString());
						args.add(u.getNickName());
						args.add(String.valueOf(u.getDefaultBody()));
						
						conn.sendTCP(new Packet(PacketIDs.PACKET_LOGIN_SUCCESS, null, args));

						ServerInterface.addMessage("LOGUEADO CON ID: " + u.getID());
					} else {
						conn.sendTCP(new Packet(PacketIDs.PACKET_LOGIN_FAILED, "", null));
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
		}
	}
}
