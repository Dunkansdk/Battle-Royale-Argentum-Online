package com.bonkan.brao.server.utils;

import java.awt.Rectangle;

/**
 * <p>Funciones auxiliares y otras yerbas</p>
 */
public class CommonUtils {

	public static final int VIEWPORT_WIDTH = 1280;
	public static final int VIEWPORT_HEIGHT = 720;
	
	/**
	 * <p>Chequea si dos usuarios (sus posiciones) est�n en la misma �rea de visi�n.</p>
	 * <p>Es f�cil de hacer porque el �rea de visi�n es CONSTANSTE para todos los usuarios
	 * mas all� de la resoluci�n, lo hacemos as� para evitar que usuarios con mayores resoluciones
	 * tengan ventaja.</p>
	 * @param p1	&emsp;{@link com.bonkan.brao.server.utils.Position Position} posici�n del player1
	 * @param p2	&emsp;{@link com.bonkan.brao.server.utils.Position Position} posici�n del player1
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
