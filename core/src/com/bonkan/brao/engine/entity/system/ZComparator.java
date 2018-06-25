package com.bonkan.brao.engine.entity.system;

import java.util.Comparator;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.bonkan.brao.engine.entity.component.TransformComponent;

public class ZComparator implements Comparator<Entity> {
    private ComponentMapper<TransformComponent> transform;

    public ZComparator(){
    	transform = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public int compare(Entity entityA, Entity entityB) {
        return (int) Math.signum(transform.get(entityB).position.y - transform.get(entityA).position.y);
    }
}
