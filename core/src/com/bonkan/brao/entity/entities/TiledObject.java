package com.bonkan.brao.entity.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.bonkan.brao.entity.Entity;

public class TiledObject extends Entity {

    private TiledMapTileLayer layer;
    private OrthogonalTiledMapRenderer map;

    public TiledObject(float x, float y, OrthogonalTiledMapRenderer map)
    {
        super(x, y);
        this.layer = null;
        this.map = map;
    }

	@Override
    public void render(SpriteBatch batch)
    {
    	map.getBatch().begin();
    	map.renderTileLayer(layer);
    	map.getBatch().end();
    }

    @Override
    public void update(float delta)
    {

    }

    public TiledMapTileLayer getLayer() {
        return layer;
    }

    public void updateLayer(TiledMapTileLayer layer) {
        this.layer = layer;
    }

}
