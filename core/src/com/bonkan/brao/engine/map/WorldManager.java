package com.bonkan.brao.engine.map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class WorldManager
{
	private World world;
	private float timeStep;
	private int velocityIterations;
	private int positionIterations;
	private Vector2 myGravity;
	private boolean gravityIsTemporarilySet;

	/**
	 * Crea un {@link com.badlogic.gdx.physics.box2d.World} con valores default
	 */
	public WorldManager()
	{
		timeStep = 1.0f / 45.f;
		velocityIterations = 6;
		positionIterations = 3;
		myGravity = new Vector2(0, 0f);
		gravityIsTemporarilySet = false;
		world = new World(myGravity, true);
		
		// Seteamos el ContactListener para obtener las collisiones
		world.setContactListener(new WorldContactListener());
	}

	/**
	 * Update world
	 */
	public void step()
	{
		world.step(timeStep, velocityIterations, positionIterations);
	}

	/**
	 * Establece una gravedad para x o y
	 * @param gravity
	 */
	public void setGravity(Vector2 gravity)
	{
		myGravity = gravity;
		if (!gravityIsTemporarilySet)
		{
			world.setGravity(myGravity);
		}
	}

	public void setGravityButDontUpdateWorld(Vector2 gravity)
	{
		myGravity = gravity;
	}

	/**
	 * Setea la gravedad temporalmente (Servira para algun efecto?)
	 * @param gravity
	 */
	public void setGravityTemporarily(Vector2 gravity)
	{
		world.setGravity(gravity);
		gravityIsTemporarilySet = true;
	}

	/**
	 * Vuelve la gravedad a la normalidad
	 */
	public void undoTemporaryGravitySet()
	{
		world.setGravity(myGravity);
		gravityIsTemporarilySet = false;
	}
	
	public World getWorld() {
		return world;
	}

}
