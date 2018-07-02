package com.bonkan.brao.engine.map.factory;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Make a Box2D Body
 *
 */
public class BodyFactory {
	
	// Filters
	public static final short BIT_WALL = 1;
    public static final short BIT_PLAYER = 2;
    public static final short BIT_SENSOR = 4;
    public static final short BIT_NOLIGHT = 8;
    public static final short BIT_BREAKABLE = 16;

    /**
     * Crea el {@link com.badlogic.gdx.physics.box2d.Body} de un player
     * @param data	Instancia que crea el body
     * @return {@link com.badlogic.gdx.physics.box2d.Body}
     */
    public static Body createPlayerBox(final World world, float x, float y, int width, int height, Object data) {
    	Body pBody;
        BodyDef bodyDef = new BodyDef();

        // Siempre va a ser dinamico un player
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.fixedRotation = true;
        pBody = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.filter.categoryBits = BIT_PLAYER;
        fixtureDef.filter.maskBits = BIT_PLAYER | BIT_WALL | BIT_SENSOR;
        fixtureDef.filter.groupIndex = 0;
        
        // Asociamos la instancia que lo crea como UserData (Para acceder a los datos desde el WorldContactListener)
        pBody.createFixture(fixtureDef).setUserData(data);
        
        shape.dispose();
        return pBody;
    }
    
    /**
     * Crea un objeto en el mapa (Los obtiene de el editor Tiled)
     * @param {@link com.bonkan.brao.engine.map.factory.ShapeFactory} Shape que crea el MapManager dependiendo de que se haya creado en el Tiled
     * @param data	Instancia que crea el body
     * @return {@link com.badlogic.gdx.physics.box2d.Body}
     */
    public static Body createMapBox(final World world, Shape shape, boolean isStatic, boolean fixedRotation, Object data) {
    	Body pBody;
		BodyDef bodyDef = new BodyDef();
		bodyDef.fixedRotation = fixedRotation;
		
		if(isStatic) {
			bodyDef.type = BodyDef.BodyType.StaticBody;
		} else {
			bodyDef.type = BodyDef.BodyType.DynamicBody;
		}
		
		pBody = world.createBody(bodyDef);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = 1.0f;
		fixtureDef.filter.categoryBits = BIT_WALL; // Es un...
		fixtureDef.filter.maskBits = BIT_PLAYER | BIT_WALL | BIT_SENSOR; // Collisiona contra...
		fixtureDef.filter.groupIndex = 0;
		
		// Asociamos la instancia que lo crea como UserData (Para acceder a los datos desde el WorldContactListener)
		pBody.createFixture(fixtureDef).setUserData(data);
		
		return pBody;
	}

}