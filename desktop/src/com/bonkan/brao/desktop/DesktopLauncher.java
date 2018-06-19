package com.bonkan.brao.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.bonkan.brao.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Game.TILTE;
		config.width = Game.V_WIDTH;
		config.height = Game.V_HEIGHT;
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;
		new LwjglApplication(new Game(), config);
	}
}
