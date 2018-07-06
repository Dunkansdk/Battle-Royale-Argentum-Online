package com.bonkan.brao.engine.entity.humans;

import java.util.UUID;

import com.badlogic.gdx.physics.box2d.World;
import com.bonkan.brao.engine.entity.Human;

public class Enemy extends Human {

	public Enemy(int x, int y, int bodyIndex, int headIndex, UUID id, String userName, World world) {
		super(x, y, bodyIndex, headIndex, id, userName, world);
	}
	
}