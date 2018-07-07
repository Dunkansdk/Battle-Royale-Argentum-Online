package com.bonkan.brao.server;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import com.bonkan.brao.server.items.Item;
import com.bonkan.brao.server.packets.Packet;
import com.bonkan.brao.server.packets.PacketIDs;
import com.bonkan.brao.server.users.MatchUser;
import com.bonkan.brao.server.utils.CommonUtils;
import com.bonkan.brao.server.utils.JSONManager;
import com.bonkan.brao.server.utils.Position;

/**
 * <p>Clase para cada partida que se está jugando en el server.</p>
 */
public class Match {

	private HashMap<UUID, MatchUser> users; // usuarios del match
	private UUID id;
	private ArrayList<Integer> openedChests; // cofres abiertos
	private HashMap<UUID, Item> mapItems; // items tirados en el mapa
	
	public Match(UUID id)
	{
		this.id = id;
		users = new HashMap<UUID, MatchUser>();
		openedChests = new ArrayList<Integer>();
		mapItems = new HashMap<UUID, Item>();
	}
	
	public void addUser(MatchUser u)
	{
		users.put(u.getID(), u);
	}
	
	public void sendDataToAll(Packet p)
	{
		for (Map.Entry<UUID, MatchUser> entry : users.entrySet())
			entry.getValue().sendData(p);
	}
	
	public void sendDataToAllButUser(Packet p, UUID id)
	{
		for (Map.Entry<UUID, MatchUser> entry : users.entrySet())
		{
			if(!entry.getValue().getID().equals(id))
				entry.getValue().sendData(p);
		}
	}
	
	public void sendDataToUser(Packet p, UUID id)
	{
		if(users.get(id) != null)
			users.get(id).sendData(p);
	}
	
	public void sendDataToArea(Packet p, UUID id)
	{
		for (Map.Entry<UUID, MatchUser> entry : users.entrySet())
		{
			if(!entry.getKey().equals(id))
			{
				MatchUser mu = entry.getValue();
				if(CommonUtils.areInViewport(users.get(id).getPos(), mu.getPos()))
					mu.sendData(p);
			}
		}
	}
	
	public void sendDataToAreaUDP(Packet p, UUID id)
	{
		for (Map.Entry<UUID, MatchUser> entry : users.entrySet())
		{
			if(!entry.getKey().equals(id))
			{
				MatchUser mu = entry.getValue();
				if(CommonUtils.areInViewport(users.get(id).getPos(), mu.getPos()))
					mu.sendDataUDP(p);
			}
		}
	}
	
	public HashMap<UUID, MatchUser> getUsers()
	{
		return users;
	}
	
	public MatchUser getUserByID(UUID id)
	{
		return users.get(id);
	}
	
	public UUID getID()
	{
		return id;
	}
	
	public boolean itemExists(UUID id)
	{
		return (mapItems.get(id) != null);
	}
	
	public void removeItem(UUID id)
	{
		if(mapItems.get(id) != null)
			mapItems.remove(id);
	}
	
	public boolean openedChest(int id)
	{
		return(openedChests.contains(id));
	}
	
	public void openChest(int id, Position chestPos)
	{
		openedChests.add(id);
		
		ArrayList<String> args = new ArrayList<String>();
		UUID itemID = UUID.randomUUID();
		int rarity = ThreadLocalRandom.current().nextInt(1, 4);
		
		args.add(itemID.toString()); // id del item
		args.add(String.valueOf(rarity)); // rareza del item
		args.add(String.valueOf(chestPos.getX())); // posicion X del item
		args.add(String.valueOf(chestPos.getY() - 48)); // posicion Y del item
		args.add(JSONManager.getItemName(1)); // nombre del item (en este caso el item 1)
		args.add(JSONManager.getItemAtlasName(1)); // nombre en el atlas del cliente del item (en este caso el item 1)
		
		// agregamos el item al mapa de items
		mapItems.put(itemID, new Item(itemID, chestPos.getX(), chestPos.getY() - 48));
		
		// mandamos los items a todos los users
		sendDataToAll(new Packet(PacketIDs.PACKET_ITEM_THROWN, null, args));
	}
}
