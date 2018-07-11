package com.bonkan.brao.engine.ui;

import java.awt.Rectangle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.bonkan.brao.engine.entity.entities.Item;
import com.bonkan.brao.engine.utils.AssetsManager;
import com.bonkan.brao.engine.utils.Constants;

public class ItemSlot {

	private Vector2 pos;
	private Texture slotTexture;
	private boolean isEmpty;
	private boolean isRedPot;
	private boolean isBluePot;
	private Item item;
	private GlyphLayout glyphLayout; // para las potas
	
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
		if(isRedPot)
		{
			glyphLayout.setText(AssetsManager.getDefaultFont(), "Q");
			AssetsManager.getDefaultFont().draw(batch, "Q", pos.x + 5, pos.y + 5 + glyphLayout.height);
		}
		else if(isBluePot)
		{
			glyphLayout.setText(AssetsManager.getDefaultFont(), "R");
			AssetsManager.getDefaultFont().draw(batch, "R", pos.x + 5, pos.y + 5 + glyphLayout.height);
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
