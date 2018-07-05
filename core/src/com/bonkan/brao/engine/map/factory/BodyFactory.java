package com.bonkan.brao.engine.map.factory;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * <p>Fábrica de {@link com.badlogic.gdx.physics.box2d.Body bodies} de box2d.</p>
 */
public class BodyFactory {
	
	// Filters
	public static final short BIT_WALL = 1;
    public static final short BIT_PLAYER = 2;
    public static final short BIT_SENSOR = 4;
    public static final short BIT_NOLIGHT = 8;
    public static final short BIT_BREAKABLE = 16;

    /**
     * <p>Crea el {@link com.badlogic.gdx.physics.box2d.Body} de un Human.</p>
     * @return {@link com.badlogic.gdx.physics.box2d.Body}
     */
    public static Body createPlayerBox(final World world, float x, float y, int width, int height) 
    {
    	Body pBody;
        BodyDef bodyDef = new BodyDef(); 

		bodyDef.type = BodyDef.BodyType.KinematicBody;
        
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        pBody = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        
        pBody.createFixture(fixtureDef);
        
        shape.dispose();
        return pBody;
    }
    
    /**
     * <p>Crea un objeto en el mapa (Los obtiene de el editor Tiled).</p>
     * @param {@link com.bonkan.brao.engine.map.factory.ShapeFactory} Shape que crea el MapManager dependiendo de que se haya creado en el Tiled
     * @return {@link com.badlogic.gdx.physics.box2d.Body}
     */
    public static Body createMapBox(final World world, Shape shape) {
    	Body pBody;
		BodyDef bodyDef = new BodyDef();
		bodyDef.fixedRotation = true;

		bodyDef.type = BodyDef.BodyType.StaticBody;
		
		pBody = world.createBody(bodyDef);  
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.0f;
		
		// Asociamos la instancia que lo crea como UserData (Para acceder a los datos desde el WorldContactListener)
		pBody.createFixture(fixtureDef);
		
		return pBody;
	}

}