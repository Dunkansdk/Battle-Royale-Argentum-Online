package com.bonkan.brao.engine.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bonkan.brao.engine.utils.AssetsManager;

public class OptionWindow extends Stage {
	
	private Window window;

	public OptionWindow() {
		super(new ScreenViewport());
		
		window = new Window("Opciones", AssetsManager.getDefaultSkin());
		window.setBounds(Gdx.graphics.getWidth() / 2 - 75, Gdx.graphics.getHeight() / 2 - 35, 150, 70);
	}

}
