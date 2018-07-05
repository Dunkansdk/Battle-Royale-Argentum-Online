package com.bonkan.brao.engine.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * <p>Sistema básico de entidades.</p>
 */
public abstract class Entity {

	protected TextureRegion texture;
	protected Vector2 pos;
	
	public Entity(TextureRegion texture, int x, int y) 
	{
		this.texture = texture;
		this.pos = new Vector2(x, y);
	}
	
	public abstract void update(float delta);
	public abstract void render(SpriteBatch batch);
	public abstract void dispose();

	public TextureRegion getTexture() {
		return texture;
	}
	
	public Vector2 getPos() {
		return pos;
	}

}
