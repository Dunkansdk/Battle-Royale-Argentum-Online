package com.bonkan.brao.engine.entity.entities;

import java.util.UUID;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bonkan.brao.engine.entity.Entity;

public class Item extends Entity {

	public static final int RARITY_COMMON = 1;
	public static final int RARITY_RARE = 2;
	public static final int RARITY_EPIC = 3;
	public static final int RARITY_LEGENDARY = 4;
	
	private TextureRegion texture;
	private UUID itemID;
	private int rarity;
	private String name;

	public Item(int x, int y, int rarity, String name, TextureRegion texture, UUID itemID) {
		super(x, y);
		this.itemID = itemID;
		this.rarity = rarity;
		this.texture = texture;
		this.name = name;
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(texture, location.x, location.y);
	}
	
	public int getRarity()
	{
		return rarity;
	}
	
	public UUID getID()
	{
		return itemID;
	}
	
	public String getName()
	{
		return name;
	}

}
