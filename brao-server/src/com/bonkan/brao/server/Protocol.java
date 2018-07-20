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
		UUID id, itemID, id2;
		MatchUser mu, mu2;
		int slot;
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

				boolean agarrado = true; // para hechizos, cuando tenemos todos los slots completos
				
				if(currentMatch.itemExists(itemID))
				{
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
							
						case CommonUtils.ITEM_TYPE_SPELL:
							// buscamos un slot vacio
							boolean slotLibre = false;
							int i = 0;
							
							while(i < 4 && !slotLibre)
							{
								if(mu.getSpell(i) == -1)
								{
									mu.setSpell(i, itemIndex);
									slotLibre = true;
								}
								
								i++;
							}
							
							if(!slotLibre) // si no hay slot libre no agarramos nada
								agarrado = false;
							
							break;
					}

					if(agarrado)
					{
						mu.sendData(new Packet(PacketIDs.PACKET_PLAYER_CONFIRM_GET_ITEM, itemID.toString(), null));
						currentMatch.removeItem(itemID);
						
						args.clear();
						args.add(JSONManager.getItemAnimAtlasName(mu.getEquippedShield()));
						args.add(JSONManager.getItemAnimAtlasName(mu.getEquippedWeapon()));
						args.add(JSONManager.getItemAnimAtlasName(mu.getEquippedHelmet()));
						currentMatch.sendDataToArea(new Packet(PacketIDs.PACKET_USER_IN_AREA_EQUIPPED_ITEM, id.toString(), args), id);
						currentMatch.sendDataToAll(new Packet(PacketIDs.PACKET_REMOVE_ITEM_FROM_FLOOR, itemID.toString(), null));
					}
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
				slot = Integer.parseInt(p.getArgs().get(0));
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
		
			case PacketIDs.PACKET_PLAYER_REQUEST_UNEQUIP_SPELL:
				id = UUID.fromString((String) p.getData());
				mu = currentMatch.getUserByID(id);
				slot = Integer.parseInt(p.getArgs().get(0));
				int rarity = Integer.parseInt(p.getArgs().get(1));
				
				if(mu.getSpell(slot) > -1) // si tiene un hechizo en ese slot
				{
					if(!currentMatch.busyTile(mu.getPos().getX(), mu.getPos().getY() - CommonUtils.BODY_HEIGHT / 2)) // si el tile está libre
					{
						currentMatch.throwItem(mu.getPos().getX(), mu.getPos().getY() - CommonUtils.BODY_HEIGHT / 2, mu.getSpell(slot), rarity, 1);
						
						// confirmamos
						mu.setSpell(slot, -1);
						mu.sendData(new Packet(PacketIDs.PACKET_PLAYER_CONFIRM_UNEQUIP_SPELL, String.valueOf(slot), null));
					}
				}
				break;
		
			case PacketIDs.PACKET_PLAYER_REQUEST_SPELL_SWAP:
				id = UUID.fromString((String) p.getData());
				mu = currentMatch.getUserByID(id);
				int slot1 = Integer.parseInt(p.getArgs().get(0));
				int slot2 = Integer.parseInt(p.getArgs().get(1));
				
				if(mu.getSpell(slot1) != -1 || mu.getSpell(slot2) != -1) // si alguno de los slots tiene algo
				{
					int aux = mu.getSpell(slot1);
					mu.setSpell(slot1, mu.getSpell(slot2));
					mu.setSpell(slot2, aux);
					
					args.clear();
					args.add(String.valueOf(slot1));
					args.add(String.valueOf(slot2));
					mu.sendData(new Packet(PacketIDs.PACKET_PLAYER_CONFIRM_SPELL_SWAP, null, args));
				}
				break;
				
			case PacketIDs.PACKET_PLAYER_REQUEST_CAST_SPELL:
				id = UUID.fromString((String) p.getData());
				mu = currentMatch.getUserByID(id);
				slot = Integer.parseInt(p.getArgs().get(0));
				
				if(mu.getSpell(slot) != -1 && mu.getMana() >= JSONManager.getItemManaCost(mu.getSpell(slot)))
				{
					// le bajamos la mana y lo casteamos
					mu.setMana(mu.getMana() - JSONManager.getItemManaCost(mu.getSpell(slot)));
					
					args.clear();
					args.add(String.valueOf(mu.getHP()));
					args.add(String.valueOf(mu.getMana()));
					mu.sendData(new Packet(PacketIDs.PACKET_UPDATE_PLAYER_HP_AND_MANA, null, args));
				
					args.clear();
					args = p.getArgs();
					args.add(String.valueOf(mu.getSpell(slot))); // agrego el indice del spell a los argumentos
					mu.sendData(new Packet(PacketIDs.PACKET_PLAYER_CONFIRM_CAST_SPELL, null, args)); // lo mando con los mismos argumentos que llegaron, el SLOT no me sirve pero x, y, destX y destY si (además, le agregamos el index del spell)
				
					currentMatch.sendDataToArea(new Packet(PacketIDs.PACKET_USER_IN_AREA_CASTED_SPELL, id.toString(), args), id);
				}
				break;
				
			case PacketIDs.PACKET_PLAYER_REQUEST_USE_POTION:
				id = UUID.fromString((String) p.getData());
				mu = currentMatch.getUserByID(id);
				int pot = Integer.parseInt(p.getArgs().get(0));
				
				switch(pot)
				{
					case CommonUtils.RED_POTION_INDEX:
						if(mu.getRedPotionsAmount() > 0)
						{
							mu.setHP(mu.getHP() + 20);
							if(mu.getHP() > mu.getMaxHP()) mu.setHP(mu.getMaxHP());
							mu.addRedPotions(-1);
							
							mu.sendData(new Packet(PacketIDs.PACKET_PLAYER_CONFIRM_USE_POTION, null, p.getArgs()));
						}
						break;
				
					case CommonUtils.BLUE_POTION_INDEX:
						if(mu.getBluePotionsAmount() > 0)
						{
							mu.setMana(mu.getMana() + 100);
							if(mu.getMana() > mu.getMaxMana()) mu.setMana(mu.getMaxMana());
							
							mu.addBluePotions(-1);
							
							mu.sendData(new Packet(PacketIDs.PACKET_PLAYER_CONFIRM_USE_POTION, null, p.getArgs()));
						}
						break;
				}
				break;
				
			case PacketIDs.PACKET_PLAYER_HIT_USER_WITH_SPELL:
				id = UUID.fromString((String) p.getData());
				mu = currentMatch.getUserByID(id);
				//int index = Integer.parseInt(p.getArgs().get(1)); // indice del hechizo
				
				id2 = UUID.fromString(p.getArgs().get(0));
				mu2 = currentMatch.getUserByID(id2);
				
				if(mu != null && mu2 != null) // corroboramos que el cliente no tenga ids truchas
				{
					// MU le tira el hechizo a MU2
					// calculamos el daño segun el indice del hechizo, ahora no tengo ganas
					args.clear();
					args.add("100"); // esto es el daño, jeje
					
					mu2.setHP(mu2.getHP() - 100);
					
					if(mu2.getHP() < 0)
						mu2.setHP(0);
					
					mu2.sendData(new Packet(PacketIDs.PACKET_RECEIVE_DAMAGE, null, args));
					currentMatch.sendDataToArea(new Packet(PacketIDs.PACKET_USER_IN_AREA_RECEIVED_DAMAGE, id2.toString(), args), id2);
				}
				break;
				
			case PacketIDs.PACKET_EXPLODE_USER_SPELL:
				id = UUID.fromString((String) p.getData());
				mu = currentMatch.getUserByID(id);
				
				id2 = UUID.fromString(p.getArgs().get(0));
				mu2 = currentMatch.getUserByID(id2);
				
				mu2.sendData(new Packet(PacketIDs.PACKET_CONFIRM_EXPLODE_USER_SPELL, id.toString(), null));
				break;
		}
	}
}
