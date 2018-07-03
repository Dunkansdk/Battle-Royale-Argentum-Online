package com.bonkan.brao.engine.map;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bonkan.brao.engine.map.factory.Sensor;

/**
 * <p>Collisiones entre {@link com.badlogic.gdx.physics.box2d.Body Bodies}</p>
 */
public class WorldContactListener implements ContactListener {

	@Override
	public void beginContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        
        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;
        
        if(fa.isSensor() || fb.isSensor())
        {
        	if(fa.getUserData() instanceof Sensor) ((Sensor) fa.getUserData()).setColliding(true);
        	if(fb.getUserData() instanceof Sensor) ((Sensor) fb.getUserData()).setColliding(true);
        }

	}

	@Override
	public void endContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        
        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;
        
        if(fa.isSensor() || fb.isSensor())
        {
        	if(fa.getUserData() instanceof Sensor) ((Sensor) fa.getUserData()).setColliding(false);
        	if(fb.getUserData() instanceof Sensor) ((Sensor) fb.getUserData()).setColliding(false);
        }
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}
