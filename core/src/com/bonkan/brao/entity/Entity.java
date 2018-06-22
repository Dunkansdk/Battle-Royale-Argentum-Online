package com.bonkan.brao.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

@SuppressWarnings("rawtypes")
public abstract class Entity implements Comparable {

    protected float x;
    protected float y;

    public Entity(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public abstract void render(SpriteBatch batch);
    public abstract void update(float delta);

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    /**
     * Permite hacer el sorting
     */
    @Override
    public int compareTo(Object o) {
        if(o != null && o instanceof Entity) {
            if(y > ((Entity) o).y) return -1;
        }
        return 1;
    }

}
