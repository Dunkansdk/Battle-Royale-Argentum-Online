package com.bonkan.brao.server;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.bonkan.brao.server.packets.Packet;
import com.bonkan.brao.server.users.MatchUser;
import com.bonkan.brao.server.utils.CommonUtils;

/**
 * <p>Clase para cada partida que se está jugando en el server.</p>
 */
public class Match {

	private HashMap<UUID, MatchUser> users;
	private UUID id;
	
	public Match(UUID id)
	{
		this.id = id;
		users = new HashMap<UUID, MatchUser>();
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
}
