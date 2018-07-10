package com.bonkan.brao.engine.entity.entities;

import java.util.UUID;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bonkan.brao.engine.entity.Entity;

/**
 * <p>Clase que maneja los items como ENTIDADES (es decir, los que están tirados en el piso
 * y, por ende, renderizados en el mapa).</p>
 */
public class Item extends Entity {

	public static final int RARITY_COMMON = 1;
	public static final int RARITY_RARE = 2;
	public static final int RARITY_EPIC = 3;
	public static final int RARITY_LEGENDARY = 4;
	
	private TextureRegion texture;
	private String animTexture; // es el ID del item en el atlas, no guardamos la textura porque hay distintos atlas para distintos tipos de items
	private UUID itemID;
	private int rarity;
	private String name;
	private int type;

	public Item(int x, int y, int rarity, String name, TextureRegion texture, String animTexture, int type, UUID itemID) {
		super(x, y);
		this.itemID = itemID;
		this.rarity = rarity;
		this.texture = texture;
		this.name = name;
		this.type = type;
		this.animTexture = animTexture;
	}

	@Override
	public void update(float delta) {
		
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(texture, location.x, location.y);
	}
	
	public String getAnimTexture()
	{
		return animTexture;
	}
	
	public int getType()
	{
		return type;
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

	// para los ItemSlots
	public TextureRegion getTexture()
	{
		return texture;
	}
}
