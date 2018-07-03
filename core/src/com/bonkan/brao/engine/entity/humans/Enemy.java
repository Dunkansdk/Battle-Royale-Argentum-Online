package com.bonkan.brao.engine.entity.humans;

import java.util.UUID;

import com.badlogic.gdx.physics.box2d.World;
import com.bonkan.brao.engine.entity.Human;
import com.bonkan.brao.engine.map.factory.BodyFactory;
import com.bonkan.brao.engine.utils.Constants;

public class Enemy extends Human {

	public Enemy(float x, float y, int bodyIndex, int headIndex, UUID id, String userName, World world) {
		super(bodyIndex, headIndex, id, userName, world);
		this.body = BodyFactory.createPlayerBox(world, x, y, Constants.BODY_WIDTH, Constants.BODY_HEIGHT, this);
	}
	
}