package com.bonkan.brao.engine.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import com.bonkan.brao.engine.entity.component.CollisionComponent;
import com.bonkan.brao.engine.entity.component.PlayerComponent;
import com.bonkan.brao.engine.entity.component.TypeComponent;

public class CollisionSystem  extends IteratingSystem {
	ComponentMapper<CollisionComponent> cm;
	ComponentMapper<PlayerComponent> pm;

	 /**
	  * 
	  */
	public CollisionSystem() {
		// only need to worry about player collisions
		super(Family.all(CollisionComponent.class,PlayerComponent.class).get());
		
		 cm = ComponentMapper.getFor(CollisionComponent.class);
		 pm = ComponentMapper.getFor(PlayerComponent.class);
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		// get player collision component
	CollisionComponent cc = cm.get(entity);
	
	Entity collidedEntity = cc.collisionEntity;
	if(collidedEntity != null){
		TypeComponent type = collidedEntity.getComponent(TypeComponent.class);
		if(type != null) {
			switch(type.type){
			case TypeComponent.ENEMY:
				//do player hit enemy thing
				System.out.println("player hit enemy");
				break;
			case TypeComponent.SCENERY:
				//do player hit scenery thing
				System.out.println("player hit scenery");
				break;
			case TypeComponent.OTHER:
				//do player hit other thing
				System.out.println("player hit other");
				break; //technically this isn't needed				
			}
			cc.collisionEntity = null; // collision handled reset component
			}
		}
		
	}

}
