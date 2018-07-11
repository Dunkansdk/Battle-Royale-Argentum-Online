package com.bonkan.brao.engine.entity.entities.human;

import java.util.UUID;

import com.badlogic.gdx.physics.box2d.World;
import com.bonkan.brao.engine.entity.entities.Human;

/**
 *<p>El player.</p>
 */
public class Player extends Human {
	
	private int maxHealth;
	private int maxMana;
	private int health;
	private int mana;

	public Player(int x, int y, int bodyIndex, int headIndex, int health, int mana, UUID id, String userName, World world) {
		super(x, y, bodyIndex, headIndex, id, userName, world);		
		this.health = health - 50;
		this.mana = mana;
		this.maxHealth = health;
		this.maxMana = mana;
		lastValidState = PlayerState.MOVE_DOWN;
	}
	
	public int getMaxHealth()
	{
		return maxHealth;
	}
	
	public int getMaxMana()
	{
		return maxMana;
	}
	
	public int getHealth() 
	{
		return health;
	}
	
	public int getMana() 
	{
		return mana;
	}
}
