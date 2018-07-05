package com.bonkan.brao.engine.map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

/**
 *<p>Maneja el {@link com.badlogic.gdx.physics.box2d.World mundo}!!</p>
 */
public class WorldManager
{
	public static World world;
	private static final float timeStep = 1.0f / 45.0f;
	private static final int velocityIterations = 6;
	private static final int positionIterations = 3;
	private static Vector2 myGravity;
	private static boolean gravityIsTemporarilySet;
	private static float accumulator = 0f;

	/**
	 * <p>Crea un {@link com.badlogic.gdx.physics.box2d.World World} con valores default.</p>
	 */
	public static void init()
	{
		myGravity = new Vector2(0, 0f);
		gravityIsTemporarilySet = false;
		world = new World(myGravity, true);
	}
	
	/**
	 * <p>Limitamos con un acumulador para que no chupe tanta memoria (util en equipos lentos)</p>
	 * @param deltaTime
	 */
	public static void doPhysicsStep(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= timeStep) {
            world.step(timeStep, velocityIterations, positionIterations);
            accumulator -= timeStep;
        }
    }

	/**
	 * <p>Establece una gravedad (vertical u horizontal).</p>
	 * @param gravity	&emsp;{@link com.badlogic.gdx.math.Vector2 Vector2} la gravedad
	 */
	public static void setGravity(Vector2 gravity)
	{
		myGravity = gravity;
		if (!gravityIsTemporarilySet)
			world.setGravity(myGravity);
	}

	/**
	 * <p>Setea la gravedad pero no actualiza el world</p>
	 * @param gravity	&emsp;{@link com.badlogic.gdx.math.Vector2 Vector2} la gravedad
	 */
	public static void setGravityButDontUpdateWorld(Vector2 gravity)
	{
		myGravity = gravity;
	}

	/**
	 * <p>Setea la gravedad temporalmente (Servira para algun efecto?)</p>
	 * @param gravity	&emsp;{@link com.badlogic.gdx.math.Vector2 Vector2} la gravedad
	 */
	public static void setGravityTemporarily(Vector2 gravity)
	{
		world.setGravity(gravity);
		gravityIsTemporarilySet = true;
	}

	/**
	 * <p>Vuelve la gravedad a la normalidad</p>
	 */
	public static void undoTemporaryGravitySet()
	{
		world.setGravity(myGravity);
		gravityIsTemporarilySet = false;
	}

}
