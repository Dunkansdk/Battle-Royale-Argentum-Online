package com.bonkan.brao.networking;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * <p>Clase que maneja los atributos comunes de un usuario logeado (tanto cuando está
 * en el lobby como cuando está jugando).</p>
 */
public class LoggedUser {

	private UUID loggedID;
	private String loggedUserName;
	private int loggedDefaultBody;
	private ConcurrentLinkedQueue<Packet> incomingData;
	
	//TODO: ESTO DESPUES HAY QUE BORRARLO Y PASARLO A OTRA CLASE, ESTA CORRESSPONDE AL USER DEL LOBBY
	private int hp, mana;
	private int xPos, yPos;
	
	public LoggedUser(UUID loggedID, String loggedUserName, int loggedDefaultBody, int hp, int mana, int x, int y)
	{
		this.loggedID = loggedID;
		this.loggedUserName = loggedUserName;
		this.loggedDefaultBody = loggedDefaultBody;
		this.hp = hp;
		this.mana = mana;
		this.xPos = x;
		this.yPos = y;
		this.incomingData = new ConcurrentLinkedQueue<Packet>();
	}
	
	public UUID getLoggedID() 
	{
		return loggedID;
	}

	public String getLoggedUserName() 
	{
		return loggedUserName;
	}

	public int getLoggedDefaultBody() 
	{
		return loggedDefaultBody;
	}

	public int getHP()
	{
		return hp;
	}

	public int getMana()
	{
		return mana;
	}

	public int getX()
	{
		return xPos;
	}

	public int getY() 
	{
		return yPos;
	}
	
	public void addIncomingData(Packet p)
	{
		incomingData.add(p);
	}
	
	public boolean hasIncomingData()
	{
		return(incomingData.size() > 0);
	}
	
	public ConcurrentLinkedQueue<Packet> getIncomingData()
	{
		return incomingData;
	}
}
