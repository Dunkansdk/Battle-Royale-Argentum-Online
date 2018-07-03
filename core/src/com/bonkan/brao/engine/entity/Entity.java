package com.bonkan.brao.engine.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * <p>Sistema básico de entidades.</p>
 */
public abstract class Entity {

	protected TextureRegion texture;
	protected Body body;
	
	public Entity(TextureRegion texture) 
	{
		this.texture = texture;
	}
	
	public abstract void update(float delta);
	public abstract void render(SpriteBatch batch);
	public abstract void dispose();

	public TextureRegion getTexture() {
		return texture;
	}

}
