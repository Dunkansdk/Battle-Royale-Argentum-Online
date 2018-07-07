package com.bonkan.brao.engine.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * <p>Sistema básico de entidades.</p>
 */
public abstract class Entity {

	protected Vector2 location;
	
	public Entity(int x, int y) 
	{
		this.location = new Vector2(x, y);
	}
	
	public abstract void update(float delta);
	public abstract void render(SpriteBatch batch);

	public Vector2 getPos() {
		return location;
	}

}
