package com.bonkan.brao.engine.ui;

import java.awt.Rectangle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bonkan.brao.engine.entity.EntityManager;
import com.bonkan.brao.engine.entity.entities.Item;
import com.bonkan.brao.engine.input.KeyBindings;
import com.bonkan.brao.engine.utils.AssetsManager;
import com.bonkan.brao.engine.utils.Constants;

public class ItemSlot {

	protected Vector2 pos;
	protected Texture slotTexture;
	protected boolean isEmpty;
	protected boolean isRedPot;
	protected boolean isBluePot;
	protected Item item;
	protected GlyphLayout glyphLayout; // para las potas
	
	// slots
	public static final int INVENTORY_WEAPON_SLOT = 0;
	public static final int INVENTORY_SHIELD_SLOT = 1;
	public static final int INVENTORY_HELMET_SLOT = 2;
	public static final int INVENTORY_RED_POTION_SLOT = 3;
	public static final int INVENTORY_BLUE_POTION_SLOT = 4;

	public ItemSlot(float x, float y, boolean isRedPot, boolean isBluePot)
	{
		this.pos = new Vector2(x, y);
		this.isEmpty = true;
		this.slotTexture = AssetsManager.getTexture("slot.png");
		this.isRedPot = isRedPot;
		this.isBluePot = isBluePot;
		this.glyphLayout = new GlyphLayout();
	}
	
	public void setItem(Item i)
	{
		this.item = i;
		this.isEmpty = false;
	}
	
	public void unequip()
	{
		this.isEmpty = true;
	}
	
	public void render(SpriteBatch batch)
	{
		// dibujamos el "slot"
		batch.draw(slotTexture, pos.x, pos.y);

		// dibujamos el item (si hay)
		if(!isEmpty)
			batch.draw(item.getTexture(), pos.x + Constants.ITEM_SIZE / 2, pos.y + Constants.ITEM_SIZE / 2);
	
		// si es pota, dibujamos la hotkey
		if(isRedPot && !isEmpty)
		{
			glyphLayout.setText(AssetsManager.getDefaultFont(), KeyBindings.getKeyText(KeyBindings.KEY_RED_POTION));
			AssetsManager.getDefaultFont().draw(batch, KeyBindings.getKeyText(KeyBindings.KEY_RED_POTION), pos.x + 5, pos.y + 5 + glyphLayout.height);
			glyphLayout.setText(AssetsManager.getDefaultFont(), String.valueOf(EntityManager.getPlayer().getRedPotionsAmount()));
			AssetsManager.getDefaultFont().draw(batch, String.valueOf(EntityManager.getPlayer().getRedPotionsAmount()), pos.x + 60 - glyphLayout.width, pos.y + 60);
		}
		else if(isBluePot && !isEmpty)
		{
			glyphLayout.setText(AssetsManager.getDefaultFont(), KeyBindings.getKeyText(KeyBindings.KEY_BLUE_POTION));
			AssetsManager.getDefaultFont().draw(batch, KeyBindings.getKeyText(KeyBindings.KEY_BLUE_POTION), pos.x + 5, pos.y + 5 + glyphLayout.height);
			glyphLayout.setText(AssetsManager.getDefaultFont(), String.valueOf(EntityManager.getPlayer().getBluePotionsAmount()));
			AssetsManager.getDefaultFont().draw(batch, String.valueOf(EntityManager.getPlayer().getBluePotionsAmount()), pos.x + 60 - glyphLayout.width, pos.y + 60);
		}
	}
	
	// para ver si se clickeo el slot
	public Rectangle getRect()
	{
		return new Rectangle((int) pos.x, (int) pos.y, Constants.ITEM_SIZE * 2, Constants.ITEM_SIZE * 2);
	}
	
	public boolean isEmpty()
	{
		return isEmpty;
	}
	
	public Item getItem()
	{
		return item;
	}
}
