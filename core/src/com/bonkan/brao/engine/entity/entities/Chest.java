package com.bonkan.brao.engine.entity.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bonkan.brao.engine.entity.Entity;
import com.bonkan.brao.engine.utils.AtlasManager;

public class Chest extends Entity {

	private TextureRegion texture;
	private int chestID;
	private boolean opened;
	
	public Chest(TextureRegion texture, float x, float y, int id) {
		super((int)x, (int)y);
		this.texture = texture;
		this.chestID = id;
		opened = false;
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(texture, location.x, location.y);
	}
	
	public TextureRegion getTexture() 
	{
		return texture;
	}
	
	public void open()
	{
		opened = true;
		texture = AtlasManager.getWorldSprite("opened_chest");
	}
	
	public boolean getOpened()
	{
		return opened;
	}
	
	public int getID()
	{
		return chestID;
	}
}
