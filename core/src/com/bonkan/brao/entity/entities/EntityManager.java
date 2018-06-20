package com.bonkan.brao.entity.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bonkan.brao.entity.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class EntityManager {

    private ArrayList<Entity> entities;

    public EntityManager() {
        entities = new ArrayList<Entity>();
    }

    public void insert(Entity e) {
        entities.add(e);
    }

    public void render(SpriteBatch batch) {
        for(Entity entity : entities) {
            entity.render(batch);
        }
    }

    public void update(float delta) {
        Collections.sort(entities); // Render Sorting
        for(Entity entity : entities) {
            entity.update(delta);
        }
    }

}
