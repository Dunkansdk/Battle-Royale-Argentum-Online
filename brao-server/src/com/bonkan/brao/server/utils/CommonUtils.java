package com.bonkan.brao.server.utils;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.bonkan.brao.server.users.MatchUser;

/**
 * <p>Funciones auxiliares y otras yerbas</p>
 */
public class CommonUtils {

	public static final int VIEWPORT_WIDTH = 1280;
	public static final int VIEWPORT_HEIGHT = 720;
	
	public static final int BODY_WIDTH = 25;
	public static final int BODY_HEIGHT = 40;
	
	public static final int DIR_DOWN = 0;
	public static final int DIR_UP = 1;
	public static final int DIR_LEFT = 2;
	public static final int DIR_RIGHT = 3;
	
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

    /**
     * <p>Chequea las direcciones bloqueadas que tiene un usuario con respecto a la posición de todos los otros usuarios</p>
     * @param users
     * @param userID
     * @param x
     * @param y
     * @return
     */
    public static boolean[] blockedDirections(HashMap<UUID, MatchUser> users, UUID userID, int x, int y)
    {
    	boolean[] ret = new boolean[8];
    	MatchUser player = users.get(userID);
    	
    	if(player != null)
    	{
    		int realX = (int) x - BODY_WIDTH / 2;
        	int realY = (int) y - BODY_HEIGHT / 2;
        	
        	Rectangle playerRectLeft = new Rectangle(realX - 2, realY, BODY_WIDTH, BODY_HEIGHT);
        	Rectangle playerRectRight = new Rectangle(realX + 2, realY, BODY_WIDTH, BODY_HEIGHT);
        	Rectangle playerRectUp = new Rectangle(realX, realY + 2, BODY_WIDTH, BODY_HEIGHT);
        	Rectangle playerRectDown = new Rectangle(realX, realY - 2, BODY_WIDTH, BODY_HEIGHT);
        	
        	for (Map.Entry<UUID, MatchUser> mu : users.entrySet()) 
        	{
    			if(!mu.getValue().getID().equals(userID))
    			{
    				Rectangle enemyRect = new Rectangle((int) mu.getValue().getPos().getX() - BODY_WIDTH / 2, (int) mu.getValue().getPos().getY() - BODY_HEIGHT / 2, BODY_WIDTH, BODY_HEIGHT);
    				
    				if(enemyRect.intersects(playerRectLeft)) ret[DIR_LEFT] = true;
    				if(enemyRect.intersects(playerRectRight)) ret[DIR_RIGHT] = true;
    				if(enemyRect.intersects(playerRectUp)) ret[DIR_UP] = true;
    				if(enemyRect.intersects(playerRectDown)) ret[DIR_DOWN] = true;
    			}
    		}
    	}

    	return ret;
    }
	
}
