package com.bonkan.brao.server;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import com.bonkan.brao.server.mysql.MySQLHandler;
import com.bonkan.brao.server.packets.Packet;
import com.bonkan.brao.server.packets.PacketIDs;
import com.bonkan.brao.server.ui.ServerInterface;
import com.bonkan.brao.server.users.User;
import com.esotericsoftware.kryonet.Connection;

public class Protocol {

	public static ConcurrentHashMap<UUID, User> userList;

	public static void init()
	{
		userList = new ConcurrentHashMap<UUID, User>();
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
					{
						// si logea exitosamente, creamos un user y lo metemos al mapa
						UUID userID = UUID.randomUUID();
						
						userList.put(userID, new User(p.getArgs().get(0), userID));
						
						conn.sendTCP(new Packet(PacketIDs.PACKET_LOGIN_SUCCESS, userID.toString(), null));

						ServerInterface.addMessage("LOGUEADO CON ID: " + userID);
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
