package com.bonkan.brao.server.users;

import java.util.UUID;

import com.bonkan.brao.server.ui.ServerInterface;
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
	
	private int equippedWeapon;
	private int equippedShield;
	
	// guardo estos aca para evitar problemas
	private int equippedWeaponRarity;
	private int equippedShieldRarity; 
	
	public MatchUser(String nickName, UUID id, int defaultBody, Connection conn, int hp, int mana, Position pos, UUID matchID) {
		super(nickName, id, defaultBody, conn);
		this.hp = hp;
		this.mana = mana;
		this.pos = pos;
		this.matchID = matchID;
		this.state = PlayerState.NONE;
		this.equippedShield = -1;
		this.equippedWeapon = -1;
	}
	
	public void update()
	{
		if(state == PlayerState.MOVE_DOWN)
			this.pos.set(this.pos.getX(), this.pos.getY() - 2);
		else if(state == PlayerState.MOVE_UP)
			this.pos.set(this.pos.getX(), this.pos.getY() + 2);
		else if(state == PlayerState.MOVE_LEFT_DOWN)
			this.pos.set(this.pos.getX() - 2, this.pos.getY() - 2);
		else if(state == PlayerState.MOVE_RIGHT_DOWN)
			this.pos.set(this.pos.getX() + 2, this.pos.getY() - 2);
		else if(state == PlayerState.MOVE_LEFT_UP)
			this.pos.set(this.pos.getX() - 2, this.pos.getY() + 2);
		else if(state == PlayerState.MOVE_RIGHT_UP)
			this.pos.set(this.pos.getX() + 2, this.pos.getY() + 2);
		else if(state == PlayerState.MOVE_RIGHT)
			this.pos.set(this.pos.getX() + 2, this.pos.getY());
		else if(state == PlayerState.MOVE_LEFT)
			this.pos.set(this.pos.getX() - 2, this.pos.getY());
		
		if(state != PlayerState.NONE)
			ServerInterface.addMessage("EL PLAYER " + this.getNickName() + " SE MOVIO A " + this.getPos().getX() + ", " + this.getPos().getY());
	}

	public void setEquippedWeapon(int weapon, int rarity)
	{
		equippedWeapon = weapon;
		equippedWeaponRarity = rarity;
	}
	
	public void setEquippedShield(int shield, int rarity)
	{
		equippedShield = shield;
		equippedShieldRarity = rarity;
	}
	
	public void setPosition(int x, int y)
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
	
	public int getEquippedWeapon()
	{
		return equippedWeapon;
	}
	
	public int getEquippedShield()
	{
		return equippedShield;
	}
	
	public int getEquippedWeaponRarity()
	{
		return equippedWeaponRarity;
	}
	
	public int getEquippedShieldRarity()
	{
		return equippedShieldRarity;
	}
}
