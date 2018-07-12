package com.bonkan.brao.engine.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bonkan.brao.engine.input.KeyBindings;
import com.bonkan.brao.engine.utils.AssetsManager;
import com.bonkan.brao.engine.utils.Constants;

public class SpellSlot extends ItemSlot {

	// slots
	public static final int SLOT_SPELL_1 = 0;
	public static final int SLOT_SPELL_2 = 1;
	public static final int SLOT_SPELL_3 = 2;
	public static final int SLOT_SPELL_4 = 3;
	
	private int slot;

	public SpellSlot(float x, float y, int slot) {
		super(x, y, false, false);
		this.slot = slot;
	}

	@Override
	public void render(SpriteBatch batch)
	{
		// dibujamos el "slot"
		batch.draw(slotTexture, pos.x, pos.y);

		// dibujamos el item (si hay)
		if(!isEmpty)
		{
			batch.draw(item.getTexture(), pos.x + Constants.ITEM_SIZE / 2, pos.y + Constants.ITEM_SIZE / 2);
		
			switch(slot)
			{
				case SLOT_SPELL_1:
					glyphLayout.setText(AssetsManager.getDefaultFont(), KeyBindings.getKeyText(KeyBindings.KEY_SPELL_1));
					AssetsManager.getDefaultFont().draw(batch, KeyBindings.getKeyText(KeyBindings.KEY_SPELL_1), pos.x + 5, pos.y + 5 + glyphLayout.height);
					break;
					
				case SLOT_SPELL_2:
					glyphLayout.setText(AssetsManager.getDefaultFont(), KeyBindings.getKeyText(KeyBindings.KEY_SPELL_2));
					AssetsManager.getDefaultFont().draw(batch, KeyBindings.getKeyText(KeyBindings.KEY_SPELL_2), pos.x + 5, pos.y + 5 + glyphLayout.height);
					break;
					
				case SLOT_SPELL_3:
					glyphLayout.setText(AssetsManager.getDefaultFont(), KeyBindings.getKeyText(KeyBindings.KEY_SPELL_3));
					AssetsManager.getDefaultFont().draw(batch, KeyBindings.getKeyText(KeyBindings.KEY_SPELL_3), pos.x + 5, pos.y + 5 + glyphLayout.height);
					break;
					
				case SLOT_SPELL_4:
					glyphLayout.setText(AssetsManager.getDefaultFont(), KeyBindings.getKeyText(KeyBindings.KEY_SPELL_4));
					AssetsManager.getDefaultFont().draw(batch, KeyBindings.getKeyText(KeyBindings.KEY_SPELL_4), pos.x + 5, pos.y + 5 + glyphLayout.height);
					break;
			}
		}
	}
}
