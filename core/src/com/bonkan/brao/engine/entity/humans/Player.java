package com.bonkan.brao.engine.entity.humans;

import java.util.ArrayList;
import java.util.UUID;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.bonkan.brao.engine.entity.Human;
import com.bonkan.brao.engine.map.factory.BodyFactory;
import com.bonkan.brao.engine.map.factory.Sensor;
import com.bonkan.brao.engine.utils.Constants;

/**
 *<p>El player.</p>
 */
public class Player extends Human {
	
	private int health;
	private int mana;
	private ArrayList<Sensor> sensors;
	private Vector2 lastPos;

	public Player(float x, float y, int bodyIndex, int headIndex, int health, int mana, UUID id, String userName, World world) {
		super(bodyIndex, headIndex, id, userName, world);
		
		sensors = new ArrayList<Sensor>();
		sensors.add(new Sensor(0));
		sensors.add(new Sensor(1));
		sensors.add(new Sensor(2));
		sensors.add(new Sensor(3));
		
		this.health = health;
		this.mana = mana;
		this.body = BodyFactory.createPlayerBox(world, x, y, Constants.BODY_WIDTH, Constants.BODY_HEIGHT, this);
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
		if(state == playerState.MOVE_LEFT_DOWN)
		{
			if(!sensors.get(0).getCollidingWithEnemy() && !sensors.get(3).getCollidingWithEnemy())
				body.setLinearVelocity(-256, -256);
			else if(!sensors.get(0).getCollidingWithEnemy())
				body.setLinearVelocity(-256, 0);
			else if(!sensors.get(3).getCollidingWithEnemy())
				body.setLinearVelocity(0, -256);
			else
				body.setLinearVelocity(0, 0);
		} else if (state == playerState.MOVE_LEFT_UP) {
			if(!sensors.get(0).getCollidingWithEnemy() && !sensors.get(2).getCollidingWithEnemy())
				body.setLinearVelocity(-256, 256);
			else if(!sensors.get(0).getCollidingWithEnemy())
				body.setLinearVelocity(-256, 0);
			else if(!sensors.get(2).getCollidingWithEnemy())
				body.setLinearVelocity(0, 256);
			else
				body.setLinearVelocity(0, 0);
		} else if(state == playerState.MOVE_RIGHT_DOWN)
		{
			if(!sensors.get(1).getCollidingWithEnemy() && !sensors.get(3).getCollidingWithEnemy())
				body.setLinearVelocity(256, -256);
			else if(!sensors.get(1).getCollidingWithEnemy())
				body.setLinearVelocity(256, 0);
			else if(!sensors.get(3).getCollidingWithEnemy())
				body.setLinearVelocity(0, -256);
			else
				body.setLinearVelocity(0, 0);
		} else if (state == playerState.MOVE_RIGHT_UP) {
			if(!sensors.get(1).getCollidingWithEnemy() && !sensors.get(2).getCollidingWithEnemy())
				body.setLinearVelocity(256, 256);
			else if(!sensors.get(1).getCollidingWithEnemy())
				body.setLinearVelocity(256, 0);
			else if(!sensors.get(2).getCollidingWithEnemy())
				body.setLinearVelocity(0, 256);
			else
				body.setLinearVelocity(0, 0);
		} 
		else if(state == playerState.MOVE_LEFT && !sensors.get(0).getCollidingWithEnemy())
			body.setLinearVelocity(-256, 0);
		else if(state == playerState.MOVE_RIGHT && !sensors.get(1).getCollidingWithEnemy())
			body.setLinearVelocity(256, 0);
		else if(state == playerState.MOVE_UP && !sensors.get(2).getCollidingWithEnemy())
			body.setLinearVelocity(0, 256);
		else if(state == playerState.MOVE_DOWN && !sensors.get(3).getCollidingWithEnemy())
			body.setLinearVelocity(0, -256);
		else if(state == playerState.NONE)
			body.setLinearVelocity(0, 0);
		else
			body.setLinearVelocity(0, 0);
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
