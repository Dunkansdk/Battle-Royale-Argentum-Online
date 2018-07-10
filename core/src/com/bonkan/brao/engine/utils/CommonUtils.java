package com.bonkan.brao.engine.utils;

import java.awt.Rectangle;

import com.badlogic.gdx.math.Vector2;
import com.bonkan.brao.Game;

public class CommonUtils {

	@SuppressWarnings("static-access")
	public static boolean areInViewport(Game app, Vector2 p1, Vector2 p2)
	{
		// usamos rects de java awt para chequear
		Rectangle rect1 = new Rectangle((int) p1.x - app.V_WIDTH / 2, (int) p1.y - app.V_HEIGHT / 2, app.V_WIDTH, app.V_HEIGHT);
		Rectangle rect2 = new Rectangle((int) p2.x - app.V_WIDTH / 2, (int) p2.y - app.V_HEIGHT / 2, app.V_WIDTH, app.V_HEIGHT);
		
		return rect1.intersects(rect2);
	}

}
