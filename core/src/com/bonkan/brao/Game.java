package com.bonkan.brao;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bonkan.brao.state.GameStateManager;

public class Game extends ApplicationAdapter {

	public boolean DEBUG = false;

	// Game information
	public static final String TILTE = "Battle Royale AO";
	public static final float SCALE = 1.0f;
	public static final int V_WIDTH = 720;
	public static final int V_HEIGHT = 480;

	private OrthographicCamera camera;
	private GameStateManager gameState;
	private SpriteBatch batch;

	@Override
	public void create () {
		float width = Gdx.graphics.getWidth();
		float height = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, width / SCALE, height / SCALE);
		batch = new SpriteBatch();
		gameState = new GameStateManager(this);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameState.update(Gdx.graphics.getDeltaTime());
		gameState.render();
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) Gdx.app.exit();
	}

	@Override
	public void resize(int width, int height) {
		gameState.resize((int)(width / SCALE), (int)(height / SCALE));
	}

	@Override
	public void dispose () {
		gameState.dispose();
		batch.dispose();
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public SpriteBatch getBatch() {
		return batch;
	}
}
