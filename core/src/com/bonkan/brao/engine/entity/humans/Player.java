package com.bonkan.brao.engine.entity.humans;

import java.util.UUID;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bonkan.brao.engine.entity.Human;

/**
 *<p>El player.</p>
 */
public class Player extends Human {
	
	private int health;
	private int mana;
	private Vector2 lastPos;

	public Player(float x, float y, int bodyIndex, int headIndex, int health, int mana, UUID id, String userName, World world) {
		super(x, y, bodyIndex, headIndex, id, userName, world);		
		this.health = health;
		this.mana = mana;
		this.lastPos = new Vector2(x, y);
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getMana() {
		return mana;
	}
		
	public void setPos(float x, float y)
	{
		pos.x = x;
		pos.y = y;
	}
	
	public void setLastPos(float x, float y)
	{
		lastPos.x = x;
		lastPos.y = y;
	}
	
	public Vector2 getLastPos()
	{
		return lastPos;
	}
	
}
