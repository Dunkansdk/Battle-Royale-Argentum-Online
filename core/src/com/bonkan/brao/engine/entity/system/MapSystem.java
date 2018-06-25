package com.bonkan.brao.engine.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.bonkan.brao.engine.entity.component.TiledMapComponent;

public class MapSystem extends IteratingSystem {

	private OrthogonalTiledMapRenderer tiledRenderer;
    private OrthographicCamera camera;
    
    private ComponentMapper<TiledMapComponent> tm;

    public MapSystem(OrthogonalTiledMapRenderer tiledRenderer, OrthographicCamera camera) {
        super(Family.all(TiledMapComponent.class).get());
        tm = ComponentMapper.getFor(TiledMapComponent.class);
        this.tiledRenderer = tiledRenderer;
        this.camera = camera;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TiledMapComponent tiledMap = tm.get(entity);

        tiledRenderer.setView(camera);
        tiledRenderer.setMap(tiledMap.map);
        tiledRenderer.render();
    }
}
