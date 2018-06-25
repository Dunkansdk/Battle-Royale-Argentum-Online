package com.bonkan.brao.engine.entity.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import com.bonkan.brao.engine.controller.KeyboardController;
import com.bonkan.brao.engine.entity.component.B2dBodyComponent;
import com.bonkan.brao.engine.entity.component.PlayerComponent;
import com.bonkan.brao.engine.entity.component.StateComponent;

public class PlayerControlSystem extends IteratingSystem{

	private ComponentMapper<PlayerComponent> pm;
	private ComponentMapper<B2dBodyComponent> bodm;
	private ComponentMapper<StateComponent> sm;
	private KeyboardController controller;

	public PlayerControlSystem(KeyboardController keyCon) {
		super(Family.all(PlayerComponent.class).get());
		controller = keyCon;
		pm = ComponentMapper.getFor(PlayerComponent.class);
		bodm = ComponentMapper.getFor(B2dBodyComponent.class);
		sm = ComponentMapper.getFor(StateComponent.class);
	}
	
	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		B2dBodyComponent b2body = bodm.get(entity);
		StateComponent state = sm.get(entity);
		
		int horizontalForce = 0;
        int verticalForce = 0;
		
		if(controller.up) {
			verticalForce += 1;
		}
		if(controller.down) {
			verticalForce -= 1;
		}
		if(controller.left) {
			horizontalForce -= 1;
		}
		if(controller.right) {
			horizontalForce += 1;
		}
		
		b2body.body.setLinearVelocity(horizontalForce * 250, verticalForce * 250);

	}
}
