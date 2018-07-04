package com.bonkan.brao.engine.entity.humans;

import java.util.ArrayList;
import java.util.UUID;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bonkan.brao.engine.entity.Human;
import com.bonkan.brao.engine.map.factory.Sensor;

/**
 *<p>El player.</p>
 */
public class Player extends Human {
	
	private int health;
	private int mana;
	private ArrayList<Sensor> sensors;
	private Vector2 lastPos;

	public Player(float x, float y, int bodyIndex, int headIndex, int health, int mana, UUID id, String userName, World world) {
		super(bodyIndex, headIndex, id, userName, x, y);
		
		sensors = new ArrayList<Sensor>();
		sensors.add(new Sensor(0));
		sensors.add(new Sensor(1));
		sensors.add(new Sensor(2));
		sensors.add(new Sensor(3));
		
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
	
	public Sensor getSensor(int index) {
		return sensors.get(index);
	}

	@Override
	public void update(float delta)
	{

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
