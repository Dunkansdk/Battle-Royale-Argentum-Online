package com.bonkan.brao.engine.map;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bonkan.brao.engine.entity.Player;

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
        
        // Le informamos a la instancia que esta tocando otro body
        if(fa.getUserData() instanceof Player) {
        	Player p = (Player) fa.getUserData();
        	p.setContact(true);
        }
        
        if(fb.getUserData() instanceof Player) {
        	Player p = (Player) fb.getUserData();
        	p.setContact(true);
        }
        
        System.out.println("Begin Contact: " + fa.getBody().getPosition() + " with " + fb.getBody().getPosition());
	}

	@Override
	public void endContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        
        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;
        
        // Le informamos a la instancia que ya no esta tocando otro body
        if(fa.getUserData() instanceof Player) {
        	Player p = (Player) fa.getUserData();
        	p.setContact(false);
        }
        
        if(fb.getUserData() instanceof Player) {
        	Player p = (Player) fb.getUserData();
        	p.setContact(false);
        }
        
		System.out.println("End Contact: " + fa.getBody().getPosition() + " with " + fb.getBody().getPosition());
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}
