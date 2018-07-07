package com.bonkan.brao.engine.entity.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bonkan.brao.engine.entity.Entity;

public class WorldObject extends Entity {

	public WorldObject(TextureRegion texture, float x, float y) {
		super(texture, (int)x, (int)y);
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(texture, location.x, location.y);
	}

	@Override
	public void dispose() {

	}

}
