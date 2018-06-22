package com.bonkan.brao.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.ArrayList;
import java.util.Collections;

public class EntityManager {

    private ArrayList<Entity> entities;

    public EntityManager() {
        entities = new ArrayList<Entity>();
    }

    public void insert(Entity e) {
        entities.add(e);
    }
    
    public void insertAll(ArrayList<Entity> e) {
    	entities.addAll(e);
    }

    /**
     * 
     * @param batch	SpriteBatch del GameApp
     * @param map	objetos del mapa
     */
    public void render(SpriteBatch batch, OrthogonalTiledMapRenderer map) {
    	for(Entity entity : entities) {
            batch.begin();
            entity.render(batch);
            batch.end();
        }
    }

    @SuppressWarnings("unchecked")
	public void update(float delta) {
        Collections.sort(entities); // Render Sorting
        for(Entity entity : entities) {
            entity.update(delta);
        }
    }

}
