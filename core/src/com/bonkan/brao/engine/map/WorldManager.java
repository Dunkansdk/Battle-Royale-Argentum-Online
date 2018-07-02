package com.bonkan.brao.engine.map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 *<p>Maneja el {@link com.badlogic.gdx.physics.box2d.World mundo}!!</p>
 */
public class WorldManager
{
	private World world;
	private float timeStep;
	private int velocityIterations;
	private int positionIterations;
	private Vector2 myGravity;
	private boolean gravityIsTemporarilySet;

	/**
	 * <p>Crea un {@link com.badlogic.gdx.physics.box2d.World World} con valores default.</p>
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
	 * <p>a.k.a update()</p>
	 */
	public void step()
	{
		world.step(timeStep, velocityIterations, positionIterations);
	}

	/**
	 * <p>Establece una gravedad (vertical u horizontal).</p>
	 * @param gravity	&emsp;{@link com.badlogic.gdx.math.Vector2 Vector2} la gravedad
	 */
	public void setGravity(Vector2 gravity)
	{
		myGravity = gravity;
		if (!gravityIsTemporarilySet)
			world.setGravity(myGravity);
	}

	/**
	 * <p>Setea la gravedad pero no actualiza el world</p>
	 * @param gravity	&emsp;{@link com.badlogic.gdx.math.Vector2 Vector2} la gravedad
	 */
	public void setGravityButDontUpdateWorld(Vector2 gravity)
	{
		myGravity = gravity;
	}

	/**
	 * <p>Setea la gravedad temporalmente (Servira para algun efecto?)</p>
	 * @param gravity	&emsp;{@link com.badlogic.gdx.math.Vector2 Vector2} la gravedad
	 */
	public void setGravityTemporarily(Vector2 gravity)
	{
		world.setGravity(gravity);
		gravityIsTemporarilySet = true;
	}

	/**
	 * <p>Vuelve la gravedad a la normalidad</p>
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
