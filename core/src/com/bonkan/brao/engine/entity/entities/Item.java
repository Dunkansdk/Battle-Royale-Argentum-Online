package com.bonkan.brao.engine.entity.entities;

import java.awt.Rectangle;
import java.util.UUID;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.bonkan.brao.engine.entity.Entity;
import com.bonkan.brao.engine.ui.ItemTooltip;
import com.bonkan.brao.engine.utils.Constants;

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
	private String desc;
	private int type;
	private int amount;
	private int itemIndex;
	
	private ShaderProgram outline;
	
	public Item(int x, int y, int rarity, int amount, String name, String desc, TextureRegion texture, String animTexture, int type, UUID itemID, int itemIndex) {
		super(x, y);
		this.itemID = itemID;
		this.rarity = rarity;
		this.amount = amount;
		this.texture = texture;
		this.name = name;
		this.desc = desc;
		this.type = type;
		this.animTexture = animTexture;
		this.itemIndex = itemIndex;
		loadShader();
	}
	
	public void loadShader() 
	{
		String vertexShader;
		String fragmentShader;
		vertexShader = Gdx.files.internal("GlowVertex.glsl").readString();
		fragmentShader = Gdx.files.internal("GlowFragment.glsl").readString();
		outline = new ShaderProgram(vertexShader, fragmentShader);
		if (!outline.isCompiled()) throw new GdxRuntimeException("Couldn't compile shader: " + outline.getLog());
	}

	@Override
	public void update(float delta) {}
	
	public void update(float delta, int mouseX, int mouseY) 
	{
		Rectangle rect = new Rectangle((int) location.x, (int) location.y, Constants.ITEM_SIZE, Constants.ITEM_SIZE);
	
		if(rect.contains(mouseX, mouseY)) 
		{
			ItemTooltip.setPosition(mouseX, mouseY);
			ItemTooltip.setTitle(name + " +" + rarity);
			ItemTooltip.setDesc(desc);
			
			switch(rarity)
			{
				case RARITY_COMMON:
					ItemTooltip.setColor(Color.WHITE);
					break;
					
				case RARITY_RARE:
					ItemTooltip.setColor(Constants.RARE_ITEM_GLOW_COLOR);
					break;
					
				case RARITY_EPIC:
					ItemTooltip.setColor(Constants.EPIC_ITEM_GLOW_COLOR);
					break;
					
				case RARITY_LEGENDARY:
					ItemTooltip.setColor(Constants.LEGENDARY_ITEM_GLOW_COLOR);
					break;
			}
			
			ItemTooltip.setVisible(true);
		}
	}

	@Override
	public void render(SpriteBatch batch) {
		// Seteamos el shader
		outline.begin();
		outline.setUniformf("u_viewportInverse", new Vector2(1f / (texture.getRegionWidth() + 5), 1f / (texture.getRegionHeight() + 5)));
		outline.setUniformf("u_offset", 0.4f);
		outline.setUniformf("u_step", Math.min(1f, texture.getRegionWidth() / 70f));
		outline.setUniformf("u_color", new Vector3(0, 0, 255));
		outline.end();
		
		// Dibujamos el glow
		batch.setShader(outline);
		batch.begin();
		batch.draw(texture, location.x - 2, location.y);
		batch.end();
		
		// Dibujamos el item
		batch.setShader(null);
		batch.begin();
		batch.draw(texture, location.x, location.y);
		batch.end();
	}
	
	public int getIndex()
	{
		return itemIndex;
	}
	
	public String getAnimTexture()
	{
		return animTexture;
	}
	
	public int getAmount()
	{
		return amount;
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
