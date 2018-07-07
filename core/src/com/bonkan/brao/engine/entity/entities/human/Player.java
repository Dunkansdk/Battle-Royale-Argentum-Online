package com.bonkan.brao.engine.entity.entities.human;

import java.util.UUID;

import com.badlogic.gdx.physics.box2d.World;
import com.bonkan.brao.engine.entity.entities.Human;

/**
 *<p>El player.</p>
 */
public class Player extends Human {
	
	private int health;
	private int mana;
	private PlayerState lastValidState; // ultimo state distinto de NONE (para chequear orientacion)
	
	public Player(int x, int y, int bodyIndex, int headIndex, int health, int mana, UUID id, String userName, World world) {
		super(x, y, bodyIndex, headIndex, id, userName, world);		
		this.health = health;
		this.mana = mana;
		lastValidState = PlayerState.MOVE_DOWN;
	}
	
	public int getHealth() 
	{
		return health;
	}
	
	public int getMana() 
	{
		return mana;
	}
	
	public void setLastValidState(PlayerState state)
	{
		lastValidState = state;
	}
	
	public PlayerState getLastValidState()
	{
		return lastValidState;
	}
}
