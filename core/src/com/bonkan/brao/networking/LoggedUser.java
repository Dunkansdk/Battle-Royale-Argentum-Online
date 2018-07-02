package com.bonkan.brao.networking;

import java.util.UUID;

/**
 * <p>Clase que maneja los atributos comunes de un usuario logeado (tanto cuando está
 * en el lobby como cuando está jugando).</p>
 */
public class LoggedUser {

	private UUID loggedID;
	private String loggedUserName;
	private int loggedDefaultBody;
	
	public LoggedUser(UUID loggedID, String loggedUserName, int loggedDefaultBody)
	{
		this.loggedID = loggedID;
		this.loggedUserName = loggedUserName;
		this.loggedDefaultBody = loggedDefaultBody;
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
}
