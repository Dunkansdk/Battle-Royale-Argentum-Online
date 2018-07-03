package com.bonkan.brao.engine.entity.humans;

import java.util.ArrayList;
import java.util.UUID;

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

	public Player(float x, float y, int bodyIndex, int headIndex, int health, int mana, UUID id, String userName, World world) {
		super(bodyIndex, headIndex, id, userName, world);
		this.health = health;
		this.mana = mana;
		this.body = BodyFactory.createPlayerBox(world, x, y, Constants.BODY_WIDTH, Constants.BODY_HEIGHT, this);
		sensors = new ArrayList<Sensor>();
		sensors.add(new Sensor(0));
		sensors.add(new Sensor(1));
		sensors.add(new Sensor(2));
		sensors.add(new Sensor(3));
	}
	
	public int getHealth() {
		return health;
	}
	
	public int getMana() {
		return mana;
	}
	
	public Sensor getSensor(int index) {
		System.out.println(sensors.size());
		return sensors.get(index);
	}

}
