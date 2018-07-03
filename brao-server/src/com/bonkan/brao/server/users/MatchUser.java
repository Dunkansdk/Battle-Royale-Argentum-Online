package com.bonkan.brao.server.users;

import java.util.UUID;

import com.bonkan.brao.server.utils.Position;

/**
 * <p>Clase para los usuarios que están jugando.</p>
 */
public class MatchUser extends LobbyUser {

	private int hp;
	private int mana;
	private Position pos; // posicion en el mapa (del Body de box2d)
	private UUID matchID;
	
	public MatchUser(String nickName, UUID id, int defaultBody, int hp, int mana, Position pos, UUID matchID) {
		super(nickName, id, defaultBody);
		this.hp = hp;
		this.mana = mana;
		this.pos = pos;
		this.matchID = matchID;
	}

	public void setPosition(float x, float y)
	{
		pos.set(x, y);
	}
	
	public int getHP() 
	{
		return hp;
	}

	public int getMana() 
	{
		return mana;
	}

	public Position getPos() 
	{
		return pos;
	}
	
	public UUID getMatchID()
	{
		return matchID;
	}
	
}
