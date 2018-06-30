package com.bonkan.brao.engine.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public abstract class Entity {

	protected TextureRegion texture;
	
	public Entity(TextureRegion texture) 
	{
		this.texture = texture;
	}
	
	abstract void update(float delta);
	abstract void render(SpriteBatch batch);
	abstract void dispose();

	public TextureRegion getTexture() {
		return texture;
	}

}
