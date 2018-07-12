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
import com.bonkan.brao.server.utils.CommonUtils;
import com.bonkan.brao.server.utils.JSONManager;
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
		UUID id, itemID;
		MatchUser mu, mu2;
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
						args.add(String.valueOf(mu.getState())); // state
						args.add(JSONManager.getItemAnimAtlasName(mu.getEquippedShield())); // escudo
						args.add(JSONManager.getItemAnimAtlasName(mu.getEquippedWeapon())); // arma
						args.add(JSONManager.getItemAnimAtlasName(mu.getEquippedHelmet())); // casco
						
						currentMatch.sendDataToArea(new Packet(PacketIDs.PACKET_PLAYER_SEND_FULL_BODY, u.getID().toString(), args), u.getID());
						
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
				args.add(String.valueOf(p.getArgs().get(0))); // state

				currentMatch.sendDataToArea(new Packet(PacketIDs.PACKET_USER_CHANGED_STATE, id.toString(), args), id);
				break;
				
			case PacketIDs.PACKET_PLAYER_MOVED:
				
				id = UUID.fromString(((String)p.getData()));
				mu = currentMatch.getUserByID(id);
				
				mu.setPosition(Integer.parseInt(p.getArgs().get(0)), Integer.parseInt(p.getArgs().get(1)));

				args.clear();
				args.add(String.valueOf(mu.getPos().getX()));
				args.add(String.valueOf(mu.getPos().getY()));

				currentMatch.sendDataToArea(new Packet(PacketIDs.PACKET_USER_MOVED, id.toString(), args), id);
				break;
				
			case PacketIDs.PACKET_TRY_OPEN_CHEST:
				
				int chestID = Integer.parseInt((String) p.getData());
				Position chestPos = new Position(Integer.parseInt(p.getArgs().get(0)), Integer.parseInt(p.getArgs().get(1)));
				
				if(!currentMatch.openedChest(chestID))
				{
					currentMatch.openChest(chestID, chestPos);
					currentMatch.sendDataToAll(new Packet(PacketIDs.PACKET_CHEST_OPENED, p.getData(), null));
				}
				
				break;
				
			case PacketIDs.PACKET_PLAYER_REQUEST_GET_ITEM:
				
				id = UUID.fromString((String) p.getData());
				mu = currentMatch.getUserByID(id);
				itemID = UUID.fromString(p.getArgs().get(0));
				int amount = Integer.parseInt(p.getArgs().get(1));
				
				if(currentMatch.itemExists(itemID))
				{
					mu.sendData(new Packet(PacketIDs.PACKET_PLAYER_CONFIRM_GET_ITEM, itemID.toString(), null));
					
					int itemIndex = currentMatch.getItemIndex(itemID);
					int rarity = currentMatch.getItemRarity(itemID);

					switch(JSONManager.getItemType(itemIndex))
					{
						case CommonUtils.ITEM_TYPE_SHIELD:
							
							// si tiene otro lo tiramos
							if(mu.getEquippedShield() > -1) // si tiene escudo
								currentMatch.throwItem(mu.getPos().getX(), mu.getPos().getY() - CommonUtils.BODY_HEIGHT / 2, mu.getEquippedShield(), rarity, amount);
							
							mu.setEquippedShield(itemIndex, rarity);
							break;
							
						case CommonUtils.ITEM_TYPE_WEAPON:
							
							// si tiene otro lo tiramos
							if(mu.getEquippedWeapon() > -1) // si tiene arma
								currentMatch.throwItem(mu.getPos().getX(), mu.getPos().getY() - CommonUtils.BODY_HEIGHT / 2, mu.getEquippedWeapon(), rarity, amount);
							
							mu.setEquippedWeapon(itemIndex, rarity);
							break;
							
						case CommonUtils.ITEM_TYPE_HELMET:
							
							// si tiene otro lo tiramos
							if(mu.getEquippedHelmet() > -1) // si tiene casco
								currentMatch.throwItem(mu.getPos().getX(), mu.getPos().getY() - CommonUtils.BODY_HEIGHT / 2, mu.getEquippedHelmet(), rarity, amount);
							
							mu.setEquippedHelmet(itemIndex, rarity);
							break;
							
						case CommonUtils.ITEM_TYPE_RED_POTION:
							mu.addRedPotions(amount);
							break;
							
						case CommonUtils.ITEM_TYPE_BLUE_POTION:
							mu.addBluePotions(amount);
							break;
					}
					
					currentMatch.removeItem(itemID);
					
					args.clear();
					args.add(JSONManager.getItemAnimAtlasName(mu.getEquippedShield()));
					args.add(JSONManager.getItemAnimAtlasName(mu.getEquippedWeapon()));
					args.add(JSONManager.getItemAnimAtlasName(mu.getEquippedHelmet()));
					currentMatch.sendDataToArea(new Packet(PacketIDs.PACKET_USER_IN_AREA_EQUIPPED_ITEM, id.toString(), args), id);
					currentMatch.sendDataToAll(new Packet(PacketIDs.PACKET_REMOVE_ITEM_FROM_FLOOR, itemID.toString(), null));
				}
				
				break;
				
			case PacketIDs.PACKET_PLAYER_REQUEST_FULL_BODY:
				
				id = UUID.fromString((String) p.getData());
				UUID requestedID = UUID.fromString(p.getArgs().get(0));
				mu = currentMatch.getUserByID(requestedID);
				
				args.clear();
				args.add(String.valueOf(mu.getDefaultBody())); // body
				args.add("1"); // head
				args.add(String.valueOf(mu.getPos().getX())); // x
				args.add(String.valueOf(mu.getPos().getY())); // y
				args.add(String.valueOf(mu.getNickName())); // nick
				args.add(String.valueOf(mu.getState())); // state
				args.add(JSONManager.getItemAnimAtlasName(mu.getEquippedShield()));
				args.add(JSONManager.getItemAnimAtlasName(mu.getEquippedWeapon()));
				
				mu2 = currentMatch.getUserByID(id);
				mu2.sendData(new Packet(PacketIDs.PACKET_PLAYER_SEND_FULL_BODY, requestedID.toString(), args));
				break;
				
			case PacketIDs.PACKET_USER_ENTERED_PLAYER_AREA:
				
				id = UUID.fromString((String) p.getData()); // id del player
				UUID userID = UUID.fromString(p.getArgs().get(0)); // id del user que entró a su área
				
				// le mandamos al user el body del player del area a la que entró
				mu = currentMatch.getUserByID(id);
				args.clear();
				args.add(String.valueOf(mu.getDefaultBody())); // body
				args.add("1"); // head
				args.add(String.valueOf(mu.getPos().getX())); // x
				args.add(String.valueOf(mu.getPos().getY())); // y
				args.add(String.valueOf(mu.getNickName())); // nick
				args.add(String.valueOf(mu.getState())); // state
				args.add(JSONManager.getItemAnimAtlasName(mu.getEquippedShield()));
				args.add(JSONManager.getItemAnimAtlasName(mu.getEquippedWeapon()));
				args.add(JSONManager.getItemAnimAtlasName(mu.getEquippedHelmet()));

				mu2 = currentMatch.getUserByID(userID);
				mu2.sendData(new Packet(PacketIDs.PACKET_PLAYER_SEND_FULL_BODY, id.toString(), args));
				break;
				
			case PacketIDs.PACKET_PLAYER_REQUEST_UNEQUIP_ITEM:
				id = UUID.fromString((String) p.getData());
				mu = currentMatch.getUserByID(id);
				int slot = Integer.parseInt(p.getArgs().get(0));
				int cant = Integer.parseInt(p.getArgs().get(1)); // para las potas

				switch(slot)
				{
					case CommonUtils.INVENTORY_SHIELD_SLOT:
						if(mu.getEquippedShield() > -1) // si tiene escudo
						{
							if(!currentMatch.busyTile(mu.getPos().getX(), mu.getPos().getY() - CommonUtils.BODY_HEIGHT / 2)) // si el tile está libre
							{
								args.clear();
								args.add(null);
								args.add(JSONManager.getItemAnimAtlasName(mu.getEquippedWeapon()));
								args.add(JSONManager.getItemAnimAtlasName(mu.getEquippedHelmet()));
								currentMatch.sendDataToArea(new Packet(PacketIDs.PACKET_USER_IN_AREA_EQUIPPED_ITEM, id.toString(), args), id);
								currentMatch.throwItem(mu.getPos().getX(), mu.getPos().getY() - CommonUtils.BODY_HEIGHT / 2, mu.getEquippedShield(), mu.getEquippedShieldRarity(), cant);
								
								// confirmamos
								mu.setEquippedShield(-1, 0);
								mu.sendData(new Packet(PacketIDs.PACKET_PLAYER_CONFIRM_UNEQUIP_ITEM, String.valueOf(slot), null));
							}
						}
						break;
						
					case CommonUtils.INVENTORY_WEAPON_SLOT:
						if(mu.getEquippedWeapon() > -1) // si tiene escudo
						{
							if(!currentMatch.busyTile(mu.getPos().getX(), mu.getPos().getY() - CommonUtils.BODY_HEIGHT / 2)) // si el tile está libre
							{
								args.clear();
								args.add(JSONManager.getItemAnimAtlasName(mu.getEquippedShield()));
								args.add(null);
								args.add(JSONManager.getItemAnimAtlasName(mu.getEquippedHelmet()));
								currentMatch.sendDataToArea(new Packet(PacketIDs.PACKET_USER_IN_AREA_EQUIPPED_ITEM, id.toString(), args), id);
								currentMatch.throwItem(mu.getPos().getX(), mu.getPos().getY() - CommonUtils.BODY_HEIGHT / 2, mu.getEquippedWeapon(), mu.getEquippedWeaponRarity(), cant);
								
								// confirmamos
								mu.setEquippedWeapon(-1, 0);
								mu.sendData(new Packet(PacketIDs.PACKET_PLAYER_CONFIRM_UNEQUIP_ITEM, String.valueOf(slot), null));
							}
						}
						break;
						
					case CommonUtils.INVENTORY_HELMET_SLOT:
						if(mu.getEquippedHelmet() > -1) // si tiene escudo
						{
							if(!currentMatch.busyTile(mu.getPos().getX(), mu.getPos().getY() - CommonUtils.BODY_HEIGHT / 2)) // si el tile está libre
							{
								args.clear();
								args.add(JSONManager.getItemAnimAtlasName(mu.getEquippedShield()));
								args.add(JSONManager.getItemAnimAtlasName(mu.getEquippedWeapon()));
								args.add(null);
								currentMatch.sendDataToArea(new Packet(PacketIDs.PACKET_USER_IN_AREA_EQUIPPED_ITEM, id.toString(), args), id);
								currentMatch.throwItem(mu.getPos().getX(), mu.getPos().getY() - CommonUtils.BODY_HEIGHT / 2, mu.getEquippedHelmet(), mu.getEquippedHelmetRarity(), cant);
								
								// confirmamos
								mu.setEquippedHelmet(-1, 0);
								mu.sendData(new Packet(PacketIDs.PACKET_PLAYER_CONFIRM_UNEQUIP_ITEM, String.valueOf(slot), null));
							}
						}
						break;
						
					case CommonUtils.INVENTORY_RED_POTION_SLOT:
						
						if(!currentMatch.busyTile(mu.getPos().getX(), mu.getPos().getY() - CommonUtils.BODY_HEIGHT / 2)) // si el tile está libre
						{
							if(mu.getRedPotionsAmount() > cant)
							{
								mu.addRedPotions(-cant);
								currentMatch.throwItem(mu.getPos().getX(), mu.getPos().getY() - CommonUtils.BODY_HEIGHT / 2, CommonUtils.RED_POTION_INDEX, 1, cant);
	
								args.clear();
								args.add(String.valueOf(CommonUtils.RED_POTION_INDEX));
								args.add(String.valueOf(cant));
								mu.sendData(new Packet(PacketIDs.PACKET_PLAYER_REMOVE_POTION, null, args));
							} else {
								cant = mu.getRedPotionsAmount();
								mu.addRedPotions(-cant);
								currentMatch.throwItem(mu.getPos().getX(), mu.getPos().getY() - CommonUtils.BODY_HEIGHT / 2, CommonUtils.RED_POTION_INDEX, 1, cant);
								
								args.clear();
								args.add(String.valueOf(CommonUtils.RED_POTION_INDEX));
								args.add(String.valueOf(cant));
								mu.sendData(new Packet(PacketIDs.PACKET_PLAYER_REMOVE_POTION, null, args));
							}
						}
						
						break;
						
					case CommonUtils.INVENTORY_BLUE_POTION_SLOT:
						
						if(!currentMatch.busyTile(mu.getPos().getX(), mu.getPos().getY() - CommonUtils.BODY_HEIGHT / 2)) // si el tile está libre
						{
							if(mu.getBluePotionsAmount() > cant)
							{
								mu.addBluePotions(-cant);
								currentMatch.throwItem(mu.getPos().getX(), mu.getPos().getY() - CommonUtils.BODY_HEIGHT / 2, CommonUtils.BLUE_POTION_INDEX, 1, cant);
								
								args.clear();
								args.add(String.valueOf(CommonUtils.BLUE_POTION_INDEX));
								args.add(String.valueOf(cant));
								mu.sendData(new Packet(PacketIDs.PACKET_PLAYER_REMOVE_POTION, null, args));
							} else {
								cant = mu.getBluePotionsAmount();
								mu.addBluePotions(-cant);
								currentMatch.throwItem(mu.getPos().getX(), mu.getPos().getY() - CommonUtils.BODY_HEIGHT / 2, CommonUtils.BLUE_POTION_INDEX, 1, cant);
								
								args.clear();
								args.add(String.valueOf(CommonUtils.BLUE_POTION_INDEX));
								args.add(String.valueOf(cant));
								mu.sendData(new Packet(PacketIDs.PACKET_PLAYER_REMOVE_POTION, null, args));
							}
						}
						
						break;
				}
				
				break;
		}
	}
}
