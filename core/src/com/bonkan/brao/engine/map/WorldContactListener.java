package com.bonkan.brao.engine.map;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.bonkan.brao.engine.entity.humans.Enemy;
import com.bonkan.brao.engine.entity.humans.Player;
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
        
        if(fa.isSensor() && fa.getUserData() instanceof Sensor && ((Sensor)fa.getUserData()).getDireccion() == 0) {
        	System.out.println("[FA] Sensorleft: " + fa.getBody().getPosition() + " with " + fb.getBody().getPosition());
        	((Sensor)fa.getUserData()).setColliding(true);
        }
        
        if(fb.isSensor() && fb.getUserData() instanceof String && ((Sensor)fb.getUserData()).getDireccion() == 0) {
        	System.out.println("[FB] Sensorleft: " + fa.getBody().getPosition() + " with " + fb.getBody().getPosition());
        	((Sensor)fa.getUserData()).setColliding(true);
        }
        
        if(fa.isSensor() && fa.getUserData() instanceof String && ((Sensor)fa.getUserData()).getDireccion() == 1) {
        	System.out.println("[FA] Sensorright: " + fa.getBody().getPosition() + " with " + fb.getBody().getPosition());
        	((Sensor)fa.getUserData()).setColliding(true);
        }
        
        if(fb.isSensor() && fb.getUserData() instanceof String && ((Sensor)fb.getUserData()).getDireccion() == 1){
        	System.out.println("[FB] Sensorright: " + fa.getBody().getPosition() + " with " + fb.getBody().getPosition());
        	((Sensor)fa.getUserData()).setColliding(true);
        }
        
        if(fa.isSensor() && fa.getUserData() instanceof String && ((Sensor)fa.getUserData()).getDireccion() == 2) {
        	System.out.println("[FA] Sensorup: " + fa.getBody().getPosition() + " with " + fb.getBody().getPosition());
        	((Sensor)fa.getUserData()).setColliding(true);
        }
        
        if(fb.isSensor() && fb.getUserData() instanceof String && ((Sensor)fb.getUserData()).getDireccion() == 2) {
        	System.out.println("[FB] Sensorup: " + fa.getBody().getPosition() + " with " + fb.getBody().getPosition());
        	((Sensor)fa.getUserData()).setColliding(true);
        }
        
        if(fa.isSensor() && fa.getUserData() instanceof String && ((Sensor)fa.getUserData()).getDireccion() == 3) {
        	System.out.println("[FA] SensorDown: " + fa.getBody().getPosition() + " with " + fb.getBody().getPosition());
        	((Sensor)fa.getUserData()).setColliding(true);
        }
        
        if(fb.isSensor() && fb.getUserData() instanceof String && ((Sensor)fb.getUserData()).getDireccion() == 3) {
        	System.out.println("[FB] SensorDown: " + fa.getBody().getPosition() + " with " + fb.getBody().getPosition());
        	((Sensor)fa.getUserData()).setColliding(true);
        }

	}

	@Override
	public void endContact(Contact contact) {
		Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();
        
        if(fa == null || fb == null) return;
        if(fa.getUserData() == null || fb.getUserData() == null) return;
        /*
        if(fa.isSensor() && fa.getUserData() instanceof Sensor && ((Sensor)fa.getUserData()).getDireccion() == 0) {
        	System.out.println("[FA] Sensorleft: " + fa.getBody().getPosition() + " with " + fb.getBody().getPosition());
        	Player player = ((Sensor)fb.getUserData()).getPlayer();
        	player.resetSensorCollision(1);
        }
        
        if(fb.isSensor() && fb.getUserData() instanceof String && ((Sensor)fb.getUserData()).getDireccion() == 0) {
        	System.out.println("[FB] Sensorleft: " + fa.getBody().getPosition() + " with " + fb.getBody().getPosition());
        	Player player = ((Sensor)fb.getUserData()).getPlayer();
        	player.resetSensorCollision(0);
        }
        
        if(fa.isSensor() && fa.getUserData() instanceof String && ((Sensor)fa.getUserData()).getDireccion() == 1) {
        	System.out.println("[FA] Sensorright: " + fa.getBody().getPosition() + " with " + fb.getBody().getPosition());
        	Player player = ((Sensor)fb.getUserData()).getPlayer();
        	player.resetSensorCollision(0);
        }
        
        if(fb.isSensor() && fb.getUserData() instanceof String && ((Sensor)fb.getUserData()).getDireccion() == 1){
        	System.out.println("[FB] Sensorright: " + fa.getBody().getPosition() + " with " + fb.getBody().getPosition());
        	Player player = ((Sensor)fb.getUserData()).getPlayer();
        	player.resetSensorCollision(1);
        }
        
        if(fa.isSensor() && fa.getUserData() instanceof String && ((Sensor)fa.getUserData()).getDireccion() == 2) {
        	System.out.println("[FA] Sensorup: " + fa.getBody().getPosition() + " with " + fb.getBody().getPosition());
        	Player player = ((Sensor)fb.getUserData()).getPlayer();
        	player.resetSensorCollision(2);
        }
        
        if(fb.isSensor() && fb.getUserData() instanceof String && ((Sensor)fb.getUserData()).getDireccion() == 2) {
        	System.out.println("[FB] Sensorup: " + fa.getBody().getPosition() + " with " + fb.getBody().getPosition());
        	Player player = ((Sensor)fb.getUserData()).getPlayer();
        	player.resetSensorCollision(2);
        }
        
        if(fa.isSensor() && fa.getUserData() instanceof String && ((Sensor)fa.getUserData()).getDireccion() == 3) {
        	System.out.println("[FA] SensorDown: " + fa.getBody().getPosition() + " with " + fb.getBody().getPosition());
        	Player player = ((Sensor)fb.getUserData()).getPlayer();
        	player.resetSensorCollision(3);
        }
        
        if(fb.isSensor() && fb.getUserData() instanceof String && ((Sensor)fb.getUserData()).getDireccion() == 3) {
        	System.out.println("[FB] SensorDown: " + fa.getBody().getPosition() + " with " + fb.getBody().getPosition());
        	Player player = ((Sensor)fb.getUserData()).getPlayer();
        	player.resetSensorCollision(3);
        }
        */

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		
	}

}
