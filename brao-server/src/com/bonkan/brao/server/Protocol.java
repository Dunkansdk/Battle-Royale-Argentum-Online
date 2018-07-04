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
						
						ArrayList<String> args = new ArrayList<String>();
						
						args.add(u.getID().toString());
						args.add(u.getNickName());
						args.add(String.valueOf(u.getDefaultBody()));
						
						// TODO: HARDCODED !! cuando se loguea un user, no se crea el MatchUser,
						// solamente el LobbyUser, pero hacemos esto para testear
						MatchUser mu = new MatchUser(p.getArgs().get(0), u.getID(), rs.getInt("default_body"), conn, 250, 2000, new Position(150, 120), currentMatch.getID());
						currentMatch.addUser(mu);
						
						// TODO: esto lo metemos aca para testear tambien, el paquete PACKET_LOGIN_SUCCESS, no deberia mandar la hp, mana y posicion (no esta jugando todavia)
						args.add(String.valueOf(mu.getHP()));
						args.add(String.valueOf(mu.getMana()));
						args.add(String.valueOf(mu.getPos().getX()));
						args.add(String.valueOf(mu.getPos().getY()));

						conn.sendTCP(new Packet(PacketIDs.PACKET_LOGIN_SUCCESS, null, args));

						// TODO: tambien hay que sacar esto
						args.clear();
						args.add(String.valueOf(mu.getPos().getX()));
						args.add(String.valueOf(mu.getPos().getY()));
						args.add(String.valueOf(mu.getDefaultBody()));
						args.add("1");
						args.add(String.valueOf(mu.getID()));
						args.add(mu.getNickName());
						
						// mostramos a todos los users el user q logeo
						currentMatch.sendDataToArea(new Packet(PacketIDs.PACKET_USER_ENTERED_PLAYER_AREA, null, args), u.getID());
						
						ServerInterface.addMessage("LOGUEADO CON ID: " + u.getID());
						
					} else {
						conn.sendTCP(new Packet(PacketIDs.PACKET_LOGIN_FAILED, "", null));
					}
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
				
			case PacketIDs.PACKET_PLAYER_MOVED:
				// buscamos al user en el Match y le cambiamos la posicion
				UUID id = UUID.fromString((String) p.getData());
				MatchUser mu = currentMatch.getUserByID(id);
				
				if(mu != null)
					mu.setPosition(Float.valueOf(p.getArgs().get(0)), Float.valueOf(p.getArgs().get(1)));
				
				ArrayList<String> args = new ArrayList<String>();
				args.add(String.valueOf(mu.getPos().getX()));
				args.add(String.valueOf(mu.getPos().getY()));
				args.add(String.valueOf(mu.getDefaultBody()));
				args.add("1");
				args.add(String.valueOf(mu.getID()));
				args.add(mu.getNickName());
				
				currentMatch.sendDataToArea(new Packet(PacketIDs.PACKET_USER_IN_AREA_MOVED, mu.getID().toString(), args), id);
				break;
				
			case PacketIDs.PACKET_PLAYER_CHANGED_STATE:

				MatchUser mu2 = currentMatch.getUserByID(UUID.fromString((String) p.getData()));
				mu2.setState(p.getArgs().get(0));
				
				// avisamos al resto de los players en el area que se cambio el state
				currentMatch.sendDataToArea(new Packet(PacketIDs.PACKET_USER_CHANGED_STATE, mu2.getID().toString(), p.getArgs()), mu2.getID());
				break;
		}
	}
}
