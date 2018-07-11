package com.bonkan.brao.server.utils;

import java.awt.Rectangle;

/**
 * <p>Funciones auxiliares y otras yerbas</p>
 */
public class CommonUtils {

	public static final int VIEWPORT_WIDTH = 1280;
	public static final int VIEWPORT_HEIGHT = 720;
	
	public static final int BODY_WIDTH = 25;
	public static final int BODY_HEIGHT = 40;
	
	public static final int ITEM_SIZE = 32;
	
	// tipos de items
	public static final int ITEM_TYPE_SHIELD = 0;
	public static final int ITEM_TYPE_WEAPON = 1;
	public static final int ITEM_TYPE_HELMET = 2;
	
	// slots de items
	public static final int INVENTORY_WEAPON_SLOT = 0;
	public static final int INVENTORY_SHIELD_SLOT = 1;
	public static final int INVENTORY_HELMET_SLOT = 2;
	public static final int INVENTORY_RED_POTION_SLOT = 3;
	public static final int INVENTORY_BLUE_POTION_SLOT = 4;
	
	/**
	 * <p>Chequea si dos usuarios (sus posiciones) están en la misma área de visión.</p>
	 * <p>Es fácil de hacer porque el área de visión es CONSTANSTE para todos los usuarios
	 * mas allá de la resolución, lo hacemos así para evitar que usuarios con mayores resoluciones
	 * tengan ventaja.</p>
	 * @param p1	&emsp;{@link com.bonkan.brao.server.utils.Position Position} posición del player1
	 * @param p2	&emsp;{@link com.bonkan.brao.server.utils.Position Position} posición del player1
	 * @return	boolean
	 */
	public static boolean areInViewport(Position p1, Position p2)
	{
		// usamos rects de java awt para chequear
		Rectangle rect1 = new Rectangle((int) p1.getX() - VIEWPORT_WIDTH / 2, (int) p1.getY() - VIEWPORT_HEIGHT / 2, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		Rectangle rect2 = new Rectangle((int) p2.getX() - VIEWPORT_WIDTH / 2, (int) p2.getY() - VIEWPORT_HEIGHT / 2, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		
		return rect1.intersects(rect2);
	}

}
