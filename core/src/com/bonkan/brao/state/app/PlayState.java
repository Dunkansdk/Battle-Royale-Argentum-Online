package com.bonkan.brao.state.app;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.bonkan.brao.entity.entities.EntityManager;
import com.bonkan.brao.state.AbstractGameState;
import com.bonkan.brao.state.GameStateManager;

public class PlayState extends AbstractGameState {

    private OrthogonalTiledMapRenderer tiled;
    private TiledMap map;
    private EntityManager entity;

    public PlayState(GameStateManager gameState) {
        super(gameState);

        map = new TmxMapLoader().load("map1.tmx");
        tiled = new OrthogonalTiledMapRenderer(map);
        entity = new EntityManager();
    }

    @Override
    public void update(float delta)
    {
        tiled.setView(camera);
        entity.update(delta);
    }

    @Override
    public void render()
    {
        tiled.render();
        entity.render(app.getBatch());
    }

    @Override
    public void dispose() {
        tiled.dispose();
        map.dispose();
    }
}