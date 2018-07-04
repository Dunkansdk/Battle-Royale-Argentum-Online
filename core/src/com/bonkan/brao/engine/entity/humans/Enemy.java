package com.bonkan.brao.engine.entity.humans;

import java.util.UUID;

import com.bonkan.brao.engine.entity.Human;

public class Enemy extends Human {

	public Enemy(float x, float y, int bodyIndex, int headIndex, UUID id, String userName) {
		super(bodyIndex, headIndex, id, userName, x, y);
	}
	
	@Override
	public void update(float delta)
	{

	}
	
}