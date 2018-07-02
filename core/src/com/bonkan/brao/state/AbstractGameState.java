package com.bonkan.brao.state;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bonkan.brao.Game;

/**
 * <p>Clase abstracta que agrupa el funcionamiento de los estados del juego.</p>
 */
public abstract class AbstractGameState {

    protected GameStateManager gameState;
    protected Game app;
    protected SpriteBatch batch;
    protected OrthographicCamera camera;

    public AbstractGameState(GameStateManager gameState) {
        this.gameState = gameState;
        this.app = gameState.getApp();
        batch = app.getBatch();
        camera = app.getCamera();
    }

    public abstract void update(float delta);
    public abstract void render();
    public abstract void dispose();

    public void resize(int width, int height) {
        //camera.setToOrtho(false, width, height);
    }

}
