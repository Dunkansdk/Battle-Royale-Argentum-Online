package com.bonkan.brao.state.app;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.bonkan.brao.engine.utils.AssetsManager;
import com.bonkan.brao.state.AbstractGameState;
import com.bonkan.brao.state.GameStateManager;

public class LoadingState extends AbstractGameState {
	
	private BitmapFont font;
	private ShapeRenderer spRender;
	
	public LoadingState(GameStateManager gameState) {
		super(gameState);
		
		// cargo la fuente xq a esta altura ni el assetmanager se cargo !!
		FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("segoeui.ttf"));
 		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
 		parameter.size = 14;
 		font = freeTypeFontGenerator.generateFont(parameter);
		
 		spRender = new ShapeRenderer();
	}

	@Override
	public void update(float delta) {
		if(AssetsManager.update())
			gameState.setState(GameStateManager.State.LOGIN);
	}

	@SuppressWarnings("static-access")
	@Override
	public void render() {
		int progress = (int) (AssetsManager.getProgress() * 100);
		app.getBatch().begin();
		spRender.setProjectionMatrix(app.getBatch().getProjectionMatrix());
		spRender.begin(ShapeType.Filled);
		spRender.setColor(Color.WHITE);
		spRender.rect(0, 150, progress * app.V_WIDTH / 100, 20);
		spRender.end();
		app.getBatch().end();
		
		// lo separo porque sino se lo come el ShapeRender y no lo renderiza
		app.getBatch().begin();
		font.draw(app.getBatch(), "Loading: " + progress + "%", 300, 300);
		app.getBatch().end();
	}

	@Override
	public void dispose() {
		
	}

}
