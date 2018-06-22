package com.bonkan.brao.state.app;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.bonkan.brao.entity.EntityManager;
import com.bonkan.brao.entity.entities.character.Player;
import com.bonkan.brao.state.AbstractGameState;
import com.bonkan.brao.state.GameStateManager;
import com.bonkan.brao.utils.Utils;

public class PlayState extends AbstractGameState {

    private OrthogonalTiledMapRenderer tiled;
    private TiledMap map;
    private EntityManager entity;

    public PlayState(GameStateManager gameState) {
        super(gameState);
        
        map = new TmxMapLoader().load("map1.tmx");
        tiled = new OrthogonalTiledMapRenderer(map);
        entity = new EntityManager();        
        
        // Agregamos todos los objetos del TiledMap al EntityManager para ordenarlos
        entity.insertAll(Utils.splitLayer(map, tiled, "objects"));
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
    	// Dibujamos el suelo, siempre atras
    	int[] floor = {0};
    	tiled.render(floor);
    	
    	// Dibujamos los objetos del mapa y el resto de las entidades
        entity.render(app.getBatch(), tiled);
    }

    @Override
    public void dispose() {
        tiled.dispose();
        map.dispose();
    }
}