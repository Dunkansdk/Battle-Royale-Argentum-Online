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
	
	@Override
	public void update(float delta)
	{
		if(state == playerState.MOVE_DOWN)
			body.setLinearVelocity(0, -256);
		if(state == playerState.MOVE_LEFT)
			body.setLinearVelocity(256, 0);
		if(state == playerState.MOVE_RIGHT)
			body.setLinearVelocity(-256, 0);
		if(state == playerState.MOVE_UP)
			body.setLinearVelocity(0, 256);
	}
	
}