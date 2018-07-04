package com.bonkan.brao.server.users;

import java.util.UUID;

import com.bonkan.brao.server.packets.Packet;
import com.esotericsoftware.kryonet.Connection;

/**
 * <p>Maneja todos los atributos de un usuario que está logeado en el LOBBY (aún no está jugando).</p>
 */
public class LobbyUser {

	private UUID id;
	private String nickName;
	private Connection socket;
	private int defaultBody;

	public LobbyUser(String nickName, UUID id, int defaultBody, Connection socket)
	{
		this.nickName = nickName;
		this.id = id;
		this.defaultBody = defaultBody;
		this.socket = socket;
	}
	
	public void sendData(Packet p)
	{
		socket.sendTCP(p);
	}
	
	public String getNickName()
	{
		return nickName;
	}
	
	public UUID getID()
	{
		return id;
	}

	public int getDefaultBody()
	{
		return defaultBody;
	}
}
