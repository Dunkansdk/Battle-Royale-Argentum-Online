package com.bonkan.brao.entity.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.bonkan.brao.entity.Entity;

public class ObjectMap extends Entity {

    private TiledMapTileLayer layer;

    public ObjectMap(float x, float y)
    {
        super(x, y);
        this.layer = null;
    }

    @Override
    public void render(SpriteBatch batch)
    {

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
