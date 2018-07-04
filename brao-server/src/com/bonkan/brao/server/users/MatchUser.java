package com.bonkan.brao.server.users;

import java.util.UUID;

import com.bonkan.brao.server.utils.Position;
import com.esotericsoftware.kryonet.Connection;

/**
 * <p>Clase para los usuarios que están jugando.</p>
 */
public class MatchUser extends LobbyUser {

	private int hp;
	private int mana;
	private Position pos; // posicion en el mapa (del Body de box2d)
	private UUID matchID;
	private PlayerState state;
	
	public MatchUser(String nickName, UUID id, int defaultBody, Connection conn, int hp, int mana, Position pos, UUID matchID) {
		super(nickName, id, defaultBody, conn);
		this.hp = hp;
		this.mana = mana;
		this.pos = pos;
		this.matchID = matchID;
		this.state = PlayerState.NONE;
	}

	public void setPosition(float x, float y)
	{
		pos.set(x, y);
	}
	
	public void setState(String st)
	{
		state = PlayerState.valueOf(st);
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
	
	public PlayerState getState()
	{
		return state;
	}
}
