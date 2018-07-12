package com.bonkan.brao.engine.utils;

import com.badlogic.gdx.graphics.Color;

/**
 * <p>Clase auxiliar con constantes comunes.</p>
 */
public class Constants {

	// constantes varias
	public static final int BODY_WIDTH = 25;
	public static final int BODY_HEIGHT = 40;
	public static final int ITEM_SIZE = 32;
	
	// tipos de items
	public static final int ITEM_TYPE_SHIELD = 0;
	public static final int ITEM_TYPE_WEAPON = 1;
	public static final int ITEM_TYPE_HELMET = 2;
	public static final int ITEM_TYPE_RED_POTION = 3;
	public static final int ITEM_TYPE_BLUE_POTION = 4;
	public static final int ITEM_TYPE_SPELL = 5;
	
	public static final int RED_POTION_INDEX = 3;
	public static final int BLUE_POTION_INDEX = 4;
	
	public static final Color RARE_ITEM_GLOW_COLOR = new Color(22 / 255f, 100 / 255f, 226 / 255f, 255 / 255f);
	public static final Color EPIC_ITEM_GLOW_COLOR = new Color(182 / 255f, 18 / 255f, 232 / 255f, 255 / 255f);
	public static final Color LEGENDARY_ITEM_GLOW_COLOR = new Color(232 / 255f, 150 / 255f, 18 / 255f, 255 / 255f);

}
