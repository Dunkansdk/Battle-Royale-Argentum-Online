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
import com.bonkan.brao.server.users.MatchUser;
import com.bonkan.brao.server.utils.Position;
import com.esotericsoftware.kryonet.Connection;

/**
 * <p>La clase protocolo maneja la llegada y el envío de paquetes.</p>
 */
public class Protocol {

	public static ConcurrentHashMap<UUID, LobbyUser> userList; // en el lobby

	// TODO: HARDCODED !!!!
    // CREAMOS UN MATCH AL TUN TUN Y METEMOS LOS USERS QUE SE LOGUEAN
	private static Match currentMatch; // además, cuando haya queues, el match se crea una vez que se llena la queue
	
	public static void init()
	{
		userList = new ConcurrentHashMap<UUID, LobbyUser>();
		currentMatch = new Match(UUID.randomUUID());
	}
	
	/**
	 * <p>Maneja la llegada de paquetes.</p>
	 * @param conn		&emsp;{@link com.esotericsoftware.kryonet.Connection Connection} el cliente que mandó el paquete
	 * @param p			&emsp;{@link com.bonkan.brao.server.packets.Packet Packet} el paquete que mandó
	 */
	public static void handleData(Connection conn, Packet p)
	{
		// auxiliares
		UUID id;
		MatchUser mu;
		ArrayList<String> args = new ArrayList<String>();
		
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
						LobbyUser u = new LobbyUser(p.getArgs().get(0), UUID.randomUUID(), rs.getInt("default_body"), conn);
						userList.put(u.getID(), u);
						
						args.clear();
						
						args.add(u.getID().toString());
						args.add(u.getNickName());
						args.add(String.valueOf(u.getDefaultBody()));
						
						// TODO: HARDCODED !! cuando se loguea un user, no se crea el MatchUser,
						// solamente el LobbyUser, pero hacemos esto para testear
						mu = new MatchUser(p.getArgs().get(0), u.getID(), rs.getInt("default_body"), conn, 250, 2000, new Position(150, 120), currentMatch.getID());
						currentMatch.addUser(mu);
						
						// TODO: esto lo metemos aca para testear tambien, el paquete PACKET_LOGIN_SUCCESS, no deberia mandar la hp, mana y posicion (no esta jugando todavia)
						args.add(String.valueOf(mu.getHP()));
						args.add(String.valueOf(mu.getMana()));
						args.add(String.valueOf(mu.getPos().getX()));
						args.add(String.valueOf(mu.getPos().getY()));

						conn.sendTCP(new Packet(PacketIDs.PACKET_LOGIN_SUCCESS, null, args));
						
						ServerInterface.addMessage("LOGUEADO CON ID: " + u.getID());
						
						args.clear();
						args.add(String.valueOf(mu.getDefaultBody())); // body
						args.add("1"); // head
						args.add(String.valueOf(mu.getPos().getX())); // x
						args.add(String.valueOf(mu.getPos().getY())); // y
						args.add(String.valueOf(mu.getNickName())); // nick
						
						currentMatch.sendDataToArea(new Packet(PacketIDs.PACKET_USER_ENTERED_AREA, u.getID().toString(), args), u.getID());
						
					} else {
						conn.sendTCP(new Packet(PacketIDs.PACKET_LOGIN_FAILED, "", null));
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
				
			case PacketIDs.PACKET_PLAYER_CHANGED_STATE:
				
				id = UUID.fromString(((String)p.getData()));
				mu = currentMatch.getUserByID(id);
				
				mu.setState(p.getArgs().get(0));

				// mandamos todo por si no lo tenia en el area
				args.clear();
				args.add(String.valueOf(mu.getDefaultBody())); // body
				args.add("1"); // head
				args.add(String.valueOf(mu.getPos().getX())); // x
				args.add(String.valueOf(mu.getPos().getY())); // y
				args.add(String.valueOf(mu.getNickName())); // nick
				args.add(String.valueOf(p.getArgs().get(0))); // state

				currentMatch.sendDataToArea(new Packet(PacketIDs.PACKET_USER_CHANGED_STATE, id.toString(), args), id);
				break;
				
			case PacketIDs.PACKET_PLAYER_MOVED:
				
				id = UUID.fromString(((String)p.getData()));
				mu = currentMatch.getUserByID(id);
				
				mu.setPosition(Integer.parseInt(p.getArgs().get(0)), Integer.parseInt(p.getArgs().get(1)));

				args.clear();
				args.add(String.valueOf(mu.getDefaultBody())); // body
				args.add("1"); // head
				args.add(String.valueOf(mu.getPos().getX())); // x
				args.add(String.valueOf(mu.getPos().getY())); // y
				args.add(String.valueOf(mu.getNickName())); // nick
				args.add(String.valueOf(mu.getState())); // state

				currentMatch.sendDataToArea(new Packet(PacketIDs.PACKET_USER_MOVED, id.toString(), args), id);
				break;
				
			case PacketIDs.PACKET_TRY_OPEN_CHEST:
				
				int chestID = Integer.parseInt((String) p.getData());
				
				if(!currentMatch.openedChest(chestID))
				{
					currentMatch.openChest(chestID);
					currentMatch.sendDataToAll(new Packet(PacketIDs.PACKET_CHEST_OPENED, p.getData(), null));
				}
				
				break;
		}
	}
}
