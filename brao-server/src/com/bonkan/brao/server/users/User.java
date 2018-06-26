package com.bonkan.brao.server.users;

import java.util.UUID;

import com.bonkan.brao.server.packets.Packet;
import com.bonkan.brao.server.users.game.GameData;
import com.esotericsoftware.kryonet.Connection;

public class User {

	private UUID id;
	private String nickName;
	private GameData gameData;
	private Connection socket;

	public User(String nickName, UUID id)
	{
		this.nickName = nickName;
		this.id = id;
	}
	
	public void sendData(Packet p)
	{
		socket.sendTCP(p);
	}
	
	public String getNickName()
	{
		return nickName;
	}
	
	public GameData getGameData()
	{
		return gameData;
	}
	
	public UUID getID()
	{
		return id;
	}
}
